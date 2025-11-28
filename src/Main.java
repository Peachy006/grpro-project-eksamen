
import itumulator.executable.*;
import itumulator.simulator.themeOne.Burrow;
import itumulator.simulator.themeOne.Grass;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.simulator.themeTwo.Wolf;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class Main {


    public static void main(String[] args) throws IOException{

        String path = "resources/week-2/t2-1c.txt";


        InputReader reader = new InputReader(path);

        HashMap<String, EntityConfig> configMap = reader.getConfigMap();

        Random r = new Random();
        int size = reader.getSize();
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


        //read input

        for(String type : configMap.keySet()) {
            EntityConfig information = configMap.get(type);
            ArrayList<Integer> listOfAmount = information.getSpawnAmount();

            int spawnCount;

            if(listOfAmount.size() == 2) {
                spawnCount = r.nextInt(listOfAmount.get(1) - listOfAmount.get(0) + 1) + listOfAmount.get(0);
            } else {
                spawnCount = listOfAmount.get(0);
            }

            for(int i = 0; i < spawnCount; i++) {
                createAndPlaceElement(w, type, information.getSpawnLocation(), size);
            }

        }


        p.show();

        for(int i = 0; i < 200; i++) {
            p.simulate();
        }
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
                entity = new Burrow();
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