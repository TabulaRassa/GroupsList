package ua.pp.igor_m.groupslist;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MovePeople extends ListActivity implements Constants
{
  private Cursor cursorPeople;
  private ListView lvPeople;
  private ArrayList<GLPeople> alPeople;
  private ArrayAdapter<GLPeople> aaPeople;
  private GLDBAdapter glAdapter;
  private GLPeople _people;
  private Cursor cursor;
  private long rowId;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    lvPeople = getListView();
    alPeople = new ArrayList<GLPeople>();
    aaPeople = new ArrayAdapter<GLPeople>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, alPeople);
    setListAdapter(aaPeople);
    lvPeople.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    glAdapter = new GLDBAdapter(this);
    fillList();
  }

  private void fillList()
  {
    alPeople.clear();
    glAdapter.open();
    cursor = glAdapter.getAllPeople();
    if (cursor.moveToFirst())
      do
      {
     // need add phone and address???
        _people = new GLPeople(MovePeople.this, cursor.getInt(cursor.getColumnIndex(_ID)), cursor.getString(cursor.getColumnIndex(_NAME)), cursor.getInt(cursor.getColumnIndex(_ID_G)));
        aaPeople.add(_people);
      } while (cursor.moveToNext());
    cursor.close();
    glAdapter.close();
    aaPeople.notifyDataSetChanged();

  }

  @Override
  public void onBackPressed()
  {
    int j;

    Bundle extras = getIntent().getExtras();
    if (extras != null)
    {
      rowId = extras.getLong("row_idG");
      SparseBooleanArray sbArray = lvPeople.getCheckedItemPositions();
      Log.d(_TAG, "size " + sbArray.size());
      for (int i = 0; i < sbArray.size(); i++)
      {
        _people = aaPeople.getItem(sbArray.keyAt(i));
        j = _people.getId();
        Log.d(_TAG, "j " + j + ", rowId " + rowId);
        glAdapter.setPeopleGroup(j, rowId);
      }
    }
    super.onBackPressed();
  }

}
