package project.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.world.NonBlocking;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Grass implements Actor, NonBlocking, DynamicDisplayInformationProvider {


    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.BLACK, "grass", true);

    }

    @Override
    public void act(World w) {
        Random r = new Random();
        if(r.nextDouble(1) <= 0.1) {
            Set<Location> neighbours = w.getSurroundingTiles(w.getLocation(this));
            ArrayList<Location> neighboursList = new ArrayList<>();
            
            for(Location l : neighbours) {
                // Check if location has no non-blocking OR if it does, it's not Grass
                if(!w.containsNonBlocking(l)) {
                    neighboursList.add(l);
                }
            }
            
            // Spread to a random location without grass
            if(!neighboursList.isEmpty()) {
                Location newLocation = neighboursList.get(r.nextInt(neighboursList.size()));
                w.setTile(newLocation, new Grass());
            }
        }
    }
}
