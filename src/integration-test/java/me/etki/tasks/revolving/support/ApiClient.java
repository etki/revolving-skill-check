package me.etki.tasks.revolving.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.etki.tasks.revolving.api.Account;
import me.etki.tasks.revolving.api.AccountInput;
import me.etki.tasks.revolving.api.DecimalValue;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.Rate;
import me.etki.tasks.revolving.api.Transfer;
import me.etki.tasks.revolving.api.TransferInput;
import me.etki.tasks.revolving.io.jackson.MapperConfigurator;
import me.etki.tasks.revolving.server.Routes;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Optional;
import java.util.UUID;

public interface ApiClient {
    static ApiClient create() {
        ObjectMapper mapper = MapperConfigurator.configure(new ObjectMapper());
        String host = Optional
                .ofNullable(System.getProperty("mts.host"))
                .orElse("localhost");
        Integer port = Optional
                .ofNullable(System.getProperty("mts.port"))
                .map(Integer::valueOf)
                .orElse(8080);
        return new Retrofit.Builder()
                .baseUrl(String.format("http://%s:%d", host, port))
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build()
                .create(ApiClient.class);
    }

    @GET(Routes.ACCOUNT)
    Call<Page<Account>> getAccountPage(@Query("page") Integer page, @Query("size") Integer size);

    @GET(Routes.ACCOUNT_SINGLE)
    Call<Account> getAccount(@Path("id") UUID id);

    @POST(Routes.ACCOUNT)
    Call<Account> createAccount(@Body AccountInput input);

    @PUT(Routes.ACCOUNT_BALANCE)
    Call<DecimalValue> setAccountBalance(@Path("id") UUID id, @Body DecimalValue value);

    @GET(Routes.RATE)
    Call<Page<Rate>> getRatePage(@Query("page") Integer page, @Query("size") Integer size);

    @GET(Routes.RATE_SINGLE)
    Call<Rate> getRate(@Path("source") String source, @Path("target") String target);

    @PUT(Routes.RATE_SINGLE)
    Call<Rate> setRate(@Path("source") String source, @Path("target") String target, @Body DecimalValue value);

    @GET(Routes.TRANSFER)
    Call<Page<Transfer>> getTransferPage(@Query("page") Integer page, @Query("size") Integer size);

    @GET(Routes.TRANSFER_SINGLE)
    Call<Transfer> getTransfer(@Path("id") UUID id);

    @POST(Routes.TRANSFER)
    Call<Transfer> createTransfer(@Body TransferInput input);
}
