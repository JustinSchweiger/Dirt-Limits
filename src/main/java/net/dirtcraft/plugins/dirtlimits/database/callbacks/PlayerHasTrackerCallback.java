package net.dirtcraft.plugins.dirtlimits.database.callbacks;

public interface PlayerHasTrackerCallback {
	void trackerFound(int amount);
	void trackerNotFound();
}
