package net.dirtcraft.plugins.dirtlimits.commands;

import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BaseCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (!sender.hasPermission(Permissions.BASE)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			List<TextComponent> listings = getListings(sender);
			sender.sendMessage(Strings.BAR_TOP);
			sender.sendMessage("");
			for (TextComponent listing : listings) {
				sender.spigot().sendMessage(listing);
			}
			sender.sendMessage("");
			sender.sendMessage(Strings.BAR_BOTTOM);

			return true;
		}

		String arg = args[0].toLowerCase();
		switch (arg) {
			case "add":
				return AddCommand.run(sender, args);
			case "bypass":
				return BypassCommand.run(sender, args);
			case "list":
				return ListCommand.run(sender, args);
			case "remove":
				return RemoveCommand.run(sender, args);
			case "edit":
				return EditCommand.run(sender, args);
			default:
				for (Player player : sender.getServer().getOnlinePlayers()) {
					if (arg.equalsIgnoreCase(player.getName()) && args[1].equalsIgnoreCase("reset")) {
						return PlayerResetCommand.run(sender, args);
					} else if (arg.equalsIgnoreCase(player.getName()) && args[1].equalsIgnoreCase("set")) {
						return PlayerSetCommand.run(sender, args);
					}
				}
		}

		return true;
	}

	private List<TextComponent> getListings(CommandSender sender) {
		List<TextComponent> listings = new ArrayList<>();

		if (sender.hasPermission(Permissions.ADD_LIMIT)) {
			TextComponent add = new TextComponent(ChatColor.GOLD + "  /dirtlimit " + ChatColor.YELLOW + "add <amount> <display-name>");
			add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Adds a limit to the block you are currently holding in your hand.")));
			add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dirtlimit add <amount> <display-name>"));
			listings.add(add);
		}

		if (sender.hasPermission(Permissions.BYPASS_LIMIT)) {
			TextComponent bypass = new TextComponent(ChatColor.GOLD + "  /dirtlimit " + ChatColor.YELLOW + "bypass");
			bypass.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Toggles whether you can bypass the limit.")));
			bypass.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dirtlimit bypass"));
			listings.add(bypass);
		}

		if (sender.hasPermission(Permissions.EDIT_LIMIT)) {
			TextComponent editLimit = new TextComponent(ChatColor.GOLD + "  /dirtlimit " + ChatColor.YELLOW + "edit <limited-block> <amount> <display-name>");
			editLimit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Edits the amount and display name of a limit.")));
			editLimit.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dirtlimit edit <limited-block> <amount> <display-name>"));
			listings.add(editLimit);
		}

		if (sender.hasPermission(Permissions.LIST_LIMITS) || sender.hasPermission(Permissions.LIST_LIMITS_OTHER)) {
			TextComponent list;
			if (sender.hasPermission(Permissions.LIST_LIMITS) && !sender.hasPermission(Permissions.LIST_LIMITS_OTHER)) {
				list = new TextComponent(ChatColor.GOLD + "  /dirtlimit " + ChatColor.YELLOW + "list [page]");
				list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "List all limited blocks.")));
				list.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dirtlimit list [page]"));
			} else {
				list = new TextComponent(ChatColor.GOLD + "  /dirtlimit " + ChatColor.YELLOW + "list [user] [page]");
				list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "List all limited blocks of a specific user.")));
				list.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dirtlimit list [user] [page]"));
			}

			listings.add(list);
		}

		if (sender.hasPermission(Permissions.EDIT_PLAYER)) {
			TextComponent editPlayer = new TextComponent(ChatColor.GOLD + "  /dirtlimit " + ChatColor.YELLOW + "<user> set <limited-block> <amount>");
			editPlayer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Use this to fix players limits.")));
			editPlayer.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dirtlimit <user> set <limited-block> <amount>"));
			listings.add(editPlayer);
		}

		if (sender.hasPermission(Permissions.RESET_PLAYER)) {
			TextComponent resetPlayer = new TextComponent(ChatColor.GOLD + "  /dirtlimit " + ChatColor.YELLOW + "<user> reset");
			resetPlayer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Use this to reset all limits of a player.")));
			resetPlayer.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dirtlimit <user> reset"));
			listings.add(resetPlayer);
		}

		return listings;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		List<String> arguments = new ArrayList<>();

		if (args.length == 1) {
			if (sender.hasPermission(Permissions.ADD_LIMIT)) {
				arguments.add("add");
			}

			if (sender.hasPermission(Permissions.LIST_LIMITS)) {
				arguments.add("list");
			}

			if (sender.hasPermission(Permissions.BYPASS_LIMIT)) {
				arguments.add("bypass");
			}

			if (sender.hasPermission(Permissions.RESET_PLAYER) || sender.hasPermission(Permissions.EDIT_PLAYER)) {
				for (Player player : sender.getServer().getOnlinePlayers()) {
					arguments.add(player.getName());
				}
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("add") && sender.hasPermission(Permissions.ADD_LIMIT)) {
			arguments.add("<AMOUNT>");
		} else if (args.length >= 3 && args[0].equalsIgnoreCase("add") && sender.hasPermission(Permissions.ADD_LIMIT)) {
			arguments.add("<DISPLAY NAME>");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("list") && sender.hasPermission(Permissions.LIST_LIMITS) && !sender.hasPermission(Permissions.LIST_LIMITS_OTHER)) {
			arguments.add("[PAGE]");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("list") && sender.hasPermission(Permissions.LIST_LIMITS_OTHER)) {
			for (Player player : sender.getServer().getOnlinePlayers()) {
				arguments.add(player.getName());
			}
		} else if (args.length == 3 && args[0].equalsIgnoreCase("list") && sender.hasPermission(Permissions.LIST_LIMITS_OTHER)) {
			arguments.add("[PAGE]");
		} else if (args.length == 2 && (sender.hasPermission(Permissions.EDIT_PLAYER) || sender.hasPermission(Permissions.RESET_PLAYER))) {
			if (sender.hasPermission(Permissions.EDIT_PLAYER)) {
				arguments.add("set");
			}

			if (sender.hasPermission(Permissions.RESET_PLAYER)) {
				arguments.add("reset");
			}
		} else if (args.length == 3 && args[1].equalsIgnoreCase("set") && sender.hasPermission(Permissions.EDIT_PLAYER)) {
			arguments.addAll(LimitManager.getLimitsAsString());
		} else if (args.length == 4 && sender.hasPermission(Permissions.EDIT_PLAYER)) {
			arguments.add("<AMOUNT>");
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.equalsIgnoreCase("<display name>") || argument.equalsIgnoreCase("<amount>") || argument.equalsIgnoreCase("[page]")) {
				tabResults.add(argument);
				continue;
			}

			if (argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
