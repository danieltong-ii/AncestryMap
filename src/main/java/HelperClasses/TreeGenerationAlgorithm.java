package HelperClasses;

import DataAccess.*;
import Model.Event;
import Model.Location;
import Model.Person;
import Model.User;
import TempDatabase.*;
import com.google.gson.Gson;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;

public class TreeGenerationAlgorithm {

    private LocationData locDB;
    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<Person> personList = new ArrayList<>();

    private NamesData fnames = new NamesData();
    private NamesData mnames = new NamesData();
    private NamesData snames = new NamesData();

    public TreeGenerationAlgorithm() {
    }

    public void generateNamesDatabase() {
        String fPath = "fnames.json";
        String mPath = "mnames.json";
        String sPath = "snames.json";

        try {
            fnames = generateNames(fPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mnames = generateNames(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            snames = generateNames(sPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateLocationDatabase() {
        MakeLocationDB locDBMaker = new MakeLocationDB();

        try {
            locDB = locDBMaker.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadToDatabase() throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDAO pDao = new PersonDAO(conn);
        EventDAO eDao = new EventDAO(conn);

        for (Event event : eventList) {
            eDao.insert(event);
        }
        for (Person person : personList) {
            pDao.insert(person);
        }
        db.closeConnection(true);
    }

    public void generateParents(Person child, User user, int childBirthYear, int generationsRequested) {

        if (generationsRequested == 0) {
            personList.add(child);
            return;
        } else {
            //Creating a mother and a father for the child
            Person mother = makeAParent(user, "f");
            Person father = makeAParent(user, "m");
            exchangeSpouseID(mother, father);

            int motherBirthYear = 0;
            int fatherBirthYear = 0;
            //Creating birth event for the mother and father based on child's birthdate; return birthdate
            motherBirthYear = makeBirthEvent(user, mother, childBirthYear,motherBirthYear, "mother", fatherBirthYear);
            fatherBirthYear = makeBirthEvent(user, father, childBirthYear,fatherBirthYear, "father", motherBirthYear);

            //Creating marriage and death events for the mother and father based on child's birthdate; return marriageEvent
            Event motherMarriage = makeMarriageAndDeathEvent(user, mother, childBirthYear, motherBirthYear);
            Event fatherMarriage = makeMarriageAndDeathEvent(user, father, childBirthYear, fatherBirthYear);
            syncMarriageDetails(motherMarriage, fatherMarriage);

            //Set child's mother and father IDs
            child.setMotherID(mother.getPersonID());
            child.setFatherID(father.getPersonID());
            personList.add(child);

            //Generating parents for the mother and father; user's username stays consistent
            generateParents(mother, user, motherBirthYear, generationsRequested-1);
            generateParents(father, user, fatherBirthYear, generationsRequested-1);
        }
    }

    public void syncMarriageDetails(Event motherMarriage, Event fatherMarriage) {
        fatherMarriage.setCity(motherMarriage.getCity());
        fatherMarriage.setCountry(motherMarriage.getCountry());
        fatherMarriage.setYear(motherMarriage.getYear());
        fatherMarriage.setLatitude(motherMarriage.getLatitude());
        fatherMarriage.setLongitude(motherMarriage.getLongitude());

        eventList.add(motherMarriage);
        eventList.add(fatherMarriage);
    }

    public void exchangeSpouseID(Person mother, Person father) {
        mother.setSpouseID(father.getPersonID());
        father.setSpouseID(mother.getPersonID());
    }

    public Person makeAParent(User user, String gender) {
        String personID = UUID.randomUUID().toString();
        String associatedUsername = user.getUsername();
        String firstName;

        if (gender.equals("m")) {
            firstName = mnames.getName();
        } else {
            firstName = fnames.getName();
        }
        String lastName = user.getLastName();
        return new Person(personID, associatedUsername, firstName, lastName, gender);
    }

    public Event makeEvent(User user, Person person, String eventType, int childBirthYear, int personBirthYear) {
        Location eventLoc = locDB.getLocation();
        int MAX_AGE = 100;
        int MINIMUM = 1;
        int MIN_YEARS_OLDER_THAN_CHILD = 17;
        int MAX_YEARS_OLDER_THAN_CHILD = 50;
        int year = 0;

        String eventID = UUID.randomUUID().toString();
        String associatedUsername = user.getUsername();
        String personID = person.getPersonID();
        Float latitude = eventLoc.getLatitude();
        Float longitude = eventLoc.getLongitude();
        String country = eventLoc.getCountry();
        String city = eventLoc.getCity();

        if (eventType.equals("birth")) {
            year = getRandomNumber(childBirthYear - MAX_YEARS_OLDER_THAN_CHILD, childBirthYear - MIN_YEARS_OLDER_THAN_CHILD);
        } else if (eventType.equals("marriage")) {
            year = getRandomNumber(personBirthYear + MIN_YEARS_OLDER_THAN_CHILD, childBirthYear);
        } else if (eventType.equals("death")) {
            year = getRandomNumber(childBirthYear + MINIMUM, personBirthYear + MAX_AGE);
        }
        return new Event(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }

    /**
     * Makes three events for person. Returns the marriage event so that we can match the event
     * for both spouses. Parent's birth year is generated and passed up via pass by reference
     *
     * @param user   user of our FamilyMap app
     * @param person ancestor
     * @return marriage event object
     */
    public Event makeMarriageAndDeathEvent(User user, Person person, int childBirthYear, int parentBirthYear) {
        Event marriage = makeEvent(user, person, "marriage", childBirthYear, parentBirthYear);
        Event death = makeEvent(user, person, "death", childBirthYear, parentBirthYear);
        eventList.add(death);

        return marriage;
    }

    public int makeBirthEvent(User user, Person person, int childBirthYear, int parentBirthYear, String whichParent, int SOBirthYear) {
        Event birth = makeEvent(user, person, "birth", childBirthYear, parentBirthYear);
        int AGE_GAP = 5;
        int year = getRandomNumber(SOBirthYear - AGE_GAP, SOBirthYear + AGE_GAP);
        if (whichParent.equals("father")) {
            birth.setYear(year);
        }
        eventList.add(birth);
        return birth.getYear();
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public NamesData generateNames(String path) throws IOException {
        Reader reader = null;
        Gson gs = new Gson();

        try {
            reader = new FileReader("json/" + path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (reader != null) {
            return gs.fromJson(reader, NamesData.class);
        }
        return null;
    }

    public int getEventListSize(){
        return eventList.size();
    }
    public int getPersonListSize(){
        return personList.size();
    }
}
