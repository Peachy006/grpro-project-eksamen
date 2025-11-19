package itumulator.simulator.themeOne;

import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.*;
import java.util.*;

public class Rabbit implements Actor {

    int energy = 100;

    @Override
    public void act(World w) {



        Location currentLocation = w.getLocation(this);
        
        // Check if there's grass at current location before trying to get it
        if (w.containsNonBlocking(currentLocation)) {
            Object nonBlockingObject = w.getNonBlocking(currentLocation);
            
            if (nonBlockingObject instanceof Grass) {
                energy += 30;
                if(energy > 100) energy = 100;
                w.delete(nonBlockingObject);
            }
        }
        
        if (energy >= 10) {
            Random r = new Random();
            Set<Location> neighbours = w.getEmptySurroundingTiles(currentLocation);
            if (neighbours.isEmpty()) return;
            
            ArrayList<Location> neighboursList = new ArrayList<>(neighbours);
            Location newLocation = neighboursList.get(r.nextInt(neighboursList.size()));
            w.move(this, newLocation);
            energy -= 10;
        } else {
            w.delete(this);
        }
    }
}

