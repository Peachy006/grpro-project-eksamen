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

public class Bear extends Predator implements Actor {

    ///  !!!TIL SEAN OG WILLIAM
    /// koden er super ophobet, fordi at vi ikke har shared methoeds i animal
    /// koden skal restruktureres, ved at samle delte metoer i animal når vi mødes
    /// vi skal sikkert også lave en prey og predittor class
    /// jeg tænker ikke i vil have jeg roder i det før vi er samlet

    Set<Animal> trespassers;


    public Bear() {super(200,200,0); }


    @Override
    public void act(World w) {
        Location l = w.getLocation(this);
        this.trespassers = territory.getTrespassers();

        if (energy <= 0) {
            w.delete(this);
        }

        if (w.getDayDuration() % w.getDayDuration() == 0 && w.isDay()) {
            this.age(w);

        }

        // on spawn make list of territory
        if (!hasTerritory) {
            super.makeTerritory(w);
        }

        // if its hungru start hunting
        if (energy >= 100) {hunting = true;}
        else {hunting = false;}


        // make random move within territorry if not hunting
        if (!hunting) {
            territory.moveInTerritory(w,this);
            this.hunt(w,this);
        } else {
            this.moveRandomly(w,l);
            this.hunt(w,this);
        }

        energy -= 10;
    }


    public void eat() {
        // make bushes to eat
    }
}

