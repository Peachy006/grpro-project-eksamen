
package itumulator.simulator;

import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public abstract class Animal {
    private World world;

    protected int energy;
    protected int totalEnergy;
    protected int age;

    protected Random r;

    Animal(int energy, int totalEnergy, int age) {
        this.energy = energy;
        this.totalEnergy = totalEnergy;
        this.age = 0;

        r = new Random();
    }

    protected void age(World w) {
        this.age++;
        totalEnergy = totalEnergy - 10;

        if (energy > totalEnergy) {
            energy = totalEnergy;
        } else if (energy <= 0) {
            this.kill(w);
        }
    }

    protected void kill(World w) {
        w.delete(this);
    }

    // for override
    protected void eat(){}

    protected int getRandomInt(int n) {
        return r.nextInt(n);
    }

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
    protected boolean moveTowards(World w, Location currentLocation, Location targetLocation) {
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
    protected int getEnergy() {return energy;}

    protected void setEnergy(int energy) {this.energy = energy;}

    protected int getTotalEnergy() {return totalEnergy;}

    protected void setTotalEnergy(int totalEnergy) {this.totalEnergy = totalEnergy;}
}