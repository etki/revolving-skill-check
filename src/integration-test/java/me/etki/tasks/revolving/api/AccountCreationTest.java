package me.etki.tasks.revolving.api;

import me.etki.tasks.revolving.support.ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.math.BigDecimal;

@SuppressWarnings("WeakerAccess")
public class AccountCreationTest {
    private static ApiClient client;

    @BeforeAll
    public static void beforeAll() {
        client = ApiClient.create();
    }

    @Test
    public void doesntCreateAccountWithoutCurrency() throws Exception {
        Response<Account> response = client.createAccount(new AccountInput().setBalance(BigDecimal.ZERO)).execute();
        Assertions.assertEquals(400, response.code());
    }

    @Test
    public void allowsCreationWithNegativeBalance() throws Exception {
        BigDecimal balance = BigDecimal.valueOf(-10);
        AccountInput input = new AccountInput(balance, "RUR").normalize();
        Response<Account> response = client.createAccount(input).execute();
        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(0, balance.compareTo(response.body().getBalance()));
    }

    @Test
    public void normalizesCurrencyName() throws Exception {
        String currency = "RUR";
        AccountInput input = new AccountInput(BigDecimal.ZERO, currency.toLowerCase());
        Response<Account> response = client.createAccount(input).execute();
        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(currency, response.body().getCurrency());
    }
}
