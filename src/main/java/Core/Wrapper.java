package Core;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.HttpResponse;
import java.io.*;

import Core.HttpClient.HttpClientWrapper;

/*
    Wrapper library class implements IComparator interface

 */
public class Wrapper<X,Y>  implements IComparator<X,Y>  {

    private final String ENCODING_TYPE = "UTF-8";

    private X x = null;
    private Y y = null;

    public Wrapper () {}

    public Wrapper(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /*
        Take two file inputs
        return a wrapped responses

     */
    public Wrapper<X,Y> getData(File f1, File f2) throws IOException {

        if (!f1.exists() || f1.length() == 0 || !f2.exists() || f2.length() == 0) {

            System.out.println("Inputs files could be empty please check " + f1.getName() + " : " + f2.getName() + " exit...");
            System.exit(0);
        }

        HttpResponse responseOne = null, responseTwo = null;
        String fileOneLine = null, fileTwoLine = null;
        Integer errorCodeResponseOne = null,errorCodeResponseTwo = null;
        BufferedReader bufferedReaderOne = null, bufferedReaderTwo = null;
        StringBuffer stringBufferResultOne = null, stringBufferResultTwo = null;
        Wrapper<X,Y> compareObjects = null;

        LineIterator lineIteratorOne = FileUtils.lineIterator(f1, ENCODING_TYPE);
        LineIterator lineIteratorTwo = FileUtils.lineIterator(f2, ENCODING_TYPE);

        while (lineIteratorOne.hasNext() && lineIteratorTwo.hasNext()) {
            try {
                fileOneLine = lineIteratorOne.nextLine().trim();
                fileTwoLine = lineIteratorTwo.nextLine().trim();

                if (!fileOneLine.isEmpty() && !fileTwoLine.isEmpty()) {
                    responseOne = HttpClientWrapper.httpRequest(fileOneLine);
                    responseTwo = HttpClientWrapper.httpRequest(fileTwoLine);

                    if(responseOne == null || responseTwo == null){
                        System.out.println(fileOneLine+" skip "+fileTwoLine+" --> Response seen as null: ");
                        continue;
                    }
                    errorCodeResponseOne = responseOne.getStatusLine().getStatusCode();
                    errorCodeResponseTwo = responseTwo.getStatusLine().getStatusCode();
                    if (responseOne != null && errorCodeResponseOne == 200 &&
                            responseTwo != null && errorCodeResponseTwo == 200) {

                        bufferedReaderOne = new BufferedReader(new InputStreamReader(responseOne.getEntity().getContent()));

                        stringBufferResultOne = new StringBuffer();
                        String lineOne = "";
                        while ((lineOne = bufferedReaderOne.readLine()) != null) {
                            stringBufferResultOne.append(lineOne);
                        }
                        bufferedReaderOne.close();
                        bufferedReaderTwo = new BufferedReader(new InputStreamReader(responseTwo.getEntity().getContent()));
                        stringBufferResultTwo = new StringBuffer();
                        String lineTwo = "";
                        while ((lineTwo = bufferedReaderTwo.readLine()) != null) {
                            stringBufferResultTwo.append(lineTwo);
                        }
                        bufferedReaderTwo.close();
                        Object responseObjectOne = serializeObjectForComaringResponse(stringBufferResultOne);
                        Object responseObjectTwo = serializeObjectForComaringResponse(stringBufferResultTwo);
                        if (compare((X) responseObjectOne, (Y) responseObjectTwo)) {
                            System.out.println(fileOneLine + " <equal> " + fileTwoLine);
                            compareObjects = new Wrapper(responseObjectOne, responseObjectTwo);
                        } else {
                            System.out.println(fileOneLine + " <not equal> " + fileTwoLine);
                            compareObjects = new Wrapper(responseObjectOne, responseObjectTwo);
                        }

                    } else {

                        System.out.println(fileOneLine+" skip "+fileTwoLine+" --> "+errorCodeResponseOne+" "+errorCodeResponseTwo);
                    }
                }
            } catch (ClientProtocolException e) {
                System.out.println(fileOneLine+" skip "+fileTwoLine+" --> "+errorCodeResponseOne+" "+errorCodeResponseTwo);
            }
        }
        LineIterator.closeQuietly(lineIteratorOne);
        LineIterator.closeQuietly(lineIteratorTwo);
        return compareObjects;
    }

    /*
        Takes two Objects of response type as input
        returns boolean true if objects matches
     */
    public boolean compare(X a, Y b){

        if (new CompareToBuilder()
                .append((byte[])a,(byte[])b)
                .toComparison() == 0 && a!=null && b!=null)
            return true;
        return false;
    }

    /*
        Takes Objects to serialize it
        returns byte array
     */
    private byte[] serializeObjectForComaringResponse(Object obj) throws IOException
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