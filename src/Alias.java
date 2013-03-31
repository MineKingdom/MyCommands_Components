import java.util.HashMap;
import java.util.Map;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.command.annotated.NestedCommand;
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
    public void alias(CommandContext args, CommandSource source) throws CommandException {}

    public static class AliasSubCommands implements Listener {

        private final Map<String, String> aliases;

        public AliasSubCommands(Plugin plugin) {
            this.aliases = new HashMap<String, String>();
        }

        @Command(aliases = { "create" }, desc = "Creates a new alias.", min = 2)
        @CommandPermissions("mycommands.alias.create")
        public void create(CommandContext args, CommandSource source) throws CommandException {
            String name = args.getString(0);
            String alias = args.getJoinedString(1).toFormatString();
            aliases.put(name, alias);
            source.sendMessage(ChatStyle.CYAN, "Created a the alias \"" + name + "\" -> \"" + alias + "\"");
        }

        @Command(aliases = { "remove", "rm" }, desc = "Removes an alias.", min = 1, max = 1)
        @CommandPermissions("mycommands.alias.remove")
        public void remove(CommandContext args, CommandSource source) throws CommandException {
            aliases.remove(args.getString(0));
            source.sendMessage(ChatStyle.CYAN, "Removed the alias \"" + args.getString(0) + "\"");
        }

        @Command(aliases = { "list", "ls" }, desc = "Removes an alias.", min = 0, max = 0)
        @CommandPermissions("mycommands.alias.list")
        public void list(CommandContext args, CommandSource source) throws CommandException {
            source.sendMessage(ChatStyle.CYAN, "Aliases :");
            for (Map.Entry<String, String> entry : aliases.entrySet()) {
                source.sendMessage(ChatStyle.CYAN, " * " + entry.getKey() + " -> " + entry.getValue());
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
                ChatArguments args = new ChatArguments();
                for (int i = 1; i < split.length; i++) {
                    args.append(split[i] + " ");
                }
                args.append(event.getArguments());
                event.setArguments(args);
            }
        }
    }
}
