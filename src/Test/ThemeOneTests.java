package Test;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.themeOne.Grass;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeOneTests {

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

        // Setup Display Information (from Tests.java)
        DisplayInformation grassDisplay = new DisplayInformation(Color.GREEN, "grass");
        p.setDisplayInformation(Grass.class, grassDisplay);

        DisplayInformation rabbitDisplay = new DisplayInformation(Color.RED, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, rabbitDisplay);
    }

    // k1 1a
    @Test
    void KOneOneA() {
        // your assertions here
    }

    // k1 1b
    @Test
    void grassSpreadsToAnEmptyNeighbourWithinNSteps() {
        // Arrange
        Location origin = new Location(2, 2);
        w.setTile(origin, new Grass());

        // since its 10% I run a lot of times
        for (int i = 0; i < 50; i++) {
            p.simulate();
        }

        // check if there is at least one grass
        boolean grew = false;
        for (Location n : w.getSurroundingTiles(origin)) {
            if (w.containsNonBlocking(n) && w.getNonBlocking(n) instanceof Grass) {
                grew = true;
                break;
            }
        }
        assertTrue(grew, "Grass should spread to at least one neighbouring tile within 50 steps");
    }

    @Test
    void RabbitGainsEnergyWhenEatingGrass() {
        Location testlocation = new Location(2, 2);

        Rabbit rabbit = new Rabbit();
        rabbit.setEnergy(50);

        w.setTile(testlocation, rabbit);

        Grass grass = new Grass();
        w.setTile(testlocation, grass);

        int energyStart = rabbit.getEnergy();
        int totalEnergy = 150;
        int expectedEnergyGain = 30;
        int RabbitMove = 10;

        p.simulate();

        int finalEnergy = rabbit.getEnergy();

        assertTrue(finalEnergy > energyStart);

        // Calculates that the Rabbit will move after eating grass
        int nettoExpectedEnergy = (energyStart + expectedEnergyGain) - RabbitMove;
        int expectedFinalEnergy = Math.min(nettoExpectedEnergy, totalEnergy);

        assertEquals(expectedFinalEnergy, finalEnergy);
        assertFalse(w.containsNonBlocking(testlocation));
    }

    @Test
    void DoesRabbitsPlaceRandomlyOnMap() {
        int RabbitsPlaced = 100000;
        Map<Location, Integer> placementCounts = new HashMap<>();

        for (int i = 0; i < RabbitsPlaced; i++) {
            Rabbit rabbit = new Rabbit();

            setElement(w, rabbit, size);

            Location location = w.getLocation(rabbit);
            assertNotNull(location);

            placementCounts.put(location, placementCounts.getOrDefault(location, 0) + 1);
            w.remove(rabbit);
        }

        // Calculate average
        int totalTiles = size * size;
        double expectedAverage = (double) RabbitsPlaced / totalTiles;

        // Set deviation
        double deviation = expectedAverage * 0.20;
        int outsideDeviation = 0;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Location l = new Location(x, y);
                int count = placementCounts.getOrDefault(l, 0);
                if (Math.abs(count - expectedAverage) > deviation) {
                    outsideDeviation++;
                }
            }
        }

        double PercentageOutsideDeviation = (double) outsideDeviation / totalTiles;
        assertTrue(PercentageOutsideDeviation < 0.05);
    }

    @Test
    void DoesGrassPlaceRandomlyOnMap() {
        int GrassPlaced = 100000;
        Map<Location, Integer> placementCounts = new HashMap<>();

        for (int i = 0; i < GrassPlaced; i++) {
            Grass grass = new Grass();
            setNonBlockingElement(w, grass, size);

            Location location = w.getLocation(grass);
            assertNotNull(location);

            placementCounts.put(location, placementCounts.getOrDefault(location, 0) + 1);
            w.remove(grass);
        }
        int totalTiles = size * size;
        double expectedAverage = (double) GrassPlaced / totalTiles;

        double deviation = expectedAverage * 0.20;
        int outsideDeviation = 0;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Location l = new Location(x, y);
                int count = placementCounts.getOrDefault(l, 0);
                if (Math.abs(count - expectedAverage) > deviation) {
                    outsideDeviation++;
                }
            }
        }
        double PercentageOutsideDeviation = (double) outsideDeviation / totalTiles;
        // Added missing assertion from original file
        assertTrue(PercentageOutsideDeviation < 0.05);
    }

    // --- Helper Methods ---

    public static void setElement(World w, Object o, int size) {
        Random r = new Random();
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        Location l = new Location(x, y);
        while (!w.isTileEmpty(l)) {
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

        while (w.containsNonBlocking(l)) {
            x = r.nextInt(size);
            y = r.nextInt(size);
            l = new Location(x, y);
        }
        w.setTile(l, o);
    }
}