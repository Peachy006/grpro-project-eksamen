package itumulator.simulator.themeTwo;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.simulator.Actor;

import java.awt.*;

public class Bush implements DynamicDisplayInformationProvider, Actor {

    protected boolean hasBerries;
    protected int berryCount;
    protected int berryCountMax;


    public Bush() {
        this.hasBerries = true;
        this.berryCount = 10;
        this.berryCountMax = 10;
    }


    @Override
    public DisplayInformation getInformation() {
        if(this.hasBerries) return new DisplayInformation(Color.RED, "bush-berries");
        return new DisplayInformation(Color.GREEN, "bush");
    }

    public void act(World w) {

    }


}
