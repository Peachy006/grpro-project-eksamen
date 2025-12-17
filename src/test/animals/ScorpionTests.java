package test.animals;

import itumulator.executable.*;
import itumulator.world.*;
import project.animals.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScorpionTests {
    private World world;
    private Program program;

    Scorpion scorpion;
    Rabbit rabbit;

    @BeforeEach
    void setUp() {
        int size = 2;
        int delay = 1000;
        int display_size = 800;

        program = new Program(size, display_size, delay);
        world = program.getWorld();

        scorpion = new Scorpion();
        rabbit = new Rabbit(false);
    }

    @Test
    void ScorpionStingTest() {
        world.setTile(new Location(0,0),scorpion);
        world.setTile(new Location(1,0),rabbit);

        DisplayInformation ds = rabbit.getInformation();

        assertEquals("rabbit-small", ds.getImageKey());

        scorpion.sting(world);

        assertEquals("-sleeping", ds.getImageKey());

        ds = rabbit.getInformation();

        world.remove(scorpion);
        for (int i = 0; i < 10; i++) {
            program.simulate();
        }

        ds = rabbit.getInformation();

        assertEquals("rabbit-small", ds.getImageKey());
    }
}
