package me.etki.tasks.revolving.api;

import me.etki.tasks.revolving.support.ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class TransferTest {

    private static ApiClient client;

    @BeforeAll
    public static void beforeAll() {
        client = ApiClient.create();
    }

    private static String randomCurrency() {
        return UUID.randomUUID().toString().toUpperCase().substring(0, 8);
    }

    private static Account createAccount(String currency, BigDecimal balance) throws Exception {
        AccountInput payload = new AccountInput(balance, currency);
        Response<Account> response = client.createAccount(payload).execute();
        Assertions.assertEquals(200, response.code());
        return response.body();
    }

    private static Account createAccount(String currency) throws Exception {
        return createAccount(currency, BigDecimal.TEN);
    }

    private static Account createAccount() throws Exception {
        return createAccount(randomCurrency());
    }

    private static Account getAccount(UUID id) throws Exception {
        Response<Account> response = client.getAccount(id).execute();
        Assertions.assertEquals(200, response.code());
        return response.body();
    }

    private static Rate setRate(String source, String target, BigDecimal rate) throws Exception {
        Response<Rate> response = client.setRate(source, target, new DecimalValue(rate)).execute();
        Assertions.assertEquals(200, response.code());
        return response.body();
    }

    @Test
    public void doesntPerformTransferForMissingAccount() throws Exception {
        String sourceCurrency = randomCurrency();
        String targetCurrency = randomCurrency();
        setRate(sourceCurrency, targetCurrency, BigDecimal.TEN);
        TransferInput input = new TransferInput()
                .setSource(UUID.randomUUID())
                .setTarget(UUID.randomUUID())
                .setAmount(BigDecimal.TEN)
                .setCurrency(sourceCurrency);
        Response<Transfer> transfer = client.createTransfer(input).execute();
        Assertions.assertEquals(400, transfer.code());
    }

    @Test
    public void doesntPerformTransferWithMissingRate() throws Exception {
        String sourceCurrency = randomCurrency();
        String targetCurrency = randomCurrency();
        Account source = createAccount(sourceCurrency);
        Account target = createAccount(targetCurrency);
        TransferInput input = new TransferInput()
                .setSource(source.getId())
                .setTarget(target.getId())
                .setAmount(BigDecimal.TEN)
                .setCurrency(sourceCurrency);
        Response<Transfer> transfer = client.createTransfer(input).execute();
        Assertions.assertEquals(400, transfer.code());
    }

    @Test
    public void doesntAcceptNegativeAmount() throws Exception {
        String currency = randomCurrency();
        Account source = createAccount(currency);
        Account target = createAccount(currency);
        // since accounts are of same currency, no exchange rate is needed
        TransferInput input = new TransferInput()
                .setSource(source.getId())
                .setTarget(target.getId())
                .setAmount(BigDecimal.valueOf(-10))
                .setCurrency(currency);
        Response<Transfer> transfer = client.createTransfer(input).execute();
        Assertions.assertEquals(400, transfer.code());
    }


    @Test
    public void doesntAcceptZeroAmount() throws Exception {
        String currency = randomCurrency();
        Account source = createAccount(currency);
        Account target = createAccount(currency);
        // since accounts are of same currency, no exchange rate is needed
        TransferInput input = new TransferInput()
                .setSource(source.getId())
                .setTarget(target.getId())
                .setAmount(BigDecimal.ZERO)
                .setCurrency(currency);
        Response<Transfer> transfer = client.createTransfer(input).execute();
        Assertions.assertEquals(400, transfer.code());
    }

    @Test
    public void doesntOverdraft() throws Exception {
        String currency = randomCurrency();
        Account source = createAccount(currency, BigDecimal.ONE);
        Account target = createAccount(currency);
        // since accounts are of same currency, no exchange rate is needed
        TransferInput input = new TransferInput()
                .setSource(source.getId())
                .setTarget(target.getId())
                .setAmount(BigDecimal.TEN)
                .setCurrency(currency);
        Response<Transfer> transfer = client.createTransfer(input).execute();
        Assertions.assertEquals(400, transfer.code());
    }

    @Test
    public void performsValidTransfer() throws Exception {
        String sourceCurrency = randomCurrency();
        String targetCurrency = randomCurrency();
        String transferCurrency = randomCurrency();
        Account source = createAccount(sourceCurrency, BigDecimal.TEN);
        Account target = createAccount(targetCurrency, BigDecimal.ZERO);
        setRate(sourceCurrency, transferCurrency, new BigDecimal("0.5"));
        setRate(transferCurrency, targetCurrency, new BigDecimal("0.5"));
        TransferInput input = new TransferInput()
                .setSource(source.getId())
                .setTarget(target.getId())
                .setAmount(new BigDecimal(2))
                .setCurrency(transferCurrency);
        Response<Transfer> transfer = client.createTransfer(input).execute();
        Assertions.assertEquals(200, transfer.code());
        Assertions.assertEquals(input.getSource(), source.getId());
        Assertions.assertEquals(input.getTarget(), target.getId());
        Assertions.assertEquals(input.getAmount(), transfer.body().getAmount());
        Assertions.assertEquals(input.getCurrency(), transfer.body().getCurrency());
        Response<Transfer> validation = client.getTransfer(transfer.body().getId()).execute();
        Assertions.assertEquals(200, validation.code());
        Account updatedSource = getAccount(source.getId());
        Account updatedTarget = getAccount(target.getId());
        Assertions.assertEquals(0, updatedSource.getBalance().compareTo(new BigDecimal(6)));
        Assertions.assertEquals(0, updatedTarget.getBalance().compareTo(BigDecimal.ONE));
    }

    @Test
    public void doesntNeedRateForSameCurrency() throws Exception {
        String currency = randomCurrency();
        Account source = createAccount(currency, BigDecimal.ONE);
        Account target = createAccount(currency, BigDecimal.ZERO);
        TransferInput input = new TransferInput()
                .setSource(source.getId())
                .setTarget(target.getId())
                .setAmount(BigDecimal.ONE)
                .setCurrency(currency);
        Response<Transfer> transfer = client.createTransfer(input).execute();
        Assertions.assertEquals(200, transfer.code());
        Account updatedSource = getAccount(source.getId());
        Account updatedTarget = getAccount(target.getId());
        Assertions.assertEquals(0, updatedSource.getBalance().compareTo(BigDecimal.ZERO));
        Assertions.assertEquals(0, updatedTarget.getBalance().compareTo(BigDecimal.ONE));
    }
}
