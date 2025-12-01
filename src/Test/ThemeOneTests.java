package Test;

import itumulator.executable.DisplayInformation;
import itumulator.executable.EntityConfig;
import itumulator.executable.InputReader;
import itumulator.executable.Program;
import itumulator.simulator.themeOne.Burrow;
import itumulator.simulator.themeOne.Grass;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.simulator.themeTwo.Bear;
import itumulator.simulator.themeTwo.Wolf;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
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

    @Test
    void DoesRabbitCreateBurrow() {
        Location start = new Location(2, 2);
        Rabbit rabbit = new Rabbit();
        //Sets energy so Rabbit doesn't die and the test fails
        rabbit.setEnergy(5000);

        w.setTile(start, rabbit);
        Location finalBurrowLocation = null;
        //Simulates 100 steps and stops if Rabbit creates a Burrow
        for (int i = 0; i < 100; i++) {
            p.simulate();

            if (rabbit.getBurrowLocation() != null) {
                finalBurrowLocation = rabbit.getBurrowLocation();
                break;
            }
        }
        //Check if Rabbit knows Location of Burrow
        boolean rabbitKnowsBurrow = finalBurrowLocation != null;
        assertTrue(rabbitKnowsBurrow);

        //Check Burrow on the location Rabbit gives
        if (rabbitKnowsBurrow) {
            boolean burrowOnMap = w.containsNonBlocking(finalBurrowLocation) && w.getNonBlocking(finalBurrowLocation) instanceof Burrow;
            assertTrue(burrowOnMap);

            //Checks if Burrow has registered Rabbit
            Burrow burrowIsCreated = (Burrow) w.getNonBlocking(finalBurrowLocation);
            assertTrue(burrowIsCreated.rabbits.contains(rabbit));

        }
    }

    @Test
    void DoesRabbitReproduce() {
        Location locationA = new Location(2, 2);
        Location locationB = new Location(2, 3);
        Location locationC = new Location(3, 3);

        Rabbit rabbitA = new Rabbit();
        Rabbit rabbitB = new Rabbit();

        rabbitA.setAge(5);
        rabbitB.setAge(5);

        w.setTile(locationA, rabbitA);
        w.setTile(locationB, rabbitB);

        assertTrue(w.isTileEmpty(locationC));

        int startRabbitCount = 2;

        p.simulate();

        int endRabbitCount = 0;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Object object = w.getTile(new Location(x, y));
                if (object instanceof Rabbit) {
                    endRabbitCount++;
                }
            }
        }

        assertEquals(startRabbitCount + 1, endRabbitCount);

        //We can expand the test to see if their cd-Timer does go on cd
    }


    @Test
    void DoesRabbitDieWhenAging() {
        Location startLocation = new Location(2, 2);
        Rabbit rabbit = new Rabbit();

        rabbit.setAge(9);
        rabbit.setDayCount(4);

        w.setTile(startLocation, rabbit);
        assertTrue(w.contains(rabbit));

        p.simulate();

        assertFalse(w.contains(rabbit));
        assertTrue(w.isTileEmpty(startLocation));

    }

    @Test
    void TestIfWorldMatchInput() throws IOException {

        String path = "resources/week-2/tf2-2.txt";

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

        int nextPackID = 1;
        
        for (String type : configMap.keySet()) {
            ArrayList<EntityConfig> configurations = configMap.get(type);
            
            for(EntityConfig information : configurations) {
                int spawnCount;
                if(information.getSpawnAmount().size() == 2) {
                    int min = information.getSpawnAmount().get(0);
                    int max = information.getSpawnAmount().get(1);

                    spawnCount = random.nextInt(max - min + 1) + min;
                } else {
                    spawnCount = information.getSpawnAmount().get(0);
                }
                switch(type) {
                    case "grass": grassCount += spawnCount; break;
                    case "wolf": wolfCount += spawnCount; break;
                    case "burrow": burrowCount += spawnCount; break;
                    case "rabbit": rabbitCount += spawnCount; break;
                    case "bear": bearCount += spawnCount; break;
                    case "bush": bushCount += spawnCount; break;

                }
                for(int i = 0; i < spawnCount; i++) {
                    createAndPlaceElementWithPackID(w, type, information.getSpawnLocation(), reader.getSize(), nextPackID);
                }
                
                if(type.equals("wolf")) {
                    nextPackID++;
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

        for(int x = 0; x < w.getSize(); x++) {
            for(int y = 0; y < w.getSize(); y++) {
                Location l = new Location(x, y);

                if(w.containsNonBlocking(l)) {
                    Object entity = w.getNonBlocking(l);
                    if (entity instanceof Grass) actualGrassCount++;
                    if (entity instanceof Burrow) actualBurrowCount++;
                   // if (entity instanceof Bush)  actualBushCount++;
                }
                if(!w.isTileEmpty(l)) {
                    Object entity = w.getTile(l);
                    if(entity instanceof Wolf) actualWolfCount++;
                    if (entity instanceof Rabbit) actualRabbitCount++;
                    if (entity instanceof Bear)  actualBearCount++;
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


        switch(type) {
            case("grass"): {
                entity = new Grass();
                isBlocking = false;
                break;
            }
            case("burrow"): {
                entity = new Burrow(burrowDefaultSize);
                isBlocking = false;
                break;
            }
            case("rabbit"): {
                entity = new Rabbit();
                isBlocking = true;
                break;
            }
            case("wolf"): {
                entity = new Wolf(1);
                isBlocking = true;
                break;
            }
            case("bear"): {
                entity = new Bear();
                isBlocking = true;
                break;
            }

            default: {
                System.out.println("Invalid entity type");
            }
        }

        if(entity == null) return;
        if(specificLocation != null) {
            w.setTile(specificLocation, entity);
        } else {
            if(isBlocking) {
                setElement(w, entity, size);
            } else {
                setNonBlockingElement(w, entity, size);
            }
        }
    }
    
    public static void createAndPlaceElementWithPackID(World w, String type, Location specificLocation, int size, int packID) {

        Object entity = null;
        boolean isBlocking = true;


        switch(type) {
            case("grass"): {
                entity = new Grass();
                isBlocking = false;
                break;
            }
            case("burrow"): {
                entity = new Burrow(burrowDefaultSize);
                isBlocking = false;
                break;
            }
            case("rabbit"): {
                entity = new Rabbit();
                isBlocking = true;
                break;
            }
            case("wolf"): {
                entity = new Wolf(packID);
                isBlocking = true;
                break;
            }
            case("bear"): {
                entity = new Bear();
                isBlocking = true;
                break;
            }

            default: {
                System.out.println("Invalid entity type");
            }
        }

        if(entity == null) return;
        if(specificLocation != null) {
            w.setTile(specificLocation, entity);
        } else {
            if(isBlocking) {
                setElement(w, entity, size);
            } else {
                setNonBlockingElement(w, entity, size);
            }
        }
    }
}