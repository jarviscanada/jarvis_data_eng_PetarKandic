package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertEquals;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class QuoteServiceTest {

    @Autowired
    private QuoteService quoteService;

    @MockBean
    private QuoteDao quoteDao;

    @Test
    public void testFindIexQuoteByTicker()
    {
        String ticker = "TSLA";
        IexQuote quote = quoteService.findIexQuoteByTicker(ticker);
        assertEquals(quote.getTicker(), ticker);
    }

    @Test
    public void testFindQuotesInDataBase()
    {
        List<Quote> quoteList = quoteService.findAllQuotes();
        assertEquals(quoteList.get(0).getTicker(), "AAPL");
        assertEquals(quoteList.get(1).getTicker(), "GOOG");
        assertEquals(quoteList.get(2).getTicker(), "HSBC");
    }

    @Test
    public void updateQuoteAndTest()
    {
        Quote aQuote = quoteService.findAllQuotes().get(3);
        aQuote.setLastPrice(999999d);
        quoteService.saveQuote(aQuote);
        assertEquals(quoteService.findAllQuotes().get(3).getLastPrice(), 999999d, 0);
    }
}