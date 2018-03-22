package me.etki.tasks.revolving.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Acknowledgement {
    @Getter
    @Setter
    private boolean acknowledged;
}
