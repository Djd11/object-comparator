import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Core.Wrapper;


public class ComparatorLibTests {

    @Test
    public void testGetDataForValidFile() throws IOException {

        File f1= new File(System.getProperty("user.dir")+"/src/test/resources/file1.txt");
        File f2 = new File(System.getProperty("user.dir")+"/src/test/resources/file2.txt");

        Wrapper<Object,Object> xyWrapper = new Wrapper<Object, Object>();
        assertNotNull(xyWrapper.getData(f1,f2));
    }


    @Test
    public void testComparevalidInputsSet1() throws IOException {

        Object x ="a",y = "b";
        Object serResOne = TestHelper.serializeObjectForComaringResponse(x);
        Object serResTwo = TestHelper.serializeObjectForComaringResponse(y);

        Wrapper<Object,Object> xyWrapper = new Wrapper<>();
        assertFalse(xyWrapper.compare(serResOne,serResTwo));
    }

    @Test
    public void testComparevalidInputsSet2() throws IOException {

        Object x ="a",y = "a";
        Object serResOne = TestHelper.serializeObjectForComaringResponse(x);
        Object serResTwo = TestHelper.serializeObjectForComaringResponse(y);

        Wrapper<Object,Object> xyWrapper = new Wrapper<>();
        assertTrue(xyWrapper.compare(serResOne,serResTwo));
    }
    @Test
    public void testCompareInvalidInputsSet1() throws IOException {

        Object x = null,y = "";
        Object serResOne = TestHelper.serializeObjectForComaringResponse(x);
        Object serResTwo = TestHelper.serializeObjectForComaringResponse(y);

        Wrapper<Object,Object> xyWrapper = new Wrapper<>();
        assertFalse(xyWrapper.compare(serResOne,serResTwo));

    }

    @Test
    public void testCompareInvalidInputsSet2() throws IOException {

        Object x = "",y = null;
        Object serResOne = TestHelper.serializeObjectForComaringResponse(x);
        Object serResTwo = TestHelper.serializeObjectForComaringResponse(y);

        Wrapper<Object,Object> xyWrapper = new Wrapper<>();
        assertFalse(xyWrapper.compare(serResOne,serResTwo));
    }
}
