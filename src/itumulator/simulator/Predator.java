package itumulator.simulator;

import itumulator.simulator.themeOne.Rabbit;
import itumulator.simulator.themeTwo.*;
import itumulator.world.*;


import java.util.Set;

public abstract class Predator extends Animal {

    protected boolean hunting;

    protected boolean hasTerritory;
    protected Territory territory;


    protected Predator(int energy, int totalEnergy, int age) {
        super(energy, totalEnergy, age);
        hunting = false;
        hasTerritory = false;
    }

    protected void makeTerritory(World w) {
        this.territory = new Territory(w, this);
        hasTerritory = true;
    }

    public void hunt(World w) {
        int energy = this.getEnergy();
        int totalEnergy = this.getTotalEnergy();

        // make set of animals nearby
        Set<Animal> nearbyAnimals = w.getAll(Animal.class, w.getSurroundingTiles(w.getLocation(this)));

        for (Animal target : nearbyAnimals) {
            // Skip self
            if (target == this) {
                continue;
            }
            
            // if it is prey
            if (target instanceof Prey) {
                int gainEnergy = target.getEnergy();
                
                // Eat the prey
                w.delete(target);
                
                // Gain energy
                int newEnergy = energy + gainEnergy;
                if (newEnergy > totalEnergy) {
                    this.setEnergy(totalEnergy);
                } else {
                    this.setEnergy(newEnergy);
                }

                return;  // Successfully hunted, stop looking

            // if it is a predator attack it
            } else if (target instanceof Predator && energy > 100) {
                this.attack(w, target);
                return;
            }
        }
    }

    protected void attack(World w, Animal target) {
        int targetEnergy = target.getEnergy();
        int dmg = super.getRandomInt(20);

        if (target instanceof Predator) {
            target.setEnergy(targetEnergy - dmg);
        }
    }

    public boolean hasTerritory() {return hasTerritory;}

    public Territory getTerritory() {return territory;}

    ///----------overrides----------
    @Override
    public void eat(){}
}
