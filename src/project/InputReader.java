package project;

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

        Pattern rangePattern = Pattern.compile("(\\d+-\\d+)(?:\\s*\\((\\d+),(\\d+)\\))?");

        Pattern singleAmountPattern = Pattern.compile("(\\d+)(?:\\s*\\((\\d+),(\\d+)\\))?");


        while ((s = br.readLine()) != null) {
            s = s.trim();
            if (s.isEmpty()) {
                continue;
            }

            String[] lineParts = s.split("\\s+", 2);
            if (lineParts.length != 2) {
                continue;
            }

            String entityType = lineParts[0];
            String specification = lineParts[1];

            ArrayList<Integer> amountList = null;
            Location spawnLoc = null;

            Matcher rangeMatcher = rangePattern.matcher(specification);

            if (rangeMatcher.matches()) {

                String range = rangeMatcher.group(1);
                String[] rangeParts = range.split("-");

                amountList = new ArrayList<>();
                amountList.add(Integer.parseInt(rangeParts[0]));
                amountList.add(Integer.parseInt(rangeParts[1]));

                if (rangeMatcher.group(2) != null && rangeMatcher.group(3) != null) {
                    int x = Integer.parseInt(rangeMatcher.group(2));
                    int y = Integer.parseInt(rangeMatcher.group(3));
                    spawnLoc = new Location(x, y);
                }

            } else {
                Matcher amountMatcher = singleAmountPattern.matcher(specification);

                if (amountMatcher.matches()) {
                    amountList = new ArrayList<>();
                    amountList.add(Integer.parseInt(amountMatcher.group(1)));

                    if (amountMatcher.group(2) != null && amountMatcher.group(3) != null) {
                        int x = Integer.parseInt(amountMatcher.group(2));
                        int y = Integer.parseInt(amountMatcher.group(3));
                        spawnLoc = new Location(x, y);
                    }
                } else {
                    System.err.println("Warning: Skipping malformed line specification: " + s);
                    continue;
                }
            }

            EntityConfig config = new EntityConfig(amountList, spawnLoc);

            // Add to list instead of replacing
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