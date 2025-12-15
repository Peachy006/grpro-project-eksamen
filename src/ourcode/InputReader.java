package ourcode;

import itumulator.executable.EntityConfig;
import itumulator.world.Location;

import java.io.*;
import java.util.*;
import java.util.regex.*;


public class InputReader {
    private BufferedReader br;

    private int size;
    private HashMap<String, ArrayList<EntityConfig>> configMap;


    public InputReader(String file) throws IOException {

        br = new BufferedReader(new FileReader(file));
        this.configMap = new HashMap<>();

        String s = br.readLine();
        if (s == null) {
            throw new IOException("Input file is empty");
        }
        this.size = Integer.parseInt(s.trim());

        // Pattern:
        // ^\\s* - start, optional whitespace
        // ([a-z]+\\s+)? - optional prefix word (like "cordyceps" or "carcass") followed by space
        // ([a-z]+) - entity type (like "rabbit", "wolf", "fungi")
        // \\s+ - required space/spaces
        // ((\\d+-\\d+|\\d+) - amount (either range 1-5 or single 3)
        // (?:\\s*\\((\\d+),(\\d+)\\))? - optional location coordinates like (2,3)
        // \\s*$ - optional trailing whitespace, end
        Pattern linePattern = Pattern.compile("^\\s*([a-z]+\\s+)?([a-z]+)\\s+((\\d+-\\d+|\\d+)(?:\\s*\\((\\d+),(\\d+)\\))?)\\s*$");

        while ((s = br.readLine()) != null) {
            String originalLine = s.trim();
            if (originalLine.isEmpty()) {
                continue;
            }

            Matcher matcher = linePattern.matcher(originalLine);

            if (!matcher.matches()) {
                System.err.println("Warning: Skipping malformed line specification: " + originalLine);
                continue;
            }

            // Group 1 is the optional prefix (with trailing space), Group 2 is the entity type
            String prefix = matcher.group(1);
            String entityType = matcher.group(2);

            // If a prefix exists (like "cordyceps " or "carcass "), prepend it to the entityType
            // to make a unique configuration key, eg. cordyceps_rabbit"
            if (prefix != null) {
                prefix = prefix.trim(); // Remove the trailing space
                entityType = prefix + "_" + entityType;
            }

            //group 4 is the amount/range string
            String amountRangeStr = matcher.group(4);

            //group 5 and 6 are the optional location coordinates
            String xStr = matcher.group(5);
            String yStr = matcher.group(6);

            // debug output
            System.out.println("Read the following: " + entityType + " with amount: " + amountRangeStr);


            ArrayList<Integer> amountList = new ArrayList<>();
            Location spawnLoc = null;

            // parse amount or range
            if (amountRangeStr.contains("-")) {
                // case range: (example: 1-5)
                String[] rangeParts = amountRangeStr.split("-");
                amountList.add(Integer.parseInt(rangeParts[0]));
                amountList.add(Integer.parseInt(rangeParts[1]));
            } else {
                // case single amount: (example: 3)
                amountList.add(Integer.parseInt(amountRangeStr));
            }

            // --- Parse Location ---
            if (xStr != null && yStr != null) {
                int x = Integer.parseInt(xStr);
                int y = Integer.parseInt(yStr);
                spawnLoc = new Location(x, y);
            }

            // --- Create and Store Config ---
            EntityConfig config = new EntityConfig(amountList, spawnLoc);

            // Add to lit instead of replacing
            if (!configMap.containsKey(entityType)) {
                configMap.put(entityType, new ArrayList<>());
            }
            configMap.get(entityType).add(config);
        }
        br.close();
    }


    public int getSize() {
        return size;
    }

    public HashMap<String, ArrayList<EntityConfig>> getConfigMap() {
        return configMap;
    }


    public ArrayList<EntityConfig> getConfigs(String type) {
        return configMap.get(type);
    }

    public ArrayList<Integer> getSpawnAmount(String type) {
        ArrayList<EntityConfig> configs = configMap.get(type);
        return (configs != null && !configs.isEmpty()) ? configs.get(0).getSpawnAmount() : null;
    }


    public Location getSpawnLocation(String type) {
        ArrayList<EntityConfig> configs = configMap.get(type);
        return (configs != null && !configs.isEmpty()) ? configs.get(0).getSpawnLocation() : null;
    }
}