package de.elliepotato.jesus;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface IRecorder {

    void logJoin(Location location);

    void logLeave(Location location);

    void logItemSpawn(Location location, CreativeItemResult result, ItemStack itemStack);

    void logCommandExecute(Location location, String command);

    enum CreativeItemResult {
        PLACED_IN_INV,
        DELETED,
        DROPPED
    }

}
