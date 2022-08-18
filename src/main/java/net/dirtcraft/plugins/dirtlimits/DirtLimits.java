package net.dirtcraft.plugins.dirtlimits;

import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.database.Database;
import net.dirtcraft.plugins.dirtlimits.utils.Utilities;
import org.bukkit.plugin.java.JavaPlugin;

public final class DirtLimits extends JavaPlugin {
	private static DirtLimits plugin;

	public static DirtLimits getPlugin() {
		return plugin;
	}

	@Override
	public void onEnable() {
		plugin = this;
		Utilities.loadConfig();
		Database.initialiseDatabase();
		LimitManager.initialize();
		Utilities.registerCommands();
		Utilities.registerListener();
	}

	@Override
	public void onDisable() {
		Database.closeDatabase();
	}
}
