package net.dirtcraft.plugins.dirtlimits.commands;

import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import net.dirtcraft.plugins.dirtlimits.utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class EditCommand {

	public static boolean run(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.EDIT_LIMIT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length < 4) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + ChatColor.RED + "/dirtlimit edit <limited block> <amount> <display name>");
			return true;
		}

		if (!Utilities.isInteger(args[2])) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + ChatColor.RED + "/dirtlimit edit <limited block> <amount> <display name>");
			return true;
		}

		if (Integer.parseInt(args[2]) < 1) {
			sender.sendMessage(Strings.NO_NEGATIVE_AMOUNT);
			return true;
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 3; i < args.length; i++) {
			if (i == args.length - 1) {
				builder.append(ChatColor.stripColor(args[i]));
			} else {
				builder.append(ChatColor.stripColor(args[i])).append(" ");
			}
		}

		String displayName = builder.toString();
		int amount = Integer.parseInt(args[2]);

		if (!LimitManager.doesLimitExist(Material.getMaterial(args[1]))) {
			sender.sendMessage(Strings.LIMIT_DOES_NOT_EXIST.replace("{material}", args[1]));
			return true;
		}

		editLimit(sender, Material.getMaterial(args[1]), amount, displayName);

		return true;
	}

	private static void editLimit(CommandSender sender, Material blockToEdit, int amount, String displayName) {
		DatabaseOperation.editLimit(blockToEdit, amount, displayName, () -> {
			sender.sendMessage(Strings.LIMIT_EDITED.replace("{material}", blockToEdit.toString()).replace("{amount}", amount + "").replace("{display name}", displayName));
			LimitManager.editLimit(blockToEdit, amount, displayName);
		});
	}
}
