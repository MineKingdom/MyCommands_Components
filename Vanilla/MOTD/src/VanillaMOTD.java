import org.spout.api.Server;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.command.annotated.Platform;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.configuration.VanillaConfiguration;

public class VanillaMOTD {

    private Plugin plugin;

    public VanillaMOTD(Plugin plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = { "motd" }, desc = "Gets or sets the MOTD")
    @Permissible("mycommands.motd")
    @Platform(org.spout.api.Platform.SERVER)
    public void motd(CommandSource source, CommandArguments args) throws CommandException {
        if (args.length() == 0) {
            source.sendMessage(VanillaConfiguration.MOTD.getString());
        } else {
            String motd = args.getJoinedString(0);
            VanillaConfiguration.MOTD.setValue(motd);
            VanillaPlugin.getInstance().getConfig().save();
            
            ((Server) plugin.getEngine()).broadcastMessage(source.getName() + " has set the message of the day to : " + motd);
        }
    }
}
