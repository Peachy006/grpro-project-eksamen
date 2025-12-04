package itumulator.simulator;

import itumulator.simulator.themeOne.Rabbit;
import itumulator.simulator.themeTwo.*;
import itumulator.world.*;


import java.util.Set;

public abstract class Predator extends Animal {

    protected boolean hunting;

    protected boolean hasTerritory;
    protected Territory territory;


    protected Predator(int energy, int age) {
        super(energy, age);
        hunting = false;
        hasTerritory = false;
    }

    protected void makeTerritory(World w) {
        this.territory = new Territory(w, this);
        hasTerritory = true;
    }

    // this kills animal its hunting
    // make sure that predators dont eat predators
    public void hunt(World w, Animal target) {

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
    }

    protected void attack(Animal target) {
        int targetEnergy = target.getEnergy();
        int dmg = r.nextInt(20);

        if (target instanceof Predator) {
            target.setEnergy(targetEnergy - dmg);
        }
    }

    public boolean hasTerritory() {return hasTerritory;}

    public Territory getTerritory() {return territory;}

    protected abstract void hunt(World w);

    ///----------overrides----------
    @Override
    public void eat(){}
}
