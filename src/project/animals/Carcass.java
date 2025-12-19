package project.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import project.plants.Fungi;

import java.awt.*;
import java.util.Random;

public class Carcass implements DynamicDisplayInformationProvider, Actor {

    protected boolean isLargeCarcass;
    protected boolean hasFungi;
    protected int age = 15; // set age, here age just keeps track of lifespan

    public Carcass(boolean isLargeCarcass, boolean hasFungi) {
        this.isLargeCarcass = isLargeCarcass;
        this.hasFungi = hasFungi;
    }

    @Override
    public DisplayInformation getInformation() {
        if(this.isLargeCarcass) return new DisplayInformation(Color.BLACK, "carcass");
        return new DisplayInformation(Color.BLACK, "carcass-small");
    }

    @Override
    public void act(World w) {
        Random r = new Random();
        this.age--;
        if(this.age <= 0) {
            removeCarcass(w); // lifespan runs out
        }

        if(!this.hasFungi && r.nextDouble() > 0.93) {
            this.hasFungi = true;
        }
    }


    //this handles removing the carcass, if the carcass has fungi then a fungi will be placed on the tile of the carcass
    public void removeCarcass(World w) {
        if(w == null) return;

        if (!w.contains(this) || !w.isOnTile(this)) return;

        Location l = w.getLocation(this);
        w.remove(this);
        if(this.hasFungi) {
            if(this.isLargeCarcass) {
                w.setTile(l, new Fungi("large"));
                return;
            }
            w.setTile(l, new Fungi("small"));
        }
    }

    public int getNutritionValue() {
        return (age * 2) + 5;
    }

    public boolean isLargeCarcass() {
        return this.isLargeCarcass;
    }

    public boolean hasFungi() {
        return this.hasFungi;
    }

    public void setHasFungi(boolean hasFungi) {
        this.hasFungi = hasFungi;
    }
}
