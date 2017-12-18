package Core.HttpClient;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/*
    Http Client
 */
public class HttpClientWrapper {

    /*
        Takes a string url as input
        returns a HttpResponse object
     */
    public static HttpResponse httpRequest(String url) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(get);
        } catch (Exception e){
            System.out.println("Exception while making http call; Message : " + e.getMessage());
        }
        return response;
    }
}
