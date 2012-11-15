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
import org.spout.vanilla.data.Weather;

public class VanillaWeather {
    
    private Plugin plugin;
    
    public VanillaWeather(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"weather"}, desc = "Sets the weather of a Vanilla world", min = 1, max = 2)
    @CommandPermissions("mycommands.weather")
    public void time(CommandContext args, CommandSource source) throws CommandException
    {
        World world;
        if ( source instanceof Player && args.length() == 1 )
            world = ((Player) source).getWorld();
        else if ( args.length() == 2 )
            world = plugin.getEngine().getWorld(args.getString(1));
        else
            throw new CommandException("You have to specify a world to change it's time from the console.");
        
        Weather weather = Weather.get(args.getString(0).toUpperCase());
        
        if ( weather == null )
            throw new CommandException("'" + args.getString(0) + "' is not a valid weather state.");
        
        VanillaSky.getSky(world).setWeather(weather);
        source.sendMessage(ChatStyle.CYAN, source.getName() + " sets the weather of " + world.getName() + " to " + args.getString(0) + ".");
    }
}
