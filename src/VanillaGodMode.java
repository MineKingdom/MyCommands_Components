

import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.plugin.component.living.neutral.Human;

public class VanillaGodMode {
    
    private Plugin plugin;
    
    public VanillaGodMode(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Command(aliases = {"god"}, usage = "[player]", desc = "Grants immortality to a player.", min = 0, max = 1)
    @CommandPermissions("mycommands.god")
    public void god(CommandContext args, CommandSource source) throws CommandException
    {
        if (!(source instanceof Player))
            throw new CommandException("You may only give god mode to players.");
        
        Human human;
        if (args.length() == 0)
        {
            human = ((Player) source).get(Human.class);
        }
        else
        {
            Player player = plugin.getEngine().getPlayer(args.getString(0), false);
            if ( player == null )
                throw new CommandException(args.getString(0) + " is not online.");
            human = player.get(Human.class);
        }
        human.setGodMode(true);
    }
    
    @Command(aliases = {"ungod"}, usage = "[player]", desc = "Grants mortality to a god.",  min = 0, max = 1)
    @CommandPermissions("mycommands.ungod")
    public void ungod(CommandContext args, CommandSource source) throws CommandException
    {
        if (!(source instanceof Player))
            throw new CommandException("You may only take god mode from players.");
        
        Human human;
        if (args.length() == 0)
        {
            human = ((Player) source).get(Human.class);
        }
        else
        {
            Player player = plugin.getEngine().getPlayer(args.getString(0), false);
            if ( player == null )
                throw new CommandException(args.getString(0) + " is not online.");
            human = player.get(Human.class);
        }
        human.setGodMode(false);
    }
}
