package net.dirtcraft.plugins.dirtlimits.database;

import net.dirtcraft.plugins.dirtlimits.DirtLimits;
import net.dirtcraft.plugins.dirtlimits.data.Limit;
import net.dirtcraft.plugins.dirtlimits.data.Tracker;
import net.dirtcraft.plugins.dirtlimits.database.callbacks.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseOperation {

	public static void addLimit(final Material blockToLimit, final int amount, final String itemName, final AddLimitCallback addLimitCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("INSERT INTO LIMITS VALUES (?, ?, ?)")) {
				statement.setString(1, blockToLimit.toString());
				statement.setString(2, itemName);
				statement.setInt(3, amount);
				statement.execute();

				Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), addLimitCallback::onSuccess);

			} catch (SQLException e) {e.printStackTrace();}
		});
	}

	public static void getLimits(final GetLimitsCallback getLimitsCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement selectLimits = connection.prepareStatement("SELECT * FROM LIMITS")) {
				List<Limit> limits = new ArrayList<>();
				ResultSet limitsResult = selectLimits.executeQuery();

				while (limitsResult.next()) {
					String block = limitsResult.getString("limits_block").trim();
					String displayName = limitsResult.getString("limits_displayName").trim();
					int amount = limitsResult.getInt("limits_amount");
					limits.add(new Limit(Material.valueOf(block), displayName, amount));
				}

				Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), () -> getLimitsCallback.onSuccess(limits));
			} catch (SQLException e) {e.printStackTrace();}
		});
	}

	public static void removeLimit(final Material blockToRemove, final RemoveLimitCallback removeLimitCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("DELETE FROM LIMITS WHERE limits_block = ?")) {
				statement.setString(1, blockToRemove.toString());
				statement.execute();
				Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), removeLimitCallback::onSuccess);
			} catch (SQLException ignored) {}
		});
	}

	public static void editLimit(final Material blockToEdit, final int amount, final String displayName, final EditLimitCallback editLimitCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE LIMITS SET limits_amount = ?, limits_displayName = ? WHERE limits_block = ?")) {
				statement.setInt(1, amount);
				statement.setString(2, displayName);
				statement.setString(3, blockToEdit.toString());
				statement.execute();
				Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), editLimitCallback::onSuccess);
			} catch (SQLException ignored) {}
		});
	}

	public static void playHasTrackers(final UUID uuid, final PlayerHasTrackerCallback playerHasTrackerCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM TRACKER WHERE tracker_playeruuid = ?")) {
				statement.setString(1, uuid.toString());
				ResultSet resultSet = statement.executeQuery();
				int results = 0;

				while (resultSet.next()) {
					results++;
				}

				if (results != 0) {
					int finalResults = results;
					Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), () -> playerHasTrackerCallback.trackerFound(finalResults));
				} else {
					Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), playerHasTrackerCallback::trackerNotFound);
				}
			} catch (SQLException e) {e.printStackTrace();}
		});
	}

	public static void incrementTracker(final Tracker tracker, final int amount, final int max) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE TRACKER SET tracker_placed = LEAST(?, tracker_placed + ?) WHERE tracker_playeruuid = ? AND limits_block = ?")) {
				statement.setInt(1, max);
				statement.setInt(2, amount);
				statement.setString(3, tracker.getPlayerUuid().toString());
				statement.setString(4, tracker.getLimitedBlock().toString());
				statement.execute();
			} catch (SQLException e) {e.printStackTrace();}
		});
	}

	public static void decrementTracker(final Tracker tracker) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE TRACKER SET tracker_placed = GREATEST(0, tracker_placed - 1) WHERE tracker_playeruuid = ? AND limits_block = ?")) {
				statement.setString(1, tracker.getPlayerUuid().toString());
				statement.setString(2, tracker.getLimitedBlock().toString());
				statement.execute();
			} catch (SQLException e) {e.printStackTrace();}
		});
	}

	public static void getTrackersForPlayer(final UUID uuid, final GetTrackersCallback getTrackersCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM TRACKER WHERE tracker_playeruuid = ?")) {
				statement.setString(1, uuid.toString());
				ResultSet trackersResult = statement.executeQuery();
				List<Tracker> trackers = new ArrayList<>();
				while (trackersResult.next()) {
					String block = trackersResult.getString("limits_block").trim();
					String userName = trackersResult.getString("tracker_playerName");
					int placed = trackersResult.getInt("tracker_placed");
					trackers.add(new Tracker(uuid, userName, Material.valueOf(block), placed));
				}
				Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), () -> getTrackersCallback.onSuccess(trackers));
			} catch (SQLException ignored) {}
		});
	}

	public static void resetPlayersTracker(final String playerName, final PlayerCallback playerCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("DELETE FROM TRACKER WHERE tracker_playerName = ?")) {
				statement.setString(1, playerName);
				statement.execute();
				Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), playerCallback::onSuccess);
			} catch (SQLException ignored) {}
		});
	}

	public static void editTrackerForPlayer(final UUID playerUuid, final Material limitedBlock, final int amount, final EditTrackerCallback editTrackerCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE TRACKER SET tracker_placed = ? WHERE tracker_playeruuid = ? AND limits_block = ?")) {
				statement.setInt(1, amount);
				statement.setString(2, playerUuid.toString());
				statement.setString(3, limitedBlock.toString());
				statement.execute();
				Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), editTrackerCallback::onSuccess);
			} catch (SQLException ignored) {}
		});
	}

	public static void addTrackersForPlayer(final UUID uuid, final String playerName, final List<Limit> limits, final AddTrackerCallback addTrackerCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtLimits.getPlugin(), () -> {
			try (Connection connection = Database.getConnection()) {
				int trackersAdded = 0;
				for (Limit limit : limits) {
					try (PreparedStatement insert = connection.prepareStatement("INSERT INTO TRACKER(tracker_playeruuid, tracker_playername, limits_block, tracker_placed) VALUES (?, ?, ?, ?)")) {
						insert.setString(1, uuid.toString());
						insert.setString(2, playerName);
						insert.setString(3, limit.getBlock().toString());
						insert.setInt(4, 0);
						insert.execute();
						trackersAdded++;
					} catch (SQLException ignored) {}
				}

				int finalTrackersAdded = trackersAdded;
				Bukkit.getScheduler().runTask(DirtLimits.getPlugin(), () -> addTrackerCallback.onSuccess(finalTrackersAdded));
			} catch (SQLException ignored) {}
		});
	}
}
