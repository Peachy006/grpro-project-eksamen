package project.structures;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import project.animals.Wolf;
import project.inherits.Pack;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WolfBurrow implements Actor, NonBlocking, DynamicDisplayInformationProvider {

    public int packIdForBurrow;
    Set<Wolf> wolvesInBurrow = new HashSet<>();

    public WolfBurrow(int packIdForBurrow) {
        this.packIdForBurrow = packIdForBurrow;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.RED, "hole");
    }

    private static int steps = 5;

    private static final class MatingTask {
        final int packId;
        final boolean parentAHasFungi;
        final boolean parentBHasFungi;
        int remainingSteps;

        //this was just to keep track of the mating, the reason we do this in the same file as WolfBurrow is because it is so simple.
        // if it was more complicated, like entityConfig, we would move it to a seperate file

        MatingTask(int packId, boolean parentAHasFungi, boolean parentBHasFungi, int remainingSteps) {
            this.packId = packId;
            this.parentAHasFungi = parentAHasFungi;
            this.parentBHasFungi = parentBHasFungi;
            this.remainingSteps = remainingSteps;
        }
    }

    private final List<MatingTask> pendingMatings = new ArrayList<>();



    //the act method currently handles the mating logic as well as placing the wolves in the world again.
    //in the future this should be moved to a seperate method.
    @Override
    public void act(World world) {
        if (world == null || !world.contains(this) || !world.isOnTile(this)) return;

        for (int i = pendingMatings.size() - 1; i >= 0; i--) {
            MatingTask task = pendingMatings.get(i);
            task.remainingSteps--;

            if (task.remainingSteps > 0) continue;

            Location burrowLoc = world.getLocation(this);
            List<Location> empty = new ArrayList<>(world.getEmptySurroundingTiles(burrowLoc));

            if (empty.size() < 3) {
                task.remainingSteps = 1; // retry next step when space might be free
                continue;
            }

            //placement positions
            Location aLoc = empty.get(0);
            Location bLoc = empty.get(1);
            Location pupLoc = empty.get(2);

            Wolf parentA = new Wolf(task.packId, task.parentAHasFungi);
            Wolf parentB = new Wolf(task.packId, task.parentBHasFungi);

            Wolf pup = new Wolf(task.packId, false);

            //place

            world.setTile(aLoc, parentA);
            world.setTile(bLoc, parentB);
            world.setTile(pupLoc, pup);

            //get the pack and add them back to the pack

            Pack pack = Pack.getInstance();
            pack.addToPack(parentA);
            pack.addToPack(parentB);
            pack.addToPack(pup);

            pendingMatings.remove(i);
        }
    }

    //here we use matingTasks in order to do this
    public void startMating(Wolf a, Wolf b) {
        if (a == null || b == null) return;
        if (a.getPackID() != packIdForBurrow || b.getPackID() != packIdForBurrow) return;

        pendingMatings.add(new MatingTask(packIdForBurrow, a.hasFungi(), b.hasFungi(), steps));
    }

    public void addWolf(Wolf wolf) {
        wolvesInBurrow.add(wolf);
    }

    public void removeWolf(Wolf wolf) {
        wolvesInBurrow.remove(wolf);
    }
}
