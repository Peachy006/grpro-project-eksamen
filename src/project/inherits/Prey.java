package project.inherits;

public abstract class Prey extends Animal {

    protected Prey(int energy, int age, boolean hasFungi) {
        super(energy, age, hasFungi);
    }

    @Override
    public void eat() {}
}
