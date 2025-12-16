package test.plants;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;
import project.plants.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.HashMap;



public class GrassTests {
    private World world;
    private Program program;

    private Grass grass;


    @BeforeEach
    void setUp() {
        int size = 2;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        grass = new Grass();
    }

    @Test
    void generalBushTest() {
        world.setTile(new Location(0,0), grass);

        int count = 0;
        boolean spawnedGrass = false;

        while (count <= 1) {
            count = 0;

            program.simulate();
            HashMap<Object, Location> temp = (HashMap<Object, Location>) world.getEntities();

            for(Location loc : temp.values()) {
                count++;
            }
            spawnedGrass = true;
        }

        assertTrue(spawnedGrass);
    }
}
