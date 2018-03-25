package me.etki.tasks.revolving.domain;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.Transfer;
import me.etki.tasks.revolving.api.TransferInput;
import me.etki.tasks.revolving.database.AsyncExecutor;
import me.etki.tasks.revolving.database.entity.AccountEntity;
import me.etki.tasks.revolving.database.entity.RateEntity;
import me.etki.tasks.revolving.database.entity.TransferEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class TransferManager {
    private static final String PAGE_QUERY = "SELECT t FROM Transfer t ORDER BY t.createdAt";
    private static final String COUNT_QUERY = "SELECT COUNT(t) FROM Transfer t";

    private final AsyncExecutor executor;

    @Inject
    public TransferManager(AsyncExecutor executor) {
        this.executor = executor;
    }
    public CompletableFuture<Page<Transfer>> findPage(int page, int size) {
        return executor.execute(manager -> {
            List<Transfer> results = manager
                    .createQuery(PAGE_QUERY, TransferEntity.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList()
                    .stream()
                    .map(TransferEntity::toDomainEntity)
                    .collect(Collectors.toList());
            Long count = (Long) manager.createQuery(COUNT_QUERY).getSingleResult();
            Page.Metadata metadata = Page.Metadata.from(page, size, count.intValue());
            return new Page<>(results, metadata);
        });
    }

    public CompletableFuture<Page<Transfer>> getPage(int page, int size) {
        return Helpers.requirePage(findPage(page, size));
    }

    @SuppressWarnings("squid:S1602")
    public CompletableFuture<Optional<Transfer>> find(UUID id) {
        return executor.execute(manager -> {
            //noinspection CodeBlock2Expr
            return Optional
                    .ofNullable(manager.find(TransferEntity.class, id))
                    .map(TransferEntity::toDomainEntity);
        });
    }

    public CompletableFuture<Transfer> get(UUID id) {
        return Helpers.requireEntity(find(id));
    }

    public CompletableFuture<Transfer> create(TransferInput input) {
        return executor.execute(manager ->
                new TransferExecution(manager, input.normalize()).execute()
        );
    }

    @RequiredArgsConstructor
    private static class TransferExecution {
        private static final Logger LOGGER = LoggerFactory.getLogger(TransferExecution.class);

        private final EntityManager manager;
        private final TransferInput input;

        public Transfer execute() {
            AccountEntity source = getAccount(input.getSource());
            AccountEntity target = getAccount(input.getTarget());
            LOGGER.info(
                    "Transfering `{}` {} from account `{}` to `{}`",
                    input.getAmount(),
                    input.getCurrency(),
                    input.getSource(),
                    input.getTarget()
            );
            RateEntity withdrawalRate = null;
            RateEntity topUpRate = null;
            if (!target.getCurrency().equals(input.getCurrency())) {
                topUpRate = getRate(target.getCurrency(), input.getCurrency());
            }
            if (!source.getCurrency().equals(input.getCurrency())) {
                withdrawalRate = getRate(source.getCurrency(), input.getCurrency());
            }
            BigDecimal withdrawal = Optional
                    .ofNullable(withdrawalRate)
                    .map(rate -> input.getAmount().multiply(rate.getValue()))
                    .orElse(input.getAmount());
            BigDecimal topUp = Optional
                    .ofNullable(topUpRate)
                    .map(rate -> input.getAmount().multiply(rate.getValue()))
                    .orElse(input.getAmount());
            LOGGER.info("Withdrawing `{}` {} from account `{}`", withdrawal, source.getCurrency(), source.getId());
            if (source.getBalance().compareTo(withdrawal) < 0) {
                String message = "Account " + input.getSource() + " doesn't have enough funds";
                throw new ConstraintViolationException(message, Collections.emptySet());
            }
            source.setBalance(source.getBalance().subtract(withdrawal));
            LOGGER.info("Adding `{}` {} to account `{}`", topUp, target.getCurrency(), target.getId());
            target.setBalance(target.getBalance().add(topUp));
            manager.persist(source);
            manager.persist(target);
            TransferEntity entity = createEntity();
            manager.persist(entity);
            return entity.toDomainEntity();
        }

        private TransferEntity createEntity() {
            TransferEntity entity = new TransferEntity();
            entity.setSource(input.getSource());
            entity.setTarget(input.getTarget());
            entity.setAmount(input.getAmount());
            entity.setCurrency(input.getCurrency());
            return entity;
        }

        private AccountEntity getAccount(UUID id) {
            return Optional
                    .ofNullable(manager.find(AccountEntity.class, id))
                    .orElseThrow(() -> {
                        String message = "Couldn't find account with id " + id;
                        return new ConstraintViolationException(message, Collections.emptySet());
                    });
        }

        private RateEntity getRate(String source, String target) {
            RateEntity.Identifier id = new RateEntity.Identifier(source, target);
            return Optional
                    .ofNullable(manager.find(RateEntity.class, id))
                    .orElseThrow(() -> {
                        String message = "Couldn't find exchange rate " + source + " -> " + target;
                        return new ConstraintViolationException(message, Collections.emptySet());
                    });
        }
    }
}
