import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.Dog;
import itumulator.simulator.Person;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.awt.*;
import java.beans.ParameterDescriptor;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MainTests {
    World w;
    int size = 2;
    @BeforeEach
    public void Setup() {
        w = new World(size);
    }


    //Denne test checker hvad der sker hvis vi forsøger at lave flere karakterer end der er plads til
    //Testen efter mit fiks ligger længere nede ved navnet "testForInfiniteRandomLoopAfterFix"
    @Test
    void testForInfiniteRandomLoop() {
        Random r = new Random();
        assertTimeoutPreemptively(Duration.ofMillis(5000), () -> {
            w.setDay();

            int amount = 5;


            for(int i = 0; i < amount; i++) {
                int x = r.nextInt(size);
                int y = r.nextInt(size);
                Location l = new Location(x, y);


                while(!w.isTileEmpty(l)) {
                    x = r.nextInt(size);
                    y = r.nextInt(size);
                    l = new Location(x, y);
                }
                w.setTile(l, new Person());
            }
        });
    }

    @Test
    void testForInfiniteRandomLoopAfterFix() {
        Random r = new Random();
        assertTimeoutPreemptively(Duration.ofMillis(5000), () -> {
            Person p = new Person();
            w.setDay();

            int amount = 5;

            Location testLocation = new Location((size/2), (size/2));


            for(int i = 0; i < amount; i++) {
                int x = r.nextInt(size);
                int y = r.nextInt(size);
                Location l = new Location(x, y);

                if(worldIsFull(w)) continue;


                while(!w.isTileEmpty(l)) {
                    x = r.nextInt(size);
                    y = r.nextInt(size);
                    l = new Location(x, y);
                }
                w.setTile(l, new Person());
            }
        });
    }


    //8a
    @Test
    void testIfDogIsFollowingOwner() {
        Random r = new Random();
        int size = 10;
        int delay = 1000;
        int display_size = 800;
        ArrayList<Person> persons = new ArrayList<>();
        ArrayList<Dog> dogs = new ArrayList<>();
        Program pro = new Program(size, display_size, delay);
        World w = pro.getWorld();

        Person p = new Person();

        w.setDay();

        int amount = 5;


        for(int i = 0; i < amount; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            if(worldIsFull(w)) continue;


            while(!w.isTileEmpty(l) && w.getEmptySurroundingTiles(l) != null) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }
            Person per = new Person();
            w.setTile(l, per);
            persons.add(per);

            //generate dog for testcase

            Set<Location> neighbours2 = w.getEmptySurroundingTiles(l);
            if(!neighbours2.isEmpty()) {
                List<Location> list2 = new ArrayList<>(neighbours2);
                Location dogLocation = list2.get(r.nextInt(list2.size()));
                Dog dog = new Dog(per);
                w.setTile(dogLocation, dog);
                dogs.add(dog);
                per.dog = dog;
            }
        }

        for(int j = 0; j < 50; j++) {
            pro.simulate();
            for(int k = 0; k < persons.size(); k++) {
                Person per = persons.get(k);
                Dog dog = dogs.get(k);

                if(w.isOnTile(per) && w.isOnTile(dog)) {
                    Location personLoc = w.getLocation(per);
                    Location dogLoc = w.getLocation(dog);

                    assertTrue(isNearby(personLoc, dogLoc));
                }
            }
        }
    }


    private boolean worldIsFull(World w) {
        for (int x = 0; x < w.getSize(); x++) {
            for (int y = 0; y < w.getSize(); y++) {
                if (w.isTileEmpty(new Location(x, y))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isNearby(Location loc1, Location loc2) {
        int dx = Math.abs(loc1.getX() - loc2.getX());
        int dy = Math.abs(loc1.getY() - loc2.getY());

        // Maksimal afstand er 1 i både x og y retning
        return dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0);
    }


}
