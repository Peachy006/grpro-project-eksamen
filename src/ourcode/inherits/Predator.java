package ourcode.inherits;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.animals.Carcass;

import java.util.Set;

public abstract class Predator extends Animal {

    protected boolean hunting;

    protected boolean hasTerritory;
    protected Territory territory;

    protected Predator(int energy, int age, boolean hasFungy) {
        super(energy, age, hasFungy);
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

        // eat the prey
        w.delete(target);

        // gain energy
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


    protected final boolean interactWithNearbyAnimals(World w, boolean stopAfterFirstAction) {
        if (!w.contains(this) || !w.isOnTile(this)) return false;

        Set<Location> neighbours = w.getSurroundingTiles(w.getLocation(this));
        Set<Animal> nearbyAnimals = w.getAll(Animal.class, neighbours);

        boolean acted = false;

        for(Location l : neighbours) {
            Object o = w.getTile(l);
            if(o instanceof Carcass carcass) {
                carcass.removeCarcass(w);
                addEnergyFromFood(carcass.getNutritionValue());

            }
        }

        for (Animal target : nearbyAnimals) {
            if (target == null) continue;
            if (!w.contains(target) || !w.isOnTile(target)) continue; // target might have been deleted earlier

            if (canEat(target, w)) {
                hunt(w, target);
                acted = true;
            } else if (canAttack(target, w)) {
                attack(target);
                acted = true;
            }

            if (acted && stopAfterFirstAction) {
                return true;
            }
        }

        return acted;
    }

    protected boolean canEat(Animal target, World w) {
        return target instanceof Prey;
    }

    protected void addEnergyFromFood(int gainEnergy) {
        int newEnergy = energy + gainEnergy;
        if (newEnergy > totalEnergy) {
            this.setEnergy(totalEnergy);
        } else {
            this.setEnergy(newEnergy);
        }
    }


     //decide if this predator can attack this target
     // default: can attack other predators (not itself)

    protected boolean canAttack(Animal target, World w) {
        return target instanceof Predator && target != this;
    }

    public boolean hasTerritory() { return hasTerritory; }

    public Territory getTerritory() { return territory; }

    protected abstract void hunt(World w);

    ///----------overrides----------
    @Override
    public void eat() {}
}
