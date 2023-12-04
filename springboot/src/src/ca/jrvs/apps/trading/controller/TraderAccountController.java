package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.Trader;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.service.TraderAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Api(value = "Trader", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Controller
@RequestMapping("/trader")
public class TraderAccountController
{
    public TraderAccountService traderAccountService;

    @Autowired
    public TraderAccountController(TraderAccountService traderAccountService)
    {
        this.traderAccountService = traderAccountService;
    }

    @ApiOperation(value = "Create a trader and account.")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(
            path = "firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TraderAccountView createTrader(@PathVariable String firstname,
                                          @PathVariable String lastname,
                                          @PathVariable @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate dob,
                                          @PathVariable String country, @PathVariable String email)
    {
        try {
            Trader trader = new Trader();
            trader.setFirstName(firstname);
            trader.setLastName(lastname);
            trader.setCountry(country);
            trader.setEmail(email);
            trader.setDob(Date.valueOf(dob));
            return traderAccountService.createTraderAndAccount(trader);
        }
        catch (Exception e)
        {
            throw ResponseExceptionUtil.getResponseStaticException(e);
        }
    }

    @ApiOperation(value = "Create a trader and an account with DTO")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(path = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TraderAccountView createTrader(@RequestBody Trader trader)
    {
        try
        {
            return traderAccountService.createTraderAndAccount(trader);
        }
        catch (Exception e)
        {
            ResponseExceptionUtil.getResponseStaticException(e);
        }
        return null;
    }

    @ApiOperation(value = "Delete a trader")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Unable to delete the user!")})
    @DeleteMapping(path = "/traderID/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrader(@PathVariable Integer traderId)
    {
        try
        {
            traderAccountService.deleteTraderById(traderId);
        }
        catch (Exception e)
        {
            throw ResponseExceptionUtil.getResponseStaticException(e);
        }
    }

    @ApiOperation(value = "Deposit a fund")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "traderId not found!"),
            @ApiResponse(code = 404, message = "Unable to deposit due to user input!")
    })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PutMapping(path = "/deposit/traderId/{traderId}/amount/{amount}")
    public Account depositFund(@PathVariable Integer traderId, @PathVariable Double amount)
    {
        try
        {
            return traderAccountService.deposit(traderId, amount);
        }
        catch (Exception e)
        {
            throw ResponseExceptionUtil.getResponseStaticException(e);
        }
    }

    @ApiOperation(value = "Withdraw a fund")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "traderId is not found"),
            @ApiResponse(code = 404, message = "Unable to deposit due to user input")
    })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PutMapping(path = "withdraw/traderId/{traderId}/amount/{amount}")
    public Account withdrawFund(@PathVariable Integer traderId, @PathVariable Double amount)
    {
        try
        {
            return traderAccountService.withdraw(traderId, amount);
        }
        catch (Exception e)
        {
            throw ResponseExceptionUtil.getResponseStaticException(e);
        }
    }
}
