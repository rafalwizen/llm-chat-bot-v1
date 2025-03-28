import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class HuggingFaceClient {
    private static final String API_URL = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.1";
    private static String API_KEY;

    public HuggingFaceClient() {
        API_KEY = loadApiKeyFromProperties();
    }

    public String sendRequest(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        json.put("inputs", prompt);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Error: " + response.code() + " " + response.message();
            }
            String responseBody = response.body().string();
            return parseResponse(responseBody);
        }
    }

    private String parseResponse(String responseBody) {
        JSONArray jsonArray = new JSONArray(responseBody);
        if (!jsonArray.isEmpty()) {
            JSONObject firstObject = jsonArray.getJSONObject(0);
            return firstObject.optString("generated_text", "No response from AI.");
        }
        return "No response from AI.";
    }


    private static String loadApiKeyFromProperties() {
        try {
            Properties props = new Properties();
            props.load(Files.newInputStream(Paths.get("config.properties")));
            return props.getProperty("api.key");
        } catch (IOException e) {
            throw new RuntimeException("Nie można wczytać klucza API", e);
        }
    }
}
