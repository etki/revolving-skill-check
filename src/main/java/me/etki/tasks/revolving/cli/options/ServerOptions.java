package me.etki.tasks.revolving.cli.options;

import com.github.rvesse.airline.annotations.Option;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.etki.tasks.revolving.api.configuration.ServerConfiguration;

@Accessors(chain = true)
public class ServerOptions {
    @Getter
    @Setter
    @Option(name = "--host")
    public String host = "localhost";

    @Getter
    @Setter
    @Option(name = "--port")
    public int port = 8080;

    public ServerConfiguration toConfiguration() {
        return new ServerConfiguration()
                .setHost(host)
                .setPort(port);
    }
}
