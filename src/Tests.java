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
    void KOneOneB() {
        Grass grass = new Grass();
        //ensure space all around
        Location l = new Location(1, 1);
        w.setTile(l, grass);
        int counter = 0;
        while(counter < 1) {
            p.simulate();
            Set<Location> neighbours = w.getSurroundingTiles(l);
            for(int i = 0; i < neighbours.size(); i++) {
                if(w.getNonBlocking(l) != null) counter++;
            }
        }
        assertTrue(counter > 0);
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
