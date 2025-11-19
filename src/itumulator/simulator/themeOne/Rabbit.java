package itumulator.simulator.themeOne;

import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.*;
import java.util.*;

public class Rabbit implements Actor {

    int energy = 100;
    int totalEnergy = 100;
    int age = 1;
    int dayCount = 0;
    @Override
    public void act(World w) {

        //aging logic
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

