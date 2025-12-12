package ourcode.structures;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import ourcode.animals.Rabbit;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Burrow implements Actor, NonBlocking, DynamicDisplayInformationProvider {

    private final String size;
    public Set<Rabbit> rabbits = new HashSet<>();


    public Burrow(String size) {
        this.size = size;
    }

    @Override
    public DisplayInformation getInformation() {
        // Optional: pass true for random orientation each render
        if(this.size.equals("small")) {
            return new DisplayInformation(Color.BLACK, "hole-small", true);
        } else if(this.size.equals("large")) {
            return new DisplayInformation(Color.BLACK, "hole", true);
        } else {
            System.out.println("Invalid burrow size: " + this.size + " (it will now be set to large as default");
            return new DisplayInformation(Color.BLACK, "hole", true);
        }
    }

    @Override
    public void act(World w) {
        Set<Rabbit> rabbitsCopy = new HashSet<>(rabbits);
        for(Rabbit rabbit : rabbitsCopy) {
            if(!w.contains(rabbit)) {
                rabbits.remove(rabbit);
            }
        }
    }

    public void addRabbit(Rabbit rabbit) {
        this.rabbits.add(rabbit);
    }

}
