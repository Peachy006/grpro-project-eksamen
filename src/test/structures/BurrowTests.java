package test.structures;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;
import project.plants.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.HashMap;


public class BurrowTests {
    private Program program;
    private World world;

    Rabbit rabbit1;

    @BeforeEach
    void setUp() {
        int size = 2;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        rabbit1 = new Rabbit(false);
    }

    @Test
    void generalBurrowTest() {

        world.setTile(new Location(0,0), rabbit1);

        world.setNight();

        // make sure it makes a burrow lol
        for (int i = 0; i < 500; i++) {
            rabbit1.burrowLogic(world, world.getLocation(rabbit1));
        }

        // did rabbit make burrow?
        assertNotNull(rabbit1.getBurrowLocation());

        // let the rabbit get in its burrow
        program.simulate();
        program.simulate();

        HashMap<Object, Location> temp = (HashMap<Object, Location>) world.getEntities();

        // is rabbit in burrow?
        assertNull(temp.get(rabbit1));

        world.setDay();

        program.simulate();


        temp = (HashMap<Object, Location>) world.getEntities();

        // did rabbit leave burrow?
        assertNotNull(temp.get(rabbit1));
    }
}
