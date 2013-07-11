import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minekingdom.MyCommands.annotated.NestedCommand;

import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.server.PreCommandEvent;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;

public class Alias implements Listener {

    public Alias(Plugin plugin) {}

    @Command(aliases = { "alias" }, desc = "Manages the server's aliases.")
    @NestedCommand(AliasSubCommands.class)
    public void alias(CommandSource source, CommandArguments args) throws CommandException {}

    public static class AliasSubCommands implements Listener {

        private final Map<String, String> aliases;

        public AliasSubCommands(Plugin plugin) {
            this.aliases = new HashMap<String, String>();
        }

        @Command(aliases = { "create" }, desc = "Creates a new alias.", min = 2)
        @Permissible("mycommands.alias.create")
        public void create(CommandSource source, CommandArguments args) throws CommandException {
            String name = args.getString(0);
            String alias = args.getJoinedString(1);
            aliases.put(name, alias);
            source.sendMessage("Created a the alias \"" + name + "\" -> \"" + alias + "\"");
        }

        @Command(aliases = { "remove", "rm" }, desc = "Removes an alias.", min = 1, max = 1)
        @Permissible("mycommands.alias.remove")
        public void remove(CommandSource source, CommandArguments args) throws CommandException {
            aliases.remove(args.getString(0));
            source.sendMessage("Removed the alias \"" + args.getString(0) + "\"");
        }

        @Command(aliases = { "list", "ls" }, desc = "Removes an alias.", min = 0, max = 0)
        @Permissible("mycommands.alias.list")
        public void list(CommandSource source, CommandArguments args) throws CommandException {
            source.sendMessage("Aliases :");
            for (Map.Entry<String, String> entry : aliases.entrySet()) {
                source.sendMessage(" * " + entry.getKey() + " -> " + entry.getValue());
            }
        }

        @EventHandler(order = Order.LATE)
        public void onCommand(PreCommandEvent event) {
            if (event.isCancelled())
                return;

            String alias = this.aliases.get(event.getCommand());
            if (alias != null) {
                String[] split = alias.split(" ");
                event.setCommand(split[0]);
                List<String> args = new ArrayList<String>();
                for (int i = 1; i < split.length; i++) {
                    args.add(split[i]);
                }
                args.addAll(event.getArguments().get());
                event.setArguments(args.toArray(new String[0]));
            }
        }
    }
}
