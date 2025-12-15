package project.plants;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import project.animals.Carcass;

import java.awt.*;
import java.util.Random;
import java.util.Set;

public class Fungi implements Actor, DynamicDisplayInformationProvider {

    protected int lifespan;
    Random r = new Random();

    public Fungi(String size) {
        if(size.equals("large")) lifespan = 9;
        else lifespan = 18;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.pink, "fungi");
    }

    @Override
    public void act(World w) {
        lifespan--;
        lookForOtherCarcassesToSpreadTo(w);
        if(lifespan <= 0){
            if (!w.contains(this) || !w.isOnTile(this)) return;
            Location l = w.getLocation(this);

            w.remove(this);
            fungiTriesToGrowGrass(w, l);

        }
    }

    public void lookForOtherCarcassesToSpreadTo(World w) {
        if(w == null) return;

        if (!w.contains(this) || !w.isOnTile(this)) return;

        Set<Location> neighbours = w.getSurroundingTiles(w.getLocation(this), 2);
        for(Location l : neighbours) {
            if(w.getTile(l) instanceof Carcass carcass) {
                if(carcass.hasFungi()) {
                    continue;
                } else {
                    carcass.setHasFungi(true);
                }
            }
        }
    }

    public void fungiTriesToGrowGrass(World w, Location loc) {
        if(w == null) return;
        Set<Location> neighbours = w.getSurroundingTiles(loc);
        for(Location l : neighbours) {
            if(w.getNonBlocking(l) == null && r.nextDouble(1) > 0.8) {}
        }
    }
}
