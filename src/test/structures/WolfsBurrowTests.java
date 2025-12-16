package test.structures;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.structures.WolfBurrow;

import static org.junit.jupiter.api.Assertions.*;

public class WolfsBurrowTests {
    private Program program;
    private World world;

    Wolf wolf1;
    Wolf wolf2;
    Wolf wolf3;
    Wolf wolf4;

    @BeforeEach
    void setUp() {
        int size = 4;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        wolf1 = new Wolf(1, false);
        wolf2 = new Wolf(1,false);

        wolf3 = new Wolf(2,false);
        wolf4 = new Wolf(2,false);
    }

    @Test
    void DoesWolfDenSpawnWithPack() {
        world.setTile(new Location(0,0),wolf1);
        world.setTile(new Location(1,0),wolf2);

        world.setTile(new Location(3,3),wolf3);
        world.setTile(new Location(2,3),wolf4);

        wolf1.findPack();
        wolf2.findPack();

        assertEquals(wolf1.getPackID(),wolf2.getPackID(), "packID:" + wolf1.getPackID());

        while (wolf1.getBurrow() == null){
            wolf1.createBurrowIfDoesntHaveBurrow(world);
        }

        program.simulate();

        assertNotNull(wolf1.getBurrow());
        assertNotNull(wolf2.getBurrow());

    }
}
