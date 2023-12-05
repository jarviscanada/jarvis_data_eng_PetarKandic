package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.Trader;
import ca.jrvs.apps.trading.model.TraderAccountView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class TraderAccountService
{
    private Logger logger = LoggerFactory.getLogger(TraderAccountService.class);
    private TraderDao traderDao;
    private AccountDao accountDao;

    @Autowired
    public TraderAccountService(TraderDao traderDao, AccountDao accountDao)
    {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
    }
    /**
     * Create a new trader and initialize a new account with 0 amount
     * - validate user input (all fields must be non empty)
     * - create a trader
     * - create an account
     * - create, setup, and return a new traderAccountView
     *
     * Assumption: to simplify the logic, each trader has only one account where traderId == accountId
     *
     * @param trader cannot be null. All fields cannot be null except for id (auto-generated by db)
     * @return traderAccountView
     * @throws IllegalArgumentException if a trader has null fields or id is not null
     */

    @Transactional
    public TraderAccountView createTraderAndAccount(Trader trader) {

        for (Trader aTrader: traderDao.findAll())
        {
            if (Objects.equals(aTrader.getEmail(), trader.getEmail()))
            {
                logger.error("Trader already exists! Prexisting email detected!");
                return null;
            }
        }
        trader = traderDao.save(trader);
        Account account = new Account();
        account.setTraderID(trader.getTraderID());
        account.setAmount(0d);
        account = accountDao.save(account);

        return new TraderAccountView(trader.getTraderID(), trader.getFirstName(), trader.getLastName(), 0);
    }


    /**
     * A trader can be deleted if and only if it has no open position and 0 cash balance
     * - validate traderId
     * - get trader account by traderId and check account balance
     * - get positions by accountId and check positions
     * - delete all securityOrders, account, trader (in this order)
     *
     * @param traderId must not be null
     * @throws IllegalArgumentException if traderId is null or not found or unable to delete
     */
    @Transactional
    public void deleteTraderById(Integer traderId) {
        if (traderId == null)
        {
            throw new IllegalArgumentException("Trader ID cannot be null");
        }

        Trader trader = new Trader();
        for (Trader aTrader: traderDao.findAll())
        {
            if (aTrader.getTraderID() == traderId)
            {
                trader = aTrader;
            }
        }

        Optional<Account> accountOptional = accountDao.findByTraderID(traderId);
        if (accountOptional.isPresent()) {
            Account accountOne = accountOptional.get();
            if (accountOne.getAmount() != 0)
            {
                logger.error("Account balance is not zero");
                throw new IllegalArgumentException("Account balance is not zero");
            }
            accountDao.delete(accountOne);
        }
        else
        {
            logger.error("Account not found!");
        }
        traderDao.delete(trader);
    }

    /**
     * Deposit a fund to an account by traderId
     * - validate user input
     * - find account by trader id
     * - update the amount accordingly
     *
     * @param traderId must not be null
     * @param fund must be greater than 0
     * @return updated Account
     * @throws IllegalArgumentException if traderId is null or not found,
     * 									and fund is less than or equal to 0
     */
    @Transactional
    public Account deposit(Integer traderId, Double fund)
    {
        if (traderId == null)
        {
            throw new IllegalArgumentException("Trader ID cannot be null");
        }
        if (fund <= 0)
        {
            throw new IllegalArgumentException("Fund must be greater than 0");
        }

        Optional<Account> accountOpt = accountDao.findByTraderID(traderId);
        if (!accountOpt.isPresent())
        {
            logger.error("Account not found for trader ID: " + traderId);
            throw new IllegalArgumentException("Account not found for trader ID: " + traderId);
        }

        Account account = accountOpt.get();
        account.setAmount(account.getAmount() + fund);

        return accountDao.save(account);
    }


    /**
     * Withdraw a fund to an account by traderId
     * - validate user input
     * - find account by trader id
     * - update the amount accordingly
     *
     * @param traderId must not be null
     * @param fund must be greater than 0
     * @return updated Account
     * @throws IllegalArgumentException if traderId is null or not found,
     * 									and fund is less than or equal to 0
     */
public Account withdraw(Integer traderId, Double fund) {
    if (traderId == null)
    {
        logger.error("Trader ID cannot be null");
        throw new IllegalArgumentException("Trader ID cannot be null");
    }
    if (fund <= 0)
    {
        logger.error("Fund must be greater than 0");
        throw new IllegalArgumentException("Fund must be greater than 0");
    }

    Account account = accountDao.findByTraderID(traderId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found for trader ID: " + traderId));

    double newAmount = account.getAmount() - fund;
    if (newAmount < 0)
    {
        logger.error("Insufficient funds in the account");
        throw new IllegalArgumentException("Insufficient funds in the account");
    }

    account.setAmount(newAmount);
    return accountDao.save(account);
}
}
