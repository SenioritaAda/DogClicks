package org.example;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.IntStream;

public class DogClicks2 {

    private static final int DOG_ID = 701;

    private static final int NUMBER_OF_REQUESTS = 5;
    private static final int DELAY_BETWEEN_REQUESTS_IN_SECONDS = 1;

    public static void main(String[] args) {
        final HttpClient httpClient = prepareClient();
        final HttpRequest request = prepareDogClickRequest();

        IntStream.range(0, NUMBER_OF_REQUESTS)
                .boxed()
                .forEach(iteration -> sendRequest(iteration + 1, httpClient, request));
    }

    private static HttpClient prepareClient() {
        return HttpClient.newHttpClient();
    }

    private static HttpRequest prepareDogClickRequest() {
        return HttpRequest.newBuilder(URI.create("https://nakarmpsa.olx.pl/wp-content/themes/olx-nakarm-psa/vote.php"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cache-Control", "no-cache")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "nonce=360221c765&" +
                                "user_id=0&" +
                                "pet_id=" + DOG_ID + "&" +
                                "action=feedPet&" +
                                "crossDomain=true&" +
                                "xhrFields=^%^5Bobject+Object^%^5D"))
                .build();
    }

    private static void sendRequest(final Integer requestId, final HttpClient httpClient, final HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 300) {
                System.out.printf("Sent request '%s' to '%s'. Received response: '%s'%n", requestId, request.uri(), response.body());
            } else {
                System.out.printf("Failed to send request '%s' to '%s'. Received response status '%d'%n", request, request.uri(), response.statusCode());
            }
            Thread.sleep(DELAY_BETWEEN_REQUESTS_IN_SECONDS * 1000);
        } catch (Exception e) {
            System.out.printf("Failed to send request '%s' due to '%s'%n", requestId, e.getMessage());
            e.printStackTrace();
        }

    }
}
