package proxy.auth;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

public class NameRetriever extends Thread {
    private static final String API_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private final Map<String, String> output;
    private final String uuid;

    public NameRetriever(Map<String, String> output, String uuid) {
        this.output = output;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        if (output.containsKey(uuid)) {
            return;
        }

        HttpResponse<JsonNode> response;
        try {
            response = Unirest.get(API_URL + uuid)
                .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            return;
        }

        String username = response.getBody().getObject().get("name").toString();
        output.put(uuid, username);
    }
}
