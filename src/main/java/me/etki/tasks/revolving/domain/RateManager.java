package me.etki.tasks.revolving.domain;

import com.google.inject.Inject;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.Rate;
import me.etki.tasks.revolving.database.AsyncExecutor;
import me.etki.tasks.revolving.database.entity.RateEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class RateManager {
    private static final String PAGE_QUERY = "SELECT r FROM RateEntity r ORDER BY r.id.source, r.id.target";
    private static final String COUNT_QUERY = "SELECT COUNT(r) FROM RateEntity r";

    private final AsyncExecutor executor;

    @Inject
    public RateManager(AsyncExecutor executor) {
        this.executor = executor;
    }

    public CompletableFuture<Page<Rate>> findPage(int page, int size) {
        return executor.execute(manager -> {
            List<Rate> results = manager
                    .createQuery(PAGE_QUERY, RateEntity.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList()
                    .stream()
                    .map(RateEntity::toDomainEntity)
                    .collect(Collectors.toList());
            Long count = (Long) manager.createQuery(COUNT_QUERY).getSingleResult();
            Page.Metadata metadata = Page.Metadata.from(page, size, count.intValue());
            return new Page<>(results, metadata);
        });
    }

    public CompletableFuture<Page<Rate>> getPage(int page, int size) {
        return Helpers.requirePage(findPage(page, size));
    }

    public CompletableFuture<Optional<Rate>> find(String source, String target) {
        return executor.execute(manager -> {
            RateEntity.Identifier id = new RateEntity.Identifier(source, target).normalize();
            return Optional
                    .ofNullable(manager.find(RateEntity.class, id))
                    .map(RateEntity::toDomainEntity);
        });
    }

    public CompletableFuture<Rate> get(String source, String target) {
        return Helpers.requireEntity(find(source, target));
    }

    public CompletableFuture<Rate> set(String source, String target, BigDecimal value) {
        return executor.execute(manager -> {
            RateEntity.Identifier id = new RateEntity.Identifier(source, target).normalize();
            RateEntity entity = Optional
                    .ofNullable(manager.find(RateEntity.class, id))
                    .orElseGet(RateEntity::new);
            entity.setId(id);
            entity.setValue(value);
            manager.persist(entity);
            manager.flush();
            return entity.toDomainEntity();
        });
    }
}
