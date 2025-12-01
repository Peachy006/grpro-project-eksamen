package itumulator.simulator.themeTwo;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.simulator.Animal;
import itumulator.simulator.Predator;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.world.World;
import itumulator.world.Location;


import java.awt.*;
import java.util.*;

public class Bear extends Predator implements Actor, DynamicDisplayInformationProvider{

    Set<Animal> trespassers;


    public Bear() {
        super(200,200,0);
    }


    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.ORANGE, "bear", true);
    }

    @Override
    public void act(World w) {
        age(w);
        Location l = w.getLocation(this);
        if(territory != null) {
            this.trespassers = territory.getTrespassers();
        }

        if (energy <= 10) {
            w.delete(this);
            return;
        }

        // on spawn make list of territory
        if (!hasTerritory) {
            super.makeTerritory(w);
        }

        // if its hungry start hunting
        hunting = energy < 100;


        // make random move within territory if not hunting
        if (!hunting && territory != null) {
            territory.moveInTerritory(w,this);
            this.hunt(w);
        } else {
            this.moveRandomly(w,l);
            this.hunt(w);
        }
        
        energy -= 10;
    }


    public void eat() {
        // make bushes to eat
    }
}
