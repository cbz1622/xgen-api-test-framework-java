package API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocatorService {
    String locatorServiceURI = "https://www.testdynamix.io/api";
    ObjectMapper objectMapper = new ObjectMapper();

    private String writeJsonRequestBody(String url, String pageSource) throws JsonProcessingException {
        RequestPayload requestPayload = new RequestPayload(url, pageSource);
        return objectMapper.writeValueAsString(requestPayload);
    }

    private HttpRequest requestLocatorService(String uri, String api, String queryParams, String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri + api + queryParams))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody,StandardCharsets.UTF_8))
                .build();
    }

    private HttpResponse<?> responseFromLocatorService(HttpRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        return client.send(request,HttpResponse.BodyHandlers.ofString());
    }

    public Object getLocatorWithIdentifier(String url, String pageSource, String queryParams) throws IOException, InterruptedException {
        String api = "/getLocator";
        String requestBody = writeJsonRequestBody(url, pageSource);

        HttpRequest request = requestLocatorService(locatorServiceURI,api,queryParams,requestBody);
        HttpResponse<?> response = responseFromLocatorService(request);

        return parseResponse(response.body().toString());
    }

    public Object getLocatorsWithIdentifier(String url, String pageSource, String queryParams) throws IOException, InterruptedException {
        String api = "/getLocators";
        String requestBody = writeJsonRequestBody(url, pageSource);

        HttpRequest request = requestLocatorService(locatorServiceURI,api,queryParams,requestBody);
        HttpResponse<?> response = responseFromLocatorService(request);

        return parseResponse(response.body().toString());
    }

    public Object getLocatorWithIdentifierUsingRelativeContext(String url, String pageSource, String queryParams) throws IOException, InterruptedException {
        String api = "/getLocatorWithContext";
        String requestBody = writeJsonRequestBody(url, pageSource);

        HttpRequest request = requestLocatorService(locatorServiceURI,api,queryParams,requestBody);
        HttpResponse<?> response = responseFromLocatorService(request);

        return parseResponse(response.body().toString());
    }

    private Object parseResponse(String responseBody) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        if(jsonNode.isArray()) {
            List<Map<String,String>> resultMapList = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                if(node.isObject()) {
                    Map<String,String> map = new HashMap<>();
                    node.fields().forEachRemaining(entry -> {
                        String key = entry.getKey();
                        String value = entry.getValue().asText();
                        map.put(key,value);
                    });
                    resultMapList.add(map);
                }
            }
            return resultMapList;
        } else if(jsonNode.isObject()) {
            Map<String,String> returnedKeyVal = new HashMap<>();
            jsonNode.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                String value = entry.getValue().asText();
                returnedKeyVal.put(key,value);
            });
            return returnedKeyVal;
        } else {
            throw new RuntimeException("Invalid response from server");
        }
    }
}
