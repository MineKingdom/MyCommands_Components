import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.configuration.VanillaConfiguration;

public class VanillaToggleOnlineMode {
    
    @SuppressWarnings("unused")
    private Plugin plugin;
    
    public VanillaToggleOnlineMode(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"toggleonlinemode", "onlinemode", "tom"}, max = 1, desc = "Sets the vanilla server to offline or online mode")
    @CommandPermissions("mycommands.toggleonlinemode")
    public void motd(CommandContext args, CommandSource source) throws CommandException
    {
        boolean online = args.length() == 0 ? !VanillaConfiguration.ONLINE_MODE.getBoolean() : Boolean.parseBoolean(args.getString(0));
        VanillaConfiguration.ONLINE_MODE.setValue(online);
        source.sendMessage(ChatStyle.CYAN, "The server is now in " + (online ? "online" : "offline") + " mode.");
    }
}
