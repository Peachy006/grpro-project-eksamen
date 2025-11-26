package itumulator.executable;

import itumulator.world.Location;
import itumulator.world.World;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.*;

// InputReader takes a text file (provided as a String filename) and stores the world size,
// along with a HashMap where each type (stored as a String) is linked to an ArrayList
// representing the spawn range for that type. If the list contains only one index,
// then no range is defined.
// if a object has a spawn location that will be stored as well
// buffer reader is used in case we need to read big files

public class InputReader {
    private BufferedReader br;
    private String file;

    private int size;
    private HashMap<String, ArrayList<Integer>> map;

    private HashMap<String, Location> spawnLocation;

    //constructr creates HashMap, sets size
    public InputReader(String file) throws IOException {

        //constructor creates a map, sets file name and sets the world size
        br = new BufferedReader(new FileReader(file));

        //pattern used to identify whether a specific range is given.
        Pattern pattern = Pattern.compile("(\\d+-\\d+)\\s(\\(\\d+,\\d+\\))");

        //sets map and grabs world size
        this.size = Integer.parseInt(br.readLine()); // first line is always world size
        this.map =  new HashMap<>();
        this.file = file;

        //temp
        ArrayList<Integer> list;
        String s;

        // while file not empty
        while ((s = br.readLine()) != null) {
            if(s.trim().isEmpty()) {
                continue;
            }

            list = new ArrayList<>();

            // A line will always be 2 words
            String[] line = s.split(" ");

            // The pattern used to recognize whether a range is given or not.
            Matcher matcher = pattern.matcher(line[1]);

            // if pattern matches insert into array list and after hashmap
            if (matcher.matches()) {

                // split up the string
                String[] temp1 = matcher.group(1).split("-");

                // if object has a spawn location
                if (matcher.group(2) != null) {
                    String[] temp2 = matcher.group(2).split("[,()]");
                    Location l =  new Location(Integer.parseInt(temp1[0]), Integer.parseInt(temp1[1]));
                    
                    this.spawnLocation = new HashMap<String,Location>();
                    spawnLocation.put(line[0],l);
                }

                // insert each range number as an int in the array
                list.add(Integer.parseInt(temp1[0]));
                list.add(Integer.parseInt(temp1[1]));

                // insert array into maps type
                map.put(line[0], list);
            } else {
                // inert the amount into map
                list.add(Integer.parseInt(line[1]));
                map.put(line[0], list);
            }
        }
        br.close();
    }

    //-----------Methods----------------------------
    public int getSize() {
        return size;
    }

    public HashMap<String, ArrayList<Integer>> getMap() {
        return map;
    }

    // returns arraylist with spawn for given type
    public ArrayList<Integer> getTypeList(String type) {
        return map.get(type);
    }

    public boolean hasSpawnLocation() {
        return spawnLocation != null;
    }

    public Location getSpawnLocation(String Object) {
        return spawnLocation.get(Object);
    }
}