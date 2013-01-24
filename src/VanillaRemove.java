
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.component.Component;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.plugin.protocol.entity.creature.CreatureType;

public class VanillaRemove {
    
    private final Plugin plugin;
    
    public VanillaRemove(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"remove", "rm"}, desc = "Removes entities around the source.", min = 1, max = 2, usage = "<type-1,type-2[,...type-n]> [radius]")
    @CommandPermissions("mycommands.remove")
    public void remove(CommandContext args, CommandSource source) throws CommandException
    {
        if ( source instanceof Player )
        {
            final Player player = (Player) source;
            
            String[] arr = args.getString(0).split(",");
            
            Set<String> add = new HashSet<String>();
            Set<String> remove = new HashSet<String>();
            
            for ( String s : arr )
            {
                if ( s.startsWith("!") )
                    remove.add(s.substring(1).toUpperCase());
                else
                    add.add(s.toUpperCase());
            }
            
            Set<Class<? extends Component>> types = new HashSet<Class<? extends Component>>();
            
            if ( add.isEmpty() )
            {
                for ( CreatureType type : CreatureType.values() )
                    types.add(type.getComponentType());
            }
            else
            {
                for ( String s : add )
                {
                    CreatureType creature = CreatureType.valueOf(s);
                    if ( creature != null )
                        types.add(creature.getComponentType());
                }
            }
            
            for ( String s : remove )
            {
                CreatureType creature = CreatureType.valueOf(s);
                if ( creature != null )
                    types.remove(creature.getComponentType());
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
                
                for ( Class<? extends Component> type : types )
                {
                    if ( e.has(type) )
                    {
                        e.remove();
                        break;
                    }
                }
            }
            
            source.sendMessage(ChatStyle.CYAN, "Removed " + " entities.");
        }
    }

}
