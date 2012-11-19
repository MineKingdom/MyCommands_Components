import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.configuration.VanillaConfiguration;

public class VanillaOnlineMode {
    
    @SuppressWarnings("unused")
    private Plugin plugin;
    
    public VanillaOnlineMode(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"onlinemode", "om"}, max = 1, desc = "Sets the vanilla server to offline or online mode")
    @CommandPermissions("mycommands.onlinemode")
    public void motd(CommandContext args, CommandSource source) throws CommandException
    {
        boolean online = args.length() == 0 ? !VanillaConfiguration.ONLINE_MODE.getBoolean() : Boolean.parseBoolean(args.getString(0));
        VanillaConfiguration.ONLINE_MODE.setValue(online);
        source.sendMessage(ChatStyle.CYAN, "The server is now in " + (online ? "online" : "offline") + " mode.");
    }
}
