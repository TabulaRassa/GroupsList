package ua.pp.igor_m.groupslist;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ViewBook extends Activity implements Constants
{
  private ListView peopleListView;
  private TextView bookTextView;
  private TextView bookTextCount;
  private CursorAdapter peopleAdapter;
  private GLDBAdapter glAdapter;
  Cursor givenCursor;
  Cursor peopleCursor;
  private long rowId;
  private long _idB;
  private long _idP;

  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.records);

    peopleListView = (ListView) findViewById(R.id.peoplelistView);
    bookTextView = (TextView) findViewById(R.id.peopleView);
    bookTextCount = (TextView) findViewById(R.id.bookViewCount);

    glAdapter = new GLDBAdapter(this);
    glAdapter.open();

    new ArrayList<GLPeople>();
    String[] from = new String[] { "name" };
    int[] to = new int[] { android.R.id.text1 };
    peopleAdapter = new SimpleCursorAdapter(ViewBook.this, android.R.layout.simple_list_item_multiple_choice, null, from, to);
    peopleListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    peopleListView.setAdapter(peopleAdapter);

    Bundle extras = getIntent().getExtras();
    rowId = extras.getLong("row_id");

    peopleListView.setOnItemClickListener(new OnItemClickListener()
    {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
        Integer count;
        SparseBooleanArray sbArray = peopleListView.getCheckedItemPositions();
        if (sbArray.get(position))
        {
          count = Integer.parseInt(bookTextCount.getText().toString()) - 1;
        } else
        {
          count = Integer.parseInt(bookTextCount.getText().toString()) + 1;
        }
        bookTextCount.setText(count.toString());
      }

    });
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    new getBook().execute(rowId);
  }

  @Override
  protected void onStop()
  {
    Cursor cursor = peopleAdapter.getCursor();
    if (cursor != null)
      cursor.close();
    peopleAdapter.changeCursor(null);
    super.onStop();
  }
  
  @Override
  protected void onPause()
  {
    int j;
    _idB = rowId;

    SparseBooleanArray sbArray = peopleListView.getCheckedItemPositions();

    for (int i = 0; i < sbArray.size(); i++)
    {
      j = sbArray.keyAt(i);
      _idP = peopleListView.getAdapter().getItemId(j);
      glAdapter.open();
      peopleCursor = glAdapter.getGivenByBookByPeople(_idB, _idP);
      if (peopleCursor.moveToFirst())
      {
        if (!sbArray.get(j))
        {
          glAdapter.removeGiven(peopleCursor);
        }
      } else
      {
        if (sbArray.get(j))
        {
          glAdapter.insertGiven(_idP, _idB);
        }
      }
      glAdapter.close();
      peopleCursor.close();
    }

    super.onPause();
  }

  private class getBook extends AsyncTask<Long, Object, Cursor>
  {
    GLDBAdapter glAdapter = new GLDBAdapter(ViewBook.this);

    @Override
    protected Cursor doInBackground(Long... params)
    {
      glAdapter.open();

      return glAdapter.getOneBook(params[0]);
    }

    @Override
    protected void onPostExecute(Cursor result)
    {
      super.onPostExecute(result);

      result.moveToFirst();
      int nameIndex = result.getColumnIndex(_NAME);
      int nameCount = result.getColumnIndex(_COUNT);
      bookTextView.setText(result.getString(nameIndex));
      bookTextCount.setText(result.getString(nameCount));
      result.close();
      glAdapter.close();
      getPeopleList();
    }

  }

  private void getPeopleList()
  {
    _idB = rowId;
    glAdapter.open();
    peopleAdapter.changeCursor(glAdapter.getAllPeople());
    peopleCursor = peopleAdapter.getCursor();
    int i = 0;

    if (peopleCursor.moveToFirst())
      do
      {
        _idP = peopleCursor.getLong(0);
        givenCursor = glAdapter.getGivenByBookByPeople(_idB, _idP);
        if (givenCursor.moveToFirst())
        {
          peopleListView.setItemChecked(/*peopleCursor.getPosition()*/ i, true);
        }
        i++;
        givenCursor.close();
      } while (peopleCursor.moveToNext());
    glAdapter.close();
  }

}
