package me.etki.tasks.revolving.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.etki.tasks.revolving.Constants;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Accessors(chain = true)
public class TransferInput {
    @Getter
    @Setter
    @NotNull
    private UUID source;
    @Getter
    @Setter
    @NotNull
    private UUID target;
    @Getter
    @Setter
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;
    @Getter
    @Setter
    @NotNull
    private String currency;

    public TransferInput normalize() {
        return new TransferInput()
                .setSource(source)
                .setTarget(target)
                .setAmount(amount.setScale(Constants.DECIMAL_SCALE, RoundingMode.HALF_EVEN))
                .setCurrency(currency.toUpperCase());
    }
}
