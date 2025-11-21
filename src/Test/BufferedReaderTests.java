package Test;

import itumulator.executable.TxtHandler;
import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BufferedReaderTests {
    TxtHandler txtH;

    @Test
    void t1_1a() {

        try {
            txtH = new TxtHandler("resources/Week-1-txt-files/t1-1a.txt");
        } catch (IOException e) {
            System.err.println("Failed to load");
        }

        HashMap<String,ArrayList<Integer>> map = txtH.getMap();
        System.out.println(map);
        assertNotNull(txtH.getMap());

        ArrayList<Integer> list = map.get("grass");
        System.out.println(list);

        int n = list.getFirst();
        System.out.println(n);

        assertEquals(3,n);
    }

    @Test
    void t1_3b() {

        try {
            txtH = new TxtHandler("resources/Week-1-txt-files/t1-3b.txt");
        } catch (IOException e) {
            System.err.println("Failed to load");
        }

        HashMap<String,ArrayList<Integer>> map = txtH.getMap();
        System.out.println(map);
        assertNotNull(txtH.getMap());

        ArrayList<Integer> list = map.get("burrow");
        System.out.println("burrow:" + list);


        assertEquals(15,txtH.getSize());
        System.out.println("Size: " + txtH.getSize());
    }

    @Test
    void tf1_1() {
        try {
            txtH = new TxtHandler("resources/Week-1-txt-files/tf1-1.txt");
        } catch (IOException e) {
            System.err.println("Failed to load");
        }

        HashMap<String,ArrayList<Integer>> map = txtH.getMap();
        System.out.println(map);
        assertNotNull(txtH.getMap());

        ArrayList<Integer> list = map.get("burrow");
        System.out.println("burrow:" + list);

        ArrayList<Integer> list1 = map.get("rabbit");
        System.out.println("rabbit" + list1);

        assertEquals(15,txtH.getSize());
        System.out.println("Size: " + txtH.getSize());
    }
}