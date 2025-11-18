package itumulator.simulator;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.Set;
import java.util.Random;

public class Dog implements Actor{
    Person owner;

    public Dog(Person person) {
        this.owner = person;
    }

    @Override
    public void act(World w) {
        if(owner == null || !w.isOnTile(owner) || !w.isOnTile(this)) {
            return;
        }


    }

    public void followOwner(World w) {
        Random r = new Random();
        if(owner == null || !w.isOnTile(owner) || !w.isOnTile(this)) {
            return;
        }

        Location ownerLocation = w.getLocation(owner);
        Set<Location> possibleTiles = w.getEmptySurroundingTiles(ownerLocation);

        if(!possibleTiles.isEmpty()) {
            ArrayList<Location> list = new ArrayList<>(possibleTiles);
            Location newLocation = list.get(r.nextInt(list.size()));
            w.move(this, newLocation);
        }
    }


}
