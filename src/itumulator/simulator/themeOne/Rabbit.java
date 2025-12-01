package itumulator.simulator.themeOne;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.simulator.Animal;
import itumulator.simulator.Prey;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.*;
import java.util.*;

public class Rabbit extends Prey implements Actor, DynamicDisplayInformationProvider {

    int energy = 150;
    boolean isBurrowed = false;
    int totalEnergy = 150;
    int age = 1;
    int dayCount = 0;
    int IntercourseDelayTimer = 0;
    boolean aboutToGoDown = false;

    private Location burrowLocation = null;

    public Rabbit() {
        super(150, 150, 0);
    }

    @Override
    public DisplayInformation getInformation() {
        if(this.age < 3) {
            return new DisplayInformation(Color.BLACK, "rabbit-small", true);
        }
        return new DisplayInformation(Color.BLACK, "rabbit-large", true);
    }

    public Location getBurrowLocation() {
        return burrowLocation;
    }


    @Override
    public void act(World w) {
        boolean hasMoved = false;
        Random r = new Random();

        //
        if(!isBurrowed) {
            //aging logic

            age(w);


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
                w.delete(this);
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

    public void age(World w) {
        dayCount++;
        if(dayCount >= 5) {
            dayCount = 0;
            age++;
            totalEnergy -= 10;
            if(age >= 10) {
                w.delete(this);
                return;
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
                        w.setTile(tempNeighboursList.get(r.nextInt(tempNeighboursList.size())), new Rabbit());
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

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }
}
