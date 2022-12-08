package io.github.musikhjalpen2022.swishplugin.api.bossan;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BossanAPI {

    public static HttpClient client = HttpClient.newHttpClient();

    private final static String BASE_URL = "https://bossan.musikhjalpen.se/";

    // https://bossan.musikhjalpen.se/minecraft-boessan
    // https://bossan.musikhjalpen.se/
    public static boolean requestPayment(PaymentData paymentData, Consumer<String> f) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(BASE_URL + "server/payments/swish/fundraiser"))
                    .header("accept", "*/*")
                    .header("accept-language", "en-US,en;q=0.9")
                    .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundarymQjQXJFEqEjeTFso")
                    .header("sec-fetch-dest", "empty")
                    .header("sec-fetch-mode", "cors")
                    .header("sec-fetch-site", "same-origin")
                    .header("sec-gpc", "1")
                    .header("Referer", BASE_URL + "minecraft-boessan")
                    .header("Referrer-Policy", "strict-origin-when-cross-origin")
                    .POST(HttpRequest.BodyPublishers.ofString("------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"entryTitle\"\r\n\r\nMinecraft-bössan\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"entryId\"\r\n\r\n7csTEpmeKnsiB507RhAI6i\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"amount\"\r\n\r\n" + paymentData.amount + "\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"currency\"\r\n\r\nSEK\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"payerAlias\"\r\n\r\n" + paymentData.phoneNumber + "\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"donorType\"\r\n\r\nPRIVATE\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"entryType\"\r\n\r\nfundraiser\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"isPublic\"\r\n\r\ntrue\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"name\"\r\n\r\n" + paymentData.username + "\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"message\"\r\n\r\nVia Minecraft\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"isParticipant\"\r\n\r\nfalse\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"participantName\"\r\n\r\n\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"participantEmail\"\r\n\r\n\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"participantPhone\"\r\n\r\n\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"invoiceName\"\r\n\r\n\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"invoiceCompany\"\r\n\r\n\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"invoiceEmail\"\r\n\r\n\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso\r\nContent-Disposition: form-data; name=\"isHiddenAmount\"\r\n\r\nfalse\r\n------WebKitFormBoundarymQjQXJFEqEjeTFso--\r\n"))
                    .build();
            System.out.println("phone number: " + paymentData.phoneNumber + ", amount: " + paymentData.amount + ", username: " + paymentData.username);
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
                System.out.println("status code: " + response.statusCode());
                String referenceNumber = null;
                try {
                    if (response.statusCode() != 503) {
                        JsonObject jsonObject = JsonParser.parseReader(new JsonReader(new StringReader(response.body()))).getAsJsonObject();
                        referenceNumber = jsonObject.get("referenceNumber").getAsString();
                    }
                } catch (Exception ignored) {}
                f.accept(referenceNumber);
            });
            return true;
        } catch (URISyntaxException ignored) {
            return false;
        }
    }

    public static boolean checkPayment(String referenceNumber, Consumer<PaymentStatus> f) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(BASE_URL + "server/payments/" + referenceNumber))
                    .header("accept", "*/*")
                    .header("accept-language", "en-US,en;q=0.9")
                    .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundarymQjQXJFEqEjeTFso")
                    .header("sec-fetch-dest", "empty")
                    .header("sec-fetch-mode", "cors")
                    .header("sec-fetch-site", "same-origin")
                    .GET()
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
                PaymentStatus status = PaymentStatus.ERROR;
                try {
                    System.out.println("response: " + response.body());
                    JsonObject jsonObject = JsonParser.parseReader(new JsonReader(new StringReader(response.body()))).getAsJsonObject();
                    String statusId = jsonObject.get("status").getAsString();
                    if (Objects.equals(statusId, "PAID")) {
                        status = PaymentStatus.PAID;
                    } else if (Objects.equals(statusId, "DECLINED")) {
                        status = PaymentStatus.DECLINED;
                    } else if (Objects.equals(statusId, "NOT PAID")) {
                        status = PaymentStatus.WAITING;
                    }
                } catch (Exception ignored) {}
                f.accept(status);
            });
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

}
