package ca.jrvs.apps.trading.model.config;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Entity
public class MarketDataConfig
{

    @Id
    private String host;
    private String token;

    public MarketDataConfig(){}

    public String getHost() {
        return host;
    }

     public Optional<String> executeHttpGet(String url) throws IOException, InterruptedException
     {
         String value_name = System.getenv("IEX_PUB_TOKEN");
         HttpClient client = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder()
                     .uri(URI.create(url))
                     .build();
         HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
         return Optional.of(response.body().toString());
     }

    public void setHost(String host) {
        this.host = host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
