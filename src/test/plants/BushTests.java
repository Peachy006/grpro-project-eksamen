package test.plants;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;
import project.plants.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.*;
public class BushTests {
    private World world;
    private Program program;

    private Bush bush;

    @BeforeEach
    void setUp() {
        int size = 2;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        bush = new Bush();
    }

    @Test
    void generalBushTest() {
        world.setTile(new Location(0,0), bush);
        DisplayInformation ds = bush.getInformation();
        program.simulate();

        assertEquals("bush-berries", ds.getImageKey());
        assertEquals(Color.RED, ds.getColor());

        for (int i = 0; i < 11; i++) {
            bush.eatBerry();
        }

        bush.act(world);

        ds = bush.getInformation();

        assertEquals("bush", ds.getImageKey());
        assertEquals(Color.GREEN, ds.getColor());


        for(int i = 0; i < 10; i++) {
            program.simulate();
        }

        ds = bush.getInformation();

        assertEquals("bush-berries", ds.getImageKey());
        assertEquals(Color.RED, ds.getColor());
    }
}
