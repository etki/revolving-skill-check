package me.etki.tasks.revolving.api;

import me.etki.tasks.revolving.support.ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class RateSetupTest {

    private static ApiClient client;

    @BeforeAll
    public static void beforeAll() {
        client = ApiClient.create();
    }

    private static String randomName() {
        return UUID.randomUUID().toString().toUpperCase().substring(0, 8);
    }

    @Test
    public void doesntCreateNegativeRate() throws Exception {
        DecimalValue payload = new DecimalValue(BigDecimal.valueOf(-10));
        Response<Rate> response = client.setRate(randomName(), randomName(), payload).execute();
        Assertions.assertEquals(400, response.code());
    }

    @Test
    public void doesntCreateForSameCurrency() throws Exception {
        String currency = randomName();
        Response<Rate> response = client
                .setRate(currency, currency, new DecimalValue(BigDecimal.TEN))
                .execute();
        Assertions.assertEquals(400, response.code());
    }

    @Test
    public void createsValidRate() throws Exception {
        BigDecimal value = BigDecimal.valueOf(50);
        String source = randomName();
        String target = randomName();
        Response<Rate> response = client
                .setRate(source, target, new DecimalValue(value))
                .execute();
        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(source, response.body().getSource());
        Assertions.assertEquals(target, response.body().getTarget());
        Response<Rate> query = client.getRate(source, target).execute();
        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(response.body(), query.body());
    }

    @Test
    public void normalizesCurrencyNames() throws Exception {
        String source = randomName();
        String target = randomName();
        BigDecimal value = BigDecimal.valueOf(50);
        Response<Rate> response = client
                .setRate(source.toLowerCase(), target.toLowerCase(), new DecimalValue(value))
                .execute();
        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(source, response.body().getSource());
        Assertions.assertEquals(target, response.body().getTarget());
    }
}
