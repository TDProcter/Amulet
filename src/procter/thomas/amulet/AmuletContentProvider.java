package procter.thomas.amulet;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class AmuletContentProvider extends ContentProvider {
	  public static final String AUTHORITY = "procter.thomas.amuletcontentprovider";
	  public static final String TASKS_PATH = "tasksTable";
	  public static final String DIARY_PATH = "diaryTable";
	  
	  
	  
	  public static final Uri CONTENT_URI_TASKS = Uri.parse("content://" + AUTHORITY + "/" + TASKS_PATH);
	  public static final Uri CONTENT_URI_DIARY = Uri.parse("content://" + AUTHORITY + "/" + DIARY_PATH);
	  
	  static final String TAG = "AMULETCONTENTPROVIDER";
	  
	  //Create the constants used to differentiate between the different URI requests.
	  
	  private static final int TASKS_ALLROWS = 1;
	  private static final int TASKS_SINGLE_ROW = 2;
	  private static final int DIARY_ALLROWS = 3;
	  private static final int DIARY_SINGLE_ROW = 4;
	  
	  private static final UriMatcher uriMatcher;
	  
	  //Populate the UriMatcher object, where a URI ending in Ôitems' will
	  //correspond to a request for all items, and Ôitems/[rowID]Õ
	  //represents a single row.
	  static {
	   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	   uriMatcher.addURI(AUTHORITY, TASKS_PATH, TASKS_ALLROWS);
	   uriMatcher.addURI(AUTHORITY, TASKS_PATH + "/#", TASKS_SINGLE_ROW);
	   uriMatcher.addURI(AUTHORITY, DIARY_PATH, DIARY_ALLROWS);
	   uriMatcher.addURI(AUTHORITY, DIARY_PATH + "/#", DIARY_SINGLE_ROW);	   
	  }
	  
	  // The index (key) column name for use in where clauses.
	  public static final String KEY_ID = "_id";

	  // The name and column index of each column in your database.
	  //Tasks Table - column names
	  public static final String KEY_TASKS_TASKTYPE_COLUMN =  
	   "TASKS_TASKTYPE_COLUMN";
	  public static final String KEY_TASKS_TIMESTAMP_COLUMN =  
			   "TASKS_TIMESTAMP_COLUMN";
	  public static final String KEY_TASKS_TASKVALUE_COLUMN =  
			   "TASKS_TASKVALUE_COLUMN";
	  public static final String KEY_TASKS_UNITSCONSUMED_COLUMN =  
			   "TASKS_UNITSCONSUMED_COLUMN";
	  public static final String KEY_TASKS_SYNCED_COLUMN =
			  "TASKS_SYNCED_COLUMN";
	  
	//Diary Table - column names
	  public static final String KEY_DIARY_TIMESTAMP_COLUMN =  
			   "DIARY_TIMESTAMP_COLUMN";
	  public static final String KEY_DIARY_DRINKTYPE_COLUMN =  
			   "DIARY_DRINKTYPE_COLUMN";
	  public static final String KEY_DIARY_UNITSCONSUMED_COLUMN =  
			   "DIARY_UNITSCONSUMED_COLUMN";
	  public static final String KEY_DIARY_SYNCED_COLUMN =
			  "DIARY_SYNCED_COLUMN";

	  // Database open/upgrade helper
	  private AmuletDBOpenHelper amuletDBOpenHelper;
	  
	  @Override
	  public boolean onCreate() {
	    // Construct the underlying database.
	    // Defer opening the database until you need to perform
	    // a query or transaction.
	    amuletDBOpenHelper = new AmuletDBOpenHelper(getContext(),
	        AmuletDBOpenHelper.DATABASE_NAME, null, 
	        AmuletDBOpenHelper.DATABASE_VERSION);
	    
	    return true;
	  }
	  
	  @Override
	  public Cursor query(Uri uri, String[] projection, String selection,
	      String[] selectionArgs, String sortOrder) {
	    // Open a read-only database.
	    SQLiteDatabase db = amuletDBOpenHelper.getReadableDatabase();
	  
	    // Replace these with valid SQL statements if necessary.
	    String groupBy = null;
	    String having = null;
	    
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	    
	    String rowID;
	    // If this is a row query, limit the result set to the passed in row.
		switch (uriMatcher.match(uri)) {
		case TASKS_SINGLE_ROW:
			rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(KEY_ID + "=" + rowID);
		case TASKS_ALLROWS:
			queryBuilder.setTables(AmuletDBOpenHelper.TASKS_TABLE);
			break;
		case DIARY_SINGLE_ROW:
			rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(KEY_ID + "=" + rowID);
		case DIARY_ALLROWS:
			queryBuilder.setTables(AmuletDBOpenHelper.DIARY_TABLE);
			break;

		default:
			break;
		}
	    
	    Cursor cursor = queryBuilder.query(db, projection, selection,
	        selectionArgs, groupBy, having, sortOrder);
	  
	    return cursor;
	  }

	  @Override
	  public int delete(Uri uri, String selection, String[] selectionArgs) {
	    // Open a read / write database to support the transaction.
	    SQLiteDatabase db = amuletDBOpenHelper.getWritableDatabase();
	    String TABLE_NAME = "";
	    String rowID = "";
	    // If this is a row URI, limit the deletion to the specified row.
	    switch (uriMatcher.match(uri)) {
	      case TASKS_SINGLE_ROW : 
	        rowID = uri.getPathSegments().get(1);
	        selection = KEY_ID + "=" + rowID
	            + (!TextUtils.isEmpty(selection) ? 
	              " AND (" + selection + ')' : "");
	      case TASKS_ALLROWS : 
	        TABLE_NAME = AmuletDBOpenHelper.TASKS_TABLE;
	        break;
	      
	      case DIARY_SINGLE_ROW : 
		        rowID = uri.getPathSegments().get(1);
		        selection = KEY_ID + "=" + rowID
		            + (!TextUtils.isEmpty(selection) ? 
		              " AND (" + selection + ')' : "");
	      case DIARY_ALLROWS : 
		        TABLE_NAME = AmuletDBOpenHelper.DIARY_TABLE;
		        break;
	      default: break;
	    }
	    
	    // To return the number of deleted items you must specify a where
	    // clause. To delete all rows and return a value pass in "1".
	    if (selection == null)
	      selection = "1";
	  
	    return db.delete(TABLE_NAME, selection, selectionArgs);
	  }
	  
	  
	  @Override
	  public Uri insert(Uri uri, ContentValues values) {
		  
	    // Open a read / write database to support the transaction.
	    SQLiteDatabase db = amuletDBOpenHelper.getWritableDatabase();
	    
	    // To add empty rows to your database by passing in an empty Content Values object
	    // you must use the null column hack parameter to specify the name of the column 
	    // that can be set to null.
	    String nullColumnHack = null;
	    
	    // Insert the values into the table
	    long id = -1;
	    Uri CONTENT_URI = null;
	    switch (uriMatcher.match(uri)) {
	    case TASKS_SINGLE_ROW : 
	    case TASKS_ALLROWS :
	    	id = db.insert(AmuletDBOpenHelper.TASKS_TABLE, 
	    	        nullColumnHack, values);
	    	CONTENT_URI = CONTENT_URI_TASKS;
	  	  break;
	    case DIARY_SINGLE_ROW : 
	    case DIARY_ALLROWS :
	    	id = db.insert(AmuletDBOpenHelper.DIARY_TABLE, 
	    	        nullColumnHack, values);
	    	CONTENT_URI = CONTENT_URI_DIARY;
	  	  break;
	    default: break;
	  }
	    
	    // Construct and return the URI of the newly inserted row.
	    if (id > -1)
	      return ContentUris.withAppendedId(CONTENT_URI, id);
	    else
	      return null;
	      
	    
	  }
	  

	  @Override
	  public int update(Uri uri, ContentValues values, String selection,
	    String[] selectionArgs) {
	    
	    // Open a read / write database to support the transaction.
	    SQLiteDatabase db = amuletDBOpenHelper.getWritableDatabase();
	    String TABLE_NAME = "";
	    String rowID = "";
	    // If this is a row URI, limit the deletion to the specified row.
	    switch (uriMatcher.match(uri)) {
	      case TASKS_SINGLE_ROW : 
	        rowID = uri.getPathSegments().get(1);
	        selection = KEY_ID + "=" + rowID
	            + (!TextUtils.isEmpty(selection) ? 
	              " AND (" + selection + ')' : "");
	        TABLE_NAME = AmuletDBOpenHelper.TASKS_TABLE;
	        break;
	      case DIARY_SINGLE_ROW : 
		        rowID = uri.getPathSegments().get(1);
		        selection = KEY_ID + "=" + rowID
		            + (!TextUtils.isEmpty(selection) ? 
		              " AND (" + selection + ')' : "");
		        TABLE_NAME = AmuletDBOpenHelper.DIARY_TABLE;
		        break;
	      default: break;
	    }
	  
	    return db.update(TABLE_NAME, 
	      values, selection, selectionArgs);
	  }
	  
	  @Override
	  public String getType(Uri uri) {
	    // Return a string that identifies the MIME type
	    // for a Content Provider URI
	    switch (uriMatcher.match(uri)) {
	      case TASKS_ALLROWS: return "vnd.android.cursor.dir/vnd.procter.thomas.taskhistory";
	      case TASKS_SINGLE_ROW: return "vnd.android.cursor.item/vnd.procter.thomas.taskhistory";
	      case DIARY_ALLROWS: return "vnd.android.cursor.dir/vnd.procter.thomas.drinkDiary";
	      case DIARY_SINGLE_ROW: return "vnd.android.cursor.item/vnd.procter.thomas.drinkDiary";
	      
	      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
	    }
	  }

	  private static class AmuletDBOpenHelper extends SQLiteOpenHelper {
	    
	    private static final String DATABASE_NAME = "amulet.db";
	    
	    private static final int DATABASE_VERSION = 1;
	    
	 // Table Names
	    private static final String TASKS_TABLE = "TasksTable";
	    private static final String DIARY_TABLE = "DiaryTable";
	    
	    // SQL Statement to create a new database.
	    private static final String TASKS_CREATE = "create table " +
	      TASKS_TABLE + " (" + KEY_ID +
	      " integer primary key autoincrement, " +
	      KEY_TASKS_TASKTYPE_COLUMN + " text not null, " +
	      KEY_TASKS_TIMESTAMP_COLUMN + " text not null, " +
	      KEY_TASKS_TASKVALUE_COLUMN + " text not null, " +
	      KEY_TASKS_UNITSCONSUMED_COLUMN + " text not null, " +
	      KEY_TASKS_SYNCED_COLUMN + " boolean)";
	    
	    private static final String DIARY_CREATE = "create table " +
	  	      DIARY_TABLE + " (" + KEY_ID +
	  	      " integer primary key autoincrement, " +
	  	      KEY_DIARY_DRINKTYPE_COLUMN + " text not null, " +
	  	      KEY_DIARY_TIMESTAMP_COLUMN + " text not null, " +
	  	      KEY_DIARY_UNITSCONSUMED_COLUMN + " text not null, " +
	  	      KEY_DIARY_SYNCED_COLUMN + " boolean)";
	    
	    public AmuletDBOpenHelper(Context context, String name,
	                      CursorFactory factory, int version) {
	      super(context, name, factory, version);
	    }

	    // Called when no database exists in disk and the helper class needs
	    // to create a new one.
	    @Override
	    public void onCreate(SQLiteDatabase _db) {
	    	_db.execSQL(TASKS_CREATE);
	    	_db.execSQL(DIARY_CREATE);
	    }

	    // Called when there is a database version mismatch meaning that the version
	    // of the database on disk needs to be upgraded to the current version.
	    @Override
	    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
	      // Log the version upgrade.
	      Log.w("AmuletDBAdapter", "Upgrading from version " +
	                             _oldVersion + " to " +
	                             _newVersion + ", which will destroy all old data");

	      // Upgrade the existing database to conform to the new version. Multiple
	      // previous versions can be handled by comparing _oldVersion and _newVersion
	      // values.

	      // The simplest case is to drop the old table and create a new one.
	      _db.execSQL("DROP TABLE IF IT EXISTS " + TASKS_TABLE);
	      _db.execSQL("DROP TABLE IF IT EXISTS " + DIARY_TABLE);
	      // Create a new one.
	      onCreate(_db);
	    }
	  }

	}
