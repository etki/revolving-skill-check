package me.etki.tasks.revolving.api;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DecimalValue {
    @Getter
    @Setter
    @NonNull
    @DecimalMin(value = "0", inclusive = false, groups = Positive.class)
    private BigDecimal value;

    public interface Positive {}
}
