import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.themeOne.Grass;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    private Program p;
    private World w;
    private int size;

    @BeforeEach
    void setUp() {
        Random r = new Random();
        size = 5;
        int delay = 1000;
        int display_size = 800;
        p = new Program(size, display_size, delay);
        w = p.getWorld();

        DisplayInformation grassDisplay = new DisplayInformation(Color.GREEN, "grass");
        p.setDisplayInformation(Grass.class, grassDisplay);

        DisplayInformation rabbitDisplay = new DisplayInformation(Color.RED, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, rabbitDisplay);
    }

    @Test
    void KOneOneA() {
        // your assertions here
    }

    @Test
    void grassSpreadsToAnEmptyNeighbourWithinNSteps() {
        // Arrange
        Location origin = new Location(2, 2);
        w.setTile(origin, new Grass());


        //since its 10 % i run a lot of times
        for (int i = 0; i < 50; i++) {
            p.simulate();
        }

        //check if there is at least one grass
        boolean grew = false;
        for (Location n : w.getSurroundingTiles(origin)) {
            if (w.containsNonBlocking(n) && w.getNonBlocking(n) instanceof Grass) {
                grew = true;
                break;
            }
        }
        assertTrue(grew, "Grass should spread to at least one neighbouring tile within 50 steps");
    }








    public static void setElement(World w, Object o, int size) {
        Random r = new Random();
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        Location l = new Location(x, y);
        while(!w.isTileEmpty(l)) {
            x = r.nextInt(size);
            y = r.nextInt(size);
            l = new Location(x, y);
        }
        w.setTile(l, o);
    }

    public static void setNonBlockingElement(World w, Object o, int size) {
        Random r = new Random();
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        Location l = new Location(x, y);

        while(w.containsNonBlocking(l)) {
            x = r.nextInt(size);
            y = r.nextInt(size);
            l = new Location(x, y);
        }
        w.setTile(l, o);
    }
}
