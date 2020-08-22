package de.elliepotato.jesus;

import com.google.common.collect.Maps;
import de.elliepotato.jesus.impl.FlatFileRecorder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class LoggerPlugin extends JavaPlugin implements Listener {

    private static final String PERMISSION = "jesus.logme";

    private Map<UUID, IRecorder> watchers;

    @Override
    public void onEnable() {
        watchers = Maps.newHashMap();

        if (!getDataFolder().isDirectory())
            getDataFolder().mkdir();

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Jesus sees all");
    }

    @Override
    public void onDisable() {

        watchers.clear();
        getLogger().info("Jesus sleeps");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission(PERMISSION)) {
            getWatcher(event.getPlayer()).logJoin(event.getPlayer().getLocation());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (event.getPlayer().hasPermission(PERMISSION)) {
            getWatcher(event.getPlayer()).logLeave(event.getPlayer().getLocation());
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission(PERMISSION)) {
            getWatcher(event.getPlayer()).logCommandExecute(event.getPlayer().getLocation(), event.getMessage());
        }
    }

    @EventHandler
    public void onCreative(InventoryCreativeEvent event) {
        if (!event.getWhoClicked().hasPermission(PERMISSION))
            return;

        final int rawSlot = event.getRawSlot();
        final ItemStack cursor = event.getCursor();
        final ItemStack currentItem = event.getCurrentItem();

        IRecorder.CreativeItemResult itemSpawnResult;
        ItemStack itemStack;

        if (rawSlot == -999) {
            itemSpawnResult = IRecorder.CreativeItemResult.DROPPED;
            itemStack = cursor;
        } else if (cursor.getType() == Material.AIR && currentItem.getType() != Material.AIR) {
            itemSpawnResult = IRecorder.CreativeItemResult.DELETED;
            itemStack = currentItem;
        } else {
            itemSpawnResult = IRecorder.CreativeItemResult.PLACED_IN_INV;
            itemStack = cursor;
        }

        getWatcher((Player) event.getWhoClicked()).logItemSpawn(event.getWhoClicked().getLocation(),
                itemSpawnResult,
                itemStack);

    }

    public IRecorder getWatcher(Player player) {
        if (!watchers.containsKey(player.getUniqueId())) {
            watchers.put(player.getUniqueId(), new FlatFileRecorder(this, player.getUniqueId()));
        }

        return watchers.get(player.getUniqueId());
    }

}
