package itumulator.executable;

import java.io.*;
import java.util.*;
import java.util.regex.*;

// TxtHandler takes a text file (provided as a String filename) and stores the world size,
// along with a HashMap where each type (stored as a String) is linked to an ArrayList
// representing the spawn range for that type. If the list contains only one index,
// then no range is defined.
// buffer reader is used in case we need to read big files

public class InputReader {
    BufferedReader br;
    Pattern pattern;

    //Object Type, World Size and said HashMap
    int size;
    static String type;
    String file;
    HashMap<String, ArrayList<Integer>> map;

    //constructr creates HashMap, sets size
    public InputReader(String file) throws IOException {

        // Constructor creates a HashMap, sets file name and sets the world size.
        br = new BufferedReader(new FileReader(file));

        // Pattern used to identify whether a specific range is given.
        this.pattern = Pattern.compile("(\\d+-\\d+)");

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


    // returns the range(diffrence between max and min)given for spawn type.
    public int getRange(String type) {
        ArrayList<Integer> list = map.get(type);

        // if there is not a range return zero else return range.
        if (list.size() == 1) {
            return 0;
        } else {
            return list.getLast() - list.getFirst() + 1;
        }
    }
}