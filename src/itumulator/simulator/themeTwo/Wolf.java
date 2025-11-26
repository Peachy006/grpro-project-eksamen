package itumulator.simulator.themeTwo;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.simulator.Animal;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DisplayInformation;
import java.awt.Color;


public class Wolf extends Animal implements Actor, DynamicDisplayInformationProvider {

    protected int totalEnergy = 150;
    protected boolean isRemoved = false;

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "wolf", true);
    }


    @Override
    public void act(World w) {
        if(!isRemoved) {
            Location currentLocation = w.getLocation(this);

            if (moveRandomly(w, currentLocation)) {
                totalEnergy -= 10;
            } else {
                return;
            }
        }

        if (totalEnergy <= 10 && !isRemoved) {
            w.delete(this);
            isRemoved = true;
        }
    }
}
