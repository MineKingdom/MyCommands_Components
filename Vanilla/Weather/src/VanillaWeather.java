import org.spout.api.Server;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.command.annotated.Platform;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.component.world.sky.Sky;
import org.spout.vanilla.data.Weather;

public class VanillaWeather {

    private Plugin plugin;

    public VanillaWeather(Plugin plugin) {
        this.plugin = plugin;
    }

    private World getWorld(CommandSource source, CommandArguments args) throws CommandException {
        World world;
        if (source instanceof Player && args.length() == 1)
            world = ((Player) source).getWorld();
        else if (args.length() == 2)
            world = ((Server) plugin.getEngine()).getWorld(args.getString(1));
        else
            throw new CommandException("You have to specify a world to change it's time from the console.");

        if (world == null)
            throw new CommandException("'" + args.getString(1) + "' is not a valid world.");

        return world;
    }

    @Command(aliases = { "weather" }, usage = "<weather> [world]", desc = "Sets the weather of a Vanilla world", min = 1, max = 2)
    @Platform(org.spout.api.Platform.SERVER)
    @Permissible("mycommands.weather")
    public void client(CommandSource source, CommandArguments args) throws CommandException {
        World world = getWorld(source, args);
        Weather weather = Weather.get(args.getString(0));

        if (weather == null)
            throw new CommandException("'" + args.getString(0) + "' is not a valid weather state.");

        world.get(Sky.class).setWeather(weather);
        ((Server) plugin.getEngine()).broadcastMessage(source.getName() + " sets the weather of " + world.getName() + " to " + args.getString(0) + ".");
    }
}
