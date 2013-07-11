import org.spout.api.chat.ChatSection;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.plugin.Plugin;

public class Teleport {

    private Plugin plugin;

    public Teleport(Plugin plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = { "tp", "teleport" }, flags = "f", min = 1, max = 2, usage = "[Player] <Destination>[+(world:x,y,z)]", 
            desc = "Teleports a player to the specified destination.\n" 
                    + "Without the -f flag, the command will try to find a safe location ( i.e. on solid ground, with proper space ) " 
                    + "to teleport the target to.\n" 
                    + "The command supports coordinate modifiers. This means the destination can be modified by adding other coordinates.\n" 
                    + "For instance, it is possible to be teleported 5 blocks higher than the destination using \"<destination>+(0,5,0)\"; " 
                    + "it is even possible to teleport to the destination's coordinate, but in an other world, using \"<destination>+(world:0,0,0)\".")
    @Permissible("mycommands.teleport")
    public void teleport(CommandSource source, CommandArguments args) throws CommandException {
        if (source instanceof Player) {
            final Player player = (Player) source;

            boolean force = args.hasFlag('f');

            String[] split;
            ChatSection targetArgs;
            String targetString;
            String modifierString = null;

            Player teleported;

            if (args.length() == 1) {
                targetArgs = args.get(0);
                teleported = player;
            } else {
                targetArgs = args.get(1);
                teleported = plugin.getEngine().getPlayer(args.getString(0), false);
            }

            if (teleported == null)
                throw new CommandException("Error: The specified player was not found.");

            split = targetArgs.getPlainString().split("\\+");
            targetString = split[0];
            if (split.length == 2) {
                modifierString = split[1];
            } else if (split.length > 2)
                throw new CommandException("Error: The specified modifier is invalid.");

            Point destination;
            Point modifier;

            String destinationName = null;

            if (targetString.startsWith("(")) {
                destination = getVector(teleported.getWorld(), targetString);
            } else {
                Player destinationPlayer = plugin.getEngine().getPlayer(targetString, false);
                if (destinationPlayer == null)
                    throw new CommandException("Error: The targetted player was not found.");

                destinationName = destinationPlayer.getName();
                destination = destinationPlayer.getScene().getPosition();
            }

            if (modifierString != null) {
                modifier = getVector(destination.getWorld(), modifierString);
                destination = new Point(modifier.getWorld(), destination.getX() + modifier.getX(), destination.getY() + modifier.getY(), destination.getZ() + modifier.getZ());
            }

            if (destination == null)
                throw new CommandException("Error: The destination is invalid.");

            if (!force)
                destination = safeLocation(destination);

            if (destinationName == null)
                destinationName = "(" + destination.getWorld().getName() + ":" + destination.getX() + ", " + destination.getY() + ", " + destination.getZ() + ")";

            teleported.teleport(destination.add(0, 0.1, 0));
            source.sendMessage("Teleported to : ", destinationName);
            return;
        }
    }

    private Point safeLocation(Point p) {
        final World world = p.getWorld();
        float x = p.getX(), y = p.getY(), z = p.getZ();

        int[] lx = { 1, 0, -1, 0 };
        int[] lz = { 0, 1, 0, -1 };

        for (int move = 0; move < 16; move++) {
            for (int i = 0; i < 256; i++) {
                Block b1 = world.getBlock(x, y + i - 1, z), b2 = world.getBlock(x, y + i, z), b3 = world.getBlock(x, y + i + 1, z), b4 = world.getBlock(x, y - i + 1, z), b5 = world.getBlock(x, y - i, z), b6 = world.getBlock(x, y - i - 1, z);

                if (b1.getMaterial().isSolid() && !b2.getMaterial().isSolid() && !b3.getMaterial().isSolid()) {
                    return new Point(world, x, y + i, z);
                }
                if (b6.getMaterial().isSolid() && !b5.getMaterial().isSolid() && !b4.getMaterial().isSolid()) {
                    return new Point(world, x, y - i, z);
                }
            }
            x += Math.floor(move / 2) * lx[move % 4];
            z += Math.floor(move / 2) * lz[move % 4];
        }
        return p;
    }

    private Point getVector(World world, String s) {
        if (s.charAt(0) != '(')
            if (s.charAt(s.length() - 1) != ')')
                return null;

        String[] vector = s.substring(1, s.length() - 1).split(":");
        String[] modifier;

        String worldName = "";

        if (vector.length == 1) {
            modifier = vector[0].split(",");
        } else if (vector.length == 2) {
            worldName = vector[0];
            modifier = vector[1].split(",");
        } else {
            return null;
        }

        if (modifier.length != 3)
            return null;

        Float[] floatCoord = new Float[3];
        for (int i = 0; i < 3; i++) {
            try {
                floatCoord[i] = Float.parseFloat(modifier[i]);
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        if (!worldName.equals("")) {
            World newWorld = plugin.getEngine().getWorld(worldName);
            if (newWorld == null)
                return null;

            world = newWorld;
        }

        return new Point(world, floatCoord[0], floatCoord[1], floatCoord[2]);
    }

}
