package net.dirtcraft.plugins.dirtlimits.commands;

import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.data.Tracker;
import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import net.dirtcraft.plugins.dirtlimits.utils.Utilities;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class PlayerSetCommand {
	public static boolean run(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.EDIT_PLAYER)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 4) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + "/dirtlimit <user> set <limited-block> <amount>");
			return true;
		}

		if (!Utilities.isInteger(args[3])) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + "/dirtlimit <user> set <limited-block> <amount>");
			return true;
		}

		int amount = Integer.parseInt(args[3]);
		if (amount < 0) {
			sender.sendMessage(Strings.NO_NEGATIVE_AMOUNT);
			return true;
		}

		Material block = Material.getMaterial(args[2]);
		if (block == null) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + "/dirtlimit <user> set <limited-block> <amount>");
			return true;
		}

		String playerName = args[0];

		Tracker tracker = LimitManager.getTrackerOfPlayer(playerName, block);

		DatabaseOperation.editTrackerForPlayer(tracker.getPlayerUuid(), tracker.getLimitedBlock(), amount, () -> {
			LimitManager.editTrackerOfPlayer(tracker.getPlayerUuid(), block, amount);
			sender.sendMessage(Strings.PLAYER_SET_TRACKER.replace("{player}", playerName).replace("{block}", block.name()).replace("{tracker}", Integer.toString(amount)));
		});

		return true;
	}
}
