package ua.pp.igor_m.groupslist;

import java.util.ArrayList;

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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GroupsList extends AppCompatActivity implements Constants
{
  private ListView lvGroups;
  private GLDBAdapter glAdapter;
  private Cursor cursor;
  private ArrayList<GLGroups> alGroups;
  private ArrayAdapter<GLGroups> aaGroups;
  private GLGroups _group;
  private long rowId;
  long rId;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.groups);

    alGroups = new ArrayList<GLGroups>();
    aaGroups = new ArrayAdapter<GLGroups>(this, R.layout.item, R.id.item, alGroups);
    lvGroups = (ListView) findViewById(R.id.listViewGroups);
    lvGroups.setAdapter(aaGroups);

    glAdapter = new GLDBAdapter(this);

    registerForContextMenu(lvGroups);
    lvGroups.setOnItemClickListener(itemClick);  
    
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    fillList();
  }

  public boolean onCreateOptionsMenu(Menu menu)
  {
    menu.add(0, 1, 0, "добавить");
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

  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    switch (id)
    {
    case 1:
    {
      Intent addNewGroup = new Intent(this, AddEditGroup.class);
      startActivity(addNewGroup);
      break;
    }
    }
    return super.onOptionsItemSelected(item);
  }

  private void fillList()
  {
    alGroups.clear();
    glAdapter.open();
    cursor = glAdapter.getAllGroups();
    if (cursor.moveToFirst())
      do
      {
        _group = new GLGroups(GroupsList.this, cursor.getInt(cursor.getColumnIndex(_ID)), cursor.getString(cursor.getColumnIndex(_NAME)), cursor.getInt(cursor
            .getColumnIndex(_ID_P)));
        aaGroups.add(_group);
      } while (cursor.moveToNext());
    cursor.close();
    glAdapter.close();
    aaGroups.notifyDataSetChanged();
    
    if (aaGroups.getCount() == 0)
    {
      AlertDialog.Builder builder = new AlertDialog.Builder(GroupsList.this);
      builder.setTitle(R.string.clear_list);
      builder.setMessage(R.string.add_new_record);
      builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int button)
        {
          Intent addNewGroup = new Intent(GroupsList.this, AddEditGroup.class);
          startActivity(addNewGroup);
        }
      });

      builder.setNegativeButton(R.string.button_cancel, null);
      builder.show();
    }
  }

  OnItemClickListener itemClick = new OnItemClickListener()
  {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      _group = (GLGroups) parent.getItemAtPosition(position);
      Intent i = new Intent(GroupsList.this, AddEditGroup.class);
      i.putExtra("row_id", (long) _group.getId());
      startActivity(i);
    }

  };

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
  {
    menu.add(0, 1, 0, R.string.del);
    menu.add(0, 2, 0, "Удалить все");
  }

  @Override
  public boolean onContextItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case 1:
    {
      AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
      _group = aaGroups.getItem(acmi.position);
      rowId = _group.getId();
      delGroup(-1);
      break;
    }
    case 2:
    {
      for (int i = 0; i < aaGroups.getCount(); i++)
      {
        _group = aaGroups.getItem(i);
        rowId = _group.getId();
        delGroup(rowId);
      }
      onResume();
      break;
    }
    }
    return super.onContextItemSelected(item);
  }

  private void delGroup(long rowId2)
  {
    if (rowId2 == -1)
    {
      AlertDialog.Builder builder = new AlertDialog.Builder(GroupsList.this);
      builder.setTitle(R.string.confirmTitle);
      builder.setMessage(R.string.confirmMessage);
      builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int button)
        {
          AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>()
          {
            @Override
            protected Object doInBackground(Long... params)
            {
              rId = params[0];
              glAdapter.deleteGroup((int) rId);
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
    } else
    {
      glAdapter.deleteGroup((int) rowId2);
    }
    aaGroups.notifyDataSetChanged();
  }
}
