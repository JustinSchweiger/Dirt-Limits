package net.dirtcraft.plugins.dirtlimits.commands;

import net.dirtcraft.plugins.dirtlimits.data.Limit;
import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.data.Tracker;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import net.dirtcraft.plugins.dirtlimits.utils.Utilities;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand {
	public static boolean run(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.LIST_LIMITS) && !sender.hasPermission(Permissions.LIST_LIMITS_OTHER)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 1) {
			showLimitsList(sender, sender.getName(), 1);
			return true;
		}

		if (args.length == 2 && Utilities.isInteger(args[1])) {
			showLimitsList(sender, sender.getName(), Integer.parseInt(args[1]));
			return true;
		}

		if (!sender.hasPermission(Permissions.LIST_LIMITS_OTHER)) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + ChatColor.RED + "/dirtlimit list [page]");
			return true;
		}

		if (args[1].length() <= 3 || args[1].length() > 16) {
			sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + ChatColor.RED + "/dirtlimit list [player] [page]");
			return true;
		}

		if (args.length == 3) {
			if (!Utilities.isInteger(args[2])) {
				sender.sendMessage(Strings.INVALID_ARGUMENTS_USAGE + ChatColor.RED + "/dirtlimit list [player] [page]");
				return true;
			}

			showLimitsList(sender, args[1], Integer.parseInt(args[2]));
			return true;
		}

		showLimitsList(sender, args[1], 1);

		return true;
	}

	private static void showLimitsList(CommandSender sender, String playerName, int page) {
		if (page < 1) {
			sender.sendMessage(Strings.PAGE_DOES_NOT_EXIST);
			return;
		}

		List<Limit> limits = LimitManager.getLimits();
		List<Tracker> trackers = LimitManager.getTrackerOfPlayer(playerName);
		if (limits.size() == 0) {
			sender.sendMessage(Strings.NO_LIMITS);
			return;
		}

		if (!LimitManager.playerHasTrackers(playerName)) {
			sender.sendMessage(Strings.PLAYER_NOT_ONLINE);
			return;
		}

		boolean senderEqualsPlayer = sender.getName().equals(playerName);
		if (!senderEqualsPlayer && !sender.hasPermission(Permissions.LIST_LIMITS_OTHER)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return;
		}

		int maxPage = (int) Math.ceil((double) trackers.size() / (double) Utilities.config.general.listEntries);
		if (page > maxPage) {
			sender.sendMessage(Strings.PAGE_DOES_NOT_EXIST);
			return;
		}
		
		int start = (page - 1) * Utilities.config.general.listEntries;
		int end = page * Utilities.config.general.listEntries;
		if (end > trackers.size()) {
			end = trackers.size();
		}

		sender.sendMessage(Strings.BAR_TOP);
		if (!senderEqualsPlayer) {
			sender.sendMessage(ChatColor.GREEN + playerName + ChatColor.GRAY + "'s limits:");
		}
		sender.sendMessage("");

		for (int i = start; i < end; i++) {
			BaseComponent[] editPlayer = new ComponentBuilder("")
					.append(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "\u270E" + ChatColor.DARK_GRAY + "]")
					.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/limit " + playerName + " set " + trackers.get(i).getLimitedBlock().toString() + " <AMOUNT>"))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.YELLOW + "Edit the tracker for this player."))).create();

			boolean noMoreLeft = trackers.get(i).getAmountPlaced() == LimitManager.getLimit(trackers.get(i).getLimitedBlock()).getAmount();
			BaseComponent[] tracker;
			if (noMoreLeft) {
				if (senderEqualsPlayer) {
					tracker = new ComponentBuilder("")
							.append(ChatColor.AQUA + "Placed" + ChatColor.GRAY + ": " + ChatColor.RED + String.format("%02d", trackers.get(i).getAmountPlaced()) + ChatColor.GRAY + " / " + ChatColor.RED + String.format("%02d", LimitManager.getLimit(trackers.get(i).getLimitedBlock()).getAmount()))
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "You can't place any more of this block!")))
							.create();
				} else {
					tracker = new ComponentBuilder("")
							.append(ChatColor.AQUA + "Placed" + ChatColor.GRAY + ": " + ChatColor.RED + String.format("%02d", trackers.get(i).getAmountPlaced()) + ChatColor.GRAY + " / " + ChatColor.RED + String.format("%02d", LimitManager.getLimit(trackers.get(i).getLimitedBlock()).getAmount()))
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "They can't place any more of this block!")))
							.create();
				}
			} else {
				if (senderEqualsPlayer) {
					tracker = new ComponentBuilder("")
							.append(ChatColor.AQUA + "Placed" + ChatColor.GRAY + ": " + ChatColor.GOLD + String.format("%02d", trackers.get(i).getAmountPlaced()) + ChatColor.GRAY + " / " + ChatColor.GOLD + String.format("%02d", LimitManager.getLimit(trackers.get(i).getLimitedBlock()).getAmount()))
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "You can place " + ChatColor.DARK_AQUA + (LimitManager.getLimit(trackers.get(i).getLimitedBlock()).getAmount() - trackers.get(i).getAmountPlaced()) + ChatColor.GRAY + " more of this block.")))
							.create();
				} else {
					tracker = new ComponentBuilder("")
							.append(ChatColor.AQUA + "Placed" + ChatColor.GRAY + ": " + ChatColor.GOLD + String.format("%02d", trackers.get(i).getAmountPlaced()) + ChatColor.GRAY + " / " + ChatColor.GOLD + String.format("%02d", LimitManager.getLimit(trackers.get(i).getLimitedBlock()).getAmount()))
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "They can place " + ChatColor.DARK_AQUA + (LimitManager.getLimit(trackers.get(i).getLimitedBlock()).getAmount() - trackers.get(i).getAmountPlaced()) + ChatColor.GRAY + " more of this block.")))
							.create();
				}
			}

			BaseComponent[] removeLimit = new ComponentBuilder("")
					.append(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "\u2715" + ChatColor.DARK_GRAY + "]")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/limit remove " + limits.get(i).getBlock().toString()))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Remove this limit completely."))).create();

			BaseComponent[] editLimit = new ComponentBuilder("")
					.append(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "\u270E" + ChatColor.DARK_GRAY + "]")
					.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/limit edit " + limits.get(i).getBlock().toString() + " " + limits.get(i).getAmount() + " " + limits.get(i).getDisplayName()))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Edit this limit."))).create();

			BaseComponent[] limit = new ComponentBuilder("")
					.append(ChatColor.GOLD + limits.get(i).getDisplayName())
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
							ChatColor.GRAY + "Block" + ChatColor.DARK_GRAY + ": " + ChatColor.RED + limits.get(i).getBlock().toString() + "\n" +
									"\n" +
									ChatColor.GRAY + "Display Name" + ChatColor.DARK_GRAY + ": " + ChatColor.GOLD + limits.get(i).getDisplayName() + "\n" +
									ChatColor.GRAY + "Limited to" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA + limits.get(i).getAmount()
					))).create();

			ComponentBuilder entry = new ComponentBuilder("");

			if (sender.hasPermission(Permissions.REMOVE_LIMIT)) {
				entry.append(removeLimit);
				entry.append(" ").event((HoverEvent) null).event((ClickEvent) null);
			}

			if (sender.hasPermission(Permissions.EDIT_LIMIT)) {
				entry.append(editLimit);
				entry.append(" ").event((HoverEvent) null).event((ClickEvent) null);
			}

			if (sender.hasPermission(Permissions.EDIT_PLAYER)) {
				entry.append(editPlayer);
			}

			entry.append(ChatColor.GRAY + " - ").event((HoverEvent) null).event((ClickEvent) null);
			entry.append(tracker);
			entry.append(ChatColor.GRAY + "  \u27A4  ").event((HoverEvent) null).event((ClickEvent) null);
			entry.append(limit);

			sender.spigot().sendMessage(entry.create());
		}

		TextComponent bottomBar = new TextComponent(TextComponent.fromLegacyText(Utilities.format(Strings.HALF_BAR_ONE)));
		TextComponent pagePrev;
		if (page == 1) {
			pagePrev = new TextComponent(ChatColor.BLACK + " \u00AB ");
			pagePrev.setBold(true);
			pagePrev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "You are already on the first page!")));
		} else {
			pagePrev = new TextComponent(ChatColor.GREEN + " \u00AB ");
			pagePrev.setBold(true);
			pagePrev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Previous page")));
			if (senderEqualsPlayer) {
				pagePrev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dirtlimit list " + (page - 1)));
			} else {
				pagePrev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dirtlimit list " + sender.getName() + " " + (page - 1) ));
			}
		}
		bottomBar.addExtra(pagePrev);
		bottomBar.addExtra(ChatColor.DARK_AQUA + " " + page + ChatColor.GRAY + "  /  " + ChatColor.DARK_AQUA + maxPage + " ");
		TextComponent pageNext;
		if (page == maxPage) {
			pageNext = new TextComponent(ChatColor.BLACK + " \u00BB ");
			pagePrev.setBold(true);
			pageNext.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "You are already on the last page!")));
		} else {
			pageNext = new TextComponent(ChatColor.GREEN + " \u00BB ");
			pagePrev.setBold(true);
			pageNext.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Next page")));
			if (senderEqualsPlayer) {
				pagePrev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dirtlimit list " + (page + 1)));
			} else {
				pagePrev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dirtlimit list " + sender.getName() + " " + (page + 1) ));
			}
		}
		bottomBar.addExtra(pageNext);
		bottomBar.addExtra(new TextComponent(TextComponent.fromLegacyText(Utilities.format(Strings.HALF_BAR_TWO))));
		sender.sendMessage("");
		sender.spigot().sendMessage(bottomBar);
	}
}
