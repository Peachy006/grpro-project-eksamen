package itumulator.simulator;

import itumulator.simulator.*;
import itumulator.world.Location;
import itumulator.world.World;

public abstract class Prey extends Animal {

    protected Prey(int energy, int totalEnergy, int age) {
        super(energy, totalEnergy, age);
    }

    @Override
    public void eat() {}
}
