package itumulator.simulator.themeOne;

import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.world.NonBlocking;

import java.awt.*;
import java.util.Random;
import java.util.Set;

public class Grass implements Actor, NonBlocking {



    @Override
    public void act(World w) {
        DisplayInformation di = new DisplayInformation(Color.GREEN, "grass");
        Random r = new Random();
        if(r.nextBoolean()) {
            Set<Location> neighbours = w.getEmptySurroundingTiles(w.getLocation(this));
        }
    }

}
