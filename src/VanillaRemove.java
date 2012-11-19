
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import net.minekingdom.MyCommands.MyCommands;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.component.Component;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.living.Hostile;
import org.spout.vanilla.component.living.Living;
import org.spout.vanilla.component.living.Passive;
import org.spout.vanilla.component.misc.PickupItemComponent;

public class VanillaRemove {
    
    private final MyCommands plugin;
    
    public VanillaRemove(MyCommands plugin)
    {
        this.plugin = plugin;
    }
    
    @SuppressWarnings("unchecked")
    @Command(aliases = {"remove", "rm"}, desc = "Removes entities around the source.", max = 2, usage = "<type1,type2[,...typen]> [radius]")
    @CommandPermissions("mycommands.remove")
    public void remove(CommandContext args, CommandSource source) throws CommandException
    {
        if ( source instanceof Player )
        {
            final Player player = (Player) source;
            final Point p = player.getTransform().getPosition();
            
            String[] arr = args.getString(0).split(",");
            Set<String> types = new HashSet<String>();
            for ( String s : arr )
            {
                types.add(s.toLowerCase());
            }
            
            int radius;
            if ( args.length() == 2 )
            {
                try { radius = Integer.parseInt(args.getString(1)); }
                catch ( NumberFormatException e )
                {
                    throw new CommandException("Error : '" + args.getString(1) + "' is not a valid radius.");
                }
            }
            else
            {
                radius = 64;
            }
            
            List<Entity> entities = player.getWorld().getNearbyEntities(player, radius);
            for ( Entity e : entities )
            {
                if ( e instanceof Player )
                    continue;
                
                if ( types.contains("all") )
                {
                    e.remove();
                    continue;
                }
                
                if ( types.contains("items") && e.has(PickupItemComponent.class) )
                {
                    e.remove();
                    continue;
                }
                
                if ( types.contains("living") && e.has(Living.class))
                {
                    e.remove();
                    continue;
                }


                
            }
            
            MyCommands.sendMessage((Player) source, ChatStyle.CYAN, "Removed " + " entities.");
        }
    }

}
