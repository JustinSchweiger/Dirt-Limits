package net.dirtcraft.plugins.dirtlimits.data;

import org.bukkit.Material;

public class Limit {
	private final Material block;
	private String displayName;
	private int amount;

	public Limit(Material block, String displayName, int amount) {
		this.block = block;
		this.displayName = displayName;
		this.amount = amount;
	}


	public Material getBlock() {
		return block;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
