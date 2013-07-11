import org.spout.api.Server;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.command.annotated.Platform;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;
import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.inventory.util.GridInventoryConverter;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.inventory.window.WindowType;

public class VanillaInventory {
    
    private Plugin plugin;
    
    public VanillaInventory(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Command(aliases = {"inv", "inventory"}, min = 1, max = 1, desc = "Opens the inventory of a player")
    @Platform(org.spout.api.Platform.SERVER)
    @Permissible("mycommands.inventory")
    public void inventory(CommandSource source, CommandArguments args) throws CommandException
    {
        if ( !(source instanceof Player) )
            throw new CommandException("You must be ingame to open a player's inventory");
        
        Player player = ((Server) plugin.getEngine()).getPlayer(args.getString(0), false);
        
        if ( player == null )
            throw new CommandException("'" + args.getString(0) + "' is not online.");
        
        PlayerChestWindow w = new PlayerChestWindow((Player) source, player.get(PlayerInventory.class), player.getName());
        w.open();
    }
    
    public class PlayerChestWindow extends Window
    {
        public PlayerChestWindow(Player owner, PlayerInventory inv, String title) 
        {
            super(owner, WindowType.CHEST, title, inv.getMain().size() + inv.getQuickbar().size());
            GridInventoryConverter mainGrid = new GridInventoryConverter(inv.getMain(), 9, null);
            addInventoryConverter(mainGrid);
            addInventoryConverter(new GridInventoryConverter(inv.getQuickbar(), 9, mainGrid.getGrid().getSize(), null));
        }
    }

}
