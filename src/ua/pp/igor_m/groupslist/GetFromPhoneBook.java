package ua.pp.igor_m.groupslist;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GetFromPhoneBook extends ListActivity implements Constants
{
  private Cursor cursorPhone;
  private Cursor cursor;
  private ListView lvPeople;
  private int _idP;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    lvPeople = getListView();
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1,  getModel());
    setListAdapter(adapter);
    lvPeople.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    
  }
  
  private List<String> getModel()
  {
    cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] { Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER }, null, null, Phone.DISPLAY_NAME);
    
    List<String> list = new ArrayList<String>();
    
    if (cursorPhone.moveToFirst())
    do
    {
      list.add(cursorPhone.getString(1).replace(",", "") + ", " + cursorPhone.getString(2));
    } while(cursorPhone.moveToNext());
    
    return list;
  }
  
  @Override
  public void onBackPressed()
  {
    int j;
    String _name;
    String _phone;
    
    SparseBooleanArray sbArray = lvPeople.getCheckedItemPositions();    
    
    for (int i = 0; i < sbArray.size(); i++)
    {
      j = sbArray.keyAt(i);
      try
      {
        GLDBAdapter dbAdapter = new GLDBAdapter(this);
        _name = lvPeople.getItemAtPosition(j).toString().substring(0, lvPeople.getItemAtPosition(j).toString().indexOf(",")); 
        _phone = lvPeople.getItemAtPosition(j).toString().substring(lvPeople.getItemAtPosition(j).toString().indexOf(",")+2);
        
        GLPeople _people = new GLPeople(GetFromPhoneBook.this, _name);        
        dbAdapter.insertPeople(_people);
        cursor = dbAdapter.getOnePeople(_name);
        cursor.moveToFirst();
        _idP = cursor.getInt(cursor.getColumnIndex(_ID));
        GLPhone phone = new GLPhone(_idP, _phone);
        dbAdapter.insertPhone(phone);
      }
      finally
      {
        
      }
      
    }

    super.onBackPressed();
  }
  
}
