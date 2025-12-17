package test.animals;
import itumulator.executable.*;
import itumulator.world.*;
import org.junit.jupiter.api.AfterEach;
import project.animals.*;
import project.plants.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CarcassTests {
    private World world;
    private Program program;

    private Carcass carcass;
    private Bear bear;


    @BeforeEach
    void setUp() {
        int size = 10;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        bear = new Bear(false);
        carcass = new Carcass(true,false);
    }

    // failed
    @Test
    void doesAnimalLeaveCarcassOnDeath() {

        world.setTile(new Location(0,0),bear);

        program.simulate();

        Location loc = world.getLocation(bear);

        bear.killThisAnimal(world,true);

        assertNotNull(world.getTile(loc));
    }

    @Test
    void doesCarcassRotAway() {
        world.setTile(new Location(0,0),carcass);

        for (int i = 0; i < 20; i++) {
            program.simulate();
        }

        assertNull(world.getTile(new Location(0,0)));
    }

    @Test
    void doesAnimalEarCarcass() {
        world.setTile(new Location(0,0),bear);
        world.setTile(new Location(1,0),carcass);

        bear.interactWithNearbyAnimals(world,true);

        assertNull(world.getTile(new Location(1,0)));
    }
}
