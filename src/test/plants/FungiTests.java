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

public class FungiTests {
    private World world;
    private Program program;

    private Fungi fungi1;

    Carcass carcass1;
    Carcass carcass2;


    @BeforeEach
    void setUp() {
        int size = 4;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        fungi1 = new Fungi("large");
        carcass1 = new Carcass(true,false);
        carcass2 = new Carcass(true,false);
    }

    @Test
    void doesFungiSpread() {
        world.setTile(new Location(0,0), fungi1);
        world.setTile(new Location(1,0), carcass1);

        fungi1.lookForOtherCarcassesToSpreadTo(world);

        assertTrue(carcass1.hasFungi());
    }

    @Test
    void doesFungiGrowGrass() {
        world.setTile(new Location(0,0), fungi1);

        HashMap<Object, Location> temp = (HashMap<Object, Location>) world.getEntities();
        int count = 0;

        for (Location loc : temp.values()) {
            count++;
        }

        assertEquals(1, count);

        while (count <= 1) {
            fungi1.fungiTriesToGrowGrass(world,world.getLocation(fungi1));
            temp = (HashMap<Object, Location>) world.getEntities();

            for (Location loc : temp.values()) {
                count++;
            }
        }

        assertEquals(2, count);
    }

    @Test
    void doesFungiSpreadFromCarcassToCarcass() {
        world.setTile(new Location(0,0), fungi1);
        world.setTile(new Location(2,2), carcass1);
        world.setTile(new Location(3,3), carcass2);

        fungi1.lookForOtherCarcassesToSpreadTo(world);

        assertTrue(carcass1.hasFungi());

        program.simulate();

        assertTrue(carcass2.hasFungi());
    }
}
