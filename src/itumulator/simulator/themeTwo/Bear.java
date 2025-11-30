package itumulator.simulator.themeTwo;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.simulator.Animal;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.world.World;
import itumulator.world.Location;


import java.awt.*;
import java.util.*;

public class Bear extends Animal implements Actor {

    ///  !!!TIL SEAN OG WILLIAM
    /// koden er super ophobet, fordi at vi ikke har shared methoeds i animal
    /// koden skal restruktureres, ved at samle delte metoer i animal når vi mødes
    /// vi skal sikkert også lave en prey og predittor class
    /// jeg tænker ikke i vil have jeg roder i det før vi er samlet
    ///
    ///
    int energy;
    int totalEnergy;
    int age;
    Boolean hunting;

    boolean hasTerritory;
    Territory territory;
    Set<Animal> trespassers;


    public  Bear() {
        this.energy = 200;
        this.totalEnergy = 200;
        this.age = 0;

        this.hasTerritory = false;
        this.hunting = false;
    }


    @Override
    public void act(World w) {
        Location l = w.getLocation(this);
        this.trespassers = territory.getTrespassers();

        if (energy <= 0) {
            w.delete(this);
        }

        if (w.getDayDuration() % w.getDayDuration() == 0 && w.isDay()) {
            this.age();

        }


        // on spawn make list of territory
        if (!hasTerritory) {
            this.territory = new Territory(w,this);
            hasTerritory = true;
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


    /// edited version of wolfs hunt method
    /// this should be in Animal or make a new class called preditor
    /// also make a new class called prey so that it can eat all animals thats prey
    /// also make a new class for ediable plants
    /// Animal needs a getEnergy, so that this can become a "attack all creatures" method and we dont need deffendTerritorry

    // attacks or eats animal nearby animals
    public void hunt(World w, Animal a) {

        // make list of animals nearby
        Location l = w.getLocation(a);
        Set<Animal> nearbyAnimals = w.getAll(Animal.class, w.getSurroundingTiles(l));

        if (nearbyAnimals.isEmpty()) {
            return;
        } else {

            for (Animal temp : nearbyAnimals) {
                // if trespasser is a rabbit, kill it
                if (temp instanceof Rabbit) {
                    int gainEnergy = ((Rabbit) temp).getEnergy();
                    energy += gainEnergy;

                    if (energy > totalEnergy) {
                        energy = totalEnergy;
                    }

                    // if it is a bear or wolf and not hunting attack it
                } else if (temp instanceof Bear || temp instanceof Wolf || energy > 100) {
                    ///  make a "getEnergy" method in animal so we can make them fight
                    ///  prefreably we want a "fight" method in Animal
                }
            }
        }
    }

    public void eat() {
        // make bushes to eat
    }

    public boolean hasTerritory() {
        return hasTerritory;
    }

    public void age() {
        this.age++;
        totalEnergy = totalEnergy - 10;

        if (energy > totalEnergy) {
            energy = totalEnergy;
        }
    }
}

