package me.etki.tasks.revolving.domain;

import com.google.inject.Inject;
import me.etki.tasks.revolving.api.Account;
import me.etki.tasks.revolving.api.AccountInput;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.exception.ResourceNotFoundException;
import me.etki.tasks.revolving.database.AsyncExecutor;
import me.etki.tasks.revolving.database.entity.AccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class AccountManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountManager.class);

    private static final String PAGE_QUERY = "SELECT a FROM AccountEntity a ORDER BY a.createdAt DESC";
    private static final String COUNT_QUERY = "SELECT COUNT(a) FROM AccountEntity a";

    private final AsyncExecutor executor;

    @Inject
    public AccountManager(AsyncExecutor executor) {
        this.executor = executor;
    }

    private static ResourceNotFoundException missingAccountException(UUID id) {
        String message = "Account `" + id + "` doesn't exist";
        return new ResourceNotFoundException(message);
    }

    public CompletableFuture<Page<Account>> findPage(int page, int size) {
        return executor.execute(manager -> {
            List<Account> results = manager
                    .createQuery(PAGE_QUERY, AccountEntity.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList()
                    .stream()
                    .map(AccountEntity::toDomainEntity)
                    .collect(Collectors.toList());
            Long count = (Long) manager.createQuery(COUNT_QUERY).getSingleResult();
            Page.Metadata metadata = Page.Metadata.from(page, size, count.intValue());
            return new Page<>(results, metadata);
        });
    }

    public CompletableFuture<Page<Account>> getPage(int page, int size) {
        return Helpers.requirePage(findPage(page, size));
    }

    public CompletableFuture<Account> create(AccountInput input) {
        AccountInput source = input.normalize();
        return executor
                .execute(manager -> {
                    AccountEntity entity = new AccountEntity();
                    entity.setCurrency(source.getCurrency());
                    entity.setBalance(source.getBalance());
                    manager.persist(entity);
                    return entity.toDomainEntity();
                })
                .thenApply(entity -> {
                    LOGGER.info(
                            "Created account `{}` with `{}` {}",
                            entity.getId(),
                            source.getBalance(),
                            source.getCurrency()
                    );
                    return entity;
                });
    }

    public CompletableFuture<Account> setBalance(UUID id, BigDecimal value) {
        return executor.execute(manager -> {
            AccountEntity entity = manager.find(AccountEntity.class, id);
            if (entity == null) {
                throw missingAccountException(id);
            }
            LOGGER.info("Setting account `{}` balance to {}", id, value);
            entity.setBalance(value);
            manager.persist(entity);
            return entity.toDomainEntity();
        });
    }

    @SuppressWarnings("squid:S1602")
    public CompletableFuture<Optional<Account>> find(UUID id) {
        return executor.execute(manager -> {
            //noinspection CodeBlock2Expr
            return Optional
                    .ofNullable(manager.find(AccountEntity.class, id))
                    .map(AccountEntity::toDomainEntity);
        });
    }

    public CompletableFuture<Account> get(UUID id) {
        return Helpers.requireEntity(find(id));
    }
}
