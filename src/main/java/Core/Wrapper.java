package Core;

import Core.HttpClient.HttpClientWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Wrapper<X,Y> implements IComparator<X,Y>{

    /*
      httpClient object
   */
    private CloseableHttpClient httpClient = null;

    /*
        Http post
    */
    private HttpPost httpPost = null;
    /*
        Http response
     */
    private CloseableHttpResponse f1LineResponse = null, f2LineResponse = null;
    /*
        Http entity
     */
    private HttpEntity f1_line_entity,f2_line_entity;
    /*
        Encoding define type
     */
    private final String ENCODING_TYPE = "UTF-8";

    /*
        private instance of wrapper<res1,res2>

     */
    private  Wrapper<X,Y> compareObjects = null;
    /*
        Private instance of Response type one
     */
    private X x = null;
    /*
        Private instance of Response type two
     */
    private Y y = null;
    /*
         Constructor to initialise the wrapper
     */

    String fileOneLine = null, fileTwoLine = null;
    Integer errorCodeResponseOne = null,errorCodeResponseTwo = null;
    BufferedReader bufferedReaderOne = null, bufferedReaderTwo = null;
    StringBuffer stringBufferResultOne = null, stringBufferResultTwo = null;
    LineIterator lineIteratorOne = null,lineIteratorTwo = null;
    HttpResponse responseOne = null, responseTwo = null;

    public Wrapper(X x, Y y){

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
        //per core 2 threads
        ExecutorService executorService = Executors.newFixedThreadPool(4*2);
        executorService.execute(new Runnable() {
            public void run() {

                try {
                    lineIteratorOne = FileUtils.lineIterator(f1, ENCODING_TYPE);
                    lineIteratorTwo = FileUtils.lineIterator(f2, ENCODING_TYPE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (lineIteratorOne.hasNext() && lineIteratorTwo.hasNext()) {
                    fileOneLine = lineIteratorOne.nextLine().trim();
                    fileTwoLine = lineIteratorTwo.nextLine().trim();
                    if (!fileOneLine.isEmpty() && !fileTwoLine.isEmpty()) {
                        try {
                            responseOne = HttpClientWrapper.httpRequest(fileOneLine);
                            responseTwo = HttpClientWrapper.httpRequest(fileTwoLine);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(responseOne == null || responseTwo == null){
                            System.out.println(fileOneLine+" skip "+fileTwoLine+" --> Response seen as null: ");
                            continue;
                        }
                        errorCodeResponseOne = responseOne.getStatusLine().getStatusCode();
                        errorCodeResponseTwo = responseTwo.getStatusLine().getStatusCode();
                        if (responseOne != null && errorCodeResponseOne == 200 &&
                                responseTwo != null && errorCodeResponseTwo == 200) {

                            try {
                                bufferedReaderOne = new BufferedReader(new InputStreamReader(responseOne.getEntity().getContent()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            stringBufferResultOne = new StringBuffer();
                            String lineOne = "";
                            try {
                                while ((lineOne = bufferedReaderOne.readLine()) != null) {
                                    stringBufferResultOne.append(lineOne);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                bufferedReaderOne.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                bufferedReaderTwo = new BufferedReader(new InputStreamReader(responseTwo.getEntity().getContent()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stringBufferResultTwo = new StringBuffer();
                            String lineTwo = "";
                            try {
                                while ((lineTwo = bufferedReaderTwo.readLine()) != null) {
                                    stringBufferResultTwo.append(lineTwo);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                bufferedReaderTwo.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Object responseObjectOne = null;
                            try {
                                responseObjectOne = serializeObjectForComaringResponse(stringBufferResultOne);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Object responseObjectTwo = null;
                            try {
                                responseObjectTwo = serializeObjectForComaringResponse(stringBufferResultTwo);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
                }
            }
        }) ;

        executorService.shutdown();


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
