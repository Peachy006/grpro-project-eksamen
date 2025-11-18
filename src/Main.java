import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
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
}