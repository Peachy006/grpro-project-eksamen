package test.inherits;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.animals.Rabbit;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTests {

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


    @Test
    void TestingAgeMethod() {

        Location startLocation = new Location(2, 2);
        Rabbit rabbit = new Rabbit(false);

        w.setTile(startLocation, rabbit);
        int age = rabbit.getAge();

        for (int i = 0; i < 4; i++) {
            rabbit.age(w);

            assertEquals(age, rabbit.getAge());
        }

        rabbit.age(w);
        int expectedAge = age + 1;

        assertEquals(expectedAge, rabbit.getAge());
        assertEquals(140, rabbit.getEnergy());
    }

    @Test
    void TestMoveMethod() {
        Location startLocation = new Location(2, 2);
        Location endLocation = new Location(2, 3);

        Rabbit rabbit = new Rabbit(false);

        w.setTile(startLocation, rabbit);

        assertEquals(startLocation, w.getLocation(rabbit));
        assertNull(w.getTile(endLocation));

        rabbit.move(w, endLocation);
        assertEquals(endLocation, w.getLocation(rabbit));
        assertEquals(rabbit, w.getTile(endLocation));
        assertNull(w.getTile(startLocation));

    }

    @Test
    void TestMoveRandomlyMethod() {
        Location startLocation = new Location(3, 3);
        Rabbit rabbit = new Rabbit(false);
        w.setTile(startLocation, rabbit);

        boolean moved = rabbit.moveRandomly(w, startLocation);
        assertNotEquals(startLocation, w.getLocation(rabbit));
        assertTrue(moved);
        assertNull(w.getTile(startLocation));

    }

    @Test
    void TestMoveTowardsMethod() {
        Location startLocation = new Location(5, 5);
        Location endLocation = new Location(8, 6);

        Rabbit rabbit = new Rabbit(false);
        w.setTile(startLocation, rabbit);

        Location expectedBestLocation = new Location(6, 6);

        boolean moved = rabbit.moveTowards(w, startLocation, endLocation);
        assertTrue(moved);
        Location newLocation = w.getLocation(rabbit);

        assertEquals(expectedBestLocation, newLocation);
        assertNull(w.getTile(startLocation));

    }


    @Test
    void TestKillThisAnimalMethod() {
        Location startLocation = new Location(5, 5);
        Rabbit rabbit = new Rabbit(false);
        w.setTile(startLocation, rabbit);

        assertNotNull(w.getLocation(rabbit));

        rabbit.killThisAnimal(w,false);
        assertNull(w.getLocation(rabbit));
        assertNull(w.getTile(startLocation));

    }

    //Missing test of get/set methods in Animal. Not needed.

}