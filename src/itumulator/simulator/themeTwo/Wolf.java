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
// todo: worlf hule
// todo: add pack
// todo: reproduve in hule
// todo: class

public class Wolf extends Predator implements Actor, DynamicDisplayInformationProvider {
    protected boolean isRemoved = false;
    protected Random random = new Random();

    protected int packID;
    protected Pack packs;
    protected ArrayList<Wolf> wolfPack;
    protected boolean hasPack;
    protected int lookForPackRadius; // controls how tight the pack is

    public Wolf(int packID) {
        super(200, 0);
        this.packID = packID;
        hasPack = false;
        lookForPackRadius = 1;
        wolfPack = new ArrayList<Wolf>();
        this.packs = Pack.getInstance();
    }


    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "wolf", true);
    }


    @Override
    public void act(World w) {
        // if wolf does not have a pack find one
        if(!hasPack) {
            findPack();
        }

        //update pack
        if(hasPack) {
            wolfPack = packs.getPack(this);
        }

        if (!isRemoved) {
            age(w);
            
            Location currentLocation = w.getLocation(this);


            moveWithPack(w, currentLocation);


            //Wolf tries to hunt
            hunt(w);

            // when energy <= 10 it dies and is removed from the pack
            if (energy <= 10 && !isRemoved) {
                packs.removeFromPack(this);
                w.delete(this);
                isRemoved = true;
            }
        }
    }

    // picks a random wolf and ether moves towards it or randomly around it
    public void moveWithPack(World w, Location currentLocation) {
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

    // finds the pack controller and adds itself to a pack
    public void findPack() {
        packs.addToPack(this);
        hasPack = true;
    }

    public boolean hasPack() {
        return hasPack;
    }

    public int getPackID() {
        return packID;
    }
}