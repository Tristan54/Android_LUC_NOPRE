package fr.luc_nopre.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class TodoDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todo.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoContract.TodoEntry.TABLE_NAME + " (" +
                    TodoContract.TodoEntry._ID + " INTEGER PRIMARY KEY," +
                    TodoContract.TodoEntry.COLUMN_NAME_LABEL + " TEXT," +
                    TodoContract.TodoEntry.COLUMN_NAME_TAG + " TEXT,"  +
                    TodoContract.TodoEntry.COLUMN_NAME_DONE +  " INTEGER,"+
                    TodoContract.TodoEntry.COLUMN_NAME_POSITION + " INTEGER)";

    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Rien pour le moment
    }

    static ArrayList<TodoItem> getItems(Context context) {
        TodoDbHelper dbHelper = new TodoDbHelper(context);

        // Récupération de la base
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Création de la projection souhaitée
        String[] projection = {
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_NAME_LABEL,
                TodoContract.TodoEntry.COLUMN_NAME_TAG,
                TodoContract.TodoEntry.COLUMN_NAME_DONE,
                TodoContract.TodoEntry.COLUMN_NAME_POSITION
        };

        // Requête
        Cursor cursor = db.query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Exploitation des résultats
        ArrayList<TodoItem> items = new ArrayList<TodoItem>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry._ID));
            String label = cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_LABEL));
            TodoItem.Tags tag = TodoItem.getTagFor(cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_TAG)));
            boolean done = (cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_DONE)) == 1);
            int position = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_POSITION));
            TodoItem item = new TodoItem(label, tag, done, id);
            item.setId(id);
            updatePosition(item,context);
            items.add(item);
        }

         // Ménage
        dbHelper.close();

        // Retourne le résultat
        return items;
    }

    static void addItem(TodoItem item, Context context) {
        TodoDbHelper dbHelper = new TodoDbHelper(context);

        // Récupération de la base
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Création de l'enregistrement
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_LABEL, item.getLabel());
        values.put(TodoContract.TodoEntry.COLUMN_NAME_TAG, item.getTag().getDesc());
        values.put(TodoContract.TodoEntry.COLUMN_NAME_DONE, item.isDone());
        values.put(TodoContract.TodoEntry.COLUMN_NAME_POSITION, item.getPosition());

        // Enregistrement
        long newRowId = db.insert(TodoContract.TodoEntry.TABLE_NAME, null, values);

        // Ménage
        dbHelper.close();
    }

    static void updateDone(TodoItem item, Context context){
        TodoDbHelper dbHelper = new TodoDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(TodoContract.TodoEntry.COLUMN_NAME_DONE, item.isDone());
        long newRowId = (long) db.update(TodoContract.TodoEntry.TABLE_NAME,values,"_id="+item.getId(), null);
        dbHelper.close();
    }

    static void updatePosition(TodoItem item, Context context){
        TodoDbHelper dbHelper = new TodoDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(TodoContract.TodoEntry.COLUMN_NAME_POSITION, item.getId());
        long newRowId = (long) db.update(TodoContract.TodoEntry.TABLE_NAME,values,"_id="+item.getId(), null);
        dbHelper.close();
    }

    static void clearBDD(Context context){
        TodoDbHelper dbHelper = new TodoDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        long newRowId = (long) db.delete(TodoContract.TodoEntry.TABLE_NAME,null, null);
        dbHelper.close();
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
