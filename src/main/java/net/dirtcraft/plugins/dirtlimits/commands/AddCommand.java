package net.dirtcraft.plugins.dirtlimits.commands;

import net.dirtcraft.plugins.dirtlimits.data.Limit;
import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.database.callbacks.AddLimitCallback;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import net.dirtcraft.plugins.dirtlimits.utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand {

	public static boolean run(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.ADD_LIMIT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;
		if (!player.getInventory().getItemInMainHand().getType().isBlock()) {
			sender.sendMessage(Strings.ONLY_BLOCK);
			return true;
		}

		if (args.length < 3 || !Utilities.isInteger(args[1])) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + ChatColor.RED + "/dirtlimit add <amount> <display-name>");
			return true;
		}

		if (Integer.parseInt(args[1]) < 1) {
			sender.sendMessage(Strings.NO_NEGATIVE_AMOUNT);
			return true;
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 2; i < args.length; i++) {
			if (i == args.length - 1) {
				builder.append(ChatColor.stripColor(args[i]));
			} else {
				builder.append(ChatColor.stripColor(args[i])).append(" ");
			}
		}

		String displayName = builder.toString();
		int amount = Integer.parseInt(args[1]);
		Material blockToLimit = player.getInventory().getItemInMainHand().getType();

		if (blockToLimit == Material.AIR) {
			sender.sendMessage(Strings.NO_AIR);
			return true;
		}

		if (LimitManager.doesLimitExist(blockToLimit)) {
			sender.sendMessage(Strings.LIMIT_EXISTS.replace("{material}", blockToLimit.toString()));
			return true;
		}

		addLimit(sender, blockToLimit, amount, displayName);

		return true;
	}

	public static void addLimit(CommandSender sender, Material blockToLimit, int amount, String itemName) {
		DatabaseOperation.addLimit(blockToLimit, amount, itemName, new AddLimitCallback() {

			@Override
			public void onSuccess() {
				sender.sendMessage(Strings.LIMIT_ADDED.replace("{amount}", Integer.toString(amount)).replace("{material}", blockToLimit.toString()));
				LimitManager.addLimit(new Limit(blockToLimit, itemName, amount));
			}
		});
	}
}
