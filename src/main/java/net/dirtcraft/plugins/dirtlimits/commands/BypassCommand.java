package net.dirtcraft.plugins.dirtlimits.commands;

import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BypassCommand {

	public static boolean run(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.BYPASS_LIMIT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (LimitManager.isBypassing(player.getUniqueId())) {
			sender.sendMessage(Strings.BYPASS_NOW_OFF);
			LimitManager.removeBypass(player.getUniqueId());
		} else {
			sender.sendMessage(Strings.BYPASS_NOW_ON);
			LimitManager.addBypass(player.getUniqueId());
		}

		return true;
	}
}
