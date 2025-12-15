package project.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import project.inherits.Animal;
import project.inherits.Predator;
import project.plants.Bush;
import itumulator.world.World;
import itumulator.world.Location;


import java.awt.*;
import java.util.*;

public class Bear extends Predator implements Actor, DynamicDisplayInformationProvider{

    Set<Animal> trespassers;


    public Bear(boolean hasFungi) {
        super(200,0, hasFungi);
    }


    @Override
    public DisplayInformation getInformation() {
        if(this.hasFungi) return new DisplayInformation(Color.ORANGE, "bear-fungi", true);
        return new DisplayInformation(Color.ORANGE, "bear", true);
    }

    @Override
    public void act(World w) {

        if(!w.contains(this)) {
            return;
        }

        //age returns true if dead
        if(age(w)) {
            killThisAnimal(w, true);
            return;
        }

        eatBerries(w);

        Location l = w.getLocation(this);
        if(territory != null) {
            this.trespassers = territory.getTrespassers();
        }

        if (energy <= 10) {
            killThisAnimal(w, true);
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

    @Override
    public void hunt(World w) {
        interactWithNearbyAnimals(w, false); //the boolean is just if u want it to only interact once per step
    }

   public void eatBerries(World w) {
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
