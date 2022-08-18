package net.dirtcraft.plugins.dirtlimits.utils;

import com.moandjiezana.toml.Toml;
import net.dirtcraft.plugins.dirtlimits.DirtLimits;
import net.dirtcraft.plugins.dirtlimits.commands.BaseCommand;
import net.dirtcraft.plugins.dirtlimits.config.Config;
import net.dirtcraft.plugins.dirtlimits.listeners.BlockBreakListener;
import net.dirtcraft.plugins.dirtlimits.listeners.BlockPlaceListener;
import net.dirtcraft.plugins.dirtlimits.listeners.PlayerListener;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class Utilities {
	public static Config config;

	public static void loadConfig() {
		if (!DirtLimits.getPlugin().getDataFolder().exists()) {
			DirtLimits.getPlugin().getDataFolder().mkdirs();
		}
		File file = new File(DirtLimits.getPlugin().getDataFolder(), "config.toml");
		if (!file.exists()) {
			try {
				Files.copy(DirtLimits.getPlugin().getResource("config.toml"), file.toPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		config = new Toml(new Toml().read(DirtLimits.getPlugin().getResource("config.toml"))).read(file).to(Config.class);
	}

	public static void registerListener() {
		DirtLimits.getPlugin().getServer().getPluginManager().registerEvents(new BlockBreakListener(), DirtLimits.getPlugin());
		DirtLimits.getPlugin().getServer().getPluginManager().registerEvents(new BlockPlaceListener(), DirtLimits.getPlugin());
		DirtLimits.getPlugin().getServer().getPluginManager().registerEvents(new PlayerListener(), DirtLimits.getPlugin());
	}

	public static void registerCommands() {
		DirtLimits.getPlugin().getCommand("dirtlimit").setExecutor(new BaseCommand());
		DirtLimits.getPlugin().getCommand("dirtlimit").setTabCompleter(new BaseCommand());
	}

	public static void log(Level level, String msg) {
		String consoleMessage;
		if (Level.INFO.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.WHITE + msg;
		} else if (Level.WARNING.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.YELLOW + msg;
		} else if (Level.SEVERE.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.RED + msg;
		} else {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.GRAY + msg;
		}

		if (!config.general.coloredDebug) {
			consoleMessage = ChatColor.stripColor(msg);
		}

		DirtLimits.getPlugin().getServer().getConsoleSender().sendMessage(consoleMessage);
	}

	public static void disablePlugin() {
		DirtLimits.getPlugin().getServer().getPluginManager().disablePlugin(DirtLimits.getPlugin());
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}

		return true;
	}

	public static String format(String message) {
		return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
	}
}
