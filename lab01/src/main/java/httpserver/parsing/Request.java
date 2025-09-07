package httpserver.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;            // GET, POST, DELETE...
    private String resource;               // /home или /resource/part
    private Map<String, String> params = new HashMap<>(); // query параметры
    private String protocol;          // HTTP/1.1

    public Request (BufferedReader reader) throws IOException {
        parse(reader);
    }

    private void parse(BufferedReader reader) throws IOException {
        // Читаем первую строку: "GET /home?name=tat HTTP/1.1"
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Empty request");
        }

        String[] parts = requestLine.split(" ");
        this.method = parts[0];
        String resourceWithParams = parts[1];
        this.protocol = parts[2];

        // Делим на RESOURCES и query string
        String[] resourceParts = resourceWithParams.split("\\?", 2);
        this.resource = resourceParts[0];

        if (resourceParts.length > 1) {
            String query = resourceParts[1];
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    // --- Getters ---
    public String getMethod() { return method; }
    public String getResource() { return resource; }
    public String getProtocol() { return protocol; }
    public Map<String, String> getParams() { return params; }
}
