import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;

public class Print {
    
    public Print(Plugin plugin) {
    }
    
    @Command(aliases = {"print"}, desc = "Prints the message")
    @CommandPermissions("mycommands.print")
    public void print(CommandContext args, CommandSource source) throws CommandException {
        source.sendMessage(args.getJoinedString(0));
    }
}
