
package itumulator.simulator;

import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public abstract class Animal {


    //moves animal to a location
    public void move(World w, Location target) {
        w.move(this, target);
    }

    //basic random movement
    public boolean moveRandomly(World w, Location currentLocation) {
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
}