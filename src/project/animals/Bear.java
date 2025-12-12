package project.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import project.inherits.Animal;
import project.inherits.Predator;
import project.inherits.Prey;
import itumulator.world.World;
import itumulator.world.Location;
import project.plants.Bush;


import java.awt.*;
import java.util.*;

public class Bear extends Predator implements Actor, DynamicDisplayInformationProvider{

    Set<Animal> trespassers;
    protected int maxEnergy;


    public Bear() {
        super(200,0);
        this.maxEnergy = 200;
    }


    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.ORANGE, "bear", true);
    }

    @Override
    public void act(World w) {

        if(!w.contains(this)) {
            return;
        }

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
        hunting = energy < maxEnergy / 2;


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

    @Override
    public void hunt(World w) {

        if(!w.contains(this) || !w.isOnTile(this)) {
            return;
        }

        Set<Location> tiles = w.getSurroundingTiles(w.getLocation(this));
        Set<Animal> nearbyPrey = w.getAll(Animal.class, tiles);

        for (Animal target : nearbyPrey) {
            if ( target instanceof Prey) {
                super.hunt(w, target);
            } else if (target instanceof Predator) {
                super.attack(target);
            }
        }
    }

   public void eat(World w) {
       Location l = w.getLocation(this);
       Set<Location> neighbors = w.getSurroundingTiles(l);
       
       for(Location loc : neighbors) {
           if(w.getTile(loc) instanceof Bush bush) {
               bush.eatBerry();
               this.energy += 5;
           }
       }
   }
}
