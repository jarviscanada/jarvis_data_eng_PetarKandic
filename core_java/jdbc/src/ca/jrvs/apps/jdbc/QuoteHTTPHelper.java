package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * This class is used to retrieve data from the Alpha Vantage endpoint.
 * The obtained data is placed into the database in QuoteService.
 */
public class QuoteHTTPHelper
{
    private String apiKey;
    /**
     * Constructor method for the class.
     * apikey is passed in from properties.txt.
     * See the parseFile method in QuoteService.
     * @param apiKey
     */
    public QuoteHTTPHelper(String apiKey)
    {
        this.apiKey = apiKey;
    }

    /**
     * We retrieve the data from Alpha Vantage using HttpRequest.
     * We pass the apikey, and check if the function returns null.
     * If so, an exception is thrown.
     * The retrieved data must be manipulated somewhat.
     * We retrieve the body of the response, and convert it to a tree.
     * ObjectMapper is used to convert this into a Quote.
     * Finally, we retrieve the current timestamp, and pass it as our value for timestamp.
     * @param symbol four letters (AMZN, MSFT, etc)
     * @return the retrieved stock info.
     * @throws IllegalArgumentException
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException
    {
        Quote stocksList = new Quote();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json"))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        if (request == null)
        {
            throw new NullPointerException("Error retrieving data");
        }
        try
        {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonString = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            JsonNode root = objectMapper.readTree(jsonString);
            //Extract date from the "Global Quote"
            JsonNode value = root.get("Global Quote");
            //Read data into Quote class
            stocksList = objectMapper.readValue(value.toString(), Quote.class);
            //Get the timestamp
            Instant instant = Instant.now();
            Timestamp timestamp = Timestamp.from(instant);
            //Add the timestamp
            stocksList.setTimestamp(timestamp);
            return stocksList;
        }
        catch (InterruptedException interruptedException)
        {
            StockQuoteController.logger.error("Interruption detected!");
        }
        catch (JsonMappingException jsonMappingException)
        {
            StockQuoteController.logger.error("Mapping error!");
        }
        catch (JsonProcessingException jsonProcessingException)
        {
            StockQuoteController.logger.error("Processing exception detected!");
        }
        catch (IOException ioException)
        {
            StockQuoteController.logger.error("IOexception detected!");
        }
        return stocksList;
    }

}
