package net.dirtcraft.plugins.dirtlimits.database.callbacks;

import net.dirtcraft.plugins.dirtlimits.data.Tracker;

import java.util.List;

public interface GetTrackersCallback {
	void onSuccess(List<Tracker> trackers);
}
