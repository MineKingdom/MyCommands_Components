import org.spout.api.Server;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.plugin.Plugin;
import org.spout.api.Platform;
import org.spout.vanilla.component.world.sky.Sky;

public class VanillaTime {
    
    private Plugin plugin;
    
    public VanillaTime(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    private World getWorld(CommandContext args, CommandSource source) throws CommandException
    {
        World world;
        if ( source instanceof Player && args.length() == 1 )
            world = ((Player) source).getWorld();
        else if ( args.length() == 2 )
            world = plugin.getEngine().getWorld(args.getString(1));
        else
            throw new CommandException("You have to specify a world to change it's time from the console.");
        
        if ( world == null )
            throw new CommandException("'" + args.getString(1) + "' is not a valid world.");
        
        return world;
    }
    
    private long getTime(CommandContext args, CommandSource source) throws CommandException
    {
        String t = args.getString(0);
        if ( t.equals("day") )
            return 6000;
        else if ( t.equals("dawn") )
            return 0;
        else if ( t.equals("dusk") )
            return 12000;
        else if ( t.equals("night") )
            return 18000;
        else if ( t.matches("[0-9]+:[0-9]+") )
        {
            String[] s = t.split(":");
            return (Long.parseLong(s[0]) - 6) * 1000 + Long.parseLong(s[1]) * 1000 / 60;
        }
        else
        {
            try
            {
                return Long.parseLong(t);
            }
            catch ( NumberFormatException e )
            {
                throw new CommandException("'" + t + "' is not a valid time.");
            }
        }
    }
        
    @Command(aliases = {"time"}, desc = "Sets the time of a Vanilla world", min = 1, max = 2)
    @CommandPermissions("mycommands.time")
    public void time(CommandContext args, CommandSource source) throws CommandException
    {
        World world = getWorld(args, source);
        long time = getTime(args, source);

        world.get(Sky.class).setTime(time);
        if (plugin.getEngine().getPlatform() == Platform.CLIENT) {
            source.sendMessage(ChatStyle.CYAN, "You have set the time of " + world.getName() + " to " + (int)(Math.floor(time / 1000 + 6) % 24) + ":" + (int)Math.floor(time % 1000 * 60 / 1000) + ".");
        } else if (plugin.getEngine().getPlatform() == Platform.SERVER) {
            ((Server) plugin.getEngine()).broadcastMessage(ChatStyle.CYAN, source.getName() + " sets the time of " + world.getName() + " to " + (int)(Math.floor(time / 1000 + 6) % 24) + ":" + (int)Math.floor(time % 1000 * 60 / 1000) + ".");
        }
    }
}
