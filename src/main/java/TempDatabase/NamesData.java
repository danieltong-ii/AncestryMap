package TempDatabase;

public class NamesData {
    String[] data;

    public void NamesData() {}

    public String getName() {
        int sizeOfNameList = data.length;
        int randomNum = getRandomNumber(0,sizeOfNameList);
        return data[randomNum];
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

}
