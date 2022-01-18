package HelperClasses;

/**
 * Sole Purpose is to generate random numbers. Used by multiple classes.
 */
public class RandomNumGenerator {

    /**
     * Give a min and a max and a random number in between will be returned
     * @param min : minimum number
     * @param max : maximum number
     * @return random number
     */
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
