package me.etki.tasks.revolving.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountInput {
    @Getter
    @Setter
    @NotNull
    private BigDecimal balance = BigDecimal.ZERO;
    @Getter
    @Setter
    @NotNull
    private String currency;

    public AccountInput normalize() {
        return new AccountInput(balance, currency.toUpperCase());
    }
}
