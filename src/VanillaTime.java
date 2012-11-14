import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.component.world.VanillaSky;

public class VanillaTime {
    
    private Plugin plugin;
    
    public VanillaTime(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"time"}, desc = "Sets the time of a Vanilla world", min = 1, max = 2)
    @CommandPermissions("mycommands.togglegamemode")
    public void time(CommandContext args, CommandSource source) throws CommandException
    {
        World world;
        if ( source instanceof Player && args.length() == 1 )
            world = ((Player) source).getWorld();
        else if ( args.length() == 2 )
            world = plugin.getEngine().getWorld(args.getString(1));
        else
            throw new CommandException("You have to specify a world to change it's time from the console.");
        
        long time;
        String t = args.getString(0);
        if ( t.equals("day") )
            time = 6000;
        else if ( t.equals("dawn") )
            time = 0;
        else if ( t.equals("dusk") )
            time = 12000;
        else if ( t.equals("night") )
            time = 18000;
        else if ( t.matches("[0-9]+:[0-9]+") )
        {
            String[] s = t.split(":");
            time = (Long.parseLong(s[0]) - 6) * 1000 + Long.parseLong(s[1]) * 1000 / 60;
        }
        else
        {
            try
            {
                time = Long.parseLong(t);
            }
            catch ( NumberFormatException e )
            {
                throw new CommandException("'" + t + "' is not a valid time.");
            }
        }
        
        VanillaSky.getSky(world).setTime(time);
        source.sendMessage(ChatStyle.CYAN, source.getName() + " sets the time of " + world.getName() + " to " + (int)(Math.floor(time / 1000 + 6) % 24) + ":" + (int)Math.floor(time % 1000 * 60 / 1000) + ".");
    }
}
