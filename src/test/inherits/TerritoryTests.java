package test.inherits;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.animals.Bear;

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

        //Proof that i doesn't counter the center
        assertFalse(territory.contains(new Location(5, 5)));

        assertFalse(territory.contains(new Location(0, 5)));
        assertFalse(territory.contains(new Location(5, 15)));
    }
}

