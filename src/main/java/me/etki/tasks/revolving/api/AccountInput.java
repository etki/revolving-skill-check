package me.etki.tasks.revolving.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Accessors(chain = true)
public class AccountInput {
    @Getter
    @Setter
    @NotNull
    private BigDecimal balance = BigDecimal.ZERO;
    @Getter
    @Setter
    @NotNull
    private String currency;
}
