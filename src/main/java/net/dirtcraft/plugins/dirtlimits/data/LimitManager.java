package net.dirtcraft.plugins.dirtlimits.data;

import net.dirtcraft.plugins.dirtlimits.database.DatabaseOperation;
import net.dirtcraft.plugins.dirtlimits.utils.Utilities;
import org.bukkit.Material;

import java.util.*;
import java.util.logging.Level;

public class LimitManager {
	private static List<Limit> limits;
	private static List<Tracker> trackers;
	private static List<UUID> bypassingPlayers;

	public static void initialize() {
		DatabaseOperation.getLimits((limitsFound) -> {
			if (Utilities.config.general.debug) {
				Utilities.log(Level.WARNING, "Found " + limitsFound.size() + " limits.");
			}
			limits = new ArrayList<>(limitsFound);
			trackers = new ArrayList<>();
			bypassingPlayers = new ArrayList<>();
			limits.sort(Comparator.comparing(Limit::getDisplayName));
		});
	}

	public static void loadTrackersForPlayer(UUID uuid) {
		DatabaseOperation.getTrackersForPlayer(uuid, trackersFound -> {
			if (trackersFound.size() > 0) {
				trackers.addAll(trackersFound);
				if (Utilities.config.general.debug) {
					Utilities.log(Level.WARNING, "Loaded " + trackersFound.size() + " trackers for this player.");
				}
			}
		});
	}

	public static void removeTrackersOfPlayer(UUID uuid) {
		trackers.removeIf(tracker -> tracker.getPlayerUuid().equals(uuid));
	}

	public static void removeTrackersOfPlayer(String name) {
		trackers.removeIf(tracker -> tracker.getPlayerName().equalsIgnoreCase(name));
	}

	public static List<Limit> getLimits() {
		return new ArrayList<>(limits);
	}

	public static void addLimit(Limit limit) {
		limits.add(limit);
		limits.sort(Comparator.comparing(Limit::getDisplayName));
	}

	public static boolean doesLimitExist(Material limitToCheck) {
		for (Limit limit : limits) {
			if (limit.getBlock() == limitToCheck) {
				return true;
			}
		}

		return false;
	}

	public static void removeLimit(Material blockToRemove) {
		for (int i = 0; i < limits.size(); i++) {
			if (limits.get(i).getBlock() == blockToRemove) {
				limits.remove(i);
				break;
			}
		}
	}

	public static Collection<String> getLimitsAsString() {
		Collection<String> limitsAsString = new ArrayList<>();
		for (Limit limit : limits) {
			limitsAsString.add(limit.getBlock().toString());
		}

		return limitsAsString;
	}

	public static void editLimit(Material blockToEdit, int amount, String displayName) {
		for (Limit limit : limits) {
			if (limit.getBlock() == blockToEdit) {
				limit.setAmount(amount);
				limit.setDisplayName(displayName);
				break;
			}
		}
	}

	public static Limit getLimit(Material blockToGet) {
		for (Limit limit : limits) {
			if (limit.getBlock() == blockToGet) {
				return limit;
			}
		}

		return null;
	}

	public static Tracker getTrackerOfPlayer(UUID uuid, Material block) {
		for (Tracker tracker : trackers) {
			if (tracker.getPlayerUuid() == uuid && tracker.getLimitedBlock() == block) {
				return tracker;
			}
		}

		return null;
	}

	public static Tracker getTrackerOfPlayer(String playerName, Material block) {
		for (Tracker tracker : trackers) {
			if (tracker.getPlayerName().equals(playerName) && tracker.getLimitedBlock().equals(block)) {
				return tracker;
			}
		}

		return null;
	}

	public static List<Tracker> getTrackerOfPlayer(String playerName) {
		List<Tracker> trackersOfPlayer = new ArrayList<>();
		for (Tracker tracker : trackers) {
			if (tracker.getPlayerName().equals(playerName)) {
				trackersOfPlayer.add(tracker);
			}
		}
		return trackersOfPlayer;
	}

	public static boolean playerHasTrackers(UUID uuid) {
		for (Tracker tracker : trackers) {
			if (tracker.getPlayerUuid() == uuid) {
				return true;
			}
		}

		return false;
	}

	public static boolean playerHasTrackers(String playerName) {
		for (Tracker tracker : trackers) {
			if (tracker.getPlayerName().equalsIgnoreCase(playerName)) {
				return true;
			}
		}

		return false;
	}

	public static void editTrackerOfPlayer(UUID uuid, Material block, int amount) {
		for (Tracker tracker : trackers) {
			if (tracker.getPlayerUuid() == uuid && tracker.getLimitedBlock() == block) {
				tracker.setAmountPlaced(amount);
				break;
			}
		}
	}

	public static void incrementTracker(Tracker trackerToIncrement, int amount, int max) {
		for (Tracker tracker : trackers) {
			if (tracker.getPlayerUuid() == trackerToIncrement.getPlayerUuid() && tracker.getLimitedBlock() == trackerToIncrement.getLimitedBlock()) {
				if (tracker.getAmountPlaced() + amount > max) {
					tracker.setAmountPlaced(max);
					break;
				}

				tracker.setAmountPlaced(tracker.getAmountPlaced() + amount);
				break;
			}
		}
	}

	public static void decrementTracker(Tracker trackerToDecrement) {
		for (Tracker tracker : trackers) {
			if (tracker.getPlayerUuid() == trackerToDecrement.getPlayerUuid() && tracker.getLimitedBlock() == trackerToDecrement.getLimitedBlock()) {
				if (tracker.getAmountPlaced() == 0) {
					break;
				}

				tracker.setAmountPlaced(tracker.getAmountPlaced() - 1);
				break;
			}
		}
	}

	public static void addBypass(UUID uuid) {
		if (!bypassingPlayers.contains(uuid)) {
			bypassingPlayers.add(uuid);
		}
	}

	public static void removeBypass(UUID uuid) {
		bypassingPlayers.remove(uuid);
	}

	public static boolean isBypassing(UUID uuid) {
		return bypassingPlayers.contains(uuid);
	}
}
