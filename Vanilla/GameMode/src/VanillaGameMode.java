import org.spout.api.Server;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.command.annotated.Platform;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.data.GameMode;

public class VanillaGameMode {

    private Plugin plugin;

    public VanillaGameMode(Plugin plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = { "gamemode", "gm" }, max = 2, 
            desc = "Changes a player's game mode.\n" 
                    + "Not specifiying the game mode parameter will cycle the target's current game mode.\n" 
                    + "The cycle goes as follow : Survival (0) -> Creative (1) -> Adventure (2) -> Survival (0).")
    @Platform(org.spout.api.Platform.SERVER)
    @Permissible("mycommands.gamemode")
    public void gamemode(CommandSource source, CommandArguments args) throws CommandException {
        if (source instanceof Player) {
            GameMode mode = null;
            Player player = null;

            if (args.length() == 2) {
                player = ((Server) plugin.getEngine()).getPlayer(args.getString(0), false);
                if (player == null)
                    throw new CommandException("The specified player was not found.");

                try {
                    mode = GameMode.get(Integer.parseInt(args.getString(1)));
                } catch (NumberFormatException e) {
                    mode = GameMode.get(args.getString(1));
                }

                if (mode == null)
                    throw new CommandException("'" + args.getString(1) + "' is not a valid GameMode.");
            } else if (args.length() == 1) {
                try {
                    mode = GameMode.get(Integer.parseInt(args.getString(0)));
                } catch (NumberFormatException e) {
                    mode = GameMode.get(args.getString(0));
                }

                if (mode != null) {
                    if (!(source instanceof Player))
                        throw new CommandException("The console has no GameMode.");

                    player = (Player) source;
                } else {
                    player = ((Server) plugin.getEngine()).getPlayer(args.getString(0), false);
                    if (player == null)
                        throw new CommandException("The specified player was not found.");

                    int modeId = (player.get(Human.class).getGameMode().ordinal() + 1) % 3;

                    mode = GameMode.get(modeId);
                }
            } else {
                if (!(source instanceof Player))
                    throw new CommandException("The console has no GameMode.");

                player = (Player) source;
                int modeId = (player.get(Human.class).getGameMode().ordinal() + 1) % 3;

                mode = GameMode.get(modeId);
            }

            player.get(Human.class).setGamemode(mode);

            source.sendMessage((args.length() > 1 ? (player.getName() + " is") : "You are") + " now in " + mode.name() + " mode.");
        }
    }
}
