package ua.pp.igor_m.groupslist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class BooksList extends AppCompatActivity implements  Constants
{
  private ListView bookList;
  private CursorAdapter bookAdapter;
  private GLDBAdapter glAdapter;
  private long rowId;
  
  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);    
    setContentView(R.layout.book_list);
      
    bookList = (ListView) findViewById(R.id.bookListView_2);
    
    bookList.setOnItemClickListener(viewBookListener);
    bookList.setBackgroundColor(getResources().getColor(R.color.background));

    String[] from = new String[] { "name", "count" };
    int[] to = new int[] { R.id.itemBook, R.id.itemCount };
    bookAdapter = new SimpleCursorAdapter(BooksList.this, R.layout.item_book, null, from, to);
    bookList.setAdapter(bookAdapter);
    glAdapter = new GLDBAdapter(BooksList.this);

    registerForContextMenu(bookList);

    glAdapter.open();
    bookAdapter.changeCursor(glAdapter.getAllBook());
    glAdapter.close();
    if (bookAdapter.getCount() == 0)
    {
      AlertDialog.Builder builder = new AlertDialog.Builder(BooksList.this);
      builder.setTitle(R.string.clear_list);
      builder.setMessage(R.string.add_new_record);
      builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int button)
        {
          Intent addNewPeople = new Intent(BooksList.this, AddEditBook.class);
          startActivity(addNewPeople);
        }
      });

      builder.setNegativeButton(R.string.button_cancel, null);
      builder.show();
    }
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
  {
    menu.add(0, 1, 0, "редактировать");
    menu.add(0, 2, 1, "удалить");
  }

  @Override
  public boolean onContextItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case 1:
    {
      rowId = bookAdapter.getCursor().getInt(0);
      Intent editBook = new Intent(this, AddEditBook.class);
      editBook.putExtra("row_id", rowId);
      startActivity(editBook);
      break;
    }
    case 2:
    {
      rowId = bookAdapter.getCursor().getInt(0);
      deleteBook();
      break;
    }

    }
    return super.onContextItemSelected(item);
  }

  private void deleteBook()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(BooksList.this);

    builder.setTitle(R.string.confirmTitle);
    builder.setMessage(R.string.confirmMessage);

    builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int button)
      {
        final GLDBAdapter glAdapter = new GLDBAdapter(BooksList.this);

        AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>()
        {
          @Override
          protected Object doInBackground(Long... params)
          {
            glAdapter.removeBook(params[0]);
            return null;
          }

          @Override
          protected void onPostExecute(Object result)
          {
            onResume();
          }
        };

        deleteTask.execute(new Long[] { rowId });
      }
    });

    builder.setNegativeButton(R.string.button_cancel, null);
    builder.show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu, menu);
    if (Build.VERSION.SDK_INT <= 11)
    {
      for (int i = 0; i < menu.size(); i++)
      {
        MenuItem item = menu.getItem(i);
        SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0);
        item.setTitle(spanString);
      }
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    switch (id)
    {
    case R.id.add:
    {
      Intent addNewbook = new Intent(this, AddEditBook.class);
      startActivity(addNewbook);
      break;
    }

    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume()
  {
    super.onResume();

    glAdapter.open();
    bookAdapter.changeCursor(glAdapter.getAllBook());
    glAdapter.close();
  }

  @Override
  protected void onStop()
  {
    Cursor cursor = bookAdapter.getCursor();
    if (cursor != null)
      cursor.close();
    bookAdapter.changeCursor(null);
    super.onStop();
  }

  OnItemClickListener viewBookListener = new OnItemClickListener()
  {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      Intent i = new Intent(BooksList.this, ViewBook.class);
      i.putExtra(ROW_ID, id);
      startActivity(i);

    }

  };
  
  
}
