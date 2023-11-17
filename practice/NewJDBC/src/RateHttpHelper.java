import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import org.json.JSONObject;

import java.util.Optional;
import java.util.logging.Logger;

public class RateHttpHelper
{
    private static final Logger operationLogger = LoggerSetup.getOperationLogger();
    private static final Logger errorLogger = LoggerSetup.getErrorLogger();
    private String apiKey;
    private String apiEndpoint;
    private OkHttpClient client;

    public RateHttpHelper(String apiKey, String apiEndpoint, OkHttpClient client) {
        this.apiKey = apiKey;
        this.apiEndpoint = apiEndpoint;
        this.client = client;
    }

    public Optional<Rate> fetchRate(String fromCode, String toCode) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://alpha-vantage.p.rapidapi.com/query?from_symbol=EUR&function=FX_DAILY&to_symbol=USD&outputsize=compact&datatype=json")
                .newBuilder();
        urlBuilder.addQueryParameter("function", "FX_DAILY");
        urlBuilder.addQueryParameter("from_symbol", fromCode);
        urlBuilder.addQueryParameter("to_symbol", toCode);
        urlBuilder.addQueryParameter("outputsize", "compact");
        urlBuilder.addQueryParameter("datatype", "json");

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "be97808825mshfa6ef98d77ce050p1feb6ejsn467bd5e14ce5") // Replace with your actual API key
                .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            // Add logging for the response code
            operationLogger.info("Response Code: " + response.code());
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                //Logging for the response body
                operationLogger.info("Response Body: " + responseBody);
                Rate rate = parseRateFromResponse(responseBody);
                return Optional.ofNullable(rate);
            }
            else
            {
                // If not successful, log the response body for error details
                errorLogger.severe("Error Body: " + response.body().string());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Rate parseRateFromResponse(String responseBody)
    {

        // Create a JSONObject from the response string
        JSONObject jsonObject = new JSONObject(responseBody);

        JSONObject timeSeries = jsonObject.getJSONObject("Time Series FX (Daily)");

        JSONObject dailyData = timeSeries.getJSONObject("2023-11-02");
        Rate rate = new Rate();
        rate.setOpen(dailyData.getDouble("1. open"));
        rate.setHigh(dailyData.getDouble("2. high"));
        rate.setLow(dailyData.getDouble("3. low"));
        rate.setClose(dailyData.getDouble("4. close"));
        rate.setDate(java.sql.Date.valueOf("2023-11-02"));

        return rate;
    }
}
