package me.etki.tasks.revolving.cli.options;

import com.github.rvesse.airline.annotations.Option;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.etki.tasks.revolving.api.configuration.DatabaseConfiguration;

@Accessors(chain = true)
public class DatabaseOptions {
    @Getter
    @Setter
    @Option(name = {"--jdbc", "-d"})
    private String jdbcUrl = "h2:mem";

    public DatabaseConfiguration toConfiguration() {
        return new DatabaseConfiguration().setJdbcUrl(jdbcUrl);
    }
}
