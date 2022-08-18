package net.dirtcraft.plugins.dirtlimits.commands;

import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import org.bukkit.command.CommandSender;

public class PlayerResetCommand {
	public static boolean run(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.RESET_PLAYER)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 2) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + "/dirtlimit <player> reset");
			return true;
		}

		String playerName = args[0].trim();

		DatabaseOperation.resetPlayersTracker(playerName, () -> {
			sender.sendMessage(Strings.PLAYER_RESET_SUCCESS.replace("{player}", playerName));
			LimitManager.removeTrackersOfPlayer(playerName);
		});

		return true;
	}
}
