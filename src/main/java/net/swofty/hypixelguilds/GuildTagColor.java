package net.swofty.hypixelguilds;

import lombok.Getter;

import java.util.ArrayList;
import java.util.UUID;

public enum GuildTagColor {
	DARK_BLUE("1", null),
	DARK_RED("4", null),
	GOLD("6", null),
	DARK_PURPLE("5", null),
	LIGHT_PURPLE("d", null),
	WHITE("f", null),
	DARK_GRAY("8", null),
	RED("c", null),
	BLACK("0", null),
	GRAY("7", 0),
	BLUE("9", 15),
	DARK_GREEN("2", 25),
	YELLOW("e", 45),
	;

        /*
    put("DARK_BLUE", "1"); // no
    put("DARK_RED", "4"); // no
    put("GOLD", "6"); // no
    put("DARK_GREEN", "2"); // no
    put("GREEN", "a"); // no
    put("DARK_PURPLE", "5"); // no
    put("LIGHT_PURPLE", "d"); // no
    put("WHITE", "f"); // no
    put("DARK_GRAY", "8"); // no
    put("BLACK", "0"); // no
    put("GRAY", "7"); // available to everyone
    put("AQUA", "b"); // go buy mvp+ lol
    put("DARK_AQUA", "3"); // go buy svp lol
    put("BLUE", "9"); // guild level 15
    put("DARK_GREEN", "2"); // guild level 25
    put("YELLOW", "e"); // guild level 45
    put("RED", "c"); // no
     */

	@Getter
	private final String colorCode;
	@Getter
	private final Object requirement;

	GuildTagColor(String colorCodE, Object rEquirement) {
		this.colorCode = colorCodE;
		this.requirement = rEquirement;
	}

	public static ArrayList<String> getUnlockedColors(UUID player, Guild guild) {
		ArrayList<String> unlocked = new ArrayList<>();
		int level = GuildLevel.getLevelFromXP(guild.getExperience());
		for (GuildTagColor color : GuildTagColor.values()) {
			if (level >= (int) color.getRequirement()) {
				unlocked.add(color.name());
			}
		}
		return unlocked;
	}

	public static ArrayList<String> getColoredUnlockedColors(UUID player, Guild guild) {
		ArrayList<String> unlocked = new ArrayList<>();
		int level = GuildLevel.getLevelFromXP(guild.getExperience());
		for (GuildTagColor color : GuildTagColor.values()) {
			if (color.getRequirement() == null) continue;
			if (level >= (int) color.getRequirement()) {
				unlocked.add("§" + color.getColorCode() + color.name());
			}
		}
		return unlocked;
	}

	public static ArrayList<String> getAllColoredColors() {
		ArrayList<String> unlocked = new ArrayList<>();
		for (GuildTagColor color : GuildTagColor.values()) {
			unlocked.add("§" + color.getColorCode() + color.name());
		}
		return unlocked;
	}

	public static String getColorCode(String name) {
		for (GuildTagColor c : values()) {
			if (c.name().equalsIgnoreCase(name)) {
				return c.getColorCode();
			}
		}
		return null;
	}

	public static GuildTagColor getTagColorByCode(String colorCode) {
		for (GuildTagColor c : values()) {
			if (c.getColorCode().equals(colorCode)) {
				return c;
			}
		}
		return null;
	}
}
