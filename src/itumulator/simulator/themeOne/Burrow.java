package itumulator.simulator.themeOne;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.Color;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Burrow implements Actor, NonBlocking, DynamicDisplayInformationProvider {

    private final String imageKey;

    Set<Rabbit> rabbits = new HashSet<>();


    public Burrow() {
        // Choose once per instance so each burrow keeps its image
        // Here we choose "hole" if it is true, and hole-small if its false, this is because i saw there were
        // 2 images you could choose from
        this.imageKey = new Random().nextBoolean() ? "hole" : "hole-small";
    }

    @Override
    public DisplayInformation getInformation() {
        // Optional: pass true for random orientation each render
        return new DisplayInformation(Color.BLACK, imageKey, true);
    }

    @Override
    public void act(World w) {

    }

    public void addRabbit(Rabbit rabbit) {
        this.rabbits.add(rabbit);
    }

}
