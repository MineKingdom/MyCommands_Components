import net.minekingdom.MyCommands.MyCommands;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

public class Ping {
    
    @SuppressWarnings("unused")
    private final MyCommands plugin;
    
    public Ping(MyCommands plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"ping"}, desc = "Pings the server")
    @CommandPermissions("mycommands.ping")
    public void ping(CommandContext args, CommandSource source) throws CommandException
    {
        if ( source instanceof Player )
        {
            MyCommands.sendMessage(source, ChatStyle.CYAN, "Pong!");
        }
        else
        {
            MyCommands.log("Pong!");
        }
    }

}

