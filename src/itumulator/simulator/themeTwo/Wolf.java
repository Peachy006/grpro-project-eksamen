package itumulator.simulator.themeTwo;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.simulator.*;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DisplayInformation;
import java.awt.Color;
import java.util.*;


public class Wolf extends Predator implements Actor, DynamicDisplayInformationProvider {
    protected boolean isRemoved = false;
    protected Random random = new Random();
    protected int packID;
    protected int lookForPackRadius = 2;


    public Wolf(int packID) {
        super(400, 400, 0);
        this.packID = packID;
    }

    public int getPackID() {
        return packID;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "wolf", true);
    }


    public boolean moveWithPack(World w, Location currentLocation) {
        //Looks for wolves in pack
        Set<Location> searchPack = w.getSurroundingTiles(currentLocation, lookForPackRadius);
        Set<Wolf> nearbyWolves = w.getAll(Wolf.class, searchPack);

        ArrayList<Wolf> wolvesPack = new ArrayList<>();
        for (Wolf wolf : nearbyWolves) {
            if (wolf == this) {
                continue;
            }
            if (wolf.getPackID() == this.packID) {
                wolvesPack.add(wolf);
            }
        }
        if (wolvesPack.isEmpty()) {
            return false;
        }
        Wolf targetWolf = wolvesPack.get(random.nextInt(wolvesPack.size()));

        Location targetLocation = w.getLocation(targetWolf);

        return moveTowards(w, currentLocation, targetLocation);
    }

    @Override
    public void act(World w) {
        if (!isRemoved) {
            age(w);
            
            Location currentLocation = w.getLocation(this);
            //Wolf tries to hunt nearby 'Rabbits'
            hunt(w);
            boolean moved = false;

            if (moveWithPack(w, currentLocation)) {
                moved = true;
            }
            if (!moved) {
                if (moveRandomly(w, currentLocation)) {
                    moved = true;
                }
            }
            if (moved) {
                energy -= 10;
            }
            if (energy <= 10 && !isRemoved) {
                w.delete(this);
                isRemoved = true;
            }
        }
    }
}