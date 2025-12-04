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
    protected ArrayList<Wolf> wolfPack;
    protected boolean hasPack;
    protected int lookForPackRadius = 1;

    public Wolf(int packID) {
        super(200, 0);
        this.packID = packID;
        wolfPack = new ArrayList<Wolf>();
        hasPack = false;
    }

    public int getPackID() {
        return packID;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "wolf", true);
    }


    @Override
    public void act(World w) {

        this.updatePack(w);

        if (!isRemoved) {
            age(w);
            
            Location currentLocation = w.getLocation(this);

            //Wolf tries to hunt
            hunt(w);

            // move with pack
            moveWithPackTest(w, currentLocation);

            // dies when energy is lower then 10
            if (energy <= 10 && !isRemoved) {
                wolfPack.remove(this);
                w.delete(this);
                isRemoved = true;
            }
        }
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

    // picks a random wolf and ether moves towards it or randomly around it
    public void moveWithPackTest(World w, Location currentLocation) {
        boolean moved = false;

        // if there are wolf's in pack
        if (!wolfPack.isEmpty()) {

            //pick a random wolf's location from the pack
            Wolf targetWolf = wolfPack.get(random.nextInt(wolfPack.size()));
            Location targetWolfL = w.getLocation(targetWolf);

            //get target wolfs radius as locations
            Set<Location> locations = w.getSurroundingTiles(targetWolfL, lookForPackRadius);

            if  (locations.contains(currentLocation)) { // if wolf is in target wolfs radius move randomly
                if (moveRandomly(w, currentLocation)) {
                    moved = true;
                }
            } else { // else try and move towards target wolf
                if (moveTowards(w, currentLocation, targetWolfL)) {
                    moved = true;
                }
            }
        } else { // if no wolfs in pack just move randomly
            if (moveRandomly(w, currentLocation)) {
                moved = true;
            }
        }

        // if it moved succesfully decrees energy
        if (moved){
            energy -= 10;
        }
    }

    /// updatePack has been added so that the pack can be updatede in more places
    /// EX if a wolf reproduces it needs to be updatede
    /// this would be simpler if we made a new class for packs

    // updates wolf pack(Arraylist) according to packID
    public void updatePack(World w) {
        // first remove wolves that no longer exist in the world
        wolfPack.removeIf(wolf -> !w.contains(wolf));
    
        // takes all tiles on the map and makes a set of all wolf's on the map
        Set<Location> tiles = w.getSurroundingTiles(w.getLocation(this), w.getSize());
        Set<Wolf> wolves = w.getAll(Wolf.class, tiles);

        // if there's wolf's in pack
        if (!wolves.isEmpty()) {
            // add wolf's with the same packID (only if not already in pack)
            for (Wolf wolf : wolves) {
                if (wolf.getPackID() == this.packID && !wolfPack.contains(wolf)) {
                    wolfPack.add(wolf);
                }
            }
        }
    }

    // takes a random animal in its radius and ehter eats it or attacks it
    public void hunt(World w) {
        Set<Location> tiles = w.getSurroundingTiles(w.getLocation(this));
        Set<Animal> nearbyPrey = w.getAll(Animal.class, tiles);

        //eats prey and attacks other preditors that isn't in its pack
        for (Animal target : nearbyPrey) {
            if (target instanceof Prey) {
                super.hunt(w, target);
                return;
            } else if (target instanceof Wolf && ((Wolf) target).getPackID() != this.packID) {
                super.attack(target);
                return;
            } else if (target instanceof Predator && !(target instanceof Wolf)) {
                // Attack other predators that aren't wolves (like Bears)
                super.attack(target);
                return;
            }
        }
    }
}