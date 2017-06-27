package ua.pp.igor_m.groupslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GLDBAdapter implements Constants
{
  private SQLiteDatabase db;
  private GLHelper glHelper;
  private Cursor cursor;
  private GLBooks _book;
  private int nameIndex;
  private int countIndex;
  private int rID;

  public GLDBAdapter(Context context)
  {
    // context = _context;
    glHelper = new GLHelper(context, DATABASE_NAME, null, DB_VERSION);
  }

  public void open() throws SQLException
  {
    try
    {
      db = glHelper.getWritableDatabase();
    } catch (SQLiteException ex)
    {
      db = glHelper.getReadableDatabase();
    }
  }

  public void close()
  {
    if (db != null)
      db.close();
  }

  // people -->
  public void insertPeople(GLPeople _people)
  {
    ContentValues newPeopleValues = new ContentValues();
    newPeopleValues.put(_NAME, _people.getName());
    newPeopleValues.put(_ID_G, _people.getIdGroup()); 
    open();
    db.insert(PEOPLE_TABLE, null, newPeopleValues);
    close();
    
  }

  public void updatePeople(long id, GLPeople _people)
  {
    ContentValues newPeopleValues = new ContentValues();
    newPeopleValues.put(_NAME, _people.getName());
    newPeopleValues.put(_ID_G, _people.getIdGroup());
    open();
    db.update(PEOPLE_TABLE, newPeopleValues, _ID + " = " + id, null);
    close();
  }

  public void removePeople(long _rowIndex)
  {
    open();
    db.delete(PEOPLE_TABLE, _ID + "=" + _rowIndex, null);
    db.delete(GIVEN_TABLE, _ID_P + "=" + _rowIndex, null);
    db.delete(PHONES_TABLE, _ID_P + "=" + _rowIndex, null);
    db.delete(ADDRESS_TABLE, _ID_P + "=" + _rowIndex, null);
    cursor = db.query(GROUPS_TABLE, null, _ID_P + "=" + _rowIndex, null, null, null, null);
    if (cursor.moveToFirst())
      do
      {
        setLeaderGroup(cursor.getLong(cursor.getColumnIndex(_ID)), 0);
      } while (cursor.moveToNext());
    close();
  }

  public Cursor getOnePeople(long _id)
  {
    return db.query(PEOPLE_TABLE, null, _ID + " = " + _id, null, null, null, null);
  }

  public Cursor getOnePeople(String _name)
  {
    return db.query(PEOPLE_TABLE, null, _NAME + " = " + _name, null, null, null, null);
  }

  public Cursor getAllPeople()
  {
    return db.query(PEOPLE_TABLE, null /* new String[] { _ID, _NAME } */, null, null, null, null, _NAME);
  }

  public void setPeopleGroup(long idP, long idG)
  {
    open();
    Log.d(_TAG, "idP " + idP + ", idG " + idG);
    cursor = getOnePeople(idP);
    if (cursor.moveToFirst())
    {
      ContentValues newPeopleValues = new ContentValues();
      newPeopleValues.put(_ID_G, idG);
      db.update(PEOPLE_TABLE, newPeopleValues, _ID + " = " + idP, null);
    }
    close();
  }

  public Cursor getAllPeopleOfGroup(long _idG)
  {
    return db.query(PEOPLE_TABLE, null, _ID_G + " = " + _idG, null, null, null, _NAME);
  }

  // <-- people
  
  // --> phones
  public void insertPhone(GLPhone _phone)
  {
    ContentValues newPhoneValues = new ContentValues();
    newPhoneValues.put(_PHONE, _phone.getPhone());
    newPhoneValues.put(_ID_P, _phone.getIdP()); 
    open();
    db.insert(PHONES_TABLE, null, newPhoneValues);
    close();
    
  }

  public void updatePhone(long id, GLPhone _phone)
  {
    ContentValues newPhoneValues = new ContentValues();
    newPhoneValues.put(_PHONE, _phone.getPhone());
    newPhoneValues.put(_ID_P, _phone.getIdP());
    open();
    db.update(PHONES_TABLE, newPhoneValues, _ID + " = " + id, null);
    close();
  }

  public Cursor getOnePhone(long _id)
  {
    return db.query(PHONES_TABLE, null, _ID + " = " + _id, null, null, null, null);
  }

  public Cursor getOnePhone(String _phone)
  {
    return db.query(PHONES_TABLE, null, _PHONE + " = " + _phone, null, null, null, null);
  }

  public Cursor getPhonesByPeople(long _idP)
  {
    return db.query(PHONES_TABLE, null, _ID_P + " = " + _idP, null, null, null, null);
  }

  public void removePhone(long _rowIndex)
  {
    open();
    db.delete(PHONES_TABLE, _ID + "=" + _rowIndex, null);
    close();
  }
  // <-- phones

  //--> address
  public void insertAddress(GLAddress _address)
  {
    ContentValues newAddressValues = new ContentValues();
    newAddressValues.put(_ADDRESS, _address.getAddress());
    newAddressValues.put(_ID_P, _address.getIdP()); 
    open();
    db.insert(ADDRESS_TABLE, null, newAddressValues);
    close();
    
  }

  public void updateAddress(long id, GLAddress _address)
  {
    ContentValues newAddressValues = new ContentValues();
    newAddressValues.put(_ADDRESS, _address.getAddress());
    newAddressValues.put(_ID_P, _address.getIdP());
    open();
    db.update(ADDRESS_TABLE, newAddressValues, _ID + " = " + id, null);
    close();
  }

  public Cursor getOneAddress(long _id)
  {
    return db.query(ADDRESS_TABLE, null, _ID + " = " + _id, null, null, null, null);
  }

  public Cursor getOneAddress(String _address)
  {
    return db.query(ADDRESS_TABLE, null, _ADDRESS + " = " + _address, null, null, null, null);
  }

  public Cursor getAddressByPeople(long _idP)
  {
    return db.query(ADDRESS_TABLE, null, _ID_P + " = " + _idP, null, null, null, null);
  }

  public void removeAddress(long _rowIndex)
  {
    open();
    db.delete(ADDRESS_TABLE, _ID + "=" + _rowIndex, null);
    close();
  }
  // <-- address
  
  // --> given
  public Cursor getGiven(Cursor cursor2)
  {
    return db.query(GIVEN_TABLE, null, _ID + " = " + cursor2, null, null, null, null);
  }

  public Cursor getGivenByPeople(long _idP)
  {
    return db.query(GIVEN_TABLE, null, _ID_P + " = " + _idP, null, null, null, null);
  }

  public Cursor getGivenByBook(long _idB)
  {
    return db.query(GIVEN_TABLE, null, _ID_B + " = " + _idB, null, null, null, null);
  }

  public Cursor getGivenByBookByPeople(long _idB, long _idP)
  {
    cursor = null;
    try
    {
      cursor = db.query(GIVEN_TABLE, null, _ID_B + " = " + _idB + " and " + _ID_P + " = " + _idP, null, null, null, null);
    } catch (Exception e)
    {
      cursor = null;
    }
    return cursor;

  }

  public boolean removeGiven(Cursor cursor2)
  {
    int _idB;

    if (cursor2.moveToFirst())
    {
      _idB = cursor2.getInt(cursor2.getColumnIndex(_ID_B));
      db.delete(GIVEN_TABLE, _ID + "=" + cursor2.getInt(cursor2.getColumnIndex(_ID)), null);

      _book = new GLBooks();
      cursor = getOneBook(_idB);
      if (cursor.moveToFirst())
      {
        nameIndex = cursor.getColumnIndex(_NAME);
        countIndex = cursor.getColumnIndex(_COUNT);
        _book.book = cursor.getString(nameIndex);
        _book.count = cursor.getInt(countIndex) + 1;
        updateBook(_idB, _book);
      }
      return true;
    }

    return false;
  }

  public void insertGiven(long _idP, long _idB)
  {
    ContentValues newGivenValues = new ContentValues();
    newGivenValues.put(_ID_P, _idP);
    newGivenValues.put(_ID_B, _idB);
    db.insert(GIVEN_TABLE, null, newGivenValues);

    _book = new GLBooks();
    cursor = getOneBook(_idB);
    cursor.moveToFirst();
    nameIndex = cursor.getColumnIndex(_NAME);
    countIndex = cursor.getColumnIndex(_COUNT);
    _book.book = cursor.getString(nameIndex);
    _book.count = cursor.getInt(countIndex) - 1;
    updateBook(_idB, _book);
  }

  // <-- given

  // --> books
  public void insertBook(GLBooks _book)
  {
    ContentValues newBookValue = new ContentValues();
    newBookValue.put(_NAME, _book.getBook());
    newBookValue.put(_COUNT, _book.getCount());
    open();
    db.insert(BOOKS_TABLE, null, newBookValue);
    close();
  }

  public void updateBook(long id, GLBooks _book)
  {
    ContentValues newBookValue = new ContentValues();
    newBookValue.put(_NAME, _book.getBook());
    newBookValue.put(_COUNT, _book.getCount());
    open();
    db.update(BOOKS_TABLE, newBookValue, _ID + " = " + id, null);
    close();
  }

  public void removeBook(long _rowIndex)
  {
    open();
    db.delete(BOOKS_TABLE, _ID + "=" + _rowIndex, null);
    db.delete(GIVEN_TABLE, _ID_B + "=" + _rowIndex, null);
    close();
  }

  public Cursor getOneBook(long _id)
  {
    return db.query(BOOKS_TABLE, null, _ID + " = " + _id, null, null, null, null);
  }

  public Cursor getAllBook()
  {
    return db.query(BOOKS_TABLE, new String[] { _ID, _NAME, _COUNT }, null, null, null, null, _ID + " desc");
  }

  // <-- books

  // --> groups
  public long insertGroup(GLGroups _group)
  {

    ContentValues newGroupsValue = new ContentValues();
    newGroupsValue.put(_NAME, _group.getName());
    newGroupsValue.put(_ID_P, _group.getLeader());
    open();
    rID = (int) db.insert(GROUPS_TABLE, null, newGroupsValue);
    close();
    return rID;
  }

  public void updateGroup(long id, GLGroups _group)
  {

    ContentValues newGroupsValue = new ContentValues();
    newGroupsValue.put(_NAME, _group.getName());
    newGroupsValue.put(_ID_P, _group.getLeader());
    open();
    db.update(GROUPS_TABLE, newGroupsValue, _ID + " = " + id, null);
    close();
  }

  public void setLeaderGroup(long id, long idP)
  {

    ContentValues newGroupsValue = new ContentValues();
    newGroupsValue.put(_ID_P, idP);
    open();
    db.update(GROUPS_TABLE, newGroupsValue, _ID + " = " + id, null);
    close();
  }

  public void deleteGroup(int idG)
  {
    open();
    db.delete(GROUPS_TABLE, _ID + "=" + idG, null);
    cursor = getAllPeople();
    if (cursor.moveToFirst())
      do
      {
        ContentValues newPeopleValues = new ContentValues();
        newPeopleValues.put(_ID_G, 0);
        db.update(PEOPLE_TABLE, newPeopleValues, _ID_G + " = " + idG, null);
      } while (cursor.moveToNext());
    close();
  }

  public Cursor getOneGroup(long _id)
  {
    return db.query(GROUPS_TABLE, null, _ID + " = " + _id, null, null, null, null);
  }

  public Cursor getAllGroups()
  {
    return db.query(GROUPS_TABLE, null, null, null, null, null, _NAME);
  }

  // <-- groups

  private static class GLHelper extends SQLiteOpenHelper
  {

    public GLHelper(Context context, String name, CursorFactory factory, int version)
    {
      super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
      db.execSQL(PEOPLE_CREATE);
      db.execSQL(BOOKS_CREATE);
      db.execSQL(GIVEN_CREATE);
      db.execSQL(GROUPS_CREATE);
      db.execSQL(PHONE_CREATE);
      db.execSQL(ADDRESS_CREATE);
      db.execSQL(MESSAGES_CREATE);
      db.execSQL(SENDED_MESSEGES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
      if (oldVersion == 1)
      {
        db.execSQL("DROP TABLE IF EXISTS " + PEOPLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BOOKS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GIVEN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUPS_TABLE);
        onCreate(db);
      }
      if (oldVersion == 2 && newVersion == 3)
      {
        db.execSQL(PHONE_CREATE);
        db.execSQL(SMS_PHONES_CREATE);
        db.execSQL(ADDRESS_CREATE);
        db.execSQL(MESSAGES_CREATE);
        db.execSQL(SENDED_MESSEGES_CREATE);
        
        db.execSQL(PEOPLE_CREATE_T);        
        db.execSQL("insert into people_tmp select * from people;");        
        db.execSQL("DROP TABLE IF EXISTS " + PEOPLE_TABLE);
        db.execSQL(PEOPLE_CREATE);        
        db.execSQL("insert into people select _id, _idG, name from people_tmp;");
        db.execSQL("insert into " + PHONES_TABLE + " (" + _ID_P + ", " + _PHONE + ") select " + _ID + ", " + _PHONE + " from " + PEOPLE_TABLE_T + " where " + _PHONE + " != '';");
        db.execSQL("insert into " + ADDRESS_TABLE + " (" + _ID_P + ", " + _ADDRESS + ") select " + _ID + ", " + _ADDRESS + " from " + PEOPLE_TABLE_T + " where " + _ADDRESS + " != '';");
        
        db.execSQL("DROP TABLE IF EXISTS " + PEOPLE_TABLE_T);       
        

      }
    }

  }

}
