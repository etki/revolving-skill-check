package me.etki.tasks.revolving.api;

import lombok.*;
import lombok.experimental.Accessors;
import me.etki.tasks.revolving.Constants;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
public class DecimalValue {
    @Getter
    @Setter
    @NonNull
    @DecimalMin(value = "0", inclusive = false, groups = Positive.class)
    private BigDecimal value;

    public interface Positive {}

    public DecimalValue normalize() {
        return new DecimalValue(value.setScale(Constants.DECIMAL_SCALE, RoundingMode.HALF_EVEN));
    }
}
