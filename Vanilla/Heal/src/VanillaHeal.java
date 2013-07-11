import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.event.cause.HealthChangeCause;
import org.spout.vanilla.component.entity.living.Human;

public class VanillaHeal {

    private Plugin plugin;

    public VanillaHeal(Plugin plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = { "heal" }, max = 1, desc = "Heal the specified player.")
    @Permissible("mycommands.heal")
    public void heal(CommandSource source, CommandArguments args) throws CommandException {
        Player player = null;

        if (args.length() == 1) {
            player = plugin.getEngine().getPlayer(args.getString(0), false);

            if (player == null)
                throw new CommandException("The specified player was not found.");

            source.sendMessage(args.getString(0) + " has been healed.");
            player.sendMessage("Healed !");
        } else {
            if (!(source instanceof Player)) {
                throw new CommandException("The Console cannot heal itself.");
            }

            player = (Player) source;
            source.sendMessage("Healed !");
        }

        player.get(Human.class).getHealth().setHealth(20, HealthChangeCause.COMMAND);
    }

}
