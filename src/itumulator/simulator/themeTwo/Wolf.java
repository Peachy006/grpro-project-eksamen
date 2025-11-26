package itumulator.simulator.themeTwo;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.simulator.Animal;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DisplayInformation;
import java.awt.Color;
import java.util.*;


public class Wolf extends Animal implements Actor, DynamicDisplayInformationProvider {

    protected int totalEnergy = 150;
    protected int energy = totalEnergy;
    protected boolean isRemoved = false;
    protected Random random = new Random();

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "wolf", true);
    }


    public boolean Hunt(World w, Location currentLocation) {
        Set<Location> surroundingTiles = w.getSurroundingTiles(currentLocation);
        Set<Rabbit> nearbyRabbits = w.getAll(Rabbit.class, surroundingTiles);

        if (nearbyRabbits.isEmpty()){
            return false;
        }

        ArrayList<Rabbit> rabbits = new ArrayList<>(nearbyRabbits);
        Rabbit preyRabbit = rabbits.get(random.nextInt(rabbits.size()));

        int gainedEnergy = preyRabbit.getEnergy();
        energy += gainedEnergy;
        if (energy > totalEnergy) {
            energy = totalEnergy;
        }

        w.delete(preyRabbit);
        return true;
    }

    @Override
    public void act(World w) {
        if(!isRemoved) {
            Location currentLocation = w.getLocation(this);
            //Wolf tries to hunt nearby 'Rabbits'
            Hunt(w, currentLocation);

            if (moveRandomly(w, currentLocation)) {
                energy -= 10;
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
