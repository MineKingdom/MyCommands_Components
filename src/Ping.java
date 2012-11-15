import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;

public class Ping {
    
    @SuppressWarnings("unused")
    private Plugin plugin;
    
    public Ping(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"ping"}, desc = "Pings the server")
    @CommandPermissions("mycommands.ping")
    public void ping(CommandContext args, CommandSource source) throws CommandException
    {
        source.sendMessage(ChatStyle.CYAN, "Pong!");
    }

}

