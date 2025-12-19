package project.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import project.inherits.Prey;
import project.plants.Grass;
import project.structures.Burrow;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.*;
import java.util.*;

public class Rabbit extends Prey implements Actor, DynamicDisplayInformationProvider {

    boolean isBurrowed = false;
    int IntercourseDelayTimer = 0;

    //this was used in an attempt to make the rabbits first get removed from the world when they were on top of the burrow
    boolean aboutToGoDown = false;

    private Location burrowLocation = null;

    public Rabbit(boolean hasFungi) {
        super(150, 1, hasFungi);
    }

    @Override
    public DisplayInformation getInformation() {
        boolean small = this.age < 3;

        String key;
        if (hasFungi) {
            key = small ? "rabbit-fungi-small" : "rabbit-large-fungi";
        } else {
            key = small ? "rabbit-small" : "rabbit-large";
        }

        if (isAsleep) key += "-sleeping";

        return new DisplayInformation(Color.BLACK, key, true);
    }

    public Location getBurrowLocation() {
        return burrowLocation;
    }


    @Override
    public void act(World w) {
        boolean hasMoved = false;
        Random r = new Random();

        //
        if(!isBurrowed && w.contains(this)) {
            //aging logic
            if(age(w)) {
                killThisAnimal(w, false);
                return;
            }
            
            // Check if rabbit died from aging
            if(!w.contains(this)) {
                return;
            }

            Location currentLocation = w.getLocation(this);


            // Check if there's grass at current location before trying to get it
            if (w.containsNonBlocking(currentLocation)) {
                Object nonBlockingObject = w.getNonBlocking(currentLocation);

                if (nonBlockingObject instanceof Grass) {
                    energy += 30;
                    if(energy > totalEnergy) energy = totalEnergy;
                    w.delete(nonBlockingObject);
                }
            }

            reproduce(w);

            burrowLogic(w, currentLocation);



            //move if the rabbit has the energy
            if (energy >= 10 && !isBurrowed) {
                
                // If rabbit has a burrow and its night, move towards it
                if(this.burrowLocation != null && w.isNight()) {
                    if(!moveTowards(w, currentLocation, burrowLocation)) {
                        // if its false its because it for some reason could not move towards the burrow, it will then move randomly
                        if(!moveRandomly(w, currentLocation)) {
                            return;
                        }
                    }
                } else {
                    if(!moveRandomly(w, currentLocation)) {
                        return;
                    }
                }

                energy -= 10;
            } else {
                if(w.contains(this))
                    killThisAnimal(w, false);
                return;
            }



            //go down the rabbithole
            if(burrowLocation != null && w.isOnTile(this) && burrowLocation.equals(w.getLocation(this)) && w.isNight()) {
                isBurrowed = true;
                aboutToGoDown = true;
            }
        }

        if(aboutToGoDown) {
            if(w.contains(this)) {
                w.remove(this);
            }
            aboutToGoDown = false;
        }

        if(isBurrowed && w.isDay()) {
            if(burrowLocation != null && w.isTileEmpty(burrowLocation)) {
                w.setTile(burrowLocation, this);
                isBurrowed = false;
            }
        }
    }

    public void reproduce(World w) {
        Random r = new Random();
        //the rabbit will reproduce if another rabbit is nearby
        Set<Location> neighbours = w.getSurroundingTiles(w.getLocation(this));

        for(Location l : neighbours) {
            if(w.getTile(l) != null && w.getTile(l) instanceof Rabbit tempRabbit) {

                if(tempRabbit.age >= 5 && this.age >= 5 && tempRabbit.IntercourseDelayTimer <= 1 && this.IntercourseDelayTimer <= 1) {
                    tempRabbit.IntercourseDelayTimer = 5;
                    this.IntercourseDelayTimer = 5;

                    Set<Location> tempNeighbours = w.getEmptySurroundingTiles(w.getLocation(this));

                    if(!tempNeighbours.isEmpty()) {
                        ArrayList<Location> tempNeighboursList = new ArrayList<>(tempNeighbours);
                        w.setTile(tempNeighboursList.get(r.nextInt(tempNeighboursList.size())), new Rabbit(false));
                    }
                }
            }
        }
        if(IntercourseDelayTimer > 0) IntercourseDelayTimer--;

    }

    public void burrowLogic(World w, Location currentLocation) {
        Random r = new Random();
        //Digging Burrow logic
        if(this.burrowLocation == null && w.getNonBlocking(w.getLocation(this)) == null) {

            if(r.nextDouble() < 0.15 && !w.containsNonBlocking(currentLocation)) {
                Burrow burrow = new Burrow("small");
                w.setTile(currentLocation, burrow);
                //Set Rabbit to the Burrow it made
                this.burrowLocation = w.getLocation(this);
                burrow.addRabbit(this);
            }
        }

        //Rabbit gets Burrow at night if it doesn't have one
        if(this.burrowLocation == null && w.isNight()){
            //Checks for Burrows
            Set<Location> surroundingBurrows =  w.getSurroundingTiles(w.getLocation(this), w.getSize());
            for(Location l : surroundingBurrows) {
                Object Tile = w.getTile(l);

                if(Tile instanceof Burrow nearBurrow) {
                    this.burrowLocation = w.getLocation(nearBurrow);
                    nearBurrow.addRabbit(this);
                    break;
                }
            }
        }
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }
}
