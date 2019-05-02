package dd186.unifood;

import java.util.concurrent.ExecutionException;


public class MakeARequest {

    public MakeARequest() {
    }

    public String get(String link) {
        HttpGetRequest httpGetRequest = new HttpGetRequest();
        httpGetRequest.setLink(link);
        try {
            httpGetRequest.execute();
            return httpGetRequest.get();
        } catch (Exception e) {
            return "";
        }
    }

    public String post(String link, String json) {
        HttpPostRequest httpPostRequest = new HttpPostRequest();
        httpPostRequest.execute(link, json);
        try {
            return httpPostRequest.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "";

        } catch (InterruptedException e) {
            e.printStackTrace();
            return "";
        }
    }
}
