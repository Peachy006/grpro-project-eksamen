package itumulator.simulator;

import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    World w;

    @BeforeEach
    public void Setup() {
        w = new World(2);
    }


    //Opgave 3
    @Test
    public void personMovesDuringDay(){
         Person p = new Person(); // Personen vi bruger til at teste
         Location l = new Location(0,0); // Lokationen vi bruger
         w.setDay(); // vi sørger for at det er dag, da dette er vores test case
         w.setCurrentLocation(l); // vi lader som om vi er n˚aet til dette objekt
         w.setTile(l, p); // og placerer personen p˚a lokationen l
         p.act(w); // vi eksekverer ’act’ metoden

         // Herefter skal vi lave vores check, der er flere forskellige muligheder, vi giver her et par stykker:,→
         Location n = w.getLocation(p); // lokationen for personen.
         assertNull(w.getTile(l)); // vi forventer personen har bevæget sig..
         assertNotNull(n); // alts˚a, at den nye lokation ikke er tom.
         assertNotEquals(l, n); // og at den gamle og nye lokation
    }


    //8c''''
    @Test
    public void ifThereIsNoEmptyTiles() {
        Person p = new Person();
        Location l = new Location(0,0);
        w.setDay();
        w.setCurrentLocation(l);
        w.setTile(l, p);
        Person test1 = new Person();
        Person test2 = new Person();
        Person test3 = new Person();
        w.setTile(new Location(0,1), test1);
        w.setTile(new Location(1,0), test2);
        w.setTile(new Location(1,1), test3);
        p.act(w);
        Location n = w.getLocation(p);
        assertNotNull(n);
        assertEquals(l, n);

    }


    @Test
    void testDogSpawnDistribution() {
        int iterations = 20000;
        Map<String, Integer> spawnCounts = new HashMap<>();

        for(int i = 0; i < iterations; i++) {
            World testWorld = new World(5);
            testWorld.setDay();

            Location personLoc = new Location(2, 2);
            Person person = new Person();
            testWorld.setTile(personLoc, person);
            testWorld.setCurrentLocation(personLoc);

            //spawn dog
            Set<Location> surroundingTiles = testWorld.getEmptySurroundingTiles(personLoc);
            List<Location> list = new ArrayList<>(surroundingTiles);
            Random r = new Random();
            Location dogLocation = list.get(r.nextInt(list.size()));
            Dog dog = new Dog(person);
            testWorld.setTile(dogLocation, dog);

            // track location
            String position = (dogLocation.getX() - 2) + "," + (dogLocation.getY() - 2);
            spawnCounts.put(position, spawnCounts.getOrDefault(position, 0) + 1);
        }

        // Print
        System.out.println("\n Here is the spawn distribution for " + iterations + "iterations");
        for(String pos : spawnCounts.keySet()) {
            int count = spawnCounts.get(pos);
            double percent = (count * 100.0) / iterations;
            System.out.println("Position " + pos + ": count:  " + count + " (" +
                    String.format("%.1f%%)", percent));
        }

        //check if anything is over 20%
        boolean fairDistribution = true;
        for(int count : spawnCounts.values()) {
            if((count * 100.0 / iterations) > 20) {
                fairDistribution = false;
            }
        }

        if(fairDistribution) {
            System.out.println("it is random - pass");
        } else {
            System.out.println("its not random - fail");
        }

        assertTrue( fairDistribution);
    }
}
