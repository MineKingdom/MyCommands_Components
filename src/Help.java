import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minekingdom.MyCommands.annotated.CommandLoadOrder;
import net.minekingdom.MyCommands.annotated.CommandPlatform;
import net.minekingdom.MyCommands.annotated.CommandLoadOrder.Order;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.ChatSection;
import org.spout.api.chat.ChatSection.SplitType;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.engine.EngineStartEvent;
import org.spout.api.event.server.plugin.PluginDisableEvent;
import org.spout.api.event.server.plugin.PluginEnableEvent;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Platform;
import org.spout.api.plugin.Plugin;

@CommandLoadOrder(Order.LAST)
public class Help implements Listener {
    
    private Plugin plugin;
    private List<org.spout.api.command.Command> commands;
    
    public Help(Plugin plugin)
    {
        this.plugin = plugin;
        this.commands = formatCommands(plugin.getEngine().getRootCommand().getChildCommands());
    }
    
    @EventHandler
    public void onEngineStart(EngineStartEvent event)
    {
        this.commands = formatCommands(plugin.getEngine().getRootCommand().getChildCommands());
    }
    
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event)
    {
        this.commands = formatCommands(plugin.getEngine().getRootCommand().getChildCommands());
    }
    
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event)
    {
        this.commands = formatCommands(plugin.getEngine().getRootCommand().getChildCommands());
    }
    
    public List<org.spout.api.command.Command> formatCommands(Set<org.spout.api.command.Command> unsortedCommands)
    {
        List<org.spout.api.command.Command> commands = new ArrayList<org.spout.api.command.Command>();
        
        for ( org.spout.api.command.Command command : unsortedCommands )
        {
            if ( commands.isEmpty() )
                commands.add(command);
            
            for ( int i = 0; i < commands.size(); i++ )
            {
                if ( commands.get(i).getPreferredName().compareTo(command.getPreferredName()) > 0 )
                {
                    commands.add(i, command);
                    break;
                }
                
                if ( commands.get(i).getPreferredName().compareTo(command.getPreferredName()) == 0 )
                    break;
                
                if ( i == commands.size() - 1 )
                    commands.add(command);
            }
        }
        
        return commands;
    }
    
    @Command(aliases = {"help"}, flags = "h", usage = "[topic] [page]", desc = "Prints the documentation")
    @CommandPermissions("mycommands.help")
    @CommandPlatform(Platform.ALL)
    public void help(CommandContext args, CommandSource source) throws CommandException
    {
        if ( args.length() == 0 )
        {
            displayHelp(null, 1, args.hasFlag('h'), source);
        }
        else
        {
            ChatArguments topic = null;
            int page = 1;
            
            try 
            {
                page = Integer.parseInt(args.getString(args.length() - 1));
                if ( page <= 0 )
                    throw new CommandException("'" + page + "' is not a valid page.");
                
                if ( args.length() > 1 )
                    topic = args.getJoinedString(0).subSection(0, args.length() - 1).toChatArguments();
            }
            catch ( NumberFormatException ex )
            {
                topic = args.getJoinedString(0);
            }
            
            displayHelp(topic, page, args.hasFlag('h'), source);
        }
    }
    
    private void displayHelp(ChatArguments args, int page, boolean displayHidden, CommandSource source) throws CommandException
    {
        String output = "{{GOLD}} -------------------- HELP --------------------";
        
        List<org.spout.api.command.Command> commands;
        if ( args == null )
        {
            commands = this.commands;
        }
        else
        {
            List<ChatSection> topic = args.toSections(SplitType.WORD);
            org.spout.api.command.Command command = plugin.getEngine().getRootCommand().getChild(topic.get(0).getPlainString());
            for ( int i = 1; i < topic.size(); i++ )
            {
                if ( command == null )
                    throw new CommandException("Could not find the specified help index.");
                
                command = command.getChild(topic.get(i).getPlainString());
            }
            
            if ( command == null )
                throw new CommandException("Could not find the specified help index.");
            
            if ( command.getChildCommands().isEmpty() )
            {
                output += "\n" + command.getUsage() + "\n{{YELLOW}}" + command.getHelp();
                source.sendMessage(ChatArguments.fromFormatString(output));
                return;
            }
            
            commands = formatCommands(command.getChildCommands());
        }
        
        List<org.spout.api.command.Command> display = new ArrayList<org.spout.api.command.Command>();
        
        int tab = 0;
        for ( org.spout.api.command.Command cmd : commands )
        {
            String name = cmd.getPreferredName();
            
            if ( name.length() == 0 )
                continue;
            
            if ( !displayHidden && name.startsWith("-") || name.startsWith("+") )
                continue;
            
            if ( name.length() > tab )
                tab = name.length();
            
            display.add(cmd);
        }
        
        int entries = 0;
        for ( org.spout.api.command.Command cmd : display )
        {
            if ( entries >= 15 * page )
                break;
            
            if ( entries < 15 * (page-1) )
            {
                entries++;
                continue;
            }
            
            String name = cmd.getPreferredName();
            output += "\n{{GOLD}}[#" + name + "]";
            
            /*for (int i = tab - name.length(); i > 0; i--)
                output += " ";*/
            
            output += " {{YELLOW}}" + cmd.getUsage();
            entries++;
        }
        
        source.sendMessage(ChatArguments.fromFormatString(output));
    }

}

