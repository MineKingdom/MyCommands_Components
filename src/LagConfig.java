import java.io.File;

import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class LagConfig extends ConfigurationHolderConfiguration {
    
    public static final ConfigurationHolder SAMPLE_TIME = new ConfigurationHolder(5000, "lag", "sample-time");
    
    public LagConfig(File file)
    {
        super(new YamlConfiguration(file));
    }
}