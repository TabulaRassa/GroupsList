package ua.pp.igor_m.groupslist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ViewPeople extends Activity implements Constants, OnClickListener
{
  private ListView booksListView;
  private TextView peopleTextName;
  private TextView peopleTextPhone;
  private CursorAdapter booksAdapter;
  private GLDBAdapter glAdapter;
  Cursor bookCursor;
  Cursor givenCursor;
  private long rowId;
  private long _idB;
  private long _idP;

  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.records);

    booksListView = (ListView) findViewById(R.id.peoplelistView);
    peopleTextName = (TextView) findViewById(R.id.peopleView);
    peopleTextPhone = (TextView) findViewById(R.id.peopleViewPhone);
    
    //peopleTextName.setOnTouchListener(onTouchListener);

    peopleTextPhone.setOnClickListener(this);
    glAdapter = new GLDBAdapter(this);
    glAdapter.open();

    new ArrayList<GLBooks>();
    String[] from = new String[] { "name" };
    int[] to = new int[] { android.R.id.text1 };
    booksAdapter = new SimpleCursorAdapter(ViewPeople.this, android.R.layout.simple_list_item_multiple_choice, null, from, to);
    booksListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    booksListView.setAdapter(booksAdapter);

    Bundle extras = getIntent().getExtras();
    rowId = extras.getLong("row_id");
    
  }

  @Override
  public void onClick(View v)
  {
    switch (v.getId()) {
      case R.id.peopleViewPhone:
      {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + peopleTextPhone.getText()));
        startActivity(i);
        break;
      }
    }
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    new getPeople().execute(rowId);
  }

  protected void onStop()
  {
    Cursor cursor = booksAdapter.getCursor();
    if (cursor != null)
      cursor.close();
    booksAdapter.changeCursor(null);
    super.onStop();
  }

  @Override
  protected void onPause()
  {
    int j;
    _idP = rowId;

    SparseBooleanArray sbArray = booksListView.getCheckedItemPositions();

    for (int i = 0; i < sbArray.size(); i++)
    {
      j = sbArray.keyAt(i);
      _idB = booksListView.getAdapter().getItemId(j);
      glAdapter.open();
      bookCursor = glAdapter.getGivenByBookByPeople(_idB, _idP);
      if (bookCursor.moveToFirst())
      {
        if (!sbArray.get(j))
        {
          glAdapter.removeGiven(bookCursor);
        }
      } else
      {
        if (sbArray.get(j))
        {
          glAdapter.insertGiven(_idP, _idB);
        }
      }
      glAdapter.close();
      bookCursor.close();
    }

    super.onPause();
  }

  private class getPeople extends AsyncTask<Long, Object, Cursor>
  {
    GLDBAdapter glAdapter = new GLDBAdapter(ViewPeople.this);

    @Override
    protected Cursor doInBackground(Long... params)
    {
      glAdapter.open();

      return glAdapter.getOnePeople(params[0]);
    }

    @Override
    protected void onPostExecute(Cursor result)
    {
      super.onPostExecute(result);

      result.moveToFirst();
      peopleTextName.setText(result.getString(result.getColumnIndex(_NAME)));
      Cursor cursor = glAdapter.getPhonesByPeople(result.getInt(result.getColumnIndex(_ID)));
      if(cursor.moveToFirst())
        peopleTextPhone.setText(cursor.getString(cursor.getColumnIndex(_PHONE)));
      cursor.close();
      result.close();
      glAdapter.close();
      getBookList();
    }

  }

  private void getBookList()
  {
    _idP = rowId;
    glAdapter.open();
    booksAdapter.changeCursor(glAdapter.getAllBook());
    bookCursor = booksAdapter.getCursor();
    int i = 0;

    if (bookCursor.moveToFirst())
      do
      {
        _idB = bookCursor.getLong(0);
        givenCursor = glAdapter.getGivenByBookByPeople(_idB, _idP);
        if (givenCursor.moveToFirst())
        {
          booksListView.setItemChecked(/*bookCursor.getPosition()*/ i, true);
        }
        i++;
        givenCursor.close();
      } while (bookCursor.moveToNext());
    glAdapter.close();
  }

}
