import java.io.File;

import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class Lag {

    private Plugin plugin;

    public Lag(Plugin plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = { "lag" }, desc = "Checks the lag on a server")
    @Permissible("mycommands.lag")
    public void lag(CommandSource source, CommandArguments args) throws CommandException {
        source.sendMessage("Please wait " + LagConfig.SAMPLE_TIME.getInt() / 1000f + " seconds while the process is gathering data...\n" + "Do NOT perform heavy operations or change the system time.");

        plugin.getEngine().getScheduler().scheduleSyncDelayedTask(plugin, new CheckLagTask(source), LagConfig.SAMPLE_TIME.getLong(), TaskPriority.NORMAL);
    }

    public static class LagConfig extends ConfigurationHolderConfiguration {
        
        public static final ConfigurationHolder SAMPLE_TIME = new ConfigurationHolder(5000, "lag", "sample-time");

        public LagConfig(File file) {
            super(new YamlConfiguration(file));
        }
    }

    public static class CheckLagTask implements Runnable {

        private CommandSource source;
        private long          snapshot;

        public CheckLagTask(CommandSource source) {
            this.source   = source;
            this.snapshot = System.currentTimeMillis();
        }

        @Override
        public void run() {
            double dt    = System.currentTimeMillis() - snapshot;
            double lag   = (LagConfig.SAMPLE_TIME.getDouble() - dt) / 1000d;
            double ratio = 100 - Math.abs((dt - LagConfig.SAMPLE_TIME.getDouble()) / dt) * 100;

            source.sendMessage("-------------------- Results: --------------------");
            source.sendMessage("Ratio : " + (float) ratio + "%, elapsed milliseconds : " + dt + " ms.");
            source.sendMessage("The server is " + Math.abs((float) lag) + " seconds " + (lag > 0 ? "ahead" : "late"));
            source.sendMessage("------------------------------------------------");
        }

    }
}
