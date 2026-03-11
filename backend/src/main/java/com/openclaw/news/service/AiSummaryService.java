package com.openclaw.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class AiSummaryService {
    
    private static final String OLLAMA_API_URL = "http://127.0.0.1:11434/api/generate";
    private static final String MODEL_NAME = "qwen3.5:2b";
    private static final int TIMEOUT_MS = 30000; // 30 seconds
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public String summarize(String title, String description) {
        try {
            // Create request JSON
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", MODEL_NAME);
            requestBody.put("prompt", "Summarize this news in 2 sentences in Korean: Title: " + title + ". Content: " + description);
            requestBody.put("stream", false);
            
            // Send POST request
            String jsonResponse = sendPostRequest(OLLAMA_API_URL, objectMapper.writeValueAsString(requestBody));
            
            // Parse response to extract summary
            ObjectNode responseJson = (ObjectNode) objectMapper.readTree(jsonResponse);
            return responseJson.get("response").asText();
            
        } catch (Exception e) {
            System.err.println("Error in AI summarization: " + e.getMessage());
            // Return truncated original description on error
            if (description != null && description.length() > 200) {
                return description.substring(0, 200) + "...";
            }
            return description;
        }
    }
    
    private String sendPostRequest(String urlString, String jsonBody) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Set request properties
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setConnectTimeout(TIMEOUT_MS);
        connection.setReadTimeout(TIMEOUT_MS);
        
        // Send request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Read response
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}