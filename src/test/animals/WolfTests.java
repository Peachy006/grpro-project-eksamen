package test.animals;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class WolfTests {
    private World w;
    private Program p;

    @BeforeEach
    void setup() {
        int size = 2;
        int delay = 1000;
        int display_size = 800;

        p = new Program(size, display_size, delay);
        w = p.getWorld();
    }

    @Test
    void testWolfHuntPrey() {
        Wolf wolf = new Wolf(1, false);
        Rabbit rabbit = new Rabbit(false);

        Location wolfL = new Location(0, 0);
        Location rabbitL = new Location(1, 0);

        w.setTile(wolfL, wolf);
        w.setTile(rabbitL, rabbit);

        wolf.setEnergy(150);
        wolf.hunt(w);

        // if the rabbit is not on tile assume it's been eaten
        assertTrue(w.isTileEmpty(rabbitL));

        // did it gain energy from the rabbit
        assertNotEquals(150, wolf.getEnergy());
        assertEquals(200, wolf.getEnergy());
    }

    @Test
    void testWolfHuntPredator() {
        Wolf wolf = new Wolf(1, false);
        Bear bear = new Bear(false);

        Location wolfL = new Location(0, 0);
        Location bearL = new Location(1, 0);

        w.setTile(wolfL, wolf);
        w.setTile(bearL, bear);

        bear.setEnergy(350);

        wolf.hunt(w);

        // did wolf deal dmg to bear??
        assertNotEquals(350, bear.getEnergy());
    }


    /// this one might have problems since wolfs need one act to create a pack each
    /// fix: make a pack controller that controlls all packs that is spawned once in main
    @Test
    void testWolfHuntWolf() {
        Wolf wolf1 = new Wolf(1, false);
        Wolf wolf2 = new Wolf(2, false);

        Location wolf1L = new Location(0, 0);
        Location wolf2L = new Location(1, 0);

        w.setTile(wolf1L, wolf1);
        w.setTile(wolf2L, wolf2);

        wolf1.findPack();
        wolf2.findPack();

        wolf1.setEnergy(350);
        wolf2.setEnergy(350);

        wolf1.hunt(w);

        assertNotEquals(350, wolf2.getEnergy());
    }

    @Test
    void testWolfMoveTest() {
        Wolf wolf = new Wolf(1, false);

        Location wolfL = new Location(0, 0);

        w.setTile(wolfL, wolf);

        wolf.moveWithPack(w, wolfL);

        assertTrue(w.isTileEmpty(wolfL));
    }

    @Test
    void testWolfFindsPack() {
        Wolf wolf1 = new Wolf(1, false);
        Wolf wolf2 = new Wolf(1, false);

        wolf1.findPack();

        p.simulate();

        assertEquals(1, wolf2.getPackID());
        assertEquals(1, wolf2.getPackID());
    }

    @Test
    void doesWolfReproduce() {
        Wolf wolf1 = new Wolf(1, false);
        Wolf wolf2 = new Wolf(1, false);

        w.setTile(new Location(0, 0), wolf1);
        w.setTile(new Location(1, 0), wolf2);

        wolf1.findPack();
        wolf2.findPack();

        wolf1.createBurrowIfDoesntHaveBurrow(w);


        HashMap<Object, Location> temp = (HashMap<Object, Location>) w.getEntities();
       for (int i = 0; i < 10; i++){
            p.simulate();
            temp = (HashMap<Object, Location>) w.getEntities();
            if (temp.size() == 3){
                break;
            }
        }

        assertEquals(3, temp.size());
    }
}
