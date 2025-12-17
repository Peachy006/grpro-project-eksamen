package test.inherits;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.animals.Bear;
import project.animals.Wolf;
import project.inherits.Animal;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TerritoryTests {

    private Program p;
    private World w;
    private int size;

    @BeforeEach
    void setUp() {
        size = 20; // Using the larger size from TestW for better statistical distribution
        int delay = 1000;
        int display_size = 800;

        p = new Program(size, display_size, delay);
        w = p.getWorld();
    }


    @Test
    void DoesGetTerritoryWork(){
        Bear bear = new Bear(false);
        Location startLocation = new Location(5,5);
        w.setTile(startLocation,bear);
        bear.makeTerritory(w);

        Set<Location> territory = bear.getTerritory().getTerritory();
        //It's 80, because it doesn't count the center with.
        assertEquals(80, territory.size());

        assertTrue(territory.contains(new Location(5, 6)));
        assertTrue(territory.contains(new Location(9, 9)));

        //Proof that it doesn't count the center
        assertFalse(territory.contains(new Location(5, 5)));

        assertFalse(territory.contains(new Location(0, 5)));
        assertFalse(territory.contains(new Location(5, 15)));


    }

    @Test
    void DoesgetTrespasserswork(){
        Bear bear = new Bear(false);
        Wolf wolfInside = new Wolf(1, false);
        Wolf wolfOutside = new Wolf(1, false);

        Location bearStartLocation = new Location(4,5);
        Location wolfInsideLocation = new Location(3,2);
        Location wolfOutsideLocation = new Location(15,2);

        w.setTile(bearStartLocation, bear);
        w.setTile(wolfInsideLocation, wolfInside);
        w.setTile(wolfOutsideLocation, wolfOutside);

        bear.makeTerritory(w);

        Set<Animal> trespassers = bear.getTerritory().getTrespassers();

        assertNotNull(trespassers);
        assertEquals(1, trespassers.size());
        //Bear doesn't count itself
        assertFalse(trespassers.contains(bear));

        assertFalse(trespassers.contains(wolfOutside));



    }

    @Test
    void DoesMoveInTerritoryWork() {
        Bear bear = new Bear(false);
        Location spawnLocation = new Location(5, 5);
        w.setTile(spawnLocation, bear);
        bear.makeTerritory(w);


        Location farAway = new Location(0, 0);
        w.move(bear, farAway);


        bear.getTerritory().moveInTerritory(w, bear);

        Location afterMoveOutside = w.getLocation(bear);
        assertTrue(afterMoveOutside.getX() > 0 || afterMoveOutside.getY() > 0);

        Location edgeLocation = new Location(1, 1);
        w.move(bear, edgeLocation);


        bear.getTerritory().moveInTerritory(w, bear);

        Location afterMoveInside = w.getLocation(bear);
        assertTrue(bear.getTerritory().getTerritory().contains(afterMoveInside));
    }

}

