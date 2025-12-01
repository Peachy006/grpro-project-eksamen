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
    protected int packID;
    protected int lookForPackRadius = 3;


    public Wolf(int packID){
        this.packID = packID;
    }

    public int getPackID() {
        return packID;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "wolf", true);
    }


    public boolean hunt(World w, Location currentLocation) {
        //Wolf looks for nearby Prey
        Set<Location> surroundingTiles = w.getSurroundingTiles(currentLocation);
        Set<Rabbit> nearbyRabbits = w.getAll(Rabbit.class, surroundingTiles);

        //If there's no prey, hunts end
        if (nearbyRabbits.isEmpty()){
            return false;
        }

        //Choose a random Rabbit and eats it
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

    public boolean moveWithPack(World w, Location currentLocation) {
        //Looks for wolves in pack
        Set<Location> searchPack = w.getSurroundingTiles(currentLocation, lookForPackRadius);
        Set<Wolf> nearbyWolves = w.getAll(Wolf.class, searchPack);

        ArrayList<Wolf> wolvesPack = new ArrayList<>();
        for (Wolf wolf : nearbyWolves) {
            if(wolf == this) {
                continue;
            }
            if(wolf.getPackID() == this.packID) {
                wolvesPack.add(wolf);
            }
        }
        if (wolvesPack.isEmpty()){
            return false;
        }
        Wolf targetWolf = wolvesPack.get(random.nextInt(wolvesPack.size()));

        Location targetLocation = w.getLocation(targetWolf);

        return moveTowards(w, currentLocation, targetLocation);
    }

    @Override
    public void act(World w) {
        if(!isRemoved) {
            Location currentLocation = w.getLocation(this);
            //Wolf tries to hunt nearby 'Rabbits'
            hunt(w, currentLocation);

            if(moveWithPack(w, currentLocation)){
                energy -= 10;
                return;
            }

            if (moveRandomly(w, currentLocation)) {
                energy -= 10;
            } else {
                return;
            }
        }

        if (energy <= 10 && !isRemoved) {
            w.delete(this);
            isRemoved = true;
        }
    }
}