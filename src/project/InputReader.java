package project;

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
        // ([a-z]+\\s+)? - optional prefix word
        // ([a-z]+) - entity type
        // \\s+ - required spaces
        // ((\\d+-\\d+|\\d+) - amount
        // (?:\\s*\\((\\d+),(\\d+)\\))? - optional location
        // \\s*$ - optional whitespace
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

            String prefix = matcher.group(1);
            String entityType = matcher.group(2);

            //if a prefix excists, like cordyceps or in some cases its carcass, it will be connected with the other word,
            //so you get like "cordyceps rabbit"
            if (prefix != null) {
                prefix = prefix.trim(); // Remove the trailing space
                entityType = prefix + "_" + entityType;
            }

            //amount range
            String amountRangeStr = matcher.group(4);

            //potential specific location
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

            //store config
            EntityConfig config = new EntityConfig(amountList, spawnLoc);

            // Add it
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