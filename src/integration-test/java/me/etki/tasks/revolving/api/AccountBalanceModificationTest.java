package me.etki.tasks.revolving.api;

import me.etki.tasks.revolving.support.ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.math.BigDecimal;

@SuppressWarnings("WeakerAccess")
public class AccountBalanceModificationTest {
    private static ApiClient client;

    @BeforeAll
    public static void beforeAll() {
        client = ApiClient.create();
    }

    @Test
    public void successfullyModifiesBalance() throws Exception {
        AccountInput input = new AccountInput(BigDecimal.TEN, "RUR");
        Response<Account> creation = client.createAccount(input).execute();
        Assertions.assertEquals(200, creation.code());
        Account account = creation.body();
        BigDecimal balance = BigDecimal.valueOf(-10);
        Response<DecimalValue> execution = client
                .setAccountBalance(account.getId(), new DecimalValue(balance))
                .execute();
        Assertions.assertEquals(200, execution.code());
        Assertions.assertEquals(balance, execution.body().getValue());
    }
}
