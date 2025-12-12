package project.animals;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.simulator.*;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DisplayInformation;
import project.inherits.Animal;
import project.inherits.Pack;
import project.inherits.Predator;
import project.inherits.Prey;
import project.structures.WolfsDen;

import java.awt.Color;
import java.util.*;
// todo: worlf hule
// todo: add pack done
// todo: reproduve in hule

public class Wolf extends Predator implements Actor, DynamicDisplayInformationProvider {
    public boolean isRemoved = false;
    protected Random random = new Random();

    protected WolfsDen den;
    protected Pack packs;

    protected int packID;
    protected ArrayList<Wolf> wolfPack;
    protected int lookForPackRadius; // controls how tight the pack is
    protected boolean hasPack;

    protected boolean hasDen;
    protected Location denLocation;

    protected boolean inHeat;
    protected int cooldown;

    public Wolf(int packID) {
        super(200, 0);
        this.wolfPack = new ArrayList<Wolf>();
        this.packs = Pack.getInstance(); // static that creates an instance of class "Pack" if it's not allready made

        this.packID = packID;
        this.hasPack = false;
        this.lookForPackRadius = 1;

        this.cooldown = 0;

    }


    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "wolf", true);
    }


    @Override
    public void act(World w) {

        if (!isRemoved) {
            Location currentLocation = w.getLocation(this);

            age(w);

            if(hasPack) {
                wolfPack = packs.getPack(packID);
            } else {
                findPack();
            }

            // cooldown
            cooldown();


            //Wolf tries to hunt
            hunt(w);

            // if wolf can mate move to and enter den
            if (inHeat && hasDen && energy >= 0) {denMove(w, currentLocation);
            } else {moveWithPack(w, currentLocation);
            } // else move with pack


            // when energy <= 10 it dies and is removed from the pack
            if (energy <= 10 && !isRemoved) {
                packs.removeFromPack(this);
                w.delete(this);
                isRemoved = true;
            }

            // if wolfpack has no den there's a one in 10 chance of creating one
            if(!hasDen) {
                int rand = r.nextInt(10);

                if(rand == 0 && !w.containsNonBlocking(currentLocation)) {
                    this.den = new WolfsDen(this, currentLocation);
                    w.setTile(currentLocation, den);
                }
            }
        }
    }

    // picks a random wolf and ether moves towards it or randomly around it
    // if its in heat go in den
    public void moveWithPack(World w, Location currentLocation) {

        // if pack is not empty
        if (!wolfPack.isEmpty() && wolfPack.size() >= 2) {

            //pick a random wolf's location from the pack
            Wolf targetWolf = wolfPack.get(random.nextInt(wolfPack.size()));

            // if pointing to itself pick another wolf untill it finds another
            while (targetWolf == this) {
                targetWolf = wolfPack.get(random.nextInt(wolfPack.size()));
            }

            Location targetWolfL = w.getLocation(targetWolf);

            //get target wolfs radius as locations
            Set<Location> locations = w.getSurroundingTiles(targetWolfL, lookForPackRadius);

            if (locations.contains(currentLocation)) { // if wolf is in target wolfs radius move randomly
                moveRandomly(w, currentLocation);
            } else { // else try and move towards target wolf
                moveTowards(w, currentLocation, targetWolfL);
            }
        } else { // move randomly if there's no wolfs on the list
            moveRandomly(w, currentLocation);
        }

        // update location
        currentLocation = w.getLocation(this);
        w.setCurrentLocation(currentLocation);

        // decrees energy every turn no matter what.
        energy -= 10;

    }

    public void denMove(World w, Location currentLocation) {
        Set<Location> tiles = w.getSurroundingTiles(currentLocation, 1);

        if (tiles.contains(denLocation)) { // if wolf standing on den enter it
            den.addToDen(this);
            w.remove(this);
            isRemoved = true;
        } else { // else move somewhere else
            moveTowards(w, currentLocation, denLocation);
        }

        System.out.println("Current location: " + currentLocation);
        System.out.println("den location: " + denLocation);
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

    public int getPackID() {
        return packID;
    }

    // gets a den when its created
    public void receiveDen(WolfsDen den, Location denLocation) {
        this.den = den;
        this.hasDen = true;
        this.denLocation = denLocation;
    }

    // cools down the mating timer
    public void cooldown() {
        if (cooldown != 0) {
            inHeat = false;
            cooldown--;
        } else if (age >= 1) {
            inHeat = true;
        }
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}