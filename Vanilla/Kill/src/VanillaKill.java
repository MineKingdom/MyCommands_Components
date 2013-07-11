import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.event.cause.HealthChangeCause;
import org.spout.vanilla.component.entity.living.Human;

public class VanillaKill {

    private Plugin plugin;

    public VanillaKill(Plugin plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = { "kill" }, max = 1, desc = "Kill the specified player.")
    @Permissible("mycommands.kill")
    public void kill(CommandSource source, CommandArguments args) throws CommandException {
        Player player = null;

        if (args.length() == 1) {
            player = plugin.getEngine().getPlayer(args.getString(0), false);

            if (player == null)
                throw new CommandException("The specified player was not found.");

            source.sendMessage(args.getString(0) + " has been slain.");
            player.sendMessage("You died from an invisible yet powerful force.");
        } else {
            if (!(source instanceof Player)) {
                throw new CommandException("The Console cannot kill itself.");
            }

            player = (Player) source;
            source.sendMessage("You died from an invisible yet powerful force.");
        }

        player.get(Human.class).getHealth().kill(HealthChangeCause.COMMAND);
    }

}
