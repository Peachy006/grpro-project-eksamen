package project.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import project.inherits.Animal;
import project.inherits.Prey;

import java.awt.*;
import java.util.Set;

public class Scorpion extends Prey implements Actor, DynamicDisplayInformationProvider {


    /*
    This is a Scorpion, it has a small chance to sting a nearby animal, when it stings
    another animal it will be asleep for 8 ticks.
    it wont deal damage, it doesnt eat, it just lives for the energy it has.
     */



    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.pink, "scorpion");
    }

    public Scorpion() {
        super(30, 0, false);
    }

    @Override
    public void act(World w) {
        if(!w.contains(this)) return;
        System.out.println("Energy for scorpion: " + energy);

        if(age(w)) {
            killThisAnimal(w, false);
            return;
        }

        moveRandomly(w, w.getLocation(this));
        sting(w);

    }

    public void sting(World w) {
        Set<Location> neighbours = w.getSurroundingTiles(w.getLocation(this));
        for(Location l : neighbours) {
            if(w.getTile(l) instanceof Animal animal && !(w.getTile(l) instanceof Scorpion )) {
                if(r.nextDouble() > 0.8)
                    animal.setAsleep(8); //asleep for 8 ticks
            }
        }
    }

}
