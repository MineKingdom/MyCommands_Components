import java.util.Date;

import net.minekingdom.MyCommands.annotated.CommandConfiguration;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.api.scheduler.TaskPriority;

@CommandConfiguration(LagConfig.class)
public class Lag {
    
    private Plugin plugin;
    
    public Lag(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"lag"}, desc = "Checks the lag on a server")
    @CommandPermissions("mycommands.lag")
    public void lag(CommandContext args, CommandSource source) throws CommandException
    {
        source.sendMessage(ChatStyle.CYAN, "Please wait " + LagConfig.SAMPLE_TIME.getInt() / 1000f + " seconds while the process is gathering data...\n" +
        		"Do NOT perform heavy operations or change the system time.");
        
        plugin.getEngine().getScheduler().scheduleSyncDelayedTask(plugin, new CheckLagTask(source), LagConfig.SAMPLE_TIME.getLong(), TaskPriority.NORMAL);
    }
    
    public class CheckLagTask implements Runnable {

        CommandSource source;
        long snapshot;
        
        public CheckLagTask(CommandSource source)
        {
            this.source = source;
            snapshot = (new Date()).getTime();
        }
        
        @Override
        public void run()
        {
            double dt = (new Date()).getTime() - snapshot;
            double lag = (LagConfig.SAMPLE_TIME.getDouble() - dt) / 1000d;
            double ratio = 100 - Math.abs((dt - LagConfig.SAMPLE_TIME.getDouble()) / dt)*100;
            
            ChatStyle style;
            if ( ratio > 90 )
                style = ChatStyle.BRIGHT_GREEN;
            else if ( ratio > 70 )
                style = ChatStyle.GOLD;
            else
                style = ChatStyle.RED;
            
            source.sendMessage(ChatStyle.CYAN, "-------------------- Results: --------------------");
            source.sendMessage(ChatStyle.CYAN, "Ratio : ", style, (float) ratio, "%", ChatStyle.CYAN, ", elapsed milliseconds : ", style, dt, " ms.");
            source.sendMessage(ChatStyle.CYAN, "The server is ", Math.abs((float) lag), " seconds ", lag > 0 ? "ahead" : "late");
            source.sendMessage(ChatStyle.CYAN, "------------------------------------------------");
        }
        
    }
}

