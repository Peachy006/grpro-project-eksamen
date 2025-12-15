package test.animals;


import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.structures.Burrow;
import project.structures.WolfBurrow;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RabbitTests {
    private World w;
    private Program p;

    private Rabbit r1;

    @BeforeEach
    void setup() {
        int size = 2;
        int delay = 1000;
        int display_size = 800;

        p = new Program(size, display_size, delay);
        w = p.getWorld();

        r1 = new Rabbit(false);
    }

    @Test
    void rabbitMoves() {
        Location r1L = new Location(0,0);
        w.setTile(r1L, r1);

        p.simulate();

        assertTrue(w.isTileEmpty(r1L));
    }
    @Test
    void rabbitDies() {
        w.setTile(new Location(0,0), r1);

        r1.setEnergy(10);

        p.simulate();

        Set<Location> set = w.getSurroundingTiles(new Location(0,0));

        for (Location l : set) {
            if (!w.isTileEmpty(l)) {
                assertTrue(w.isTileEmpty(l));
                break;
            }
        }
    }

    @Test
    void rabbitBurrow() {
        
    }

}
