package ourcode.inherits;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Territory {
    int territorySize;

    World world;
    Animal owner;
    Location spawnLocation;

    Set<Location> territory;

    public Territory(World world, Animal animal) {
        this.owner = animal;
        this.world = world;

        this.spawnLocation = world.getLocation(animal);
        this.territorySize = 4;
        this.territory = world.getSurroundingTiles(world.getLocation(animal),territorySize);
    }

    /// move random movement code from animal but with territory
    // moves randomly in territory
    // if animal is outside its territory move twoards it
    public void moveInTerritory(World w, Animal thisAnimal) {
        Location animalLocation = w.getLocation(thisAnimal);

        // If outside territory, move towards spawn location
        if (!territory.contains(animalLocation)) {
            thisAnimal.moveTowards(w, animalLocation, spawnLocation);
            return;
        }

        //get empty neighbors that are within territory
        Set<Location> emptyNeighbours = w.getEmptySurroundingTiles(animalLocation);
        
        if (emptyNeighbours.isEmpty()) {
            return; //cant move, so stay  in place
        }
        
        //filter to only neighbors within territory
        ArrayList<Location> validMoves = new ArrayList<>();
        for (Location l : emptyNeighbours) {
            if (territory.contains(l)) {
                validMoves.add(l);
            }
        }
        
        // If no valid moves within territory then stay in place
        if (validMoves.isEmpty()) {
            return;
        }
        
        //move to random location
        Random r = new Random();
        Location newLocation = validMoves.get(r.nextInt(validMoves.size()));
        w.move(thisAnimal, newLocation);
    }

    public Set<Animal> getTrespassers() {
        Set<Animal> trespassers = new HashSet<>();

        // go through territory
        for (Location temp : territory) {
            Object o = world.getTile(temp);

            // if object is an animal add it to the list
            if (o instanceof Animal) {
                trespassers.add((Animal) o);
            }
        }

        return trespassers;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Set<Location> getTerritory() {
        return territory;
    }

    public Animal getTerritoryOwner() {
        return owner;
    }
}
