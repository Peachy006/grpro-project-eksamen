package test;

import itumulator.executable.DisplayInformation;
import project.EntityConfig;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.InputReader;
import project.animals.Bear;
import project.structures.Burrow;
import project.animals.Rabbit;
import project.animals.Wolf;
import project.plants.Grass;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeOneTests {

    private Program p;
    private World w;
    private int size;
    static String burrowDefaultSize = "small";

    @BeforeEach
    void setUp() {
        size = 10; // Using the larger size from TestW for better statistical distribution
        int delay = 1000;
        int display_size = 800;

        p = new Program(size, display_size, delay);
        w = p.getWorld();

    }

    //Testing helper method for placing Blocking Objects in World
    @Test
    void DoesRabbitsPlaceRandomlyOnMap() {
        int RabbitsPlaced = 100000;
        Map<Location, Integer> placementCounts = new HashMap<>();

        for (int i = 0; i < RabbitsPlaced; i++) {
            Rabbit rabbit = new Rabbit(false);

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

    //Testing helper method for placing NonBlocking Objects in World
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


    @Test //This test is not needed but nice to have because we know from "Rabbit" that Blocking objects is placed randomly on map
    void DoesWolfPlaceRandomlyOnMap() {
        int wolvesPlaced = 10000;
        Map<Location, Integer> placementCounts = new HashMap<>();
        for (int i = 0; i < wolvesPlaced; i++) {
            Wolf wolf = new Wolf(1, false);

            setElement(w, wolf, size);

            Location location = w.getLocation(wolf);
            placementCounts.put(location, placementCounts.getOrDefault(location, 0) + 1);
            w.remove(wolf);
        }

        int Totaltiles = size * size;
        double expectedAverage = (double) Totaltiles / wolvesPlaced;
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

        double percentageOutsideDeviation = (double) outsideDeviation / wolvesPlaced;
        assertTrue(percentageOutsideDeviation < 0.05);

    }

    //Input TESTS
    @Test
    void TestIfWorldMatchInput() throws IOException {

        String path = "resources/week-2/tf2-1.txt";

        InputReader reader = new InputReader(path);
        HashMap<String, ArrayList<EntityConfig>> configMap = reader.getConfigMap();

        int expectedSize = reader.getSize();
        int grassCount = 0;
        int wolfCount = 0;
        int burrowCount = 0;
        int rabbitCount = 0;
        int bearCount = 0;
        int bushCount = 0;

        Program p = new Program(reader.getSize(), 800, 1000);
        World w = p.getWorld();

        Random random = new Random();


        assertEquals(expectedSize, reader.getSize());


        for (String type : configMap.keySet()) {
            ArrayList<EntityConfig> configurations = configMap.get(type);

            for (EntityConfig information : configurations) {
                int spawnCount;
                if (information.getSpawnAmount().size() == 2) {
                    int min = information.getSpawnAmount().get(0);
                    int max = information.getSpawnAmount().get(1);

                    spawnCount = random.nextInt(max - min + 1) + min;
                } else {
                    spawnCount = information.getSpawnAmount().get(0);
                }
                switch (type) {
                    case "grass":
                        grassCount += spawnCount;
                        break;
                    case "wolf":
                        wolfCount += spawnCount;
                        break;
                    case "burrow":
                        burrowCount += spawnCount;
                        break;
                    case "rabbit":
                        rabbitCount += spawnCount;
                        break;
                    case "bear":
                        bearCount += spawnCount;
                        break;
                    case "bush":
                        bushCount += spawnCount;
                        break;

                }
                for (int i = 0; i < spawnCount; i++) {
                    createAndPlaceElement(w, type, information.getSpawnLocation(), reader.getSize());
                }
            }
        }

        assertEquals(expectedSize, w.getSize());

        int actualGrassCount = 0;
        int actualWolfCount = 0;
        int actualBurrowCount = 0;
        int actualBearCount = 0;
        int actualBushCount = 0;
        int actualRabbitCount = 0;

        for (int x = 0; x < w.getSize(); x++) {
            for (int y = 0; y < w.getSize(); y++) {
                Location l = new Location(x, y);

                if (w.containsNonBlocking(l)) {
                    Object entity = w.getNonBlocking(l);
                    if (entity instanceof Grass) actualGrassCount++;
                    if (entity instanceof Burrow) actualBurrowCount++;
                    // if (entity instanceof Bush)  actualBushCount++;
                }
                if (!w.isTileEmpty(l)) {
                    Object entity = w.getTile(l);
                    if (entity instanceof Wolf) actualWolfCount++;
                    if (entity instanceof Rabbit) actualRabbitCount++;
                    if (entity instanceof Bear) actualBearCount++;
                }
            }
        }

        assertEquals(grassCount, actualGrassCount);
        assertEquals(wolfCount, actualWolfCount);
        assertEquals(burrowCount, actualBurrowCount);
        assertEquals(bearCount, actualBearCount);
        assertEquals(bushCount, actualBushCount);
        assertEquals(rabbitCount, actualRabbitCount);
    }


    @Test
        // Test if Bear's actually gets placed in world, more specific test and can be reused to test other Elements
    void TestIfBearIsInWorld() {
        String bearType = "bear";
        int initialEnitityCount = w.getEntities().size();
        Location specificLocation = null;

        createAndPlaceElement(w, bearType, specificLocation, w.getSize());
        int actualBearCount = w.getEntities().size();
        assertEquals(initialEnitityCount + 1, actualBearCount);

        boolean bearFound =  false;
        for (int x = 0; x < w.getSize(); x++) {
            for (int y = 0; y < w.getSize(); y++) {
                Location l = new Location(x, y);

                Object entity = w.getTile(l);

                if (entity != null) {
                    if (entity instanceof Bear) {
                        bearFound = true;
                        assertEquals(Bear.class, entity.getClass());

                        break;
                    }
                }
            }
            if(bearFound) break;
        }
        assertTrue(bearFound);
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

    public static void createAndPlaceElement(World w, String type, Location specificLocation, int size) {

        Object entity = null;
        boolean isBlocking = true;


        switch (type) {
            case ("grass"): {
                entity = new Grass();
                isBlocking = false;
                break;
            }
            case ("burrow"): {
                entity = new Burrow(burrowDefaultSize);
                isBlocking = false;
                break;
            }
            case ("rabbit"): {
                entity = new Rabbit(false);
                isBlocking = true;
                break;
            }
            case ("wolf"): {
                entity = new Wolf(1, false);
                isBlocking = true;
                break;
            }
            case ("bear"): {
                entity = new Bear(false);
                isBlocking = true;
                break;
            }

            default: {
                System.out.println("Invalid entity type");
            }
        }

        if (entity == null) return;
        if (specificLocation != null) {
            w.setTile(specificLocation, entity);
        } else {
            if (isBlocking) {
                setElement(w, entity, size);
            } else {
                setNonBlockingElement(w, entity, size);
            }
        }
    }
}