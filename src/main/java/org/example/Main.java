package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

public class Main {
    private static final String KEY = "7k1O0u27I6sj8d60ZnLZz0VMQuzr32CsAj2254Yg";
    public static void main(String[] args) throws IOException {
        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet getContentRequest = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=" + KEY);
            Content dayContent = null;
            try(CloseableHttpResponse response = client.execute(getContentRequest)){
                var responseBody = response.getEntity().getContent();
                ObjectMapper mapper = new ObjectMapper();
                dayContent = mapper.readValue(responseBody, Content.class);
            }

            HttpGet getFileResuest = new HttpGet(dayContent.getUrl());
            try(CloseableHttpResponse response = client.execute(getFileResuest)){
                var responseBody = response.getEntity().getContent();
                String fileName = dayContent.getUrl().replaceAll(".*/", "");
                File file = new File(fileName);
                try(var outputStream = new FileOutputStream(file)) {
                    outputStream.write(responseBody.readAllBytes());
                }catch (IOException ex){
                    System.out.println("Can't save content");
                }
            }
        }
    }
}