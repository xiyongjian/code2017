package gosigma.study;

import org.junit.Test;
import static org.junit.Assert.*;

public class MyUnitTest {

    @Test
    public void testGetTheStringArray() {

        String[] expectedArray = {"one", "two", "three"};
        String[] result = {"one", "two", "three"};

        assertArrayEquals(expectedArray, new String[]{"one", "two", "three"});
        assertArrayEquals(expectedArray, result);
    }
}