package net.swofty.hypixelguilds;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.NumberFormat;
import java.util.*;

public class Utilities {
	private static final NumberFormat COMMA_FORMAT = NumberFormat.getInstance();

	public static void runAsync(Runnable runnable) {
		new Thread(runnable).start();
	}

	public static HashMap<?, ?> sortByValue(Map<?, ?> map) {
		List<Map.Entry<?, ?>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, (o1, o2) -> ((Comparable) o2.getValue()).compareTo(o1.getValue()));
		HashMap<Object, Object> result = new LinkedHashMap<>();
		for (Map.Entry<?, ?> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void delay(Runnable runnable, long delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay / 20 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runnable.run();
		}).start();
	}

	public static String commaify(long l) {
		return COMMA_FORMAT.format(l);
	}

	public static String getRankFromPlayer(ProxiedPlayer player) {
		return "§7";
	}
}
