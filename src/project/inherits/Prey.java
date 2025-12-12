package project.inherits;

public abstract class Prey extends Animal {

    protected Prey(int energy, int age) {
        super(energy, age);
    }

    @Override
    public void eat() {}
}
