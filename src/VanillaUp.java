import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.world.block.BlockChangeMessage;

public class VanillaUp {

    public VanillaUp(Plugin plugin) {
    }

    @Command(aliases = { "up" }, desc = "Raise your position by <x>.", min = 1, max = 1, usage = "<x>")
    @CommandPermissions("mycommands.up")
    public void up(CommandContext args, CommandSource source) throws CommandException {
        if (source instanceof Player) {
            int n;
            try {
                n = Integer.parseInt(args.get(0).getPlainString());
            } catch (NumberFormatException e) {
                throw new CommandException("Error: an integer was expected.");
            }

            final Player player = (Player) source;
            final Point p = player.getScene().getPosition();
            final Block b = p.getWorld().getBlock(p.getBlockX(), p.getBlockY() - 1 + n, p.getBlockZ());
            
            BlockChangeMessage message = new BlockChangeMessage(b.getX(), b.getY(), b.getZ(), (short) VanillaMaterials.GLASS.getMinecraftId(), 0, player.getNetworkSynchronizer().getRepositionManager());
            player.getSession().send(false, message);
            
            player.teleport(new Point(p.getWorld(), p.getX(), p.getY() + n + 0.1f, p.getZ()));
            source.sendMessage(ChatStyle.CYAN, "Raised your position by " + n + ".");
        }
    }

}
