package net.swofty.hypixelguilds;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.*;

public class GuildDatabase implements MongoDB {

	public final String id;

	public static MongoClient client;
	public static MongoDatabase database;
	public static MongoCollection<Document> collection;

	public GuildDatabase(String id) {
		this.id = id;
	}

	public GuildDatabase() {
		this.id = String.valueOf(UUID.randomUUID());
	}

	@Override
	public MongoDB connect(String URI) {
		ConnectionString cs = new ConnectionString(URI);
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
		client = MongoClients.create(settings);

		database = client.getDatabase("Swofty");
		collection = database.getCollection("guilds");
		return this;
	}

	public Document getDocument() {
		Document query = new Document("_id", id);
		Document found = collection.find(query).first();
		return found;
	}

	public void set(String key, Object value) {
		insertOrUpdate(key, value, false);
	}

	@Override
	public Object get(String key, Object def) {
		Document doc = collection.find(Filters.eq("_id", id)).first();
		if (doc == null) {
			return def;
		}
		if (doc.get(key) == null) {
			return def;
		}
		return doc.get(key);
	}

	public static Document findByGuildName(String guild) {
		Document doc = collection.find(Filters.eq("name", guild)).first();
		if (doc == null) {
			return null;
		}
		return doc;
	}

	public static Document findByGuildTag(String tag) {
		Document doc = collection.find(Filters.eq("tag", tag)).first();
		if (doc == null) {
			return null;
		}
		return doc;
	}

	@Override
	public String getString(String key, String def) {
		return get(key, def).toString();
	}

	public int getInt(String key, int def) {
		return Integer.parseInt(get(key, def).toString());
	}

	public long getLong(String key, long def) {
		return Long.parseLong(getString(key, def + ""));
	}

	public boolean getBoolean(String key, boolean def) {
		return Boolean.parseBoolean(get(key, def).toString());
	}

	public <T> List<T> getList(String key, Class<T> t) {
		Document query = new Document("_id", id);
		Document found = collection.find(query).first();

		if (found == null) {
			return new ArrayList<>();
		}

		return found.getList(key, t);
	}

	public boolean remove(String id) {
		Document query = new Document("_id", id);
		Document found = collection.find(query).first();

		if (found == null) {
			return false;
		}

		collection.deleteOne(query);
		return true;
	}

	public void insertOrUpdate(String key, Object value, boolean async) {
		if (async) {
			Utilities.runAsync(() -> {
				if (exists()) {
					Document query = new Document("_id", id);
					Document found = collection.find(query).first();

					assert found != null;
					collection.updateOne(found, Updates.set(key, value));
					return;
				}
				Document New = new Document("_id", id);
				New.append(key, value);
				collection.insertOne(New);
			});
		} else {
			if (exists()) {
				Document query = new Document("_id", id);
				Document found = collection.find(query).first();

				assert found != null;
				collection.updateOne(found, Updates.set(key, value));
				return;
			}
			Document New = new Document("_id", id);
			New.append(key, value);
			collection.insertOne(New);
		}
	}

	public boolean exists() {
		Document query = new Document("_id", id);
		Document found = collection.find(query).first();
		return found != null;
	}
}
