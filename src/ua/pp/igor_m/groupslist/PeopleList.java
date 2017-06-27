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
import android.widget.TextView;

public class PeopleList extends AppCompatActivity implements Constants
{
  
  private ListView peopleList;
  private CursorAdapter peopleAdapter;
  private GLDBAdapter glAdapter;
  private long rowId;
  private TextView tvCount;
  
  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);    
    setContentView(R.layout.people_list);
    
    tvCount = (TextView)findViewById(R.id.tvCount);
    peopleList = (ListView) findViewById(R.id.peopleListView_2);    

    peopleList.setOnItemClickListener(viewPeopleListener);
    peopleList.setBackgroundColor(getResources().getColor(R.color.background));
    tvCount.setBackgroundColor(getResources().getColor(R.color.background));

    String[] from = new String[] { "name" };
    int[] to = new int[] { R.id.item };
    peopleAdapter = new SimpleCursorAdapter(PeopleList.this, R.layout.item, null, from, to);
    peopleList.setAdapter(peopleAdapter);
    glAdapter = new GLDBAdapter(PeopleList.this);

    registerForContextMenu(peopleList);

    glAdapter.open();
    peopleAdapter.changeCursor(glAdapter.getAllPeople());
    glAdapter.close();
    tvCount.setText(peopleAdapter.getCount() + " возвещателей");
    if (peopleAdapter.getCount() == 0)
    {
      AlertDialog.Builder builder = new AlertDialog.Builder(PeopleList.this);
      builder.setTitle(R.string.clear_list);
      builder.setMessage(R.string.add_new_record);
      builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int button)
        {
          Intent addNewPeople = new Intent(PeopleList.this, AddEditPeople.class);
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
      rowId = peopleAdapter.getCursor().getInt(0);
      Intent editPeople = new Intent(this, AddEditPeople.class);
      editPeople.putExtra("row_id", rowId);
      startActivity(editPeople);
      break;
    }
    case 2:
    {
      rowId = peopleAdapter.getCursor().getInt(0);
      deletePeople();
      break;
    }

    }
    return super.onContextItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    menu.add(0, 1, 0, "добавить");
    menu.add(0, 2, 0, "добавить из справочника");
    menu.add(0, 3, 0, "разослать СМС");
    //menu.add(0, 4, 0, "надзиратель группы");
    if (Build.VERSION.SDK_INT <= 11)
    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
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
    case 1:
    {
      Intent addNewPeople = new Intent(this, AddEditPeople.class);
      startActivity(addNewPeople);
      break;
    }
    case 2:
    {
      Intent addFromBook = new Intent(this, GetFromPhoneBook.class);
      startActivity(addFromBook);
      break;
    }
    case 3:
    {
      Intent sendMessage = new Intent(this, SendMessage.class);
      startActivity(sendMessage);
      break;
    }
    /*case 4:
    {
      Intent groupLeader = new Intent(this, GroupLeader.class);
      startActivity(groupLeader);
      break;
    }*/
    
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume()
  {
    super.onResume();

    glAdapter.open();
    peopleAdapter.changeCursor(glAdapter.getAllPeople());    
    glAdapter.close();
    tvCount.setText(peopleAdapter.getCount() + " возвещателей");
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

  OnItemClickListener viewPeopleListener = new OnItemClickListener()
  {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      Intent i = new Intent(PeopleList.this, ViewPeople.class);
      i.putExtra(ROW_ID, id);
      startActivity(i);

    }

  };

  private void deletePeople()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(PeopleList.this);
    builder.setTitle(R.string.confirmTitle);
    builder.setMessage(R.string.confirmMessage);
    builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int button)
      {
        final GLDBAdapter glAdapter = new GLDBAdapter(PeopleList.this);

        AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>()
        {
          @Override
          protected Object doInBackground(Long... params)
          {
            glAdapter.removePeople(params[0]);
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

  

}
