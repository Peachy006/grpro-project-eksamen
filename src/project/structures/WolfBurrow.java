package project.structures;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import project.animals.Wolf;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class WolfBurrow implements Actor, NonBlocking, DynamicDisplayInformationProvider {

    public int packIdForBurrow;
    Set<Wolf> wolvesInBurrow = new HashSet<>();

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.RED, "hole");
    }

    @Override
    public void act(World world) {

    }

    public void addWolf(Wolf wolf) {
        wolvesInBurrow.add(wolf);
    }

    public void removeWolf(Wolf wolf) {
        wolvesInBurrow.remove(wolf);
    }
}
