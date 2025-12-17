package test.animals;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;
import project.plants.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


public class RabbitTests {
    private World world;
    private Program program;

    private Rabbit rabbit1;
    private Rabbit rabbit2;

    private Grass grass;

    @BeforeEach
    void setup() {
        int size = 2;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        rabbit1 = new Rabbit(false);
        rabbit2 = new Rabbit(false);

        grass = new Grass();
    }

    @Test
    void doesRabbitMove() {
        Location r1L = new Location(0,0);
        world.setTile(r1L, rabbit1);

        program.simulate();

        assertTrue(world.isTileEmpty(r1L));
    }
    @Test
    void rabbitDiesOfOldAge() {
        world.setTile(new Location(0,0), rabbit1);

        rabbit1.setEnergy(5);

        program.simulate();

        HashMap<Object, Location> temp = (HashMap<Object, Location>) world.getEntities();

        assertNull(temp.get(rabbit1));
    }

    @Test
    void doesRabbitEatGrass() {
        boolean hasEatGrass = false;

        Program tempProgram = new Program(1,1000,800);
        World tempWorld =  tempProgram.getWorld();

        tempWorld.setTile(new Location(0,0), rabbit1);
        tempWorld.setTile(new Location(0,0), grass);

        HashMap<Object, Location> temp = (HashMap<Object, Location>) world.getEntities();
        for (int i = 0; i < 10; i++) {
            if (temp.get(grass) == null) {
                hasEatGrass = true;
                break;
            }
        }

        assertTrue(hasEatGrass);
    }

    @Test
    void doesRabbitReproduce() {
        world.setTile(new Location(0,0), rabbit1);
        world.setTile(new Location(1,0), rabbit2);

        rabbit1.setAge(5);
        rabbit2.setAge(5);

        rabbit1.reproduce(world);

        HashMap<Object, Location> temp = (HashMap<Object, Location>) world.getEntities();
        int counter = 0;
        for (Location l : temp.values()) {
            counter++;
        }

        assertEquals(3, counter);
    }

    @Test
    void isRabbitRemovedAndNotReset() {
        world.setTile(new Location(0,0), rabbit1);
        rabbit1.setAge(2);

        world.setNight();

        while(rabbit1.getBurrowLocation() == null){
            rabbit1.burrowLogic(world, world.getLocation(rabbit1));
        }

        assertNotNull(world.getLocation(rabbit1));
        assertEquals(new Location(0,0), rabbit1.getBurrowLocation());

        program.simulate();
        program.simulate();

        HashMap<Object, Location> temp = (HashMap<Object, Location>) world.getEntities();

        assertNull(temp.get(rabbit1));

        world.setDay();

        program.simulate();
        program.simulate();

        assertEquals(2,rabbit1.getAge());
    }

}
