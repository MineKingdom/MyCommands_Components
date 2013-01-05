import net.minekingdom.MyCommands.annotated.CommandPlatform;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.plugin.Platform;
import org.spout.api.plugin.Plugin;

public class Descend {
    
    @SuppressWarnings("unused")
    private Plugin plugin;
    
    public Descend(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"descend", "desc"}, desc = "Descends a level")
    @CommandPermissions("mycommands.descend")
    @CommandPlatform(Platform.SERVER)
    public void descend(CommandContext args, CommandSource source) throws CommandException
    {
        if ( source instanceof Player )
        {
            final Player player = (Player) source;
            final World world = player.getWorld();
            final int x = player.getTransform().getPosition().getBlockX(),
                      y = player.getTransform().getPosition().getBlockY(),
                      z = player.getTransform().getPosition().getBlockZ();
            
            for ( int i = y - 2; i > y - 256; i-- )
            {
                Block b1 = world.getBlock(x, i, z),
                      b2 = world.getBlock(x, i+1, z),
                      b3 = world.getBlock(x, i+2, z);
                if ( b1.getMaterial().isSolid() && !b2.getMaterial().isSolid() && !b3.getMaterial().isSolid() )
                {
                    player.teleport(new Point(world, x, i+1+0.1f, z));
                    break;
                }
            }
            
            source.sendMessage(ChatStyle.CYAN, "Descended a level.");
        }
    }

}
