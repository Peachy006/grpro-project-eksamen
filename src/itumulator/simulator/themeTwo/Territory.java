package itumulator.simulator.themeTwo;

import itumulator.simulator.Actor;
import itumulator.simulator.Animal;
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

    public Territory(World w, Animal animal) {
        this.owner = animal;
        this.world = w;

        this.spawnLocation = w.getLocation(animal);
        this.territorySize = 4;
        this.territory = w.getSurroundingTiles(w.getLocation(animal),territorySize);
    }

    /// move random movement code from animal but with territory
    // moves randomly in territory
    // if animal is outside its territory move twoards it
    public void moveInTerritory(World w, Animal a) {
        Location l = w.getLocation(a);

        if (this.territoryCheck(l)) {
            Set<Location> emptyNeighbours = w.getEmptySurroundingTiles(w.getLocation(a));

            if (emptyNeighbours != null ) {
                ArrayList<Location> neighboursList = new ArrayList<>(emptyNeighbours);

                Random r = new Random();

                Location newLocation = neighboursList.get(r.nextInt(neighboursList.size()));

                //reroll if it is not moving on its territory
                while (!this.territoryCheck(newLocation)) {
                    // in case the list became empty
                    if (emptyNeighbours.isEmpty()) {
                        newLocation = l;
                        continue;
                    }

                    //remove tile that cant be used from the list
                    neighboursList.remove(newLocation);
                    newLocation = neighboursList.get(r.nextInt(neighboursList.size()));
                }
                w.move(a, newLocation);
                w.setCurrentLocation(newLocation);
            }
        } else {
            a.moveTowards(w,l,spawnLocation);
        }
    }

    public boolean territoryCheck(Location l) {
        return world.contains(l);
    }

    public Set<Animal> getTrespassers() {
        Set<Animal> trespassers = new HashSet<>();

        // go throught territory
        for (Location temp : territory) {
            Object o = world.getTile(temp);

            // if object is a animal add it to the list
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
