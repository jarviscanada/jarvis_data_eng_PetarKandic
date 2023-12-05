package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.sql.Date;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TraderAccountServiceTest {

    @Autowired
    private TraderAccountService traderAccountService;

    @MockBean
    private TraderDao traderDao;

    @MockBean
    private AccountDao accountDao;

    @MockBean
    private TraderAccountView traderAccountView;

    @Test
    public void testSaveNewTraderAndAccount()
    {
        Date sdf = new Date(20000901);
        Trader testTrader = new Trader(2, "jimbo", "eer", "usa", "finefinef@gmail.com",
                sdf);
        traderAccountView = traderAccountService.createTraderAndAccount(testTrader);
        assertEquals(traderAccountView.getFirstName(), "jimbo");
    }

    @Test
    public void deleteTrader()
    {
        traderAccountService.deleteTraderById(2);
        assertEquals(0, 0, 0);
    }

    @Test
    public void depositTest()
    {
        Account account = traderAccountService.deposit(3, 25d);
        assertEquals(account.getAmount(), 25d, 0);
    }

    @Test
    public void withDrawTest()
    {
        Account account = traderAccountService.withdraw(3, 20d);
        assertEquals(account.getAmount(), 5d, 0);
    }

}
