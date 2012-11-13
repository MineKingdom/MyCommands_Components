import net.minekingdom.MyCommands.MyCommands;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;

public class Up {
    
    @SuppressWarnings("unused")
    private final MyCommands plugin;
    
    public Up(MyCommands plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"up"}, desc = "Raise your position by <x>.", min = 1, max = 1, usage = "<x>")
    @CommandPermissions("mycommands.up")
    public void ping(CommandContext args, CommandSource source) throws CommandException
    {
        if ( source instanceof Player )
        {
            int n;
            try {
                n = Integer.parseInt(args.get(0).getPlainString());
            }
            catch ( NumberFormatException e )
            {
                MyCommands.sendErrorMessage(source, "Error: an integer was expected.");
                return;
            }
            
            if ( n < 0 )
            {
                MyCommands.sendErrorMessage(source, "Error: a positive integer was expected.");
                return;
            }
            
            final Player player = (Player) source;
            final Point p = player.getTransform().getPosition();
            
            player.teleport(new Point(p.getWorld(), p.getX(), p.getY() + n + 0.1f, p.getZ()));
            MyCommands.sendMessage(source, ChatStyle.CYAN, "Raised your position by " + n + ".");
        }
    }

}
