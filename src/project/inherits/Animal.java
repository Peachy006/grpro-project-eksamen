
package project.inherits;

import itumulator.world.World;
import itumulator.world.Location;
import project.animals.Carcass;

import java.util.*;

public abstract class Animal {

    protected int energy;
    protected int totalEnergy;
    protected int age;
    protected int dayCount;
    protected boolean hasFungi;
    protected int sporeCount;
    public boolean isAsleep = false;
    int wakingUpCounter = 0;

    protected Random r;

    public Animal(int energy, int age, boolean hasFungi) {
        this.dayCount = 0;
        this.sporeCount = 0;
        this.energy = energy;
        this.totalEnergy = energy;
        this.age = age;
        this.hasFungi = hasFungi;

        r = new Random();
    }


    //return true if aged
    public boolean age(World w) {

        if(isAsleep) {
            wakingUpCounter++;
            if(wakingUpCounter <= 0) {
                isAsleep = false;
            }
        }

        if(this.hasFungi) {
            this.sporeCount++;
        }
        dayCount++;
        if(dayCount >= 5) {
            this.age++;
            dayCount = 0;
            totalEnergy -= 10;

            if (energy > totalEnergy) {
                energy = totalEnergy;
            }

            if (totalEnergy <= 10 || energy <= 10) {
                return true;
            }
        }

        return false;

    }

    // for override
    protected void eat(){}

    ///----------movement----------

    //moves animal to a location
    public void move(World w, Location target) {
        w.move(this, target);
    }

    //basic random movement
    public boolean moveRandomly(World w, Location currentLocation) {
        if(isAsleep) return false;
        Set<Location> emptyNeighbours = w.getEmptySurroundingTiles(currentLocation);

        if (emptyNeighbours.isEmpty()) {
            return false;
        }

        ArrayList<Location> neighboursList = new ArrayList<>(emptyNeighbours);
        Random r = new Random();
        Location newLocation = neighboursList.get(r.nextInt(neighboursList.size()));
        w.move(this, newLocation);

        return true;
    }

    //pathfinding movement
    public boolean moveTowards(World w, Location currentLocation, Location targetLocation) {
        if(isAsleep) return false;
        Set<Location> emptyNeighbours = w.getEmptySurroundingTiles(currentLocation);

        if (emptyNeighbours.isEmpty()) {
            return false;
        }

        Location bestMove = null;
        double shortestDistance = Double.MAX_VALUE;

        for (Location candidate : emptyNeighbours) {
            int dx = candidate.getX() - targetLocation.getX();
            int dy = candidate.getY() - targetLocation.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestMove = candidate;
            }
        }

        if (bestMove != null) {
            w.move(this, bestMove);
            return true;
        }

        return false;
    }

    public void killThisAnimal(World w, boolean isLargeAnimal) {

        if(this.hasFungi) {
            w.delete(this);
        } else {
            Location thisCurrentLocation = w.getLocation(this);
            w.delete(this);
            w.setTile(thisCurrentLocation, new Carcass(isLargeAnimal, false));
        }
    }

    ///----------get/set----------
    public int getEnergy() {return energy;}

    public void setEnergy(int energy) {this.energy = energy;}

    public int getTotalEnergy() {return totalEnergy;}

    public void setTotalEnergy(int totalEnergy) {this.totalEnergy = totalEnergy;}

    public boolean hasFungi() { return hasFungi; }

    public int getAge() {return age;}

    public void setAge(int age) {
        this.age = age;
    }

    public void setAsleep(int amount) {isAsleep = true; wakingUpCounter = amount;}

    public void printInfoAboutAnimalEveryStep() {
        System.out.println("Energy: " + energy + " Total Energy: " + totalEnergy + " Age: " + age + " entityType: " + this.getClass().getSimpleName());
    }
}