import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.Dog;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.simulator.Person;

public class Main {

    public static void main(String[] args) {
        Random r = new Random();
        int size = 10;
        int delay = 1000;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World w = p.getWorld();

        int amount = 5;
//sean was here
        for(int i = 0; i < amount; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            //test if there are no empty tiles
            if(w.getSurroundingTiles(l, size) == null) continue;

            while(!w.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }
            w.setTile(l, new Person());
        }
        
        DisplayInformation di = new DisplayInformation(Color.RED, "person");
        p.setDisplayInformation(Person.class, di);
        DisplayInformation dogDisplay = new DisplayInformation(Color.green);
        p.setDisplayInformation(Dog.class, dogDisplay);

        p.show();

        for(int i = 0; i < 200; i++) {
            p.simulate();
        }
    }
}