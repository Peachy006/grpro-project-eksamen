package ourcode.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.plants.Fungi;

import java.awt.*;

public class Carcass implements DynamicDisplayInformationProvider, Actor {

    protected boolean isLargeCarcass;
    protected boolean hasFungi;
    protected int age = 15;

    public Carcass(boolean isLargeCarcass, boolean hasFungi) {
        this.isLargeCarcass = isLargeCarcass;
        this.hasFungi = hasFungi;
    }

    @Override
    public DisplayInformation getInformation() {
        if(this.isLargeCarcass) return new DisplayInformation(Color.BLACK, "carcass");
        return new DisplayInformation(Color.BLACK, "carcass-small");
    }

    public int getNutritionValue() {
        return (age * 2) + 5;
    }

    @Override
    public void act(World w) {
        this.age--;
        if(this.age <= 0) {
            removeCarcass(w);
        }
    }

    public void removeCarcass(World w) {
        Location l = w.getLocation(this);
        w.remove(this);
        if(this.hasFungi) {
            w.setTile(l, new Fungi());
        }
    }
}
