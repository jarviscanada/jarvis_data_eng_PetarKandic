package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quote")
public class QuoteController
{
    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {

        this.quoteService = quoteService;
    }


    @GetMapping(path ="/quote/all")
    @Transactional
    public List<Quote> findAllQuotes()
    {
        return quoteService.findAllQuotes();
    }

    @PutMapping(path = "iexMarketData")
    @ResponseStatus(HttpStatus.OK)
    public void updateMarketData()
    {
        try
        {
            quoteService.updateMarketData();
        }
        catch (Exception e)
        {
            throw ResponseExceptionUtil.getResponseStaticException(e);
        }
    }

    @GetMapping(path = "/savedticker")
    @Transactional
    public Quote saveQuote(Quote quote)
    {
        return quoteService.saveQuote(quote);
    }

    @GetMapping(path = "/ticker")
    @Transactional
    public void saveQuotes(List<String> tickers)
    {
        quoteService.saveQuotes(tickers);
    }


    @GetMapping(path = "iex/ticker/{ticker}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping("/")
    public IexQuote getQuote(@PathVariable String ticker)
    {
        try
        {
            IexQuote iexQuoteByTicker = quoteService.findIexQuoteByTicker(ticker);
            return iexQuoteByTicker;
        }
        catch (Throwable e)
        {
            throw ResponseExceptionUtil.getResponseStaticException(e);
        }
    }

}