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

    private static final int STEPS = 5;

    private static final class MatingTask {
        final int packId;
        final boolean parentAHasFungi;
        final boolean parentBHasFungi;
        int remainingSteps;

        MatingTask(int packId, boolean parentAHasFungi, boolean parentBHasFungi, int remainingSteps) {
            this.packId = packId;
            this.parentAHasFungi = parentAHasFungi;
            this.parentBHasFungi = parentBHasFungi;
            this.remainingSteps = remainingSteps;
        }
    }

    private final List<MatingTask> pendingMatings = new ArrayList<>();

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

            Location aLoc = empty.get(0);
            Location bLoc = empty.get(1);
            Location pupLoc = empty.get(2);

            Wolf parentA = new Wolf(task.packId, task.parentAHasFungi);
            Wolf parentB = new Wolf(task.packId, task.parentBHasFungi);

            // Pup: choose your own rule. Here: no fungi by default.
            Wolf pup = new Wolf(task.packId, false);

            world.setTile(aLoc, parentA);
            world.setTile(bLoc, parentB);
            world.setTile(pupLoc, pup);

            // Re-add to pack controller so pack logic doesn't keep dead references
            Pack pack = Pack.getInstance();
            pack.addToPack(parentA);
            pack.addToPack(parentB);
            pack.addToPack(pup);

            pendingMatings.remove(i);
        }
    }

    /**
     * Register a mating pair that has "gone into" the burrow.
     * Caller should delete both wolves from the world and remove them from Pack.
     */
    public void startMating(Wolf a, Wolf b) {
        if (a == null || b == null) return;
        if (a.getPackID() != packIdForBurrow || b.getPackID() != packIdForBurrow) return;

        pendingMatings.add(new MatingTask(packIdForBurrow, a.hasFungi(), b.hasFungi(), STEPS));
    }

    public void addWolf(Wolf wolf) {
        wolvesInBurrow.add(wolf);
    }

    public void removeWolf(Wolf wolf) {
        wolvesInBurrow.remove(wolf);
    }
}
