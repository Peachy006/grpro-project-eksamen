package test.inherits;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.animals.Bear;
import project.animals.Rabbit;
import project.animals.Wolf;

import static org.junit.jupiter.api.Assertions.*;

public class PredatorTests {

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
    void DoesHuntMethodWork() {
        Location predatorLocation = new Location(2, 2);
        Location preyLocation = new Location(2, 3);

        Wolf wolf = new Wolf(1,  false);
        Rabbit rabbit = new Rabbit(  false);

        rabbit.setEnergy(30);
        wolf.setEnergy(50);
        wolf.setTotalEnergy(100);

        w.setTile(predatorLocation,wolf);
        w.setTile(preyLocation,rabbit);

        wolf.hunt(w,rabbit);

        assertEquals(80,wolf.getEnergy());
        assertNull(w.getTile(preyLocation));

        //Testing if we go over TotalEnergy
        Rabbit rabbit2 = new Rabbit(  false);
        rabbit2.setEnergy(100);
        w.setTile(preyLocation,rabbit2);

        wolf.hunt(w,rabbit2);

        assertEquals(100,wolf.getEnergy());
        assertNull(w.getTile(preyLocation));



    }


    @Test
    void DoesAttackMethodWork() {
        Location wolfLocation = new Location(2, 2);
        Location bearLocation = new Location(2, 3);

        Wolf wolf = new Wolf(1,  false);
        Bear bear = new Bear(  false);

        wolf.setEnergy(100);
        bear.setEnergy(100);

        wolf.attack(bear);
        bear.attack(wolf);

        assertTrue(bear.getEnergy() < 100);
        assertTrue(wolf.getEnergy() < 100);



    }

    @Test

    void DoesCanEatMethodWork() {
        Wolf wolf1 =  new Wolf(1,  false);
        Wolf wolf2 =  new Wolf(1,  false);
        Rabbit rabbit = new Rabbit(  false);

        //Make method Public
        assertTrue(wolf1.canEat(rabbit, w));
        assertFalse(wolf1.canEat(wolf2, w));

    }

}
