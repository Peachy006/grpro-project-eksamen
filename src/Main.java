import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        Random r = new Random();
        int size = 10;
        int delay = 1000;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World w = p.getWorld();


        p.show();

        for(int i = 0; i < 200; i++) {
            p.simulate();
        }
    }

    public void setElement(World w, Object o, int size) {
        Random r = new Random();
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        Location l = new Location(x, y);
        while(!w.isTileEmpty(l)) {
            x = r.nextInt(size);
            y = r.nextInt(size);
        }
        w.setTile(l, o);
    }

    public void setNonBlockingElement(World w, Object o, int size) {
        Random r = new Random();
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        Location l = new Location(x, y);
        while(!w.containsNonBlocking(l)) {
            x = r.nextInt(size);
            y = r.nextInt(size);
        }
        w.setTile(l, o);
    }
}