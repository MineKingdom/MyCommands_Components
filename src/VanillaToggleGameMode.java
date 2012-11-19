import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.component.living.passive.Human;
import org.spout.vanilla.data.GameMode;

public class VanillaToggleGameMode {
    
    private Plugin plugin;
    
    public VanillaToggleGameMode(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"tgm", "togglegamemode"}, desc = "Toggles the game mode", max = 1)
    @CommandPermissions("mycommands.togglegamemode")
    public void togglegamemode(CommandContext args, CommandSource source) throws CommandException
    {
        if ( source instanceof Player )
        {
            Player player = args.length() == 1 ? plugin.getEngine().getPlayer(args.getString(0), false) : (Player) source;
            
            if ( player == null )
                throw new CommandException("The specified player was not found.");
            
            GameMode mode = player.get(Human.class).getGameMode();
            player.get(Human.class).setGamemode(mode = mode.equals(GameMode.CREATIVE) ? GameMode.SURVIVAL : GameMode.CREATIVE);
            
            source.sendMessage(ChatStyle.CYAN, player.getName() + " is now in " + mode.name() + " mode.");
        }
    }
}
