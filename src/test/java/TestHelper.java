import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TestHelper {
    /*
    Takes Objects to serialize it
    returns byte array
 */
    public static byte[] serializeObjectForComaringResponse(Object obj) throws IOException
    {
        ByteArrayOutputStream responseObjectBytesOut = new ByteArrayOutputStream();
        ObjectOutputStream responseObjectOutputstream = new ObjectOutputStream(responseObjectBytesOut);
        responseObjectOutputstream.writeObject(obj);
        responseObjectOutputstream.flush();

        byte[] bytes = responseObjectBytesOut.toByteArray();
        responseObjectBytesOut.close();
        responseObjectOutputstream.close();
        return bytes;
    }
}
