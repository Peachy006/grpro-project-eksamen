package ourcode.plants;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

import java.awt.*;

public class Fungi implements Actor, DynamicDisplayInformationProvider {

    public Fungi() {}

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.pink, "fungi");
    }

    @Override
    public void act(World world) {

    }
}
