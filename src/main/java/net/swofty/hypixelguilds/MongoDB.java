package net.swofty.hypixelguilds;

public interface MongoDB {
	MongoDB connect(String URI);

	Object get(String key, Object def);

	String getString(String key, String def);

	boolean exists();
}
