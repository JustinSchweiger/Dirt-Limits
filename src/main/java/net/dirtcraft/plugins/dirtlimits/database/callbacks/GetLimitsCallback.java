package net.dirtcraft.plugins.dirtlimits.database.callbacks;

import net.dirtcraft.plugins.dirtlimits.data.Limit;
import net.dirtcraft.plugins.dirtlimits.data.Tracker;

import java.util.List;

public interface GetLimitsCallback {
	public void onSuccess(List<Limit> limits);
}
