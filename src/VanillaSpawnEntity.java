import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.engine.entity.SpoutEntity;
import org.spout.vanilla.plugin.protocol.entity.creature.CreatureType;

public class VanillaSpawnEntity {
    
    private Plugin plugin;
    
    public VanillaSpawnEntity(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"spawnentity", "spawnmob"}, min = 1, max = 2, usage = "<entity> <quantity>", desc = "Spawns an entity into the world")
    @CommandPermissions("mycommands.spawnentity")
    public void spawnentity(CommandContext args, CommandSource source) throws CommandException
    {
        if ( source instanceof Player )
        {
            final Player player = (Player) source;
            final Entity entity = new SpoutEntity(player.getTransform().getPosition());
            
            CreatureType type = CreatureType.valueOf(args.getString(0).toUpperCase());
            if ( type == null )
            {
                String types = "";
                for (CreatureType ct : CreatureType.values())
                {
                    types += ct.name() + " ";
                }
                throw new CommandException("'" + args.getString(0) + "' is not a valid entity type. Valid types are : \n" + types);
            }
            
            entity.add(type.getComponentType());
            player.getWorld().spawnEntity(entity);
        }
    }
}
