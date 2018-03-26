package me.etki.tasks.revolving.server;

public class Routes {
    public static final String SHUTDOWN = "/v1/_shutdown";
    public static final String HEALTH = "/v1/_health";
    public static final String OPENAPI = "/v1/_openapi";
    public static final String OPENAPI_SCHEMA = Routes.OPENAPI + "/schema.yml";
    public static final String ACCOUNT = "/v1/account";
    public static final String ACCOUNT_SINGLE = ACCOUNT + "/{id}";
    public static final String ACCOUNT_BALANCE = "/v1/account/{id}/balance";
    public static final String TRANSFER = "/v1/transfer";
    public static final String TRANSFER_SINGLE = TRANSFER + "/{id}";
    public static final String RATE = "/v1/rate";
    public static final String RATE_SINGLE = RATE + "/{source}/{target}";

    private Routes() {
        // static access only
    }
}
