package test.animals;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;
import project.inherits.Territory;
import project.plants.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


public class BearTests {
    private World world;
    private Program program;

    private Bear bear1;

    @BeforeEach
    void setUp() {
        int size = 10;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        bear1 = new Bear(false);
    }

    @Test
    void bearMoves() {
        world.setTile(new Location(0,0), bear1);

        program.simulate();

        assertNotEquals(new Location(0,0), world.getLocation(bear1));
    }

    @Test
    void bearMovesWithinTerritory() {
        boolean wentOutsideOfTerritory = false;

        world.setTile(new Location(5,5), bear1);

        program.simulate();

        Territory territory = bear1.getTerritory();

        Set<Location> territorySet = territory.getTerritory();

        for (int i = 0; i < 50; i++) {
            bear1.setTotalEnergy(400);
            bear1.setEnergy(400);
            program.simulate();

            if (!territorySet.contains(world.getLocation(bear1))) {
                wentOutsideOfTerritory = true;
            }

            assertFalse(wentOutsideOfTerritory);
        }
    }

    @Test
    void bearMovesOutsideTerritoryWhenHungry() {
        boolean wentOutsideOfTerritory = false;
        world.setTile(new Location(5,5), bear1);

        program.simulate();

        Territory territory = bear1.getTerritory();
        Set<Location> territorySet = territory.getTerritory();

        bear1.setTotalEnergy(100);

        for (int i = 0; i < 500; i++) {
            bear1.setTotalEnergy(50);
            program.simulate();

            if (!territorySet.contains(world.getLocation(bear1)) ) {
                wentOutsideOfTerritory = true;
                break;
            }
        }

        assertTrue(wentOutsideOfTerritory);
    }

    @Test
    void bearEatsPrey() {
        Rabbit rabbit = new Rabbit(false);

        world.setTile(new Location(5,5), rabbit);
        world.setTile(new Location(5,6), bear1);

        bear1.hunt(world);

        HashMap<Object, Location> temp = (HashMap<Object, Location>) world.getEntities();

        assertNull(temp.get(rabbit));
    }

    @Test
    void bearEatsBerries() {
        Bush bush = new Bush();
        world.setTile(new Location(5,5), bush);
        world.setTile(new Location(5,6), bear1);

        bear1.setEnergy(300);

        bear1.eatBerries(world);

        assertNotEquals(300, bear1.getEnergy());
    }

    @Test
    void bearAttacksWolf() {
        Wolf wolf = new Wolf(1,false);

        world.setTile(new Location(5,5), wolf);
        world.setTile(new Location(5,6), bear1);

        wolf.setEnergy(300);

        bear1.hunt(world);

        assertNotEquals(300, wolf.getEnergy());
    }

    @Test
    void bearAttacksBear() {
        Bear bear2 = new Bear(false);

        world.setTile(new Location(5,5), bear2);
        world.setTile(new Location(5,6), bear1);

        bear2.setEnergy(300);

        bear1.hunt(world);

        assertNotEquals(300, bear2.getEnergy());
    }
}
