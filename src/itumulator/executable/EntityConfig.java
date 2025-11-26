package itumulator.executable;

import itumulator.world.Location;

import java.util.ArrayList;

public class EntityConfig {
    private final ArrayList<Integer> spawnAmount;
    private final Location spawnLocation;

    public EntityConfig(ArrayList<Integer> spawnAmount, Location spawnLocation) {
        this.spawnAmount = spawnAmount;
        this.spawnLocation = spawnLocation;
    }

    public ArrayList<Integer> getSpawnAmount() {
        return spawnAmount;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public boolean hasSpawnLocation() {
        return spawnLocation != null;
    }
}
