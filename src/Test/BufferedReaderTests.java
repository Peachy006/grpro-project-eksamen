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

        assertNotNull(txtH.getMap());
        System.out.println(txtH.getMap());


        ArrayList<Integer> list = txtH.getTypeList("grass");
        int temp = list[0];int temp = list[0];

        assertEquals(list[0],3);
    }
}
