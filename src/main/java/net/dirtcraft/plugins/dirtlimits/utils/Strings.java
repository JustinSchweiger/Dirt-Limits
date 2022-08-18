package net.dirtcraft.plugins.dirtlimits.utils;


import org.bukkit.ChatColor;

public class Strings {
	// ---------------------------------------------------------- GENERAL ----------------------------------------------------------
	public static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + "DirtLimits" + ChatColor.GRAY + "] ";
	public static final String INTERNAL_PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + "DirtLimits" + ChatColor.GRAY + "] ";
	public static final String NO_PERMISSION = PREFIX + ChatColor.RED + "You do not have permission to use this command.";
	public static final String NO_CONSOLE = PREFIX + ChatColor.RED + "You must be a player to use this command.";
	public static final String INVALID_ARGUMENTS_USAGE = PREFIX + ChatColor.DARK_RED + "Invalid arguments. \nUsage: ";
	public static final String NO_NEGATIVE_AMOUNT = PREFIX + ChatColor.RED + "Amount must be greater or equal 0.";
	public static final String NO_AIR = PREFIX + ChatColor.RED + "You must hold an item in your hand!";
	public static final String CANT_PLACE_MORE = ChatColor.RED + "You have reached the limit for this block!";
	public static final String ACTION_BAR_AMOUNT = ChatColor.GRAY + "You can place " + ChatColor.DARK_AQUA + "{left}" + ChatColor.GRAY + " more of this block!";
	public static final String BAR_TOP = Utilities.format("&x&f&f&7&a&0&0&m-&x&f&f&7&3&0&0&m-&x&f&f&6&c&0&0&m-&x&f&e&6&4&0&0&m-&x&f&e&5&d&0&0&m-&x&f&e&5&6&0&0&m-&x&f&e&4&f&0&0&m-&x&f&d&4&8&0&0&m-&x&f&d&4&1&0&0&m-&x&f&d&3&9&0&0&m-&x&f&d&3&2&0&0&m-&x&f&c&2&b&0&0&m-&x&f&c&2&4&0&0&m-&x&f&c&1&d&0&0&m-&x&f&c&1&6&0&0&m-&x&f&b&0&e&0&0&m-&x&f&b&0&7&0&0&m-&x&f&b&0&0&0&0&m-" + ChatColor.GRAY + "[ " + ChatColor.RED + "DirtCraft " + ChatColor.GOLD + ChatColor.BOLD + "Limits" + ChatColor.RESET + ChatColor.GRAY + " ]" + "&x&f&b&0&0&0&0&m-&x&f&b&0&7&0&0&m-&x&f&b&0&e&0&0&m-&x&f&c&1&6&0&0&m-&x&f&c&1&d&0&0&m-&x&f&c&2&4&0&0&m-&x&f&c&2&b&0&0&m-&x&f&d&3&2&0&0&m-&x&f&d&3&9&0&0&m-&x&f&d&4&1&0&0&m-&x&f&d&4&8&0&0&m-&x&f&e&4&f&0&0&m-&x&f&e&5&6&0&0&m-&x&f&e&5&d&0&0&m-&x&f&e&6&4&0&0&m-&x&f&f&6&c&0&0&m-&x&f&f&7&3&0&0&m-&x&f&f&7&a&0&0&m-");
	public static final String BAR_BOTTOM = Utilities.format("&x&f&f&7&a&0&0&m-&x&f&f&7&5&0&0&m-&x&f&f&7&0&0&0&m-&x&f&f&6&b&0&0&m-&x&f&e&6&6&0&0&m-&x&f&e&6&2&0&0&m-&x&f&e&5&d&0&0&m-&x&f&e&5&8&0&0&m-&x&f&e&5&3&0&0&m-&x&f&e&4&e&0&0&m-&x&f&d&4&9&0&0&m-&x&f&d&4&4&0&0&m-&x&f&d&3&f&0&0&m-&x&f&d&3&b&0&0&m-&x&f&d&3&6&0&0&m-&x&f&d&3&1&0&0&m-&x&f&c&2&c&0&0&m-&x&f&c&2&7&0&0&m-&x&f&c&2&2&0&0&m-&x&f&c&1&d&0&0&m-&x&f&c&1&8&0&0&m-&x&f&c&1&4&0&0&m-&x&f&b&0&f&0&0&m-&x&f&b&0&a&0&0&m-&x&f&b&0&5&0&0&m-&x&f&b&0&0&0&0&m-&x&f&b&0&0&0&0&m-&x&f&b&0&5&0&0&m-&x&f&b&0&a&0&0&m-&x&f&b&0&f&0&0&m-&x&f&c&1&4&0&0&m-&x&f&c&1&8&0&0&m-&x&f&c&1&d&0&0&m-&x&f&c&2&2&0&0&m-&x&f&c&2&7&0&0&m-&x&f&c&2&c&0&0&m-&x&f&d&3&1&0&0&m-&x&f&d&3&6&0&0&m-&x&f&d&3&b&0&0&m-&x&f&d&3&f&0&0&m-&x&f&d&4&4&0&0&m-&x&f&d&4&9&0&0&m-&x&f&e&4&e&0&0&m-&x&f&e&5&3&0&0&m-&x&f&e&5&8&0&0&m-&x&f&e&5&d&0&0&m-&x&f&e&6&2&0&0&m-&x&f&e&6&6&0&0&m-&x&f&f&6&b&0&0&m-&x&f&f&7&0&0&0&m-&x&f&f&7&5&0&0&m-&x&f&f&7&a&0&0&m-");
	public static final String HALF_BAR_ONE = "&x&f&b&7&9&0&0&m-&x&f&b&7&3&0&0&m-&x&f&b&6&c&0&0&m-&x&f&b&6&6&0&0&m-&x&f&b&6&0&0&0&m-&x&f&b&5&9&0&0&m-&x&f&b&5&3&0&0&m-&x&f&b&4&c&0&0&m-&x&f&b&4&6&0&0&m-&x&f&b&4&0&0&0&m-&x&f&b&3&9&0&0&m-&x&f&b&3&3&0&0&m-&x&f&b&2&d&0&0&m-&x&f&b&2&6&0&0&m-&x&f&b&2&0&0&0&m-&x&f&b&1&9&0&0&m-&x&f&b&1&3&0&0&m-&x&f&b&0&d&0&0&m-&x&f&b&0&6&0&0&m-&x&f&b&0&0&0&0&m-";
	public static final String HALF_BAR_TWO = Utilities.format("&x&f&b&0&0&0&0&m-&x&f&b&0&6&0&0&m-&x&f&b&0&d&0&0&m-&x&f&b&1&3&0&0&m-&x&f&b&1&9&0&0&m-&x&f&b&2&0&0&0&m-&x&f&b&2&6&0&0&m-&x&f&b&2&d&0&0&m-&x&f&b&3&3&0&0&m-&x&f&b&3&9&0&0&m-&x&f&b&4&0&0&0&m-&x&f&b&4&6&0&0&m-&x&f&b&4&c&0&0&m-&x&f&b&5&3&0&0&m-&x&f&b&5&9&0&0&m-&x&f&b&6&0&0&0&m-&x&f&b&6&6&0&0&m-&x&f&b&6&c&0&0&m-&x&f&b&7&3&0&0&m-&x&f&b&7&9&0&0&m-");

