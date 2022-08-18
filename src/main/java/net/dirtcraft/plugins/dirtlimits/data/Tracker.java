package net.dirtcraft.plugins.dirtlimits.data;

import org.bukkit.Material;

import java.util.UUID;

public class Tracker {
	private final UUID playerUuid;
	private final String playerName;
	private final Material limitedBlock;
	private int amountPlaced;

	public Tracker(UUID playerUuid, String playerName, Material limitedBlock, int amountPlaced) {
		this.playerUuid = playerUuid;
		this.playerName = playerName;
		this.limitedBlock = limitedBlock;
		this.amountPlaced = amountPlaced;
	}

	public UUID getPlayerUuid() {
		return playerUuid;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Material getLimitedBlock() {
		return limitedBlock;
	}

	public int getAmountPlaced() {
		return amountPlaced;
	}

	public void setAmountPlaced(int amountPlaced) {
		this.amountPlaced = amountPlaced;
	}
}
