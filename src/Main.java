
import itumulator.executable.*;
import project.InputReader;
import project.structures.Burrow;
import project.plants.Grass;
import project.animals.Rabbit;
import project.animals.Bear;
import project.plants.Bush;
import project.animals.Wolf;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


// todo: ryk filer ud fra simulator
//

public class Main {


    //The default size for burrows is set here, this can be changed based on who makes the burrow, but when a burrow is spawned by file it will be set to large as defualt
    static String burrowDefaultSize = "small";


    public static void main(String[] args) throws IOException{

        String path = "resources/week-2/tf2-4.txt";


        InputReader reader = new InputReader(path);

        HashMap<String, ArrayList<EntityConfig>> configMap = reader.getConfigMap();

        Random r = new Random();
        int size = reader.getSize();
        int display_size = 800;
        int delay = 1000;
        Program p = new Program(size, display_size, delay);
        World w = p.getWorld();

        int nextPackID = 1;


        //read input

        for(String type : configMap.keySet()) {
            ArrayList<EntityConfig> configurations = configMap.get(type);

            for(EntityConfig information : configurations) {
                ArrayList<Integer> listOfAmount = information.getSpawnAmount();

                int spawnCount;

                if(listOfAmount.size() == 2) {
                    spawnCount = r.nextInt(listOfAmount.get(1) - listOfAmount.get(0) + 1) + listOfAmount.get(0);
                } else {
                    spawnCount = listOfAmount.get(0);
                }

                for(int i = 0; i < spawnCount; i++) {
                    createAndPlaceElement(w, type, information.getSpawnLocation(), size, nextPackID);
                }

                if(type.equals("wolf")) {
                    nextPackID++;
                }
            }
        }

        p.show();

        for(int i = 0; i < 200; i++) {
            p.simulate();
        }
    }

    public static void createAndPlaceElement(World w, String type, Location specificLocation, int size, int packID) {

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
            case("bush"): {
                entity = new Bush();
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