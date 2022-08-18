package net.dirtcraft.plugins.dirtlimits.listeners;

import net.dirtcraft.plugins.dirtlimits.data.Limit;
import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.data.Tracker;
import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.utils.Strings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
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

		Tracker tracker = LimitManager.getTrackerOfPlayer(event.getPlayer().getUniqueId(), event.getBlock().getType());
		Limit limit = LimitManager.getLimit(event.getBlock().getType());

		LimitManager.decrementTracker(tracker);
		DatabaseOperation.decrementTracker(tracker);

		int amountLeft = limit.getAmount() - tracker.getAmountPlaced();
		if (amountLeft < 0) {
			amountLeft = 0;
		}

		if (amountLeft > limit.getAmount()) {
			amountLeft = limit.getAmount();
		}

		event.getPlayer().spigot().sendMessage(
				ChatMessageType.ACTION_BAR,
				new TextComponent(
						Strings.ACTION_BAR_AMOUNT.replace("{left}", Integer.toString(amountLeft))
				)
		);
	}
}
