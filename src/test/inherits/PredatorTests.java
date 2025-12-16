package test.inherits;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.animals.Bear;
import project.animals.Carcass;
import project.animals.Rabbit;
import project.animals.Wolf;
import project.inherits.Animal;

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

        w.setTile(wolfLocation,wolf);
        w.setTile(bearLocation,bear);

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

    @Test
    void DoesInteractWithNearbyAnimalsWork() {
        Location wolfLocation = new Location(2, 2);
        Location AnimalLocation = new Location(2, 3);

        Wolf wolf1 = new Wolf(1, false);
        Wolf wolf2 = new Wolf(1,  false);
        Wolf wolf3 = new Wolf(2,  false);
        Rabbit rabbit = new Rabbit(  false);
        Rabbit rabbit1 = new Rabbit(  false);
        Bear bear = new Bear(  false);
        Bear bear1  = new Bear(  false);

        //Testing with a Wolf and a Rabbit
        w.setTile(wolfLocation,wolf1);
        w.setTile(AnimalLocation,rabbit);

        rabbit.setEnergy(30);
        wolf1.setEnergy(50);

        wolf1.interactWithNearbyAnimals(w,true);

        assertEquals(80,wolf1.getEnergy());
        assertFalse(w.getTile(AnimalLocation)instanceof Rabbit);


        //Testing with two wolfs from the same pack
        wolf2.setEnergy(50);
        w.setTile(AnimalLocation,wolf2);

        wolf1.interactWithNearbyAnimals(w,true);

        assertEquals(50,wolf2.getEnergy());
        assertTrue(w.getTile(AnimalLocation)instanceof Wolf);
        w.delete(wolf2);

        //Testing with two wolfs from different packs

        wolf3.setEnergy(50);
        w.setTile(AnimalLocation,wolf3);

        wolf1.interactWithNearbyAnimals(w,true);

        assertTrue(wolf3.getEnergy() < 50);
        assertTrue(w.getTile(AnimalLocation)instanceof Wolf);
        w.delete(wolf3);

        //Testing with a wolf and a bear

        w.setTile(AnimalLocation,bear);
        bear.setEnergy(50);

        wolf1.interactWithNearbyAnimals(w,true);
        assertTrue(w.getTile(AnimalLocation)instanceof Bear);
        assertTrue(bear.getEnergy() < 50);
        w.delete(bear);

        //Testing if wolf interact with Carcass

        w.setTile(AnimalLocation,rabbit);
        rabbit.killThisAnimal(w,true);

        assertTrue(w.getTile(AnimalLocation)instanceof Carcass);

        wolf1.interactWithNearbyAnimals(w,true);

        assertFalse(w.getTile(AnimalLocation)instanceof Carcass);

        //Testing if bear interacts with all animals around it when StopAfterFirstAction is false

        Location bearLocation = new Location(3,3 );
        Location rabbitLocation = new Location(3,2);
        Location caracassLocation = new Location(4,2);
        Location bear1Location = new Location(4,3);


        w.setTile(AnimalLocation,wolf2);
        w.setTile(bearLocation,bear);
        w.setTile(rabbitLocation,rabbit);
        w.setTile(caracassLocation,rabbit1);
        w.setTile(bear1Location,bear1);

        rabbit1.killThisAnimal(w,true);
        //Wolf 1, Wolf 2, bear, bear1, rabbit, rabbit 1

        wolf1.setEnergy(50);
        wolf2.setEnergy(50);
        bear1.setEnergy(50);

        bear.interactWithNearbyAnimals(w,false);

        assertTrue(wolf1.getEnergy() < 50);
        assertTrue(wolf2.getEnergy() < 50);
        assertTrue(bear1.getEnergy() < 50);
        assertFalse(w.getTile(rabbitLocation)instanceof Rabbit);
        assertFalse(w.getTile(caracassLocation)instanceof Carcass);



    }

    @Test
    void DoesCanAttackWorks() {
        Wolf wolf = new Wolf(1, false);
        Wolf wolf1 = new Wolf(1,false);
        Wolf wolf2 = new Wolf(2,false);
        Bear  bear = new Bear(  false);
        Rabbit rabbit =  new Rabbit(  false);

        assertTrue(wolf.canAttack(wolf1,w));
        assertTrue(wolf.canAttack(bear,w));

        assertFalse(wolf.canAttack(rabbit,w));
        assertFalse(wolf.canAttack(wolf2,w));
        assertFalse(wolf.canAttack(wolf,w));
    }


}
