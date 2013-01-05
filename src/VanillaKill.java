import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.component.living.neutral.Human;
import org.spout.vanilla.event.cause.HealthChangeCause;

public class VanillaKill {
    
    private Plugin plugin;
    
    public VanillaKill(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"kill"}, max = 1, desc = "Kill the specified player.")
    @CommandPermissions("mycommands.kill")
    public void kill(CommandContext args, CommandSource source) throws CommandException
    {
        Player player = null;
        
        if (args.length() == 1)
        {
           player = plugin.getEngine().getPlayer(args.getString(0), false);
           
           if ( player == null )
               throw new CommandException("The specified player was not found.");

           source.sendMessage(ChatStyle.CYAN, args.getString(0) + " has been slain.");
           player.sendMessage(ChatStyle.RED, "Killed !");
        }
        else
        {
           if (!( source instanceof Player ))
           {
               throw new CommandException("The Console cannot kill itself.");
           }
           
           player = (Player) source;
           source.sendMessage(ChatStyle.RED, "Killed !");
        }    
        
        player.get(Human.class).getHealth().kill(HealthChangeCause.COMMAND);
        
    }

}
