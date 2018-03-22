package me.etki.tasks.revolving.database.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.etki.tasks.revolving.api.Rate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "rate")
@EqualsAndHashCode(of = "id")
public class RateEntity {
    @EmbeddedId
    @Getter
    @Setter
    @Valid
    private Identifier id;
    @Getter
    @Setter
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Column
    private BigDecimal value;
    @Getter
    @Setter
    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Getter
    @Setter
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersistHook() {
        createdAt = ZonedDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdateHook() {
        updatedAt = ZonedDateTime.now();
    }

    public Rate toDomainEntity() {
        return (new Rate())
                .setSource(this.id.source)
                .setTarget(this.id.target)
                .setValue(this.value)
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt);
    }

    @SuppressWarnings("WeakerAccess")
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Identifier implements Serializable {
        public static final long serialVersionUID = 1;
        @Getter
        @Setter
        @NotBlank
        @Column
        private String source;
        @Getter
        @Setter
        @NotBlank
        @Column
        private String target;

        public Identifier normalize() {
            return new Identifier(source.toUpperCase(), target.toUpperCase());
        }
    }
}
