package net.swofty.hypixelguilds;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GuildListener implements Listener {
	@EventHandler
	public void onServerConnected(PluginMessageEvent messageEvent) {
		String channel = messageEvent.getTag();
		String message = new String(messageEvent.getData());

		if (ProxyServer.getInstance().getPlayer(message) != null) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF( "MySubChannel");

			ProxyServer.getInstance().getPlayer(message).getServer().getInfo().sendData( "hypixelguilds:tag", out.toByteArray());
		}
	}
}
