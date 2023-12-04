package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.junit.runner.RunWith;
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest
{
    @Autowired
    private QuoteDao quoteDao;

    private Quote savedQuote;

    @Before
    public void insertQuote()
    {
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize(10);
        savedQuote.setBidPrice(10d);
        savedQuote.setBidSize(10);
        savedQuote.setTicker("AAPL");
        savedQuote.setLastPrice(10.1d);
        quoteDao.save(savedQuote);
    }

    @After
    public void deleteOne()
    {
        quoteDao.deleteById(savedQuote.getTicker());
    }
}
