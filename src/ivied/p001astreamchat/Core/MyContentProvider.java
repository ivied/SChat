package ivied.p001astreamchat.Core;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyContentProvider extends ContentProvider {
	final String LOG_TAG = "myLogs"; 
	String howManyShow =null;
	String having = null;
	//final int AMOUNT_OF_VISIBLE_ROWS = 15;
	// // Êîíñòàíòû äëÿ ÁÄ
	// ÁÄ
	static final String DB_NAME = "mydb20";
	static final int DB_VERSION = 10;

	// Òàáëèöà
	static final String MESSAGES_TABLE = "chats";
	static final String CHANNELS_TABLE = "channels";
    static final String SMILE_TABLE = "smiles";

    public static final String MESSAGES_ID = "_id";
    public static final String MESSAGES_SITE_NAME = "site";
    public static final String MESSAGES_CHANEL = "channel";
    public static final String MESSAGES_NICK_NAME = "nick";
    public static final String MESSAGES_MESSAGE = "message";
    public static final String MESSAGES_UNIX_TIME = "time";
    public static final String MESSAGES_SPECIFIC_ID = "identificator";
    public static final String MESSAGES_PERSONAL =  "personal";
    public static final String MESSAGES_COLOR = "color";
	// Ñêðèïò ñîçäàíèÿ òàáëèöû
	static final String DB_CREATE = "create table " + MESSAGES_TABLE + "("
			+ MESSAGES_ID + " integer primary key autoincrement, "
			+ MESSAGES_UNIX_TIME + " integer, " + MESSAGES_SITE_NAME
			+ " text, " + MESSAGES_CHANEL + " text, " + MESSAGES_NICK_NAME
			+ " text, " + MESSAGES_MESSAGE + " text, " + MESSAGES_SPECIFIC_ID
			+ " text, "  + MESSAGES_COLOR
			+ " integer, " + MESSAGES_PERSONAL + " text " + ");";
    public static final String CHANNELS_ID = "_id";
    public static final String CHANNELS_CHAT_NAME = "chat";
    public static final String CHANNELS_SITE_NAME = "site";
    public static final String CHANNELS_CHANNEL = "channel";
    public static final String CHANNELS_FLAG = "flag";
    public static final String CHANNELS_COLOR = "color";
    public static final String CHANNELS_PERSONAL = "personal";
	// Ñêðèïò ñîçäàíèÿ òàáëèöû
	static final String DB_CREATE_CHANNELS = "create table " + CHANNELS_TABLE + "("
			+ CHANNELS_ID + " integer primary key autoincrement, "
			+ CHANNELS_CHAT_NAME
			+ " text, " + CHANNELS_SITE_NAME + " text, " + CHANNELS_CHANNEL
			+ " text, " + CHANNELS_FLAG + " text, " + CHANNELS_COLOR
			+ " integer, " + CHANNELS_PERSONAL + " text " + ");";

	public static final String SMILES_ID = "_id";
    public static final String SMILES_SITE = "site";
    public static final String SMILES_SMILE = "smile";
    public static final String SMILES_REGEXP = "regexp";
    public static final String SMILES_WIDTH = "width";
    public static final String SMILES_HEIGHT = "height";

    static final String DB_CREATE_SMILES = "create table " + SMILE_TABLE + "("
            + SMILES_ID + " integer primary key autoincrement, " + SMILES_SITE
            + " text, " + SMILES_SMILE + " BLOB, " + SMILES_REGEXP
            + " text, "  + SMILES_WIDTH
            + " integer, "  + SMILES_HEIGHT
            + " integer "  + ");";


	// // Uri
	// authority
	static final String AUTHORITY = "ivied.p001astreamchat";

	// path
	static final String MESSAGES_PATH = "chats";
	static final String CHANNELS_PATH = "channels";
    static final String SMILES_PATH = "smiles";
	// Îáùèé Uri
	public static final Uri MESSAGES_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + MESSAGES_PATH);
	
	public static final Uri CHANNELS_CONTENT_URI = Uri.parse("content://" +
			AUTHORITY + "/" + CHANNELS_PATH);


    public static final Uri SMILES_CONTENT_URI = Uri.parse("content://" +
            AUTHORITY + "/" + SMILES_PATH);
	// Òèïû äàííûõ
	// íàáîð ñòðîê
	static final String MESSAGES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
			+ AUTHORITY + "." + MESSAGES_PATH;

	// îäíà ñòðîêà
	static final String MESSAGES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
			+ AUTHORITY + "." + MESSAGES_PATH;


 
	// // UriMatcher
	// îáùèé Uri
	static final int URI_MESSAGES_INSERT = 1;

	// Uri ñ óêàçàííûì ID
	static final int URI_MESSAGES_SHOW = 2;
	
	static final int URI_CHANNEL_ADD = 3;
	static final int URI_CHANNEL_SERVICE =4;
    static final int URI_SMILE_INSERT = 5;

     final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
    public final static Uri SMILE_INSERT_URI = Uri.parse("content://ivied.p001astreamchat/smiles/newSmile");
	// îïèñàíèå è ñîçäàíèå UriMatcher
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, MESSAGES_TABLE + "/insert", URI_MESSAGES_INSERT);
		
		uriMatcher.addURI(AUTHORITY, MESSAGES_TABLE + "/show", URI_MESSAGES_SHOW);
		uriMatcher.addURI(AUTHORITY, CHANNELS_TABLE + "/add", URI_CHANNEL_ADD);
		uriMatcher.addURI(AUTHORITY, CHANNELS_TABLE + "/service", URI_CHANNEL_SERVICE);
        uriMatcher.addURI(AUTHORITY, SMILE_TABLE + "/newSmile", URI_SMILE_INSERT);
	}

	DBHelper dbHelper;
	SQLiteDatabase db;

	public boolean onCreate() {
		Log.d(LOG_TAG, "onCreate");
		dbHelper = new DBHelper(getContext());
		return true;
	}

	@Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int cnt = 0;
        switch (uriMatcher.match(uri)) {
            case URI_CHANNEL_ADD:
                cnt = db.delete(CHANNELS_TABLE, selection, selectionArgs);
                break;
            case URI_SMILE_INSERT:
                cnt = db.delete(SMILE_TABLE, selection, selectionArgs);
                break;
        }
            getContext().getContentResolver().notifyChange(uri, null);
            return cnt;

    }

	// ÷òåíèå
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		db = dbHelper.getWritableDatabase();

		//String id = uri.getLastPathSegment();
		// ïðîâåðÿåì Uri
        Cursor cursor =null;
        switch (uriMatcher.match(uri)) {
            case URI_MESSAGES_SHOW:

                String idFrom ="0";
                cursor = db.query(MESSAGES_TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder, howManyShow);

                if (cursor.getCount() > MainActivity.AMOUNT_OF_VISIBLE_ROWS) {
                    int idFirst = cursor.getCount() - MainActivity.AMOUNT_OF_VISIBLE_ROWS;

                    cursor.moveToPosition(idFirst);
                    idFrom = cursor.getString(0);
                }

               cursor.close();

                selection = "( " + selection + " )" + " AND " + " _id > ?";

                List<String> wordList = new ArrayList<String>(Arrays.asList(selectionArgs));
                wordList.add(idFrom);
                String[] selectionArgsAdd = new String[wordList.size()];
                selectionArgsAdd = wordList.toArray(selectionArgsAdd);

                cursor = db.query(MESSAGES_TABLE, projection, selection,
                        selectionArgsAdd, null , null, " _id ASC ", howManyShow);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        MESSAGES_CONTENT_URI);

                break;
            case URI_MESSAGES_INSERT:
                cursor = db.query(MESSAGES_TABLE, projection, selection,
                        selectionArgs, null, null ,sortOrder, null);

                break;
            case URI_CHANNEL_ADD:

                cursor = db.query(CHANNELS_TABLE, projection, selection,
                        selectionArgs, null, having, sortOrder, howManyShow);

                break;
            case URI_CHANNEL_SERVICE:

                cursor = db.query(CHANNELS_TABLE, projection, selection,
                        selectionArgs, " chat ", having, sortOrder, howManyShow);
                break;
            case URI_SMILE_INSERT:
                cursor = db.query(SMILE_TABLE, projection, selection,
                        selectionArgs, null, having, sortOrder, howManyShow);
                break;
        }

        return cursor;
	}
	



	@Override
	public String getType(Uri uri) {
		Log.d(LOG_TAG, "getType, " + uri.toString());
		switch (uriMatcher.match(uri)) {
		case URI_MESSAGES_SHOW:
			return MESSAGES_CONTENT_TYPE;
		case URI_MESSAGES_INSERT:
			return MESSAGES_CONTENT_ITEM_TYPE;
		}
		return null;
	}

    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = null;

        switch (uriMatcher.match(uri)) {
            case URI_MESSAGES_INSERT:
                db = dbHelper.getWritableDatabase();
                long rowID = db.insert(MESSAGES_TABLE, null, values);
                resultUri = ContentUris.withAppendedId(MESSAGES_CONTENT_URI, rowID);
                break;
            case URI_CHANNEL_ADD:
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(CHANNELS_TABLE, null, values);
                resultUri = ContentUris.withAppendedId(CHANNELS_CONTENT_URI, rowID);
                break;
            case URI_SMILE_INSERT:
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(SMILE_TABLE, null, values);
                resultUri = ContentUris.withAppendedId(CHANNELS_CONTENT_URI, rowID);

                break;
            default:

                throw new IllegalArgumentException("Wrong URI: " + uri);

        }

        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

	@Override
	public int update(Uri uri, ContentValues values, String selection,
		      String[] selectionArgs) {
		db = dbHelper.getWritableDatabase();
	    int cnt = db.update(CHANNELS_TABLE, values, selection, selectionArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return cnt;

	}
	public class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
			db.execSQL(DB_CREATE_CHANNELS);
            db.execSQL(DB_CREATE_SMILES);
			
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(DBHelper.class.getName(),
			        "Upgrading database from version " + oldVersion + " to "
			            + newVersion + ", which will destroy all old data");
			    db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
			    db.execSQL("DROP TABLE IF EXISTS " + CHANNELS_TABLE);
                db.execSQL("DROP TABLE IF EXISTS " + SMILE_TABLE);
			    onCreate(db);
		}
	}

	

}
