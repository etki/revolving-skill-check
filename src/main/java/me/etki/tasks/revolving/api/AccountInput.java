package me.etki.tasks.revolving.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.etki.tasks.revolving.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
        BigDecimal value = balance.setScale(Constants.DECIMAL_SCALE, RoundingMode.HALF_EVEN);
        return new AccountInput(value, currency.toUpperCase());
    }
}
