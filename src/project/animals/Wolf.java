package project.animals;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import project.inherits.Animal;
import project.inherits.Pack;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DisplayInformation;
import project.inherits.Predator;
import project.structures.Burrow;
import project.structures.WolfBurrow;

import java.awt.Color;
import java.util.*;

public class Wolf extends Predator implements Actor, DynamicDisplayInformationProvider {
    protected boolean isRemoved = false;
    protected Random random = new Random();

    protected int packID;
    protected boolean hasBurrow = false;
    protected WolfBurrow burrow;
    protected int intercourseDelayTimer = 8; //ticks for delay of mating
    protected Pack thisWolfsPack;
    protected ArrayList<Wolf> wolfPack;
    protected boolean hasPack;
    protected int lookForPackRadius = 1; // controls how tight the pack is
    protected Wolf matingPartner = null;

    public Wolf(int nextPackID, boolean hasFungi) {
        super(200, 0, hasFungi); // max energy and current energy for wolf
        this.packID = nextPackID;
        hasPack = false;
        wolfPack = new ArrayList<>();
        this.thisWolfsPack = Pack.getInstance(); //keep track of wolfpack, generates a new one if one doesnt exists for it
        this.packID = nextPackID; //saves packID
    }

    //builds string for displayinformation, ergo pushes "-sleeping" to the name if the animal is sleeping,
    //saves us from a million if/else statements
    @Override
    public DisplayInformation getInformation() {
        boolean small = this.age < 3;

        String key;
        if (hasFungi) {
            key = small ? "wolf-fungi-small" : "wolf-fungi";
        } else {
            key = small ? "wolf-small" : "wolf";
        }

        if (isAsleep) key += "-sleeping";

        return new DisplayInformation(Color.DARK_GRAY, key, true);
    }


    @Override
    public void act(World w) {

        this.intercourseDelayTimer--;
        
        if(!w.contains(this)) return; //errorhandling
        
        boolean hasMovedThisTurn = false;
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
            if(!w.isOnTile(this) || !w.contains(this)) return; //error handling
            if(age(w)) { //age, if it returns true then the animal must die, handled here
                thisWolfsPack.removeFromPack(this);
                killThisAnimal(w, true);
                return;
            }

            //kill based on sporecount
            if(this.sporeCount >= 10) {
                killThisAnimal(w, true); //it doesnt matter if its small or large, since no carcass wil come
                return;
            }


            //mate and save as variable since it wont be moving this turn
            Location currentLocation = w.getLocation(this);
            if (!hasMovedThisTurn && intercourseDelayTimer <= 0) {
                hasMovedThisTurn = checkForMating(w);
                if (hasMovedThisTurn) return;
            }
            

            //other attempt at moving if other fails
            if(!hasMovedThisTurn) {
                hasMovedThisTurn = moveWithPack(w, currentLocation);
            }



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
    public boolean moveWithPack(World w, Location currentLocation) {
        if (wolfPack.isEmpty()) {
            return moveRandomly(w, currentLocation);
        }

        Wolf targetWolf = null;
        int attempts = 0;

        //this could be improved, but works, problem is that attempts could become wolfPack size without trying all wolves,
        //worst case it just moves randomly.
        //we had to do this since all other attemps returned large errors with different threads
        while (attempts < wolfPack.size()) {
            Wolf potentialTarget = wolfPack.get(random.nextInt(wolfPack.size()));
            try {
                if (potentialTarget != this && w.isOnTile(potentialTarget)) {
                    targetWolf = potentialTarget;
                    break;
                }
                //if the other thing fails we just use the exception to move randomly, this is to avoid breaking anything with an exception
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return moveRandomly(w, currentLocation);
            }

            attempts++;
        }

        if (targetWolf == null) {
            return moveRandomly(w, currentLocation);
        }

        try {
            Location targetWolfLocation = w.getLocation(targetWolf);
            Set<Location> locations = w.getSurroundingTiles(targetWolfLocation, lookForPackRadius);

            if (locations.contains(currentLocation)) {
                return moveRandomly(w, currentLocation);
            } else {
                return moveTowards(w, currentLocation, targetWolfLocation);
            }
        } catch (IllegalArgumentException e) {
            // fallback in case the wolf was removed between the check and the call
            return moveRandomly(w, currentLocation);
        }
    }

    // takes a random animal in its radius and either eats it or attacks it
    public void hunt(World w) {
        interactWithNearbyAnimals(w, true);
    }


    //used to check for interactWithNearbyAnimals since we need to know what to attack
    @Override
    public boolean canAttack(Animal target, World w) {
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


    //some annoying mating logic, checks if there is a nearby burrow and a nearby wolf it can mate with
    public boolean checkForMating(World w) {
        if (w == null || !w.contains(this) || !w.isOnTile(this)) return false;
        if (this.matingPartner != null) return false;

        Location myLoc = w.getLocation(this);
        Set<Location> neighbors = w.getSurroundingTiles(myLoc);

        WolfBurrow nearbyOwnBurrow = null;
        for (Location loc : neighbors) {
            Object nb = w.getNonBlocking(loc);
            if (nb instanceof WolfBurrow b && b.packIdForBurrow == this.packID) {
                nearbyOwnBurrow = b;
                break;
            }
        }
        if (nearbyOwnBurrow == null) return false;

        Wolf mate = null;

        //checks the other wolf and gives it a pattern variable
        for (Location loc : neighbors) {
            Object tile = w.getTile(loc);
            if (tile instanceof Wolf other
                    && other != this
                    && other.getPackID() == this.packID
                    && other.isAsleep == false
                    && other.matingPartner == null) {
                mate = other;
                break;
            }
        }
        if (mate == null) return false;

        this.matingPartner = mate;
        mate.matingPartner = this;


        nearbyOwnBurrow.startMating(this, mate);

        // remove from pack list to avoid stale references to deleted wolves
        //removed for now, since other wolves would otherwise call it
        thisWolfsPack.removeFromPack(this);
        thisWolfsPack.removeFromPack(mate);

        // go down the hole
        w.remove(this);
        w.remove(mate);
        this.intercourseDelayTimer = 8;
        return true;
    }


    //pretty simple, has a 6% chance to create a burrow if it doesnt have a burrow
    public void createBurrowIfDoesntHaveBurrow(World w) {
        if(!w.contains(this) || !w.isOnTile(this)) return;
        Location here = w.getLocation(this);
        if (!hasBurrow && w.getNonBlocking(here) == null && r.nextInt(100) > 94) {
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


    public WolfBurrow getBurrow() {
        return burrow;
    }
}