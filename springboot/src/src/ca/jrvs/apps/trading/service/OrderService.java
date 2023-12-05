package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.MarketOrderDto;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.model.SecurityOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class OrderService
{

    private Logger logger = LoggerFactory.getLogger(OrderService.class);
    private SecurityOrderDao securityOrderDao;

    private AccountDao accountDao;

    private QuoteDao quoteDao;

    @Autowired
    public void orderService(SecurityOrderDao securityOrderDao, AccountDao accountDao, QuoteDao quoteDao)
    {
        this.securityOrderDao = securityOrderDao;
        this.accountDao = accountDao;
        this.quoteDao = quoteDao;
    }
    /**
     * Execute a market order
     * - validate the order (e.g. size and ticker)
     * - create a securityOrder
     * - handle buy or sell orders
     * 	- buy order : check account balance
     * 	- sell order : check position for the ticker/symbol
     * 	- do not forget to update the securityOrder.status
     * - save and return securityOrder
     *
     * NOTE: you are encouraged to make some helper methods (protected or private)
     *
     * @param orderData market order
     * @return SecurityOrder from security_order table
     * @throws IllegalArgumentException for invalid inputs
     */
    @Transactional
    public SecurityOrder executeMarketOrder(MarketOrderDto orderData) {
        if (orderData == null || orderData.getSize() == 0 || orderData.getTicker() == null)
        {
            logger.error("Invalid Order Data!");
            throw new IllegalArgumentException("Invalid order data!");
        }

        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setTicker(orderData.getTicker());
        securityOrder.setSize(orderData.getSize());
        securityOrder.setStatus("PENDING");

        Optional<Account> accountOpt = accountDao.findByTraderID(orderData.getTraderId());
        if (!accountOpt.isPresent())
        {
            logger.error("Account not found for trader ID");
            throw new IllegalArgumentException("Account not found for trader id");
        }
        Account account = accountOpt.get();
        securityOrder.setAccountId(account.getId());

        if (orderData.getSize() > 0)
        {
            handleBuyMarketOrder(orderData, securityOrder, account);
        }
        else
        {
            handleSellMarketOrder(orderData, securityOrder, account);
        }

        securityOrderDao.save(securityOrder);
        return securityOrder;
    }

    /**
     * Helper method to execute a buy order
     *
     * @param marketOrderDto user order
     * @param securityOrder to be saved in database
     * @param account account
     */
    protected void handleBuyMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder, Account account) {
        Quote quote = quoteDao.findById(marketOrderDto.getTicker())
                .orElseThrow(() -> new IllegalArgumentException("Unable to find quote for ticker: " + marketOrderDto.getTicker()));
        double newDec = marketOrderDto.getSize();
        double cost = quote.getAskPrice() * newDec;

        if (account.getAmount() < cost)
        {
            securityOrder.setStatus("FAILED");
            securityOrder.setNotes("Insufficient funds");
        }
        else
        {
            account.setAmount(account.getAmount() - cost);
            accountDao.save(account);

            securityOrder.setStatus("COMPLETED");
            securityOrder.setPrice((quote.getAskPrice()));
        }
    }

    /**
     * Helper method to execute a sell order
     *
     * @param marketOrderDto user order
     * @param securityOrder to be saved in database
     * @param account account
     */
    protected void handleSellMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder, Account account)
    {


        Quote quote = quoteDao.findById(marketOrderDto.getTicker())
                .orElseThrow(() -> new IllegalArgumentException("Unable to find quote for ticker: " + marketOrderDto.getTicker()));
        Double revenue = quote.getAskPrice() * Math.abs(marketOrderDto.getSize());

        account.setAmount(account.getAmount() + revenue.doubleValue());
        accountDao.save(account);

        securityOrder.setStatus("COMPLETED");
        securityOrder.setPrice(quote.getAskPrice());
    }
}
