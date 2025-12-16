package test.inherits;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.animals.Wolf;
import project.inherits.Pack;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PackTests {
    private Program p;
    private World w;
    private int size;

    @BeforeEach
    void setUp() {
        size = 10; // Using the larger size from TestW for better statistical distribution
        int delay = 1000;
        int display_size = 800;

        p = new Program(size, display_size, delay);
        w = p.getWorld();
    }

    // This test, test all relevant methods in Pack
    // It doesn't test if there are wolves with different pack ID's
    //IMPORTANT! You need to run test in PackTests in isolation because of PackGetInstance logic
    @Test
    void TestAllPackMethods () {

        Pack packInstance = Pack.getInstance();

        Wolf wolf1 = new Wolf(1, false);
        Wolf wolf2 = new Wolf(1, false);
        Wolf wolf3 = new Wolf(1, false);

        Location startLocation1 = new Location(5, 4);
        Location startLocation2 = new Location(5, 5);
        Location startLocation3 = new Location(5, 6);

        w.setTile(startLocation1, wolf1);
        w.setTile(startLocation2, wolf2);
        w.setTile(startLocation3, wolf3);

        //Adds Wolves to flock
        packInstance.addToPack(wolf1);
        packInstance.addToPack(wolf2);
        packInstance.addToPack(wolf3);

        ArrayList<Wolf> packList = packInstance.getPack(wolf1);

        //Check List size and make sure it's not null
        assertNotNull(packList);
        assertEquals(3, packList.size());

        //See if the wolves are a part of the list
        assertTrue(packList.contains(wolf1));
        assertTrue(packList.contains(wolf2));
        assertTrue(packList.contains(wolf3));

        packInstance.removeFromPack(wolf2);

        //Check new size
        assertEquals(2, packList.size());

        //See if wolf 2 is removed and wolf 1 and 3 is still there
        assertFalse(packList.contains(wolf2));
        assertTrue(packList.contains(wolf1));
        assertTrue(packList.contains(wolf3));


        Wolf wolf4 = new Wolf(1, false);
        packInstance.addToPack(wolf4);

        assertEquals(3, packList.size());
        assertTrue(packList.contains(wolf4));
    }


    @Test
    void TestIfThereAreMorePacks () {
        Pack packInstance = Pack.getInstance();

        Wolf wolf1 = new Wolf(1, false);
        Wolf wolf2 = new Wolf(1, false);
        Wolf wolf3 = new Wolf(2, false);

        packInstance.addToPack(wolf1);
        packInstance.addToPack(wolf2);
        packInstance.addToPack(wolf3);

        ArrayList<Wolf> packList1 = packInstance.getPack(wolf1);
        ArrayList<Wolf> packList2 = packInstance.getPack(wolf3);

        assertNotNull(packList1);
        assertEquals(2, packList1.size());
        assertTrue(packList1.contains(wolf1));
        assertTrue(packList1.contains(wolf2));

        assertNotNull(packList2);
        assertEquals(1, packList2.size());
        assertTrue(packList2.contains(wolf3));
    }

}
