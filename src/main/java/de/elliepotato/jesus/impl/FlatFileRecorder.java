package de.elliepotato.jesus.impl;

import de.elliepotato.jesus.IRecorder;
import de.elliepotato.jesus.LoggerPlugin;
import de.elliepotato.jesus.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class FlatFileRecorder implements IRecorder {

    private final UUID uuid;
    private final File file;

    public FlatFileRecorder(LoggerPlugin plugin, UUID uuid) {
        this.uuid = uuid;

        File logsDir = new File(plugin.getDataFolder(), "logs");
        if (!logsDir.isDirectory())
            logsDir.mkdir();

        this.file = new File(logsDir, uuid + ".txt");
    }

    @Override
    public void logJoin(Location location) {
        writeFile("Joined at " + formatLocation(location));
    }

    @Override
    public void logLeave(Location location) {
        writeFile("Left at " + formatLocation(location));
    }

    @Override
    public void logCommandExecute(Location location, String command) {
        writeFile("Executed '" + command + "' at " + formatLocation(location));
    }

    @Override
    public void logItemSpawn(Location location, IRecorder.CreativeItemResult result, ItemStack itemStack) {
        writeFile(result + " x" + itemStack.getAmount() + " " + itemStack.getType() + " at " + formatLocation(location));
    }

    public void writeFile(String line) {
        try {
            if (!file.exists())
                file.createNewFile();

            String fullLine = "[" + UtilTime.fullDate() + "] " + line + "\n";

            Files.write(file.toPath(), fullLine.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("error writing " + line + " to  " + uuid);
            e.printStackTrace();
        }
    }

    public String formatLocation(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

}
