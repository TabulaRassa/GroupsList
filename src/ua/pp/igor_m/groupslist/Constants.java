package ua.pp.igor_m.groupslist;

public interface Constants
{
  //public static final String = "";
  public static final String DATABASE_NAME = "gl";
  
  public static final String PEOPLE_TABLE = "people";
  public static final String PEOPLE_TABLE_T = "people_tmp";
  public static final String BOOKS_TABLE = "books";
  public static final String GIVEN_TABLE = "given";
  public static final String GROUPS_TABLE = "groups";
  public static final String PHONES_TABLE = "phones";  
  public static final String SMS_PHONES_TABLE = "sms_phones";  
  public static final String ADDRESS_TABLE = "address";
  public static final String MESSAGES_TABLE = "messages";  
  public static final String SENDED_MESSEGES_TABLE = "sended_messeges";
  
  public static final int DB_VERSION = 3;
  
  public static final String _ID = "_id";
  public static final String _ID_P = "_idP";
  public static final String _ID_PH = "_idPh";
  public static final String _ID_B = "_idB";
  public static final String _ID_G = "_idG";
  public static final String _ID_M = "_idM";
  public static final String _NAME = "name";
  public static final String _TEXT = "text";
  public static final String _PHONE = "phone";
  public static final String _ADDRESS = "address";
  public static final String _DATE = "dt";
  public static final String _COUNT = "count";
  public static final String _SEND_SMS = "send_sms";
  public static final String _SEND_TIME = "send_time";
  
  public static final String _TAG = "debug tag";
  
  public static final String ROW_ID = "row_id";
  
  public static final String PEOPLE_CREATE = "create table " + PEOPLE_TABLE 
      + " (" + _ID + " integer primary key autoincrement, " 
      + _ID_G + " integer, " 
      + _NAME + " TEXT);";  
  
  public static final String PEOPLE_CREATE_T = "create temporary table " + PEOPLE_TABLE_T 
      + " (" + _ID + " integer primary key autoincrement, " 
      + _ID_G + " integer, " 
      + _NAME + " TEXT, " 
      + _PHONE + " TEXT, " 
      + _ADDRESS + " TEXT);";    
  
  public static final String PHONE_CREATE = "create table " + PHONES_TABLE
      + " (" + _ID + " integer primary key autoincrement, "
      + _ID_P + " integer, "
      + _PHONE + " TEXT);";
  
  public static final String SMS_PHONES_CREATE = "create table " + SMS_PHONES_TABLE
      + " (" + _ID + " integer primary key autoincrement, " 
      + _ID_PH + " integer, " 
      + _SEND_SMS + " integer);";
  
  public static final String ADDRESS_CREATE = "create table " + ADDRESS_TABLE 
      + " (" + _ID + " integer primary key autoincrement, " 
      + _ID_P + " integer, " 
      + _ADDRESS + " TEXT);";  
  
  public static final String BOOKS_CREATE = "create table " + BOOKS_TABLE
      + " (" + _ID + " integer primary key autoincrement, " 
      + _NAME + " TEXT, "
      + _COUNT + " integer);";
  
  public static final String GIVEN_CREATE = "create table " + GIVEN_TABLE
      + " (" + _ID + " integer primary key autoincrement, " 
      + _ID_P + " integer, " 
      + _ID_B + " integer);";
  
  public static final String MESSAGES_CREATE = "create table " + MESSAGES_TABLE
      + " (" + _ID + " integer primary key autoincrement, " 
      + _TEXT + " TEXT);";
  
  public static final String SENDED_MESSEGES_CREATE = "create table " + SENDED_MESSEGES_TABLE
      + " (" + _ID + " integer primary key autoincrement, " 
      + _ID_P + " integer, " 
      + _ID_M + " integer, "
      + _SEND_TIME + " TEXT);"; // TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS")
  
  public static final String GROUPS_CREATE = "create table " + GROUPS_TABLE
      + " (" + _ID + " integer primary key autoincrement, " 
      + _NAME + " TEXT, " 
      + _ID_P + " integer);";
  
}
