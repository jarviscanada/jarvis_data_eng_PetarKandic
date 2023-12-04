package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuoteService
{
    private Logger logger = LoggerFactory.getLogger(QuoteService.class);
    private MarketDataConfig marketDataConfig;
    private QuoteDao quoteDao;
    private ObjectMapper objectMapper;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteService(MarketDataDao marketDataDao, QuoteDao quoteDao)
    {
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
        this.marketDataConfig = new MarketDataConfig();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Saves the provided Quote to the database.
     * This assumes that the IexQuote has been transformed into a Quote.
     * See Application.
     * @param quote user provided quote.
     */
    @Transactional
    public Quote saveQuote(Quote quote)
    {
        return quoteDao.saveAndFlush(quote);
    }

    /**
     * Update quote table against IEX source
     *
     * - get all quotes from the db
     * - for each ticker get IexQuote
     * - convert IexQuote to Quote entity
     * - persist quote to db
     *
     * @throws IllegalArgumentException for invalid input
     */
    public void updateMarketData()
    {
        List<String> tickerList = new ArrayList<>();
        List<Quote> updatedQuotes = new ArrayList<>();
        List<Quote> dbQuotes = findAllQuotes();
        for (Quote aQuote: dbQuotes)
        {
            tickerList.add(aQuote.getTicker());
        }
        for (String aTicker: tickerList)
        {
            IexQuote tempQuote = findIexQuoteByTicker(aTicker);
            Quote convertedQuote = new Quote();
            convertedQuote.setTicker(tempQuote.getTicker());
            convertedQuote.setAskSize(tempQuote.getAskSize());
            convertedQuote.setAskPrice(tempQuote.getAskPrice());
            convertedQuote.setBidSize(tempQuote.getBidSize());
            convertedQuote.setBidPrice(tempQuote.getBidPrice());
            convertedQuote.setLastPrice(tempQuote.getLastPrice());
            updatedQuotes.add(convertedQuote);
        }
        for (Quote dbQuote: updatedQuotes)
        {
            quoteDao.save(dbQuote);
        }

    }

    /**
     * Find all quotes from the quote table.
     * @return a list of quotes
     */
    public List<Quote> findAllQuotes()
    {
        return quoteDao.findAll();
    }

    public List<Quote> saveQuotes(List<String> tickers)
    {
        List<Quote> toDatabase = new ArrayList<>();
        for (String aTicker: tickers)
        {
            IexQuote tempQuote = findIexQuoteByTicker(aTicker);
            Quote convertedQuote = new Quote();
            convertedQuote.setTicker(tempQuote.getTicker());
            convertedQuote.setAskSize(tempQuote.getAskSize());
            convertedQuote.setAskPrice(tempQuote.getAskPrice());
            convertedQuote.setBidSize(tempQuote.getBidSize());
            convertedQuote.setBidPrice(tempQuote.getBidPrice());
            convertedQuote.setLastPrice(tempQuote.getLastPrice());
            toDatabase.add(convertedQuote);
        }
        for (Quote aQuote: toDatabase)
        {
            quoteDao.save(aQuote);
        }
        return toDatabase;
    }

    /**
     * Scrapes the IEX endpoint.
     * Gets the most recent quote for the provided ticker.
     * Error handling included for illegal arguments and in case of no response.
     * IMPORTANT: throttling may limit number of requests over a certain period of time.
     * If the program freezes here, please wait a few minutes and try again.
     * @param ticker four letters (not case-sensitive)
     * @return the requested quote (if provided)
     */
    @Transactional
    public IexQuote findIexQuoteByTicker(String ticker) {

        String pubToken = System.getenv("IEX_PUB_TOKEN");
        String url = "https://api.iex.cloud/v1/data/core/quote/" + ticker + "?token=" + pubToken ;
        try {
            Optional<String> responseOptional = marketDataConfig.executeHttpGet(url);
            if (responseOptional.isPresent()) {
                String responseBody = responseOptional.get();
                IexQuote[] quotes = objectMapper.readValue(responseBody, IexQuote[].class);
                if (quotes.length > 0) {
                    return quotes[0];
                }
                else
                {
                    logger.error("No data found for ticker: " + ticker);
                    throw new IllegalArgumentException("No data found for ticker: " + ticker);
                }
            }
            else
            {
                logger.error("No response received for ticker" + ticker);
                throw new RuntimeException("No response received for ticker: " + ticker);
            }
        }
        catch (Exception e)
        {
            logger.error("Error fetching data for ticker: " + ticker);
            throw new RuntimeException("Error fetching data for ticker: " + ticker, e);
        }
    }


}