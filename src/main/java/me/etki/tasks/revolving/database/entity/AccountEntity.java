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
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "account")
@EqualsAndHashCode(of = "id")
public class AccountEntity {
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
    private BigDecimal amount;
    @Getter
    @Setter
    @NotNull
    @Column
    private String currency;
    @Getter
    @Setter
    @NotNull
    @Column(name = "created_at")
    private Date createdAt;
    @Getter
    @Setter
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    public void prePersistHook() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    public void preUpdateHook() {
        updatedAt = new Date();
    }
}
