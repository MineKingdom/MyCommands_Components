import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;

public class Ping {
    public Ping(Plugin plugin) {}

    @Command(aliases = { "ping" }, desc = "Pings the server")
    @Permissible("mycommands.ping")
    public void ping(CommandSource source, CommandArguments args) throws CommandException {
        source.sendMessage("Pong!");
    }
}