	// -------------------------------------------------------- ADD COMMAND --------------------------------------------------------
	public static final String ONLY_BLOCK = PREFIX + ChatColor.RED + "You can only add a limit to blocks that are placeable!";
	public static final String LIMIT_ADDED = PREFIX + ChatColor.GRAY + "Added a limit of " + ChatColor.DARK_AQUA + "{amount}" + ChatColor.GRAY + " to " + ChatColor.GOLD + "{material}" + ChatColor.GRAY + ".";
	public static final String LIMIT_EXISTS = PREFIX + ChatColor.RED + "There is already a limit set for " + ChatColor.GOLD + "{material}" + ChatColor.RED + ".";

	// -------------------------------------------------------- REMOVE COMMAND --------------------------------------------------------
	public static final String LIMIT_REMOVED = PREFIX + ChatColor.GRAY + "Removed limit for " + ChatColor.GOLD + "{material}" + ChatColor.GRAY + ".";

	// -------------------------------------------------------- EDIT COMMAND --------------------------------------------------------
	public static final String LIMIT_DOES_NOT_EXIST = PREFIX + ChatColor.RED + "There is no limit set for " + ChatColor.GOLD + "{material}" + ChatColor.RED + ".";
	public static final String LIMIT_EDITED = PREFIX + ChatColor.GRAY + "Edited limit for " + ChatColor.GOLD + "{material}" + ChatColor.GRAY + ".\nChanged the amount to " + ChatColor.DARK_AQUA + "{amount}" + ChatColor.GRAY + ".\nChanged the display name to " + ChatColor.DARK_AQUA + "{display name}" + ChatColor.GRAY + ".";

	// -------------------------------------------------------- LIST COMMAND --------------------------------------------------------
	public static final String NO_LIMITS = PREFIX + ChatColor.RED + "There are no limits yet!";
	public static final String PAGE_DOES_NOT_EXIST = PREFIX + ChatColor.RED + "This page does not exist!";
	public static final String PLAYER_NOT_ONLINE = PREFIX + ChatColor.RED + "Couldn't find a tracker for this player. Are they online?";

	// -------------------------------------------------------- PLAYER COMMAND --------------------------------------------------------
	public static final String PLAYER_RESET_SUCCESS = PREFIX + ChatColor.GRAY + "Reset all limits for " + ChatColor.GREEN + "{player}" + ChatColor.GRAY + ".";
	public static final String PLAYER_SET_TRACKER = PREFIX + ChatColor.GRAY + "Set " + ChatColor.GREEN + "{player}" + ChatColor.GRAY + "'s tracker for block " + ChatColor.GOLD + "{block}" + ChatColor.GRAY + " to " + ChatColor.DARK_AQUA + "{tracker}" + ChatColor.GRAY + ".";

	// -------------------------------------------------------- BYPASS COMMAND --------------------------------------------------------
	public static final String BYPASS_ACTIVE = ChatColor.GRAY + "You are currently bypassing the limit on this block.";
	public static final String BYPASS_NOW_ON = PREFIX + ChatColor.GRAY + "Bypassing limits is now " + ChatColor.GREEN + "ON" + ChatColor.GRAY + ".";
	public static final String BYPASS_NOW_OFF = PREFIX + ChatColor.GRAY + "Bypassing limits is now " + ChatColor.RED + "OFF" + ChatColor.GRAY + ".";
}
