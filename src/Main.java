
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.executable.InputReader;
import itumulator.simulator.themeOne.Burrow;
import itumulator.simulator.themeOne.Grass;
import itumulator.simulator.themeOne.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class Main {


    public static void main(String[] args) throws IOException{

        String path = "resources/Week-1-txt-files/t1-2a.txt";


        InputReader reader = new InputReader(path);




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


        HashMap<String, ArrayList<Integer>> map = reader.getMap();

        for(String s : map.keySet()) {
            ArrayList<Integer> list = map.get(s);
            if(list.size() == 2) {
                int randomNumberInInterval = r.nextInt(list.get(1)-list.get(0)) + list.get(0);

                for(int j = 0; j < randomNumberInInterval; j++) {
                    switch(s) {
                        case("grass"): {
                            Grass grass = new Grass();
                            setNonBlockingElement(w,grass,size);
                            break;
                        }
                        case("burrow"): {
                            Burrow b = new Burrow();
                            setNonBlockingElement(w,b,size);
                            break;
                        }
                        case("rabbit"): {
                            Rabbit rab = new Rabbit();
                            setElement(w,rab,size);
                            break;
                        }
                    }
                }
            } else {
                for(int i = 0; i < list.get(0); i++) {
                    switch(s) {
                        case("grass"): {
                            Grass grass = new Grass();
                            setNonBlockingElement(w,grass,size);
                            break;
                        }
                        case("burrow"): {
                            Burrow b = new Burrow();
                            setNonBlockingElement(w,b,size);
                            break;
                        }
                        case("rabbit"): {
                            Rabbit rab = new Rabbit();
                            setElement(w,rab,size);
                            break;
                        }
                    }
                }
            }
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