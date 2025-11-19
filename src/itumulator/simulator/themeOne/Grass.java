package itumulator.simulator.themeOne;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.world.NonBlocking;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Grass implements Actor, NonBlocking {

    @Override
    public void act(World w) {
        Random r = new Random();
        if(r.nextBoolean()) {
            Set<Location> neighbours = w.getSurroundingTiles(w.getLocation(this));
            ArrayList<Location> neighboursList = new ArrayList<>();
            
            for(Location l : neighbours) {
                // Check if location has no non-blocking OR if it does, it's not Grass
                if(!w.containsNonBlocking(l) || !(w.getNonBlocking(l) instanceof Grass)) {
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
