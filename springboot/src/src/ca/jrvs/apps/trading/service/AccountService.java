package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountJpaRepository;
import ca.jrvs.apps.trading.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService
{
    private AccountJpaRepository accountRepo;

    public AccountService(AccountJpaRepository accountRepo)
    {
          this.accountRepo = accountRepo;
    }


    @Transactional
    public void deleteAccountByTraderId(Integer traderId)
    {
        Account account = accountRepo.getReferenceById(traderId);
        if (account.getAmount() != 0)
        {
            throw new IllegalArgumentException("Balance not 0!");
        }
        accountRepo.deleteById(account.getId());
    }
}
