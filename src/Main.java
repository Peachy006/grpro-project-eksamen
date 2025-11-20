
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.themeOne.Burrow;
import itumulator.simulator.themeOne.Grass;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.Random;



public class Main {


    public static void main(String[] args) {
        Random r = new Random();
        int size = 10;
        int delay = 1000;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World w = p.getWorld();

        // Set display information for Grass
        DisplayInformation grassDisplay = new DisplayInformation(Color.GREEN, "grass");
        p.setDisplayInformation(Grass.class, grassDisplay);

        //Set dislay for rabbti

        DisplayInformation rabbitDisplay = new DisplayInformation(Color.RED, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, rabbitDisplay);


        for(int i = 0; i < 5; i++) {
            Rabbit rabbit = new Rabbit();
            setElement(w, rabbit, size);
        }

        for(int i = 0; i < 10; i++) {
            Burrow b = new Burrow();
            setNonBlockingElement(w, b, size);
        }

        for(int i = 0; i < 10; i++) {
            Grass g = new Grass();
            setNonBlockingElement(w, g, size);
        }





        p.show();

        for(int i = 0; i < 200; i++) {
            p.simulate();
        }
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