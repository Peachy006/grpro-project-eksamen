package itumulator.simulator.themeOne;

import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;
import java.util.Random;
import itumulator.simulator.themeOne.Grass;

public class Rabbit implements Actor {

    int energy = 100;

    @Override
    public void act(World w) {

        Object nonBlockingObject = w.getNonBlocking(w.getLocation(this));

        if (nonBlockingObject != null && nonBlockingObject.getClass() == Grass.class) {
            energy += 30;
            if(energy > 100) energy = 100;
        }

        if (energy >= 10) {

            Random r = new Random();
            Set<Location> neighbours = w.getSurroundingTiles(w.getLocation(this));
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

