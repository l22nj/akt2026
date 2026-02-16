package week1.soojendus.animals;

public class Animal {
    private static int COUNTER = 1;
    private final int number = COUNTER++;

    private final String noise;

    public Animal(String noise) {
        this.noise = noise;
    }

    public void makeNoise() {
        System.out.println("Loom #" + number + ": " + noise);
    }
}
