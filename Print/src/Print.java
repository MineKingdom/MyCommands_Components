import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.exception.CommandException;

public class Print {
    @Command(aliases = {"print"}, desc = "Prints the message")
    @Permissible("mycommands.print")
    public void print(CommandSource source, CommandArguments args) throws CommandException {
        source.sendMessage(args.getJoinedString(0));
    }
}
