import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;
import org.spout.api.plugin.Plugin;

public class Whois {

    private Plugin plugin;

    public Whois(Plugin plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = { "whois", "info" }, desc = "Gets information on a player", max = 1)
    @Permissible("mycommands.whois")
    public void whois(CommandSource source, CommandArguments args) throws CommandException {
        Player player;
        if (args.length() == 1) {
            player = plugin.getEngine().getPlayer(args.getString(0), false);
            if (player == null)
                throw new CommandException("Player " + args.getString(0) + " could not be found.");
        } else if (source instanceof Player) {
            player = (Player) source;
        } else {
            throw new CommandException("The command must target a player.");
        }

        Point p = player.getScene().getPosition();

        source.sendMessage("Player : " + player.getName());
        source.sendMessage("IP Address : " + player.getAddress().getHostAddress());
        source.sendMessage("world : " + player.getWorld().getName() + ", position : " + p.getX() + ", " + p.getY() + ", " + p.getZ());
    }

}
