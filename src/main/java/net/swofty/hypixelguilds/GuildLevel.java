package net.swofty.hypixelguilds;

import lombok.Getter;

public enum GuildLevel {
	ONE(0),
	TWO(1000),
	THREE(2000),
	FOUR(3000),
	FIVE(4000),
	SIX(5000),
	SEVEN(6000),
	EIGHT(7000),
	NINE(8000),
	TEN(9000),
	ELEVEN(10000),
	TWELVE(12000),
	THIRTEEN(14000),
	FOURTEEN(16000),
	FIFTEEN(18000),
	SIXTEEN(20000),
	SEVENTEEN(22000),
	EIGHTEEN(24000),
	NINETEEN(26000),
	TWENTY(28000),
	TWENTY_ONE(30000),
	TWENTY_TWO(32000),
	TWENTY_THREE(34000),
	TWENTY_FOUR(36000),
	TWENTY_FIVE(38000),
	TWENTY_SIX(40000),
	TWENTY_SEVEN(42000),
	TWENTY_EIGHT(44000),
	TWENTY_NINE(46000),
	THIRTY(48000),
	THIRTY_ONE(50000),
	THIRTY_TWO(53000),
	THIRTY_THREE(56000),
	THIRTY_FOUR(59000),
	THIRTY_FIVE(62000),
	THIRTY_SIX(65000),
	THIRTY_SEVEN(68000),
	THIRTY_EIGHT(71000),
	THIRTY_NINE(74000),
	FOURTY(77000),
	FOURTY_ONE(80000),
	FOURTY_TWO(83000),
	FOURTY_THREE(86000),
	FOURTY_FOUR(89000),
	FOURTY_FIVE(92000),
	FOURTY_SIX(95000),
	FOURTY_SEVEN(98000),
	FOURTY_EIGHT(101000),
	FOURTY_NINE(104000),
	FIFTY(107000),
	FIFTY_ONE(110000),
	FIFTY_TWO(114000),
	FIFTY_THREE(117000),
	FIFTY_FOUR(120000),
	FIFTY_FIVE(123000),
	FIFTY_SIX(126000),
	FIFTY_SEVEN(129000),
	FIFTY_EIGHT(132000),
	FIFTY_NINE(135000),
	SIXTY(138000),
	SIXTY_ONE(141000),
	SIXTY_TWO(144000),
	SIXTY_THREE(147000),
	SIXTY_FOUR(150000),
	SIXTY_FIVE(153000),
	SIXTY_SIX(156000),
	SIXTY_SEVEN(159000),
	SIXTY_EIGHT(162000),
	SIXTY_NINE(165000),
	SEVENTY(168000),
	SEVENTY_ONE(171000),
	SEVENTY_TWO(175000),
	SEVENTY_THREE(179000),
	SEVENTY_FOUR(183000),
	SEVENTY_FIVE(187000),
	SEVENTY_SIX(191000),
	SEVENTY_SEVEN(195000),
	SEVENTY_EIGHT(199000),
	SEVENTY_NINE(203000),
	EIGHTY(207000),
	EIGHTY_ONE(211000),
	EIGHTY_TWO(215000),
	EIGHTY_THREE(219000),
	EIGHTY_FOUR(223000),
	EIGHTY_FIVE(227000),
	EIGHTY_SIX(231000),
	EIGHTY_SEVEN(235000),
	EIGHTY_EIGHT(239000),
	EIGHTY_NINE(243000),
	NINETY(247000),
	NINETY_ONE(251000),
	NINETY_TWO(255000),
	NINETY_THREE(260000),
	NINETY_FOUR(265000),
	NINETY_FIVE(270000),
	NINETY_SIX(275000),
	NINETY_SEVEN(280000),
	NINETY_EIGHT(285000),
	NINETY_NINE(290000),
	ONE_HUNDRED(295000),
	;

	int nerfMultiplier = 22;

	@Getter
	public final long experienceNeeded;

	GuildLevel(long experienceNeeded) {
		this.experienceNeeded = experienceNeeded * nerfMultiplier;
	}

	public static int getLevelFromXP(long xp) {
		GuildLevel toReturn = ONE;
		for (GuildLevel level : GuildLevel.values()) {
			if (level.experienceNeeded - xp < 0 && ((level.experienceNeeded - xp) > (toReturn.experienceNeeded - xp))) {
				toReturn = level;
			}
		}
		return toReturn.ordinal() + 1;
	}

	public static GuildLevel getLevelFromLongXP(long xp) {
		GuildLevel toReturn = ONE;
		for (GuildLevel level : GuildLevel.values()) {
			if (level.experienceNeeded - xp < 0 && ((level.experienceNeeded - xp) > (toReturn.experienceNeeded - xp))) {
				toReturn = level;
			}
		}
		return toReturn;
	}

	public static GuildLevel getNextLevel(long xp) {
		GuildLevel toReturn = ONE;
		for (GuildLevel level : GuildLevel.values()) {
			if (level.experienceNeeded - xp < 0 && ((level.experienceNeeded - xp) > (toReturn.experienceNeeded - xp))) {
				toReturn = level;
			}
		}
		for (GuildLevel level : GuildLevel.values()) {
			if (level.ordinal() == toReturn.ordinal() + 1) {
				return level;
			}
		}
		return null;
	}

	public static GuildLevel getFromInteger(int level) {
		for (GuildLevel l : GuildLevel.values()) {
			if (l.ordinal() == level - 1) {
				return l;
			}
		}
		return GuildLevel.ONE;
	}

	public GuildLevel getPastLevel() {
		for (GuildLevel level : GuildLevel.values()) {
			if (level.ordinal() == this.ordinal() - 1) {
				return level;
			}
		}
		return GuildLevel.ONE;
	}
}
