package net.dirtcraft.plugins.dirtlimits.listeners;

import net.dirtcraft.plugins.dirtlimits.data.Limit;
import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.data.Tracker;
import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.utils.Permissions;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!LimitManager.doesLimitExist(event.getBlock().getType()) || !LimitManager.playerHasTrackers(event.getPlayer().getUniqueId())) {
			return;
		}

		if (LimitManager.isBypassing(event.getPlayer().getUniqueId())) {
			event.getPlayer().spigot().sendMessage(
					ChatMessageType.ACTION_BAR,
					new TextComponent(Strings.BYPASS_ACTIVE)
			);
			return;
		}

		boolean isMultiPlace = event instanceof BlockMultiPlaceEvent;
		int amount = isMultiPlace ? ((BlockMultiPlaceEvent) event).getReplacedBlockStates().size() : 1;

		Tracker tracker = LimitManager.getTrackerOfPlayer(event.getPlayer().getUniqueId(), event.getBlock().getType());
		Limit limit = LimitManager.getLimit(event.getBlock().getType());

		if (isMultiPlace) {
			if (tracker.getAmountPlaced() + amount > limit.getAmount() && !event.getPlayer().hasPermission(Permissions.BYPASS_LIMIT)) {
				event.setCancelled(true);
				event.getPlayer().spigot().sendMessage(
						ChatMessageType.ACTION_BAR,
						new TextComponent(Strings.CANT_PLACE_MORE)
				);
				return;
			}
		} else if (tracker.getAmountPlaced() >= limit.getAmount() && !event.getPlayer().hasPermission(Permissions.BYPASS_LIMIT)) {
			event.setCancelled(true);
			event.getPlayer().spigot().sendMessage(
					ChatMessageType.ACTION_BAR,
					new TextComponent(Strings.CANT_PLACE_MORE)
			);
			return;
		}

		if (isMultiPlace) {
			LimitManager.incrementTracker(tracker, amount, limit.getAmount());
			DatabaseOperation.incrementTracker(tracker, amount, limit.getAmount());
		} else {
			LimitManager.incrementTracker(tracker, 1, limit.getAmount());
			DatabaseOperation.incrementTracker(tracker, 1, limit.getAmount());
		}

		event.getPlayer().spigot().sendMessage(
				ChatMessageType.ACTION_BAR,
				new TextComponent(
						Strings.ACTION_BAR_AMOUNT.replace("{left}", Integer.toString(limit.getAmount() - tracker.getAmountPlaced()))
				)
		);
	}
}
