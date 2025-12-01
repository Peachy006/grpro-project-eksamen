package itumulator.simulator;

import itumulator.simulator.*;
import itumulator.world.Location;
import itumulator.world.World;

public abstract class Prey extends Animal {

    protected Prey(int energy, int age) {
        super(energy, age);
    }

    @Override
    public void eat() {}
}
