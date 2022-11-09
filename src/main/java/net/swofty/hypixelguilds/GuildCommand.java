package net.swofty.hypixelguilds;

import com.google.common.base.Joiner;
import com.mongodb.client.model.Filters;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.bson.Document;

import java.util.*;
import java.util.regex.Pattern;

public class GuildCommand  extends Command {
	public GuildCommand() {
		super("guild");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		handle((ProxiedPlayer) sender, args);
	}

	private static Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");

	public static HashMap<ProxiedPlayer, ProxiedPlayer> PENDING_INVITES = new HashMap<>();
	public static HashMap<ProxiedPlayer, Guild> PENDING_REQUESTS = new HashMap<>();
	private static Map<UUID, Long> cooldown = new HashMap<>();

	public static void handle(ProxiedPlayer player, String[] args) {
		if (cooldown.containsKey(player.getUniqueId())) {
			if (System.currentTimeMillis() - cooldown.get(player.getUniqueId()) < 1000) {
				player.sendMessage("§cYou are sending commands too fast!");
				return;
			}
		}
		cooldown.put(player.getUniqueId(), System.currentTimeMillis());

		switch (args.length) {
			case 0:
				player.sendMessage(getDividerAqua());
				player.sendMessage("§aGuild Commands:");
				player.sendMessage("§e/guild info §8- §bShows information about your guild");
				player.sendMessage("§e/guild accept §8- §bAccepts a guild invitation or a player into the guild");
				player.sendMessage("§e/guild join <guild> §8- §bRequests to join a guild");
				player.sendMessage("§e/guild chat <chat message> §8- §bSend a chat message to your guild chat channel");
				player.sendMessage("§e/guild create <name> §8- §bCreates a guild with the specified name");
				player.sendMessage("§e/guild demote <player> §8- §bDemotes the player to the previous rank");
				player.sendMessage("§e/guild promote <player> §8- §bPromotes the player to the next rank");
				player.sendMessage("§e/guild disband §8- §bDisbands the guild");
				player.sendMessage("§e/guild help §8- §bPrints this help message");
				player.sendMessage("§e/guild invite <player> §8- §bInvites the player to your guild");
				player.sendMessage("§e/guild join <guild> §8- §bRequest to join the specified guild");
				player.sendMessage("§e/guild kick <player> <reason> §8- §bKicks the player from your guild");
				player.sendMessage("§e/guild leave §8- §bLeaves your current guild");
				player.sendMessage("§e/guild motd §8- §bModifies the MOTD for the Guild");
				player.sendMessage("§e/guild mute <time> §8- §bMutes a player or the whole guild");
				player.sendMessage("§e/guild online §8- §bShow the current online members of your guild");
				player.sendMessage("§e/guild rename <name> §8- §bRenames the Guild");
				player.sendMessage("§e/guild tag §8- §bSets the guild [TAG]");
				player.sendMessage("§e/guild tagcolor §8- §bSets the guild's [TAG] color");
				player.sendMessage("§e/guild transfer <player> §8- §bTransfers ownership of the guild to another player");
				player.sendMessage("§e/guild unmute §8- §bUnmute a player or the whole guild");
				player.sendMessage("§e/guild description §8- §bSets guild's description");
				player.sendMessage("§e/guild discord §8- §bSets/views guild's discord");
				player.sendMessage(getDividerAqua());
				break;

			case 1:
				switch (args[0]) {
					default:
						player.sendMessage(getDividerAqua());
						player.sendMessage("§aGuild Commands:");
						player.sendMessage("§e/guild info §8- §bShows information about your guild");
						player.sendMessage("§e/guild accept §8- §bAccepts a guild invitation or a player into the guild");
						player.sendMessage("§e/guild join <guild> §8- §bRequests to join a guild");
						player.sendMessage("§e/guild chat <chat message> §8- §bSend a chat message to your guild chat channel");
						player.sendMessage("§e/guild create <name> §8- §bCreates a guild with the specified name");
						player.sendMessage("§e/guild demote <player> §8- §bDemotes the player to the previous rank");
						player.sendMessage("§e/guild disband §8- §bDisbands the guild");
						player.sendMessage("§e/guild help §8- §bPrints this help message");
						player.sendMessage("§e/guild invite <player> §8- §bInvites the player to your guild");
						player.sendMessage("§e/guild join <guild> §8- §bRequest to join the specified guild");
						player.sendMessage("§e/guild kick <player> <reason> §8- §bKicks the player from your guild");
						player.sendMessage("§e/guild leave §8- §bLeaves your current guild");
						player.sendMessage("§e/guild motd §8- §bModifies the MOTD for the Guild");
						player.sendMessage("§e/guild mute <time> §8- §bMutes a player or the whole guild");
						player.sendMessage("§e/guild online §8- §bShow the current online members of your guild");
						player.sendMessage("§e/guild rename <name> §8- §bRenames the Guild");
						player.sendMessage("§e/guild tag §8- §bSets the guild [TAG]");
						player.sendMessage("§e/guild tagcolor §8- §bSets the guild's [TAG] color");
						player.sendMessage("§e/guild transfer <player> §8- §bTransfers ownership of the guild to another player");
						player.sendMessage("§e/guild unmute §8- §bUnmute a player or the whole guild");
						player.sendMessage("§e/guild description §8- §bSets guild's description");
						player.sendMessage("§e/guild discord §8- §bSets/views guild's discord");
						player.sendMessage(getDividerAqua());
						break;

					case "online":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);

							player.sendMessage(getDividerAqua());
							player.sendMessage("§6Guild Name: " + Guild.getGuildFromPlayer(player).getName());
							player.sendMessage("§1 ");

							if (ProxyServer.getInstance().getPlayer(UUID.fromString(guild.getLeader())) != null) {
								player.sendMessage("§a                  -- Guild Master --");
								player.sendMessage((ProxyServer.getInstance().getPlayer(UUID.fromString(guild.getLeader())) == null ? "§c●" : "§a●") + "  ");
							}

							ArrayList<String> guildOfficers = guild.getOfficer();
							for (int i = 0; i < guildOfficers.size(); i++) {
								if (ProxyServer.getInstance().getPlayer(UUID.fromString(guildOfficers.get(i))) == null) {
									guildOfficers.remove(guildOfficers.get(i));
								}
							}
							if (guildOfficers.size() > 0) {
								player.sendMessage("§2 ");
								player.sendMessage("§a                  -- Guild Officers --");
								String toSend = "";
								for (String str : guildOfficers) {
									toSend += (ProxyServer.getInstance().getPlayer(UUID.fromString(str)) == null ? "§c●" : "§a●") + "  ";
								}
								player.sendMessage(toSend);
							}

							ArrayList<String> guildMembers = guild.getMembers();
							for (int x = 0; x < guildMembers.size(); x++) {
								if (ProxyServer.getInstance().getPlayer(UUID.fromString(guildMembers.get(x))) == null) {
									guildMembers.remove(guildMembers.get(x));
								}
							}
							if (guildMembers.size() > 0) {
								player.sendMessage("§3 ");
								player.sendMessage("§a                  -- Guild Members --");
								String toSend = "";
								for (String str : guildMembers) {
									toSend += (ProxyServer.getInstance().getPlayer(UUID.fromString(str)) == null ? "§c●" : "§a●") + "  ";
								}
								player.sendMessage(toSend);
							}

							player.sendMessage("§4 ");
							player.sendMessage("§eTotal Members: §a" + guild.getAllPlayers().size());
							player.sendMessage("§eOnline Members: §a" + guild.getOnlinePlayers().size());
							player.sendMessage("§eOffline Members: §a" + (guild.getAllPlayers().size() - guild.getOnlinePlayers().size()));
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "list":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);

							player.sendMessage(getDividerAqua());
							player.sendMessage("§6Guild Name: " + Guild.getGuildFromPlayer(player).getDisplayName());
							player.sendMessage("§1 ");
							player.sendMessage("§a                  -- Guild Master --");
							player.sendMessage((ProxyServer.getInstance().getPlayer(UUID.fromString(guild.getLeader())) == null ? "§c●" : "§a●") + "  ");

							if (guild.getOfficer().size() > 0) {
								player.sendMessage("§2 ");
								player.sendMessage("§a                  -- Guild Officers --");
								String toSend = "";
								for (String str : guild.getOfficer()) {
									toSend += (ProxyServer.getInstance().getPlayer(UUID.fromString(str)) == null ? "§c●" : "§a●") + "  ";
								}
								player.sendMessage(toSend);
							}

							if (guild.getMembers().size() > 0) {
								player.sendMessage("§3 ");
								player.sendMessage("§a                  -- Guild Members --");
								String toSend = "";
								for (String str : guild.getMembers()) {
									toSend += (ProxyServer.getInstance().getPlayer(UUID.fromString(str)) == null ? "§c●" : "§a●") + "  ";
								}
								player.sendMessage(toSend);
							}

							player.sendMessage("§4 ");
							player.sendMessage("§eTotal Members: §a" + guild.getAllPlayers().size());
							player.sendMessage("§eOnline Members: §a" + guild.getOnlinePlayers().size());
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "info":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);
							Map<String, Long> topPlayers = (Map<String, Long>) Utilities.sortByValue(guild.getExpHistory());
							ArrayList<Document> guilds = GuildDatabase.collection.find(Filters.exists("experience")).into(new ArrayList<>());
							guilds.sort((g1, g2) -> {
								long g1Exp = g1.getLong("experience");
								long g2Exp = g2.getLong("experience");
								return Long.compare(g2Exp, g1Exp);
							});
							int progress = Math.round((guild.getExperience() - GuildLevel.getLevelFromLongXP(guild.getExperience()).experienceNeeded) * 100 / (GuildLevel.getNextLevel(guild.getExperience()).experienceNeeded - GuildLevel.getLevelFromLongXP(guild.getExperience()).experienceNeeded));
							progress = (progress == 100 ? 0 : progress);
							player.sendMessage(getDividerAqua());
							player.sendMessage("                                     §6" + guild.getDisplayName());
							player.sendMessage("");
							player.sendMessage("§bMembers: §e" + guild.getAllPlayers().size());
							player.sendMessage("§bGuild EXP: §e" + Utilities.commaify(guild.getExperience()) + " §b(§e#" +
								  Utilities.commaify(guilds.indexOf(GuildDatabase.findByGuildName(guild.getName())) + 1)
								  + "§b)");
							player.sendMessage("§bGuild Level: §e" + GuildLevel.getLevelFromXP(guild.getExperience())
								  + " §b(§e" +
								  progress
								  + "% §bto Level " + (GuildLevel.getLevelFromXP(guild.getExperience()) + 1) + "§b)");
							player.sendMessage("");
							player.sendMessage("                                 §6Top 3 Players");
							player.sendMessage("");
							player.sendMessage("    §e1. " + topPlayers.keySet().toArray()[0].toString() + " §e§m--§r §2" + Utilities.commaify(topPlayers.get(topPlayers.keySet().toArray()[0].toString())) + " Guild Experience");
							if (topPlayers.size() > 1) {
								player.sendMessage("    §e2. " + topPlayers.keySet().toArray()[1].toString() + " §e§m--§r §2" + Utilities.commaify(topPlayers.get(topPlayers.keySet().toArray()[1].toString())) + " Guild Experience");
								if (topPlayers.size() > 2) {
									player.sendMessage("    §e3. " + topPlayers.keySet().toArray()[2].toString() + " §e§m--§r §2" + Utilities.commaify(topPlayers.get(topPlayers.keySet().toArray()[2].toString())) + " Guild Experience");
								}
							}
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "disband":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
								Guild.getGuildFromPlayer(player).getOnlinePlayers().forEach(p -> {
									p.sendMessage(getDividerAqua());
									p.sendMessage(player.getName() + " §edisbanded the guild.");
									p.sendMessage(getDividerAqua());
								});

