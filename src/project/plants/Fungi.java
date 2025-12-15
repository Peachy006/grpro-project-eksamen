package project.plants;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import project.animals.Carcass;

import java.awt.*;
import java.util.Set;

public class Fungi implements Actor, DynamicDisplayInformationProvider {

    protected int lifespan;

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

            w.remove(this);
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
}
