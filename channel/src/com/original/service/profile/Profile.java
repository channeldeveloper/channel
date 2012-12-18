package com.original.service.profile;

import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.query.Query;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.original.service.channel.Account;

@Entity(value = "profile", noClassnameStored = true)
public class Profile{
	
	String firstName;
	String lastName;
	String middleName;
	String avatar;
	
	@Id
	private ObjectId id;

	@Embedded
	Account[] accounts;

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * @return the accounts
	 */
	public Account[] getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(Account[] accounts) {
		this.accounts = accounts;
	}





	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String toString() {
		try {
			Gson gson = new Gson();

			// convert java object to JSON format,
			// and returned as JSON formatted string
			String json = gson.toJson(this);
			return json;
		} catch (Exception exp) {
			return "channel";
		}

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// Mapping
		Morphia morphia = new Morphia();

		morphia.map(Profile.class);
		// DB
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("song");

		// db mapping to object
		Datastore ds = morphia.createDatastore(mongo, "song");
		ds.ensureIndexes();

		// by mongo db
		DBCursor cursor = db.getCollection("profile").find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

		long cc = ds.getCount(Profile.class);
		System.out.println("cc:" + cc);

		// query and list

		Query<Profile> chs = ds.find(Profile.class);
		List<Profile> chslist = chs.asList();

		Iterator<Profile> ite = chs.iterator();
		while (ite.hasNext()) {
			System.out.println(ite.next());
		}

		for (int i = 0; i < chslist.size(); i++) {
			System.out.println(chslist.get(i));
		}
		
		

	}


}
