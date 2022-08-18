package net.dirtcraft.plugins.dirtlimits.listeners;

import net.dirtcraft.plugins.dirtlimits.data.LimitManager;
import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.database.callbacks.AddTrackerCallback;
import net.dirtcraft.plugins.dirtlimits.database.callbacks.PlayerHasTrackerCallback;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		DatabaseOperation.playHasTrackers(event.getPlayer().getUniqueId(), new PlayerHasTrackerCallback() {
			@Override
			public void trackerFound(int amount) {
				if (LimitManager.getLimits().size() != amount) {
					DatabaseOperation.addTrackersForPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName(), new ArrayList<>(LimitManager.getLimits()), trackersAdded -> LimitManager.loadTrackersForPlayer(event.getPlayer().getUniqueId()));
				} else {
					LimitManager.loadTrackersForPlayer(event.getPlayer().getUniqueId());
				}
			}

			@Override
			public void trackerNotFound() {
				DatabaseOperation.addTrackersForPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName(), new ArrayList<>(LimitManager.getLimits()), trackersAdded -> LimitManager.loadTrackersForPlayer(event.getPlayer().getUniqueId()));
			}
		});
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		LimitManager.removeTrackersOfPlayer(event.getPlayer().getUniqueId());
	}
}
