
package itumulator.simulator;

import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public abstract class Animal {

    protected int energy;
    protected int totalEnergy;
    protected int age;
    protected int dayCount = 0;

    protected Random r;

    public Animal(int energy, int age) {
        this.energy = energy;
        this.totalEnergy = energy;
        this.age = age;

        r = new Random();
    }

    protected void age(World w) {
        dayCount++;
        if(dayCount >= 5) {
            this.age++;
            dayCount = 0;
            totalEnergy -= 10;

            if (energy > totalEnergy) {
                energy = totalEnergy;
            }
            
            if (totalEnergy <= 10 || energy <= 10) {
                w.delete(this);
            }
        }

    }

    // for override
    protected void eat(){}

    ///----------movement----------

    //moves animal to a location
    protected void move(World w, Location target) {
        w.move(this, target);
    }

    //basic random movement
    protected boolean moveRandomly(World w, Location currentLocation) {
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

    ///----------get/set----------
    public int getEnergy() {return energy;}

    public void setEnergy(int energy) {this.energy = energy;}

    protected int getTotalEnergy() {return totalEnergy;}

    protected void setTotalEnergy(int totalEnergy) {this.totalEnergy = totalEnergy;}
}