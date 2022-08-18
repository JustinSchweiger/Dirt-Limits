package net.dirtcraft.plugins.dirtlimits.commands;

import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class RemoveCommand {
	public static boolean run(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.REMOVE_LIMIT) || args.length < 2) {
			return true;
		}

		Material blockToRemove = Material.getMaterial(args[1]);

		if (blockToRemove == null || !LimitManager.doesLimitExist(blockToRemove)) {
			return true;
		}

		removeLimit(sender, blockToRemove);
		return true;
	}

	private static void removeLimit(CommandSender sender, Material blockToRemove) {
		DatabaseOperation.removeLimit(blockToRemove, () -> {
			sender.sendMessage(Strings.LIMIT_REMOVED.replace("{material}", blockToRemove.toString()));
			LimitManager.removeLimit(blockToRemove);
		});
	}
}