								GuildDatabase.collection.deleteOne(Filters.eq("_id", Guild.getGuildFromPlayer(player).getUuid().toString()));
								Guild.GUILD_CACHE.remove(Guild.getGuildFromPlayer(player).getUuid());
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must be the guild leader to do this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "chat":
						if (Guild.inGuild(player)) {
							player.sendMessage("§cInvalid usage! '/guild chat <message>'");
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "demote":
					case "promote":
						if (Guild.inGuild(player)) {
							player.sendMessage("§cInvalid usage! '/guild " + args[0] + " <player>'");
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "kick":
						if (Guild.inGuild(player)) {
							player.sendMessage("§cInvalid usage! '/guild kick <player> <reason>'");
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "rename":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild rename <name>'");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "join":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou are already in a guild!");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild join <guild name/player>'");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "accept":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou are already in a guild!");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild accept <player>'");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "transfer":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild transfer <player>'");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "invite":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild invite <player>'");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "motd":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString()) || Guild.getGuildFromPlayer(player).getOfficer().contains(player.getUniqueId().toString())) {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§aGuild MOTD Commands:");
								player.sendMessage("§e/guild motd add <text> §8- §bAdds a line in the MOTD");
								player.sendMessage("§e/guild motd clear §8- §bClears the MOTD");
								player.sendMessage("§e/guild motd help §8- §bPrints this help message");
								player.sendMessage("§e/guild motd list §8- §bList lines in the MOTD");
								player.sendMessage("§e/guild motd preview §8- §bPreview what the MOTD will look like to players");
								player.sendMessage(getDividerAqua());
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§sGuild: Message Of The Day (Preview)");
								Guild guild = Guild.getGuildFromPlayer(player);
								for (String line : guild.getMotd().split("\n")) {
									player.sendMessage("§b| §f" + line.replace("&", "§"));
								}
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "leave":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§eYou are the Guild Master! You cannot leave until you §b/guild disband §cor §b/guild transfer§c!");
								player.sendMessage(getDividerAqua());
							} else {
								Guild guild = Guild.getGuildFromPlayer(player);
								guild.left(player.getUniqueId().toString());

								player.sendMessage(getDividerAqua());
								player.sendMessage("§eYou left the guild");
								player.sendMessage(getDividerAqua());

								guild.getOnlinePlayers().forEach(onlinePlayer -> {
									onlinePlayer.sendMessage(getDividerAqua());
									onlinePlayer.sendMessage("§e" + player.getName() + " §eleft the guild!");
									onlinePlayer.sendMessage(getDividerAqua());
								});
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "create":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou're already in a guild!");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild create <name>'");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "tag":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild tag <tag>'");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "tagcolor":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild tagcolor <color>'");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "description":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cInvalid usage! '/guild description <description>'");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "discord":
						if (Guild.inGuild(player)) {
							Guild g = Guild.getGuildFromPlayer(player);
							if (g.getLeader().equals(player.getUniqueId().toString())) {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cInvalid usage! List of usages: " +
									  "\n/guild discord view" +
									  "\n/guild discord set [discord]" +
									  "\n/guild discord public [true/false]"
								);
								player.sendMessage(getDividerAqua());
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§eGuild's discord is §b" + g.getDiscord());
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;
				}
				break;

			case 2:
				switch (args[0]) {
					case "tag":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
								if (GuildLevel.getLevelFromXP(Guild.getGuildFromPlayer(player).getExperience()) >= 5) {
									if (args[1].length() > 2) {
										if (args[1].length() < 8) {
											if (args[1].matches("[a-zA-Z0-9_]+")) {
												if (GuildDatabase.findByGuildTag(args[1].toLowerCase()) == null) {
													Guild.getGuildFromPlayer(player).setTag(args[1]);
													player.sendMessage(getDividerAqua());
													player.sendMessage("§eYou set the guild tag to §" + Guild.getGuildFromPlayer(player).getTagColor() + "[" + args[1].toUpperCase() + "]");
													player.sendMessage(getDividerAqua());
												} else {
													player.sendMessage(getDividerAqua());
													player.sendMessage("§cThat guild tag is already taken!");
													player.sendMessage(getDividerAqua());
												}
											} else {
												player.sendMessage(getDividerAqua());
												player.sendMessage("§cGuild tags can only contain letters, numbers, and underscores!");
												player.sendMessage(getDividerAqua());
											}
										} else {
											player.sendMessage(getDividerAqua());
											player.sendMessage("§cInvalid tag! Must be at most 8 characters long!");
											player.sendMessage(getDividerAqua());
										}
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cInvalid tag! Must be at least 4 characters long!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cGuild tags are unlocked at Guild Level 5!");
									player.sendMessage(getDividerAqua());
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must be the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "tagcolor":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
								if (Guild.getGuildFromPlayer(player).getTag().length() > 2) {
									Guild guild = Guild.getGuildFromPlayer(player);
									if (GuildTagColor.getUnlockedColors(player.getUniqueId(), guild).contains(args[1].toUpperCase())) {
										guild.setTagColor(GuildTagColor.getColorCode(args[1].toUpperCase()));
										player.sendMessage(getDividerAqua());
										player.sendMessage("§eYou set your guild tag color to §" + guild.getTagColor() + "[" + guild.getTag().toUpperCase() + "]");
										player.sendMessage(getDividerAqua());
									} else {
										ArrayList<String> coloredUnlockedColors = GuildTagColor.getColoredUnlockedColors(player.getUniqueId(), guild);
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cInvalid color! List of all available guild tag colors: " + Joiner.on("§7, ").join(coloredUnlockedColors.iterator()));
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cYou must have a tag set to use this command!");
									player.sendMessage(getDividerAqua());
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must be the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}

						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "promote":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
								if (ProxyServer.getInstance().getPlayer(args[1]) != null) {
									if (Guild.getGuildFromPlayer(player).getAllPlayers().contains(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString())) {
										Guild guild = Guild.getGuildFromPlayer(player);

										if (guild.getOfficer().contains(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString()) || guild.getLeader().equals(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString())) {
											player.sendMessage(getDividerAqua());
											player.sendMessage("§c" + ProxyServer.getInstance().getPlayer(args[1]).getName() + " §eis already an officer!");
											player.sendMessage(getDividerAqua());
										} else {
											ArrayList<String> member = guild.getMembers();
											member.remove(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString());
											guild.setMembers(member);

											ArrayList<String> officer = guild.getOfficer();
											officer.add(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString());
											guild.setOfficer(officer);

											ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());
											ProxyServer.getInstance().getPlayer(args[1]).sendMessage("§aYou have been promoted to officer!");
											ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());

											player.sendMessage(getDividerAqua());
											player.sendMessage("§aYou have promoted " + ProxyServer.getInstance().getPlayer(args[1]).getName() + " to officer!");
											player.sendMessage(getDividerAqua());
										}
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage(args[1] + "§c is not in your guild!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage("§cCan't find a player by the name of '" + args[1] + "'");
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must be the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "demote":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
								if (ProxyServer.getInstance().getPlayer(args[1]) != null) {
									if (Guild.getGuildFromPlayer(player).getAllPlayers().contains(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString())) {
										Guild guild = Guild.getGuildFromPlayer(player);

										if (guild.getMembers().contains(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString()) || guild.getLeader().equals(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString())) {
											player.sendMessage(getDividerAqua());
											player.sendMessage("§c" + ProxyServer.getInstance().getPlayer(args[1]).getName() + " §eis already a member!");
											player.sendMessage(getDividerAqua());
										} else {
											ArrayList<String> member = guild.getMembers();
											member.add(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString());
											guild.setMembers(member);

											ArrayList<String> officer = guild.getOfficer();
											officer.remove(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString());
											guild.setOfficer(officer);

											ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());
											ProxyServer.getInstance().getPlayer(args[1]).sendMessage("§aYou have been demoted to member!");
											ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());

											player.sendMessage(getDividerAqua());
											player.sendMessage("§aYou have demoted " + ProxyServer.getInstance().getPlayer(args[1]).getName() + " to member!");
											player.sendMessage(getDividerAqua());
										}
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage(args[1] + "§c is not in your guild!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage("§cCan't find a player by the name of '" + args[1] + "'");
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must be the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "transfer":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
								if (ProxyServer.getInstance().getPlayer(args[1]) != null) {
									if (Guild.getGuildFromPlayer(player).getAllPlayers().contains(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString())) {
										Guild guild = Guild.getGuildFromPlayer(player);

										guild.setLeader(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString());
										ArrayList<String> newOfficers = guild.getOfficer();
										newOfficers.add(player.getUniqueId().toString());
										newOfficers.remove(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString());
										guild.setOfficer(newOfficers);

										ArrayList<String> newMembers = guild.getMembers();
										newMembers.remove(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString());
										guild.setMembers(newMembers);

										guild.getOnlinePlayers().forEach(onlinePlayer -> {
											onlinePlayer.sendMessage(getDividerAqua());
											onlinePlayer.sendMessage("§e" + player.getName() + " §etransferred the guild to §e" + ProxyServer.getInstance().getPlayer(args[1]).getName());
											onlinePlayer.sendMessage(getDividerAqua());
										});
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage(args[1] + "§c is not in your guild!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage("§cCan't find a player by the name of '" + args[1] + "'");
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must be the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "kick":
						if (Guild.inGuild(player)) {
							player.sendMessage("§cInvalid usage! '/guild kick <player> <reason>'");
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "chat":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);

							String toAdd = "";
							if (guild.getLeader().equals(player.getUniqueId().toString())) {
								toAdd = "§" + guild.getTagColor() + "[GM]";
							}
							if (guild.getMembers().contains(player.getUniqueId().toString())) {
								toAdd = "§" + guild.getTagColor() + "[M]";
							}
							if (guild.getOfficer().contains(player.getUniqueId().toString())) {
								toAdd = "§" + guild.getTagColor() + "[OF]";
							}

							String finalToAdd = toAdd;
							guild.getOnlinePlayers().forEach(onlinePlayer -> {
								onlinePlayer.sendMessage("§2Guild > " + player.getName() + " " + finalToAdd + "§f: " + Joiner.on(" ").join(Arrays.copyOfRange(args, 1, args.length)));
							});
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "accept":
						if (Guild.inGuild(player)) {
							if (ProxyServer.getInstance().getPlayer(args[1]) != null && PENDING_REQUESTS.containsKey(ProxyServer.getInstance().getPlayer(args[1])) && PENDING_REQUESTS.get(ProxyServer.getInstance().getPlayer(args[1])).equals(Guild.getGuildFromPlayer(player))) {
								ProxiedPlayer requester = ProxyServer.getInstance().getPlayer(args[1]);
								Guild guild = Guild.getGuildFromPlayer(player);

								ArrayList<String> members = guild.getMembers();
								members.add(requester.getUniqueId().toString());
								guild.setMembers(members);

								guild.getOnlinePlayers().forEach(online -> {
									online.sendMessage(getDividerAqua());
									online.sendMessage("§e" + requester.getName() + " §ejoined the guild!");
									online.sendMessage(getDividerAqua());
								});

								PENDING_REQUESTS.remove(ProxyServer.getInstance().getPlayer(args[1]));
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou are already in a guild!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							if (PENDING_INVITES.containsKey(player)) {
								Guild guild = Guild.getGuildFromPlayer(PENDING_INVITES.get(player));

								ArrayList<String> member = guild.getMembers();
								member.add(player.getUniqueId().toString());
								guild.setMembers(member);

								guild.getOnlinePlayers().forEach(online -> {
									online.sendMessage(getDividerAqua());
									online.sendMessage("§e" + player.getName() + " §ejoined the guild!");
									online.sendMessage(getDividerAqua());
								});
								PENDING_INVITES.remove(player);
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou do not have a pending guild invite from this player!");
								player.sendMessage(getDividerAqua());
							}
						}
						break;

					case "invite":
						if (Guild.inGuild(player)) {
							if (ProxyServer.getInstance().getPlayer(args[1]) != null) {
								if (Guild.getGuildFromPlayer(ProxyServer.getInstance().getPlayer(args[1])) == null) {
									if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString()) || Guild.getGuildFromPlayer(player).getOfficer().contains(player.getUniqueId().toString())) {
										if (Guild.getGuildFromPlayer(player).getAllPlayers().contains(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString())) {
											player.sendMessage(getDividerAqua());
											player.sendMessage(ProxyServer.getInstance().getPlayer(args[1]).getName() + " §cis already in your guild!");
											player.sendMessage(getDividerAqua());
										} else {
											if (PENDING_INVITES.containsKey(ProxyServer.getInstance().getPlayer(args[1])) && PENDING_INVITES.get(ProxyServer.getInstance().getPlayer(args[1])).equals(player)) {
												player.sendMessage(getDividerAqua());
												player.sendMessage("§eYou've already invited somebody to your guild! Wait for them to accept");
												player.sendMessage(getDividerAqua());
											} else {
												PENDING_INVITES.put(ProxyServer.getInstance().getPlayer(args[1]), player);

												TextComponent message = new TextComponent("§bClick here §ato accept or type §b/guild accept " + player.getName());
												message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/g accept " + player.getName()));
												message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§e/guild accept " + player.getName())}));

												ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());
												ProxyServer.getInstance().getPlayer(args[1]).sendMessage(Utilities.getRankFromPlayer(player) + player.getName() + " §ahas invited you to join their guild, §6" + Guild.getGuildFromPlayer(player).getName() + "§a!");
												ProxyServer.getInstance().getPlayer(args[1]).sendMessage(message);
												ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());

												player.sendMessage(getDividerAqua());
												player.sendMessage("§eYou invited " + Utilities.getRankFromPlayer(ProxyServer.getInstance().getPlayer(args[1])) + ProxyServer.getInstance().getPlayer(args[1]).getName() + " §eto your guild. They have 5 minutes to accept.");
												player.sendMessage(getDividerAqua());
											}
										}
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cYou do not have permission to invite people to your guild!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage(Utilities.getRankFromPlayer(ProxyServer.getInstance().getPlayer(args[1])) + ProxyServer.getInstance().getPlayer(args[1]).getName() + " §cis already in another guild!");
									player.sendMessage(getDividerAqua());
								}
							} else {
								player.sendMessage("§cCan't find a player by the name of '" + args[1] + "'");
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "join":
						if (!Guild.inGuild(player)) {
							if (Guild.getGuildFromName(args[1]) != null || (ProxyServer.getInstance().getPlayer(args[1]) != null && Guild.inGuild(ProxyServer.getInstance().getPlayer(args[1])))) {
								if (PENDING_REQUESTS.containsKey(player)) {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cYou've already requested to join a guild! Wait for them to accept it or for it to expire.");
									player.sendMessage(getDividerAqua());
								} else {
									Guild g;
									if (Guild.getGuildFromName(args[1]) != null) {
										g = Guild.getGuildFromName(args[1]);
									} else {
										g = Guild.getGuildFromPlayer(ProxyServer.getInstance().getPlayer(args[1]));
									}
									if (!g.isOpen()) {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cThis guild doesn't accept join requests.");
										player.sendMessage(getDividerAqua());
										return;
									}
									PENDING_REQUESTS.put(player, g);

									Utilities.delay(() -> {
										if (PENDING_REQUESTS.containsKey(player) && PENDING_REQUESTS.get(player).equals(g)) {
											PENDING_REQUESTS.remove(player);
											player.sendMessage(getDividerAqua());
											player.sendMessage("§eYour join request to guild §6" + g.getDisplayName() + " §ehas expired.");
											player.sendMessage(getDividerAqua());
										}
									}, 6000);

									TextComponent message = new TextComponent("§bClick here §eto accept or type §b/guild accept " + player.getName());
									message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/g accept " + player.getName()));
									message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§e/guild accept " + player.getName())}));

									g.getOnlinePlayers().forEach((p) -> {
										if (g.getMembers().contains(p.getUniqueId().toString()))
											return;
										p.sendMessage(getDividerAqua());
										p.sendMessage(Utilities.getRankFromPlayer(player) + player.getName() + " §ehas requested to join your guild. This request expires in 5 minutes.");
										p.sendMessage(message);
										p.sendMessage(getDividerAqua());
									});

									player.sendMessage(getDividerAqua());
									player.sendMessage("§eYou requested to join §6" + g.getDisplayName() + "§e. Your request expires in 5 minutes.");
									player.sendMessage(getDividerAqua());
								}
							} else {
								player.sendMessage("§cCan't find a guild/player by the name of '" + args[1] + "'");
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou already are in a guild!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "rename":
						if (Guild.inGuild(player)) {
							if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString())) {
								if (Guild.getGuildFromName(args[1].toLowerCase()) == null) {
									if (args[1].length() >= 3) {
										if (args[1].length() <= 16) {
											if (args[1].matches("[a-zA-Z0-9_]+")) {
												Guild.getGuildFromPlayer(player).setName(args[1]);
												Guild.getGuildFromPlayer(player).getOnlinePlayers().forEach(onlinePlayer -> {
													onlinePlayer.sendMessage(getDividerAqua());
													onlinePlayer.sendMessage(Utilities.getRankFromPlayer(player) + player.getName() + " §arenamed the guild to §6" + args[1] + "§a!");
													onlinePlayer.sendMessage(getDividerAqua());
												});
											} else {
												player.sendMessage(getDividerAqua());
												player.sendMessage("§cGuild name can only contain letters, numbers and underscores!");
												player.sendMessage(getDividerAqua());
											}
										} else {
											player.sendMessage(getDividerAqua());
											player.sendMessage("§cGuild name must be between 3 and 16 characters!");
											player.sendMessage(getDividerAqua());
										}
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cGuild name must be at least 3 characters long!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cThere is already a guild by the name of §6" + args[1] + "§c!");
									player.sendMessage(getDividerAqua());
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must be the Guild Master use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;
					case "motd":
						switch (args[1]) {
							case "add":
								if (Guild.inGuild(player)) {
									if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString()) || Guild.getGuildFromPlayer(player).getOfficer().contains(player.getUniqueId().toString())) {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cInvalid usage! '/guild motd add <text>'");
										player.sendMessage(getDividerAqua());
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cOnly the guild leader and officers can use this command!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cYou must be in a guild to use this command!");
									player.sendMessage(getDividerAqua());
								}
								break;

							case "clear":
								if (Guild.inGuild(player)) {
									if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString()) || Guild.getGuildFromPlayer(player).getOfficer().contains(player.getUniqueId().toString())) {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cCleared Guild MOTD");
										player.sendMessage(getDividerAqua());
										Guild.getGuildFromPlayer(player).setMotd("");
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cOnly the guild leader and officers can use this command!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cYou must be in a guild to use this command!");
									player.sendMessage(getDividerAqua());
								}
								break;

							case "list":
								if (Guild.inGuild(player)) {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§sGuild: Message Of The Day (Preview)");
									Guild guild = Guild.getGuildFromPlayer(player);
									int x = 0;
									for (String line : guild.getMotd().split("\n")) {
										x++;
										player.sendMessage("§b| " + x + ". §f" + line.replace("&", "§"));
									}
									player.sendMessage(getDividerAqua());
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cYou must be in a guild to use this command!");
									player.sendMessage(getDividerAqua());
								}
								break;

							case "preview":
								if (Guild.inGuild(player)) {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§sGuild: Message Of The Day (Preview)");
									Guild guild = Guild.getGuildFromPlayer(player);
									for (String line : guild.getMotd().split("\n")) {
										player.sendMessage("§b| §f" + line.replace("&", "§"));
									}
									player.sendMessage(getDividerAqua());
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cYou must be in a guild to use this command!");
									player.sendMessage(getDividerAqua());
								}
						}
						break;

					case "create":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou're already in a guild!");
							player.sendMessage(getDividerAqua());
						} else {
							if (Guild.getGuildFromName(args[1].toLowerCase()) == null) {
								if (args[1].length() <= 16) {
									if (isAlphaNumeric(args[1])) {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§aYou created the §6" + args[1] + " §aguild!");
										player.sendMessage(getDividerAqua());
										new Guild(UUID.randomUUID(), player.getUniqueId().toString(), args[1]);
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cGuild name must be alphanumeric!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cGuild name must be 16 characters or less!");
									player.sendMessage(getDividerAqua());
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cThere is already a guild by the name of §6" + args[1] + "§c!");
								player.sendMessage(getDividerAqua());
							}
						}
						break;

					case "description":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);
							if (guild.getLeader().equals(player.getUniqueId().toString())) {
								guild.setDescription(Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length)));
								player.sendMessage(getDividerAqua());
								player.sendMessage("§eYou have successfully set guild description!");
								player.sendMessage(getDividerAqua());
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "open":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);
							if (guild.getLeader().equals(player.getUniqueId().toString())) {
								if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cPossible values: true/false");
									player.sendMessage(getDividerAqua());
									break;
								}
								boolean open = args[1].equalsIgnoreCase("true");
								guild.setOpen(open);
								player.sendMessage(getDividerAqua());
								player.sendMessage("§eYou have " + (open ? "added your guild to" : "removed your guild from") + " the Guild Finder!");
								player.sendMessage(getDividerAqua());
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "discord":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);
							if (args[1].equals("view")) {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§eGuild's discord is §b" + guild.getDiscord());
								player.sendMessage(getDividerAqua());
								break;
							} else if (guild.getLeader().equals(player.getUniqueId().toString())) {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cInvalid usage! List of usages: " +
									  "\n/guild discord view - Shows current discord" +
									  "\n/guild discord set [discord] - Sets a new discord" +
									  "\n/guild discord public [true/false] - This determines whether your discord is for guild members only or for everyone (if true - shows it in guild finder)"
								);
								player.sendMessage(getDividerAqua());
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§eGuild's discord is §b" + guild.getDiscord());
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;


				}
				break;

			default:
				switch (args[0]) {
					case "discord":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);
							if (guild.getLeader().equals(player.getUniqueId().toString())) {
								switch (args[1]) {
									case "set":
										if (args[2].contains("/") && (args[2].contains("discord.com") || args[2].contains("discord.gg")) && (args[2].contains("http://") || args[2].contains("https://"))) {
											guild.setDiscord(args[2]);
											player.sendMessage(getDividerAqua());
											player.sendMessage("§eYou have set guild's discord to §b" + guild.getDiscord() + "§e!");
											player.sendMessage(getDividerAqua());
										} else {
											player.sendMessage(getDividerAqua());
											player.sendMessage("§cYour input seems to be incorrect. Make sure it contains 'https://' or 'http://' and the discord invite link.");
											player.sendMessage(getDividerAqua());
										}
										break;
									case "view":
										player.sendMessage(getDividerAqua());
										player.sendMessage("§eGuild's discord is §b" + guild.getDiscord());
										player.sendMessage(getDividerAqua());
										break;
									case "public":
										if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
											player.sendMessage(getDividerAqua());
											player.sendMessage("§cPossible values: true/false");
											player.sendMessage(getDividerAqua());
											break;
										}
										boolean public_ = args[2].equalsIgnoreCase("true");
										if (guild.getDiscord().equals("none")) {
											player.sendMessage(getDividerAqua());
											player.sendMessage("§cYou haven't set guild discord yet. (/guild discord set <discord>)");
											player.sendMessage(getDividerAqua());
											break;
										}
										guild.setPublicDiscord(public_);
										player.sendMessage(getDividerAqua());
										player.sendMessage("§eYou have set guild's discord to be §b" + (public_ ? "public" : "private") + "§e!");
										player.sendMessage(getDividerAqua());
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "tag":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cA tag can only have one word!");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;
					case "tagcolor":
						if (Guild.inGuild(player)) {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cTags can only have one color!");
							player.sendMessage(getDividerAqua());
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;
					case "kick":
						if (args.length < 3) {
							player.sendMessage("§cInvalid usage! '/guild kick <player> <reason>'");
						} else {
							if (Guild.inGuild(player)) {
								if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString()) || Guild.getGuildFromPlayer(player).getOfficer().contains(player.getUniqueId().toString())) {
									if (ProxyServer.getInstance().getPlayer(args[1]) == null) {
										player.sendMessage("§cCan't find a player by the name of '" + args[1] + "'");
									} else {
										if (!Guild.getGuildFromPlayer(player).getAllPlayers().contains(ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString())) {
											player.sendMessage(getDividerAqua());
											player.sendMessage(Utilities.getRankFromPlayer(ProxyServer.getInstance().getPlayer(args[1])) + " §6" + ProxyServer.getInstance().getPlayer(args[1]).getName() + " §cis not in your guild!");
											player.sendMessage(getDividerAqua());
										} else {
											String leader = Guild.getGuildFromPlayer(player).getLeader();
											ArrayList<String> officers = Guild.getGuildFromPlayer(player).getOfficer();
											ArrayList<String> members = Guild.getGuildFromPlayer(player).getMembers();
											String target = ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString();

											if (members.contains(target)) {
												members.remove(target);
												Guild.getGuildFromPlayer(player).setMembers(members);

												ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());
												ProxyServer.getInstance().getPlayer(args[1]).sendMessage("§cYou have been kicked from §6" + Guild.getGuildFromPlayer(player).getName() + "§c!");
												ProxyServer.getInstance().getPlayer(args[1]).sendMessage("§cReason: §f" + Joiner.on(" ").join(Arrays.copyOfRange(args, 2, args.length)));
												ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());

												Guild.getGuildFromPlayer(player).getOnlinePlayers().forEach(online -> {
													online.sendMessage(getDividerAqua());
													online.sendMessage(Utilities.getRankFromPlayer(ProxyServer.getInstance().getPlayer(args[1])) + ProxyServer.getInstance().getPlayer(args[1]).getName() + " §chas been kicked by " + Utilities.getRankFromPlayer(player) + player.getName() + "§c!");
													online.sendMessage("§cReason: §f" + Joiner.on(" ").join(Arrays.copyOfRange(args, 2, args.length)));
													online.sendMessage(getDividerAqua());
												});
												return;
											}

											if (officers.contains(target)) {
												if (player.getUniqueId().toString().equals(leader)) {
													officers.remove(target);
													Guild.getGuildFromPlayer(player).setOfficer(officers);

													ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());
													ProxyServer.getInstance().getPlayer(args[1]).sendMessage("§cYou have been kicked from §6" + Guild.getGuildFromPlayer(player).getName() + "§c!");
													ProxyServer.getInstance().getPlayer(args[1]).sendMessage("§cReason: §f" + Joiner.on(" ").join(Arrays.copyOfRange(args, 2, args.length)));
													ProxyServer.getInstance().getPlayer(args[1]).sendMessage(getDividerAqua());

													Guild.getGuildFromPlayer(player).getOnlinePlayers().forEach(online -> {
														online.sendMessage(getDividerAqua());
														online.sendMessage(Utilities.getRankFromPlayer(ProxyServer.getInstance().getPlayer(args[1])) + ProxyServer.getInstance().getPlayer(args[1]).getName() + " §chas been kicked by " + Utilities.getRankFromPlayer(player) + player.getName() + " §c!");
														online.sendMessage("§cReason: §f" + Joiner.on(" ").join(Arrays.copyOfRange(args, 2, args.length)));
														online.sendMessage(getDividerAqua());
													});
												} else {
													player.sendMessage(getDividerAqua());
													player.sendMessage("§cYour position is not enough to kick this player!");
													player.sendMessage("§cLeader §8> §6Officer §8> §7Member");
													player.sendMessage(getDividerAqua());
												}
												return;
											}

											if (leader.equals(target)) {
												player.sendMessage(getDividerAqua());
												player.sendMessage("§cYour position is not enough to kick this player!");
												player.sendMessage("§cLeader §8> §6Officer §8> §7Member");
												player.sendMessage(getDividerAqua());
											}
										}
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cOnly the guild leader and officers can use this command!");
									player.sendMessage(getDividerAqua());
								}
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must be in a guild to use this command!");
								player.sendMessage(getDividerAqua());
							}
						}
						break;

					case "chat":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);

							String toAdd = "";
							if (guild.getLeader().equals(player.getUniqueId().toString())) {
								toAdd = "§" + guild.getTagColor() + "[GM]";
							}
							if (guild.getMembers().contains(player.getUniqueId().toString())) {
								toAdd = "§" + guild.getTagColor() + "[M]";
							}
							if (guild.getOfficer().contains(player.getUniqueId().toString())) {
								toAdd = "§" + guild.getTagColor() + "[OF]";
							}

							String finalToAdd = toAdd;
							guild.getOnlinePlayers().forEach(onlinePlayer -> {
								onlinePlayer.sendMessage("§2Guild > " + Utilities.getRankFromPlayer(player) + player.getName() + " " + finalToAdd + "§f: " + Joiner.on(" ").join(Arrays.copyOfRange(args, 1, args.length)));
							});
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "description":
						if (Guild.inGuild(player)) {
							Guild guild = Guild.getGuildFromPlayer(player);
							if (guild.getLeader().equals(player.getUniqueId().toString())) {
								guild.setDescription(Joiner.on(' ').join(Arrays.copyOfRange(args, 2, args.length)));
								player.sendMessage(getDividerAqua());
								player.sendMessage("§eYou have successfully set guild description!");
								player.sendMessage(getDividerAqua());
							} else {
								player.sendMessage(getDividerAqua());
								player.sendMessage("§cYou must the Guild Master to use this command!");
								player.sendMessage(getDividerAqua());
							}
						} else {
							player.sendMessage(getDividerAqua());
							player.sendMessage("§cYou must be in a guild to use this command!");
							player.sendMessage(getDividerAqua());
						}
						break;

					case "motd":
						switch (args[1]) {
							case "add":
								if (Guild.inGuild(player)) {
									if (Guild.getGuildFromPlayer(player).getLeader().equals(player.getUniqueId().toString()) || Guild.getGuildFromPlayer(player).getOfficer().contains(player.getUniqueId().toString())) {
										Guild guild = Guild.getGuildFromPlayer(player);
										guild.setMotd(guild.getMotd() + Joiner.on(" ").join(Arrays.copyOfRange(args, 2, args.length)) + "\n");

										player.sendMessage(getDividerAqua());
										player.sendMessage("§eSet line #" + guild.getMotd().split("\n").length + " of the MOTD to §6" + Joiner.on(" ").join(Arrays.copyOfRange(args, 2, args.length)).replace("&", "§"));
										player.sendMessage(getDividerAqua());
									} else {
										player.sendMessage(getDividerAqua());
										player.sendMessage("§cOnly the guild leader and officers can use this command!");
										player.sendMessage(getDividerAqua());
									}
								} else {
									player.sendMessage(getDividerAqua());
									player.sendMessage("§cYou must be in a guild to use this command!");
									player.sendMessage(getDividerAqua());
								}
								break;
						}
						break;

				}
				break;
		}
	}

	public static String getDividerAqua() {
		return "§b§m-----------------------------------------------------";
	}

	public static boolean isAlphaNumeric(String s) {
		return p.matcher(s).find();
	}
}
