package itumulator.simulator;

import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Person implements Actor {
    private Random r = new Random();
    public Dog dog;
    
    @Override
    public void act(World w) {
        System.out.println("Person acting");
        //remove when it becomes night
        if(w.isNight() && w.isOnTile(this)) {
            w.remove(this);
            w.remove(dog);
            return;
        }
        
        // if not on tile, dont act
        if(!w.isOnTile(this)) {
            return;
        }
        
        //move randomly during the day
        Location current = w.getLocation(this);
        Set<Location> neighbours = w.getEmptySurroundingTiles(current);

        if(!neighbours.isEmpty()) {
            List<Location> list = new ArrayList<>(neighbours);
            Location newLocation = list.get(r.nextInt(list.size()));
            w.move(this, newLocation);
        }

        //generate dog
        if(dog == null && r.nextBoolean()) {
            Set<Location> neighbours2 = w.getEmptySurroundingTiles();
            if(!neighbours2.isEmpty()) {
                List<Location> list2 = new ArrayList<>(neighbours2);
                Location dogLocation = list2.get(r.nextInt(list2.size()));
                dog = new Dog(this);
                w.setTile(dogLocation, dog);
            }
        }

        //move with the dog
        if(dog != null && w.isOnTile(dog)) {
            dog.followOwner(w);
        }
    }
}
