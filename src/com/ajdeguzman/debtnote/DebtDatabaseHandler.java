//package com.ajdeguzman.debtnote;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DebtDatabaseHandler extends SQLiteOpenHelper  {
//	
//	private static final int DATABASE_VERSION = 1;
//	private static final String DATABASE_NAME = "dbdebt";
//	private static final String TABLE_CONTACTS = "tblcontacts";
//	private static final String CONTACT_ID = "id";
//	private static final String CONTACT_NAME = "name";
//	private static final String CONTACT_PHONE = "phone";
//	private static final String CONTACT_EMAIL = "email";
//	private static final String CONTACT_ADDRESS = "address";
//	private static final String CONTACT_PICTURE= "picture";
//
//	private static final String TABLE_DEBT = "tbldebt";
//	private static final String DEBT_ID = "id";
//	private static final String DEBT_AMOUNT = "debt_amount";
//	private static final String DEBT_DATE_CREATED = "debt_date";
//	private static final String DEBT_DUE_DATE = "debt_due";
//	private static final String DEBT_TYPE = "debt_type";
//	private static final String DEBT_PERSON = "debt_person";
//	private static final String DEBT_DESC = "debt_desc";
//	private static final String DEBT_CURRENCY = "debt_currency";
//	public DebtDatabaseHandler(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//	
//	// Creating Tables
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
//				+ CONTACT_ID + " INTEGER PRIMARY KEY," 
//				+ CONTACT_NAME + " TEXT,"
//				+ CONTACT_PHONE + " TEXT,"
//				+ CONTACT_EMAIL + " TEXT,"
//				+ CONTACT_ADDRESS + " TEXT,"
//				+ CONTACT_PICTURE + " BLOB NOT NULL" + ")";
//		String CREATE_DEBT_TABLE = "CREATE TABLE " + TABLE_DEBT + "("
//				+ DEBT_ID + " INTEGER PRIMARY KEY," 
//				+ DEBT_AMOUNT + " TEXT,"
//				+ DEBT_DATE_CREATED + " TEXT,"
//				+ DEBT_DUE_DATE + " TEXT,"
//				+ DEBT_TYPE + " INTEGER,"
//				+ DEBT_PERSON + " TEXT,"
//				+ DEBT_DESC + " TEXT,"
//				+ DEBT_CURRENCY + " TEXT" + ")";
//		db.execSQL(CREATE_CONTACT_TABLE);
//		db.execSQL(CREATE_DEBT_TABLE);
//	}
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// Drop older table if existed
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBT);
//		// Create tables again
//		onCreate(db);
//	}
//	/**
//	 * All CRUD(Create, Read, Update, Delete) Operations for Contact
//	 */
//
//	// Adding new contact
//	void addContact(ContactClass contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(CONTACT_NAME, contact.getName());
//		values.put(CONTACT_PHONE, contact.getPhone());
//		values.put(CONTACT_EMAIL, contact.getEmail());
//		values.put(CONTACT_ADDRESS, contact.getAddress());
//		values.put(CONTACT_PICTURE, contact.getPicture());
//		// Inserting Row
//		db.insert(TABLE_CONTACTS, null, values);
//		db.close(); // Closing database connection
//	}
//
//	// Getting single contact
//	ContactClass getContact(int id) {
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { CONTACT_ID, 
//							CONTACT_NAME, CONTACT_PHONE,
//							CONTACT_EMAIL, CONTACT_ADDRESS,
//							CONTACT_PICTURE}, CONTACT_ID + "=?",
//						new String[] { String.valueOf(id) }, null, null, null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//		ContactClass contact = new ContactClass(Integer.parseInt(cursor.getString(0)),
//															cursor.getString(1),
//															cursor.getString(2),
//															cursor.getString(3),
//															cursor.getString(4),
//															cursor.getString(5));
//		return contact;
//	}
//	
//	// Getting All contact
//	public List<ContactClass> getAllContact() {
//		List<ContactClass> contactList = new ArrayList<ContactClass>();
//		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		if (cursor.moveToFirst()) {
//			do {
//				ContactClass contact = new ContactClass();
//				contact.setID(Integer.parseInt(cursor.getString(0)));
//				contact.setName(cursor.getString(1));
//				contact.setPhone(cursor.getString(2));
//				contact.setEmail(cursor.getString(3));
//				contact.setAddress(cursor.getString(4));
//				contact.setPicture(cursor.getString(5));
//				// Adding contact to list
//				contactList.add(contact);
//			} while (cursor.moveToNext());
//		}
//		// return contact list
//		return contactList;
//	}
//	// Getting All contact
//	public List<ContactClass> getAllContactWhere(String where, String service) {
//		List<ContactClass> contactList = new ArrayList<ContactClass>();
//		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE network = '" + where + "' AND service = '" + service + "'";
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		// looping through all rows and adding to list
//		if (cursor.moveToFirst()) {
//			do {
//				ContactClass contact = new ContactClass();
//				contact.setID(Integer.parseInt(cursor.getString(0)));
//				contact.setName(cursor.getString(1));
//				contact.setPhone(cursor.getString(2));
//				contact.setEmail(cursor.getString(3));
//				contact.setAddress(cursor.getString(4));
//				contact.setPicture(cursor.getString(5));
//				// Adding contact to list
//				contactList.add(contact);
//			} while (cursor.moveToNext());
//		}
//
//		// return contact list
//		return contactList;
//	}
//	// Updating single contact
////	public int updatePromo(ContactClass contact) {
////		SQLiteDatabase db = this.getWritableDatabase();
////		ContentValues values = new ContentValues();
////		values.put(KEY_PROMO_NAME, contact.getPromoName());
////		values.put(KEY_PROMO_CODE, contact.getPromoCode());
////		values.put(KEY_SEND_TO, contact.getSendTo());
////
////		// updating row
////		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
////				new String[] { String.valueOf(contact.getID()) });
////	}
//
//	// Deleting single contact
//	public void deleteContact(String name) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_CONTACTS, CONTACT_NAME + " = ?",
//				new String[] { String.valueOf(name) });
//		db.close();
//	}
//
//	// Deleting single contact
////	public void deleteContact(String code) {
////		SQLiteDatabase db = this.getWritableDatabase();
////		db.delete(TABLE_PROMO, KEY_PROMO_CODE + " = ?",
////				new String[] { code });
////		db.close();
////	}
//	
////	public ContactClass selectSendToWhere(String code) {
////		String query = "Select * FROM " + TABLE_PROMO + " WHERE " + KEY_PROMO_CODE + " =  \"" + code + "\"";
////		SQLiteDatabase db = this.getWritableDatabase();
////		Cursor cursor = db.rawQuery(query, null);
////		ContactClass promo = new ContactClass();
////		if (cursor.moveToFirst()) {
////			cursor.moveToFirst();
////			promo.setSendTo(cursor.getString(3));
////			promo.getSendTo();
////			cursor.close();
////		} else {
////			promo = null;
////		}
////	        db.close();
////		return promo;
////	}
//
//	public int getContactCount() {
//		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countQuery, null);
//		return cursor.getCount();
//	}
//	
//	
//	/**
//	 * All CRUD(Create, Read, Update, Delete) Operations for Debt
//	 */
//	// Adding new debt
//		void addDebt(DebtClass debt) {
//			SQLiteDatabase db = this.getWritableDatabase();
//			ContentValues values = new ContentValues();
//			values.put(DEBT_AMOUNT, debt.getDebtAmount());
//			values.put(DEBT_DATE_CREATED, debt.getDebtDate());
//			values.put(DEBT_DUE_DATE, debt.getDebtDue());
//			values.put(DEBT_TYPE, debt.getDebtType());
//			values.put(DEBT_PERSON, debt.getDebtPerson());
//			values.put(DEBT_DESC, debt.getDebDesc());
//			values.put(DEBT_CURRENCY, debt.getDebtCurrency());
//			
//			db.insert(TABLE_DEBT, null, values); // Inserting Row
//			db.close(); // Closing database connection
//		}
//
//		// Getting single contact
//		DebtClass getDebt(int id) {
//			SQLiteDatabase db = this.getReadableDatabase();
//			Cursor cursor = db.query(TABLE_DEBT, new String[] { DEBT_ID, 
//					DEBT_AMOUNT, DEBT_DATE_CREATED,
//					DEBT_DUE_DATE, DEBT_TYPE}, DEBT_ID + "=?",
//							new String[] { String.valueOf(id) }, null, null, null, null);
//			if (cursor != null)
//				cursor.moveToFirst();
//			DebtClass debt = new DebtClass(Integer.parseInt(cursor.getString(0)),
//																cursor.getString(1),
//																cursor.getString(2),
//																cursor.getString(3),
//																cursor.getInt(4),
//																cursor.getString(5),
//																cursor.getString(6),
//																cursor.getString(7));
//			return debt;
//		}
//		
//		// Getting All debt
//		public List<DebtClass> getAllDebt() {
//			List<DebtClass> debtList = new ArrayList<DebtClass>();
//			// Select All Query
//			String selectQuery = "SELECT  * FROM " + TABLE_DEBT;
//			SQLiteDatabase db = this.getWritableDatabase();
//			Cursor cursor = db.rawQuery(selectQuery, null);
//
//			// looping through all rows and adding to list
//			if (cursor.moveToFirst()) {
//				do {
//					DebtClass debt = new DebtClass();
//					debt.setID(Integer.parseInt(cursor.getString(0)));
//					debt.setDebtAmount(cursor.getString(1));
//					debt.setDebtDate(cursor.getString(2));
//					debt.setDateDue(cursor.getString(3));
//					debt.setDebtType(cursor.getInt(4));
//					debt.setDebtPerson(cursor.getString(5));
//					debt.setDebtDesc(cursor.getString(6));
//					debt.setDebtCurrency(cursor.getString(7));
//					// Adding contact to list
//					debtList.add(debt);
//				} while (cursor.moveToNext());
//			}
//
//			// return contact list
//			return debtList;
//		}
//		// Getting All contact
//		public List<DebtClass> getAllDebtWhere(int type) {
//			List<DebtClass> debtList = new ArrayList<DebtClass>();
//			// Select All Query
//			String selectQuery = "SELECT  * FROM " + TABLE_DEBT + " WHERE debt_type = " + type;
//			SQLiteDatabase db = this.getWritableDatabase();
//			Cursor cursor = db.rawQuery(selectQuery, null);
//
//			// looping through all rows and adding to list
//			if (cursor.moveToFirst()) {
//				do {
//					DebtClass debt = new DebtClass();
//					debt.setID(Integer.parseInt(cursor.getString(0)));
//					debt.setDebtAmount(cursor.getString(1));
//					debt.setDebtDate(cursor.getString(2));
//					debt.setDateDue(cursor.getString(3));
//					debt.setDebtType(cursor.getInt(4));
//					debt.setDebtPerson(cursor.getString(5));
//					debt.setDebtDesc(cursor.getString(6));
//					debt.setDebtCurrency(cursor.getString(7));
//					// Adding contact to list
//					debtList.add(debt);
//				} while (cursor.moveToNext());
//			}
//
//			// return debt list
//			return debtList;
//		}
//
//
//		// Deleting single contact
//		public void deleteDebt(DebtClass debt) {
//			SQLiteDatabase db = this.getWritableDatabase();
//			db.delete(TABLE_DEBT, DEBT_ID + " = ?",
//					new String[] { String.valueOf(debt.getID()) });
//			db.close();
//		}
//		
//		public int getDebtCount() {
//			String countQuery = "SELECT  * FROM " + TABLE_DEBT;
//			SQLiteDatabase db = this.getReadableDatabase();
//			Cursor cursor = db.rawQuery(countQuery, null);
//			return cursor.getCount();
//		}
//
//	/**
//	 * All CRUD(Create, Read, Update, Delete) Operations for History
//	 */
//	/*public void deleteHistoryWhere(String code) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_HISTORY, HIST_TYPE + " = ?",
//				new String[] { code });
//		db.close();
//	}
//	
//		public List<HistoryClass> getAllHistoryWhere(String where) {
//			List<HistoryClass> historyList = new ArrayList<HistoryClass>();
//			
//			String selectQuery = "SELECT  * FROM " + TABLE_HISTORY + " WHERE type = '" + where + "'";
//			SQLiteDatabase db = this.getWritableDatabase();
//			Cursor cursor = db.rawQuery(selectQuery, null);
//
//			
//			if (cursor.moveToFirst()) {
//				do {
//					HistoryClass history = new HistoryClass();
//					history.setID(Integer.parseInt(cursor.getString(0)));
//					history.setType(cursor.getString(1));
//					history.setRecipient(cursor.getString(2));
//					history.setMessage(cursor.getString(3));
//					history.setDate(cursor.getString(4));
//
//					historyList.add(history);
//				} while (cursor.moveToNext());
//			}
//			return historyList;
//		}
//		
//		void addHistory(HistoryClass history) {
//			SQLiteDatabase db = this.getWritableDatabase();
//			ContentValues values = new ContentValues();
//			values.put(HIST_TYPE, history.getType());
//			values.put(HIST_RECIPIENT, history.getRecipient());
//			values.put(HIST_MESSAGE, history.getMessage());
//			values.put(HIST_DATE, history.getDate());
//			db.insert(TABLE_HISTORY, null, values);
//			db.close();
//		}*/
//}


package com.ajdeguzman.debtnote;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DebtDatabaseHandler extends SQLiteOpenHelper  {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "dbdebt";
	private static final String TABLE_CONTACTS = "tblcontacts";
	private static final String CONTACT_ID = "id";
	private static final String CONTACT_NAME = "name";
	private static final String CONTACT_PHONE = "phone";
	private static final String CONTACT_EMAIL = "email";
	private static final String CONTACT_ADDRESS = "address";
	private static final String CONTACT_PICTURE= "picture";

	private static final String TABLE_DEBT = "tbldebt";
	private static final String DEBT_ID = "id";
	private static final String DEBT_AMOUNT = "debt_amount";
	private static final String DEBT_DATE_CREATED = "debt_date";
	private static final String DEBT_DUE_DATE = "debt_due";
	private static final String DEBT_TYPE = "debt_type";
	private static final String DEBT_PERSON = "debt_person";
	private static final String DEBT_DESC = "debt_desc";
	private static final String DEBT_DUE_PARSE = "debt_due_parse";

	private static final String TABLE_PAYMENT = "tblpayment";
	private static final String PAYMENT_ID = "id";
	private static final String PAYMENT_TYPE = "payment_type";
	private static final String PAYMENT_AMOUNT = "payment_amount";
	private static final String PAYMENT_DATE = "payment_date";
	
	private static final String TABLE_HISTORY = "tblhistory";
	private static final String HISTORY_ID = "id";
	private static final String HISTORY_DESC = "history_description";
	private static final String HISTORY_PERSON = "history_person";
	private static final String HISTORY_INITIAL = "history_initial";
	private static final String HISTORY_CREATED = "history_created";
	private static final String HISTORY_ENDED = "history_ended";
	private static final String HISTORY_PHOTO = "history_photo";
	private static final String HISTORY_AUTO_ID = "hist_id";
		
	public DebtDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ CONTACT_ID + " INTEGER PRIMARY KEY," 
				+ CONTACT_NAME + " TEXT,"
				+ CONTACT_PHONE + " TEXT,"
				+ CONTACT_EMAIL + " TEXT,"
				+ CONTACT_ADDRESS + " TEXT,"
				+ CONTACT_PICTURE + " BLOB NOT NULL" + ")";
		String CREATE_DEBT_TABLE = "CREATE TABLE " + TABLE_DEBT + "("
				+ DEBT_ID + " INTEGER PRIMARY KEY," 
				+ DEBT_AMOUNT + " TEXT,"
				+ DEBT_DATE_CREATED + " TEXT,"
				+ DEBT_DUE_DATE + " TEXT,"
				+ DEBT_TYPE + " INTEGER,"
				+ DEBT_PERSON + " TEXT,"
				+ DEBT_DESC + " TEXT,"
				+ DEBT_DUE_PARSE + " TEXT" + ")";
		String CREATE_PAYMENT_TABLE = "CREATE TABLE " + TABLE_PAYMENT + "("
				+ PAYMENT_ID + " INTEGER," 
				+ PAYMENT_TYPE + " INTEGER,"
				+ PAYMENT_AMOUNT + " TEXT,"
				+ PAYMENT_DATE + " TEXT" + ")";
		String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
				+ HISTORY_ID + " INTEGER,"
				+ HISTORY_DESC + " TEXT,"  
				+ HISTORY_PERSON + " TEXT,"
				+ HISTORY_INITIAL + " TEXT,"
				+ HISTORY_CREATED + " TEXT,"
				+ HISTORY_ENDED + " TEXT,"
				+ HISTORY_PHOTO + " BLOB NOT NULL," 
				+ HISTORY_AUTO_ID + " INTEGER PRIMARY KEY" + ")";
		db.execSQL(CREATE_CONTACT_TABLE);
		db.execSQL(CREATE_DEBT_TABLE);
		db.execSQL(CREATE_PAYMENT_TABLE);
		db.execSQL(CREATE_HISTORY_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		// Create tables again
		onCreate(db);
	}
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations for Contact
	 */

	void addContact(ClassContact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		String name = contact.getName().replaceAll("'","\'");
		ContentValues values = new ContentValues();
		values.put(CONTACT_NAME, name);
		values.put(CONTACT_PHONE, contact.getPhone());
		values.put(CONTACT_EMAIL, contact.getEmail());
		values.put(CONTACT_ADDRESS, contact.getAddress());
		values.put(CONTACT_PICTURE, Utility.getBytes(contact.getPicture()));
		db.insert(TABLE_CONTACTS, null, values);
		db.close();
	}

	ClassContact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { CONTACT_ID, 
							CONTACT_NAME, CONTACT_PHONE,
							CONTACT_EMAIL, CONTACT_ADDRESS,
							CONTACT_PICTURE}, CONTACT_ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		ClassContact contact = new ClassContact(Integer.parseInt(cursor.getString(0)),
															cursor.getString(1),
															cursor.getString(2),
															cursor.getString(3),
															cursor.getString(4),
															Utility.getPhoto(cursor.getBlob(5)));
		return contact;
	}
	public List<ClassContact> getContactByName(String name) {
		List<ClassContact> contactList = new ArrayList<ClassContact>();
		String name_stripped = name.replaceAll("'","''");
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE name = '" + name_stripped + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ClassContact contact = new ClassContact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhone(cursor.getString(2));
				contact.setEmail(cursor.getString(3));
				contact.setAddress(cursor.getString(4));
				contact.setPicture(Utility.getPhoto(cursor.getBlob(5)));
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		return contactList;
	}
	public List<ClassContact> getAllContact() {
		List<ClassContact> contactList = new ArrayList<ClassContact>();

		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " ORDER BY " + CONTACT_NAME + " ASC";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ClassContact contact = new ClassContact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhone(cursor.getString(2));
				contact.setEmail(cursor.getString(3));
				contact.setAddress(cursor.getString(4));
				contact.setPicture(Utility.getPhoto(cursor.getBlob(5)));
			
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		return contactList;
	}
	public List<ClassContact> getAllContactWhere(String where, String service) {
		List<ClassContact> contactList = new ArrayList<ClassContact>();
		
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE network = '" + where + "' AND service = '" + service + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ClassContact contact = new ClassContact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhone(cursor.getString(2));
				contact.setEmail(cursor.getString(3));
				contact.setAddress(cursor.getString(4));
				contact.setPicture(Utility.getPhoto(cursor.getBlob(5)));
				
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		return contactList;
	}
	public int updateContact(ClassContact contact, String contact_name) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CONTACT_NAME, contact.getName());
		values.put(CONTACT_PHONE, contact.getPhone());
		values.put(CONTACT_EMAIL, contact.getEmail());
		values.put(CONTACT_PICTURE, Utility.getBytes(contact.getPicture()));
		return db.update(TABLE_CONTACTS, values, CONTACT_NAME + " = ?",
				new String[] { contact_name });
	}

	public void deleteContact(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, CONTACT_NAME + " = ?",
				new String[] { String.valueOf(name) });
		db.close();
	}
	public int getContactCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		return cursor.getCount();
	}
	
	
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations for Debt
	 */

		void addDebt(ClassDebt debt) {
			SQLiteDatabase db = this.getWritableDatabase();
			String name = debt.getDebtPerson().replaceAll("'","\'");
			ContentValues values = new ContentValues();
			values.put(DEBT_AMOUNT, debt.getDebtAmount());
			values.put(DEBT_DATE_CREATED, debt.getDebtDate());
			values.put(DEBT_DUE_DATE, debt.getDebtDue());
			values.put(DEBT_TYPE, debt.getDebtType());
			values.put(DEBT_PERSON, name);
			values.put(DEBT_DESC, debt.getDebDesc());
			values.put(DEBT_DUE_PARSE, debt.getDebtDueParse());
			
			db.insert(TABLE_DEBT, null, values);
			db.close();
		}

		// Updating single debt
		public int updateDebt(ClassDebt debt) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DEBT_AMOUNT, debt.getDebtAmount());
			values.put(DEBT_DATE_CREATED, debt.getDebtDate());
			values.put(DEBT_DUE_DATE, debt.getDebtDue());
			values.put(DEBT_TYPE, debt.getDebtType());
			values.put(DEBT_PERSON, debt.getDebtPerson());
			values.put(DEBT_DESC, debt.getDebDesc());
			values.put(DEBT_DUE_PARSE, debt.getDebtDueParse());
	
			return db.update(TABLE_DEBT, values, DEBT_ID + " = ?",
					new String[] { String.valueOf(debt.getID()) });
		}
		public int updateDebtPerson(String debt, String name) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DEBT_PERSON, debt);
	
			return db.update(TABLE_DEBT, values, DEBT_PERSON + " = ?",
					new String[] { String.valueOf(name) });
		}
		public List<ClassDebt> getDebt(int id) {
			List<ClassDebt> debtList = new ArrayList<ClassDebt>();
			
			String selectQuery = "SELECT  * FROM " + TABLE_DEBT + " WHERE id = " + id;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					ClassDebt debt = new ClassDebt();
					debt.setID(Integer.parseInt(cursor.getString(0)));
					debt.setDebtAmount(cursor.getString(1));
					debt.setDebtDate(cursor.getString(2));
					debt.setDateDue(cursor.getString(3));
					debt.setDebtType(cursor.getInt(4));
					debt.setDebtPerson(cursor.getString(5));
					debt.setDebtDesc(cursor.getString(6));
					debt.setDebtDueParse(cursor.getString(7));
					
					debtList.add(debt);
				} while (cursor.moveToNext());
			}

			return debtList;
		}
		
		public List<ClassDebt> getAllDebt() {
			List<ClassDebt> debtList = new ArrayList<ClassDebt>();
			
			String selectQuery = "SELECT  * FROM " + TABLE_DEBT;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					ClassDebt debt = new ClassDebt();
					debt.setID(Integer.parseInt(cursor.getString(0)));
					debt.setDebtAmount(cursor.getString(1));
					debt.setDebtDate(cursor.getString(2));
					debt.setDateDue(cursor.getString(3));
					debt.setDebtType(cursor.getInt(4));
					debt.setDebtPerson(cursor.getString(5));
					debt.setDebtDesc(cursor.getString(6));
					debt.setDebtDueParse(cursor.getString(7));
					debtList.add(debt);
				} while (cursor.moveToNext());
			}

			return debtList;
		}
		public List<ClassDebt> getAllDebtWhere(int type) {
			List<ClassDebt> debtList = new ArrayList<ClassDebt>();
			String selectQuery = "SELECT  * FROM " + TABLE_DEBT + " WHERE debt_type = " + type;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					ClassDebt debt = new ClassDebt();
					debt.setID(Integer.parseInt(cursor.getString(0)));
					debt.setDebtAmount(cursor.getString(1));
					debt.setDebtDate(cursor.getString(2));
					debt.setDateDue(cursor.getString(3));
					debt.setDebtType(cursor.getInt(4));
					debt.setDebtPerson(cursor.getString(5));
					debt.setDebtDesc(cursor.getString(6));
	
					debtList.add(debt);
				} while (cursor.moveToNext());
			}
			return debtList;
		}
		public List<ClassDebt> getAllDebtWhereAndWho(int type, String name) {
			List<ClassDebt> debtList = new ArrayList<ClassDebt>();
			name = name.replaceAll("'","''");
			Log.d("HAHA", name);
			String selectQuery = "SELECT  * FROM " + TABLE_DEBT + " WHERE debt_type = " 
												   + type + " AND " + DEBT_PERSON + " ='" + name + "'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					ClassDebt debt = new ClassDebt();
					debt.setID(Integer.parseInt(cursor.getString(0)));
					debt.setDebtAmount(cursor.getString(1));
					debt.setDebtDate(cursor.getString(2));
					debt.setDateDue(cursor.getString(3));
					debt.setDebtType(cursor.getInt(4));
					debt.setDebtPerson(cursor.getString(5));
					debt.setDebtDesc(cursor.getString(6));
	
					debtList.add(debt);
				} while (cursor.moveToNext());
			}
			return debtList;
		}
		public List<ClassDebt> getAllDebtByName(String name) {
			List<ClassDebt> debtList = new ArrayList<ClassDebt>();
			name = name.replaceAll("'","''");
			String selectQuery = "SELECT  * FROM " + TABLE_DEBT + " WHERE debt_person = '" + name + "'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					ClassDebt debt = new ClassDebt();
					debt.setID(Integer.parseInt(cursor.getString(0)));
					debtList.add(debt);
				} while (cursor.moveToNext());
			}
			return debtList;
		}
		public void deleteDebt(ClassDebt debt) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_DEBT, DEBT_ID + " = ?",
					new String[] { String.valueOf(debt.getID()) });
			db.close();
		}
		public void deleteDebtById(String id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_DEBT, DEBT_ID + " = ?",
					new String[] { id });
			db.close();
		}
		public void deleteDebtByName(String name) {
			SQLiteDatabase db = this.getWritableDatabase();
			name = name.replaceAll("'","''");
			db.delete(TABLE_DEBT, DEBT_PERSON + " = ?",
					new String[] { name });
			db.close();
		}
		public int getDebtCount() {
			String countQuery = "SELECT  * FROM " + TABLE_DEBT;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			return cursor.getCount();
		}
		public int getDebtCountByType(int type) {
			String countQuery = "SELECT  * FROM " + TABLE_DEBT + " WHERE debt_type = " + type;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			return cursor.getCount();
		}
		
		public List<ClassDebt> sortDebtBy(String by, int type) {
			List<ClassDebt> debtList = new ArrayList<ClassDebt>();
			
			String selectQuery = "SELECT  * FROM " + TABLE_DEBT + " WHERE debt_type = " + type + " ORDER BY " + by;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					ClassDebt debt = new ClassDebt();
					debt.setID(Integer.parseInt(cursor.getString(0)));
					debt.setDebtAmount(cursor.getString(1));
					debt.setDebtDate(cursor.getString(2));
					debt.setDateDue(cursor.getString(3));
					debt.setDebtType(cursor.getInt(4));
					debt.setDebtPerson(cursor.getString(5));
					debt.setDebtDesc(cursor.getString(6));
					debtList.add(debt);
				} while (cursor.moveToNext());
			}

			return debtList;
		}

		/**
		 * All CRUD(Create, Read, Update, Delete) Operations for Payment
		 */

		void addPayment(ClassPayment payment) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(PAYMENT_ID, payment.getID());
			values.put(PAYMENT_TYPE, payment.getPaymentType());
			values.put(PAYMENT_AMOUNT, payment.getPaymentAmount());
			values.put(PAYMENT_DATE, payment.getPaymentDate());
			// Inserting Row
			db.insert(TABLE_PAYMENT, null, values);
			db.close(); // Closing database connection
		}
		public int updateDebtPayment(String amount, int id) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DEBT_AMOUNT, amount);
			return db.update(TABLE_DEBT, values, DEBT_ID + " = ?",
					new String[] { String.valueOf(id) });
		}
		public List<ClassPayment> getAllPaymentWhere(int id) {
			List<ClassPayment> paymentList = new ArrayList<ClassPayment>();
			String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT + " WHERE id = " + id;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					ClassPayment payment = new ClassPayment();
					payment.setPaymentType(Integer.parseInt(cursor.getString(1)));
					payment.setPaymentAmount(cursor.getString(2));
					payment.setPaymentDate(cursor.getString(3));
					paymentList.add(payment);
				} while (cursor.moveToNext());
			}
			return paymentList;
		}
		public int getLastInserted() {
			int id = 0;
			String selectQuery = "SELECT * FROM " + TABLE_DEBT + " ORDER BY " + PAYMENT_ID + " DESC LIMIT 1";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					id = Integer.parseInt(cursor.getString(0));
				} while (cursor.moveToNext());
			}

			return id;
		}

		public void deletePaymentById(int id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_PAYMENT, PAYMENT_ID + " = ?",
					new String[] { String.valueOf(id) });
			Log.d("DELETE", id+"");
			db.close();
		}

		/**
		 * All CRUD(Create, Read, Update, Delete) Operations for History
		 */


		void addHistory(ClassHistory history) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(HISTORY_ID, history.getID());
			values.put(HISTORY_DESC, history.getDesc());
			values.put(HISTORY_PERSON, history.getPerson());
			values.put(HISTORY_INITIAL, history.getInitial());
			values.put(HISTORY_CREATED, history.getCreated());
			values.put(HISTORY_ENDED, history.getEnded());
			values.put(HISTORY_PHOTO, Utility.getBytes(history.getPicture()));
			// Inserting Row
			db.insert(TABLE_HISTORY, null, values);
			db.close(); // Closing database connection
		}
		
		public int updateHistory(ClassHistory history) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(HISTORY_PERSON, history.getPerson());
			values.put(HISTORY_PHOTO,  Utility.getBytes(history.getPicture()));
			values.put(HISTORY_CREATED, history.getCreated());
			values.put(HISTORY_DESC, history.getDesc());
			return db.update(TABLE_HISTORY, values, HISTORY_ID + " = ?",
					new String[] { String.valueOf(history.getID()) });
		}
		

		public int updateHistoryEnd(ClassHistory history) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(HISTORY_ENDED, history.getEnded());
			values.put(HISTORY_ID, 0);
			return db.update(TABLE_HISTORY, values, HISTORY_ID + " = ?",
					new String[] { String.valueOf(history.getID()) });
		}
		public List<ClassHistory> getAllHistoryWhere(int id) {
			List<ClassHistory> historyList = new ArrayList<ClassHistory>();
			String selectQuery = "SELECT  * FROM " + TABLE_HISTORY + " WHERE id = " + id;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					ClassHistory history = new ClassHistory();
					history.setDesc(cursor.getString(1));
					history.setPerson(cursor.getString(2));
					history.setInitial(cursor.getString(3));
					history.setCreated(cursor.getString(4));
					history.setEnded(cursor.getString(5));
					history.setPicture(Utility.getPhoto(cursor.getBlob(6)));
					history.setHistID(Integer.parseInt(cursor.getString(7)));
					historyList.add(history);
				} while (cursor.moveToNext());
			}
			return historyList;
		}

		public void deleteHistoryById(int id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_HISTORY, HISTORY_ID + " = ?",
					new String[] { String.valueOf(id) });
			db.close();
		}
		public void deleteHistoryByHistId(int id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_HISTORY, HISTORY_AUTO_ID + " = ?",
					new String[] { String.valueOf(id) });
			db.close();
		}
}
