import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Core.Wrapper;
import org.junit.jupiter.api.Test;


public class ComparatorLibTests {

    @Test
    public void testGetDataForValidFile() throws IOException {

        Object x ="",y = "";
        Wrapper<Object,Object> xyWrapper = new Wrapper<Object, Object>(x,y);
        File f1= new File(System.getProperty("user.dir")+"/src/test/resources/file1.txt");
        File f2 = new File(System.getProperty("user.dir")+"/src/test/resources/file2.txt");
        assertNotNull(xyWrapper.getData(f1,f2));
    }


    @Test
    public void testComparevalidInputsSet1() throws IOException {

        Object x ="a",y = "b";
        Wrapper<Object,Object> xyWrapper = new Wrapper<Object, Object>(x,y);
        Object serResOne = xyWrapper.serializeObjectForComaringResponse(x);
        Object serResTwo = xyWrapper.serializeObjectForComaringResponse(y);
        assertFalse(xyWrapper.compare(serResOne,serResTwo));
    }

    @Test
    public void testComparevalidInputsSet2() throws IOException {

        Object x ="a",y = "a";
        Wrapper<Object,Object> xyWrapper = new Wrapper<Object, Object>(x,y);
        Object serResOne = xyWrapper.serializeObjectForComaringResponse(x);
        Object serResTwo = xyWrapper.serializeObjectForComaringResponse(y);
        assertTrue(xyWrapper.compare(serResOne,serResTwo));
    }
    @Test
    public void testCompareInvalidInputsSet1() throws IOException {

        Object x = null,y = "";
        Wrapper<Object,Object> xyWrapper = new Wrapper<Object, Object>(x,y);
        Object serResOne = xyWrapper.serializeObjectForComaringResponse(x);
        Object serResTwo = xyWrapper.serializeObjectForComaringResponse(y);
        assertFalse(xyWrapper.compare(serResOne,serResTwo));
    }

    @Test
    public void testCompareInvalidInputsSet2() throws IOException {

        Object x = "",y = null;
        Wrapper<Object,Object> xyWrapper = new Wrapper<Object, Object>(x,y);
        Object serResOne = xyWrapper.serializeObjectForComaringResponse(x);
        Object serResTwo = xyWrapper.serializeObjectForComaringResponse(y);
        assertFalse(xyWrapper.compare(serResOne,serResTwo));
    }
}
