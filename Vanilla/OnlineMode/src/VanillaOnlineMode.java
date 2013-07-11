import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.configuration.VanillaConfiguration;

public class VanillaOnlineMode {
    
    public VanillaOnlineMode(Plugin plugin) {
    }

    @Command(aliases = { "onlinemode", "om" }, max = 1, desc = "Sets the vanilla server to offline or online mode")
    @Permissible("mycommands.onlinemode")
    public void onlineMode(CommandSource source, CommandArguments args) throws CommandException {
        boolean online = args.length() == 0 ? !VanillaConfiguration.ONLINE_MODE.getBoolean() : Boolean.parseBoolean(args.getString(0));
        VanillaConfiguration.ONLINE_MODE.setValue(online);
        VanillaPlugin.getInstance().getConfig().save();
        source.sendMessage("The server is now in " + (online ? "online" : "offline") + " mode.");
    }
}
