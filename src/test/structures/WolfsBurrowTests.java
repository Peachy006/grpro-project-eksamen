package test.structures;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        world.setTile(new Location(4,4),wolf3);
        world.setTile(new Location(3,4),wolf1);

        program.simulate();

        while (!world.containsNonBlocking(world.getLocation(wolf1))) {
            wolf1.createBurrowIfDoesntHaveBurrow(world);
        }
    }
}
