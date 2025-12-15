package project.animals;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import project.inherits.Animal;
import project.inherits.Pack;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DisplayInformation;
import project.inherits.Predator;
import project.structures.WolfBurrow;

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
    protected boolean hasBurrow = false;
    protected WolfBurrow burrow;
    protected int intercourseDelayTimer = 0;
    protected Pack thisWolfsPack;
    protected ArrayList<Wolf> wolfPack;
    protected boolean hasPack;
    protected int lookForPackRadius = 1; // controls how tight the pack is

    public Wolf(int packID, boolean hasFungi) {
        super(200, 0, hasFungi);
        this.packID = packID;
        hasPack = false;
        wolfPack = new ArrayList<>();
        this.thisWolfsPack = Pack.getInstance();
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

        //update pack (must happen before any burrow logic that iterates the pack)
        if(hasPack) {
            wolfPack = thisWolfsPack.getPack(this);
        }

        createBurrowIfDoesntHaveBurrow(w);

        if (!isRemoved) {
            if(age(w)) {
                thisWolfsPack.removeFromPack(this);
                killThisAnimal(w, true);
                return;
            }
            if(this.sporeCount >= 10) {
                killThisAnimal(w, true); //it doesnt matter if its small or large, since no carcass wil come
                return;
            }
            
            Location currentLocation = w.getLocation(this);

            moveWithPack(w, currentLocation);

            //Wolf tries to hunt
            hunt(w);

            // when energy <= 10 it dies and is removed from the pack
            if (energy <= 10 && !isRemoved) {
                thisWolfsPack.removeFromPack(this);
                killThisAnimal(w, true);
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

    // takes a random animal in its radius and either eats it or attacks it
    public void hunt(World w) {
        interactWithNearbyAnimals(w, true);
    }

    @Override
    protected boolean canAttack(Animal target, World w) {
        if (!super.canAttack(target, w)) return false;

        // Wolves don't attack wolves in the same pack.
        if (target instanceof Wolf otherWolf) {
            return otherWolf.getPackID() != this.getPackID();
        }
        return true;
    }

    // finds the pack controller and adds itself to a pack
    public void findPack() {
        thisWolfsPack.addToPack(this);
        hasPack = true;
    }

    public boolean hasPack() {
        return hasPack;
    }

    public int getPackID() {
        return packID;
    }

    public void createBurrowIfDoesntHaveBurrow(World w) {
        Location here = w.getLocation(this);
        if (!hasBurrow && w.getNonBlocking(here) == null && r.nextInt(100) > 90) {
            WolfBurrow newBurrow = new WolfBurrow(this.packID);

            w.setTile(here, newBurrow);

            newBurrow.addWolf(this);
            this.burrow = newBurrow;
            this.hasBurrow = true;

            for (Wolf packMate : wolfPack) {
                if (packMate == null) continue;
                if (packMate.getPackID() != this.packID) continue;

                newBurrow.addWolf(packMate);
                packMate.burrow = newBurrow;
                packMate.hasBurrow = true;
            }
        }
    }
}