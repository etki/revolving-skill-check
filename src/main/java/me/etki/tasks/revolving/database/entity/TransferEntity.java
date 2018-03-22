package me.etki.tasks.revolving.database.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transfer")
@EqualsAndHashCode(of = "id")
public class TransferEntity {
    @Getter
    @Setter
    @NotNull
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column
    private UUID id;
    @Getter
    @Setter
    @NotNull
    @Column
    private UUID source;
    @Getter
    @Setter
    @NotNull
    @Column
    private UUID target;
    @Getter
    @Setter
    @NotNull
    @Column
    private String currency;
    @Getter
    @Setter
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Column
    private BigDecimal amount;
    @Getter
    @Setter
    @Column(name = "executed_at")
    private Date executedAt;

    @PrePersist
    public void prePersistHook() {
        executedAt = new Date();
    }
}
