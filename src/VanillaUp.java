import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.material.VanillaMaterials;

public class VanillaUp {
    
    @SuppressWarnings("unused")
    private Plugin plugin;
    
    public VanillaUp(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"up"}, desc = "Raise your position by <x>.", min = 1, max = 1, usage = "<x>")
    @CommandPermissions("mycommands.up")
    public void up(CommandContext args, CommandSource source) throws CommandException
    {
        if ( source instanceof Player )
        {
            int n;
            try {
                n = Integer.parseInt(args.get(0).getPlainString());
            }
            catch ( NumberFormatException e )
            {
                throw new CommandException("Error: an integer was expected.");
            }
            
            if ( n < 0 )
                throw new CommandException("Error: a positive integer was expected.");
            
            final Player player = (Player) source;
            final Point p = player.getTransform().getPosition();
            
            p.getWorld().getBlock(p.getBlockX(), p.getBlockY() - 1 + n, p.getBlockZ()).setMaterial(VanillaMaterials.GLASS);
            
            player.teleport(new Point(p.getWorld(), p.getX(), p.getY() + n + 0.1f, p.getZ()));
            source.sendMessage(ChatStyle.CYAN, "Raised your position by " + n + ".");
        }
    }

}
