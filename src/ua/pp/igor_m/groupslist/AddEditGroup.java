package ua.pp.igor_m.groupslist;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AddEditGroup extends AppCompatActivity implements Constants, OnClickListener
{
  private ListView lvGroupMembers;
  private TextView tvGL;
  private EditText etGroupName;
  private Button btAddPeople;
  private Button btAddFromPhoneBook;
  private Button btAddGL;
  private Button btMove;
  private ArrayAdapter<GLPeople> peopleAdapter;
  private ArrayList<GLPeople> peopleList;
  private GLDBAdapter glAdapter;
  private long rowId;
  private int idP;
  private GLPeople _people;
  private GLGroups _group;
  private Cursor cursor;
  private Cursor cursor2;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.create_group);

    btAddPeople = (Button) findViewById(R.id.btAddPeopleToGroup);
    btAddPeople.setOnClickListener(this);
    btAddFromPhoneBook = (Button) findViewById(R.id.btAddFromPhoneBook);
    btAddFromPhoneBook.setOnClickListener(this);
    btAddGL = (Button) findViewById(R.id.btAddGL);
    btAddGL.setOnClickListener(this);
    btMove = (Button) findViewById(R.id.btMove);
    btMove.setOnClickListener(this);

    tvGL = (TextView) findViewById(R.id.tvGL);
    etGroupName = (EditText) findViewById(R.id.editGroupName);

    peopleList = new ArrayList<GLPeople>();
    peopleAdapter = new ArrayAdapter<GLPeople>(this, R.layout.item, R.id.item, peopleList);

    lvGroupMembers = (ListView) findViewById(R.id.lvMembers);
    lvGroupMembers.setAdapter(peopleAdapter);
    
    registerForContextMenu(lvGroupMembers);

    glAdapter = new GLDBAdapter(this);
    glAdapter.open();
    Bundle extras = getIntent().getExtras();
    if (extras != null)
    {
      rowId = extras.getLong("row_id");
      cursor = glAdapter.getOneGroup(rowId);
      if (cursor.moveToFirst())
      {
        _group = new GLGroups(AddEditGroup.this, cursor.getInt(cursor.getColumnIndex(_ID)), cursor.getString(cursor.getColumnIndex(_NAME)), cursor.getInt(cursor
            .getColumnIndex(_ID_P)));
      }
      cursor.close();

    } else
    {
      _group = new GLGroups(AddEditGroup.this);
      rowId = glAdapter.insertGroup(_group);
      _group.setId((int) rowId);
    }
    etGroupName.setText(_group.getName().toString());    

    glAdapter.close();

  }

  private void fillList()
  {
    peopleList.clear();
    glAdapter.open();
    cursor = glAdapter.getAllPeopleOfGroup(rowId);
    if (cursor.moveToFirst())
      do
      {
        _people = new GLPeople(AddEditGroup.this, cursor.getInt(cursor.getColumnIndex(_ID)), cursor.getString(cursor.getColumnIndex(_NAME)), cursor.getInt(cursor.getColumnIndex(_ID_G)));
        peopleList.add(_people);
      } while (cursor.moveToNext());
    cursor.close();

    if (_group.getLeader() != 0)
    {
      idP = _group.getLeader();
      cursor2 = glAdapter.getOnePeople(idP);
      if (cursor2.moveToFirst())
      {
        //Log.d(_TAG, "leader name " + cursor2.getString(cursor2.getColumnIndex(_NAME)).toString());
        tvGL.setText(cursor2.getString(cursor2.getColumnIndex(_NAME)).toString());
      }
      cursor2.close();
    }
    else
    {
      tvGL.setText("");
    }
    glAdapter.close();
    peopleAdapter.notifyDataSetChanged();

  }

  @Override
  public void onClick(View v)
  {

    switch (v.getId())
    {
    case R.id.btAddPeopleToGroup:
    {
      Intent addNewPeople = new Intent(this, AddEditPeople.class);
      addNewPeople.putExtra("row_idG", rowId);
      startActivity(addNewPeople);
      break;
    }
    case R.id.btAddGL:
    {
      Intent groupLeader = new Intent(this, GroupLeader.class);
      groupLeader.putExtra("row_idG", rowId);
      startActivity(groupLeader);
      break;
    }
    case R.id.btAddFromPhoneBook:
    {
      Intent addFromBook = new Intent(this, GetFromPhoneBook.class);
      addFromBook.putExtra("row_idG", rowId);
      startActivity(addFromBook);
      break;
    }
    case R.id.btMove:
    {
      Intent movePeople = new Intent(this, MovePeople.class);
      movePeople.putExtra("row_idG", rowId);
      startActivity(movePeople);
      break;
    }
    }
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    fillList();    
  }  

  @Override
  protected void onPause()
  {
    _group = new GLGroups(AddEditGroup.this, etGroupName.getText().toString(), idP);
    glAdapter.updateGroup(rowId, _group);
    super.onStop();
  }
  
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
  {
    menu.add(0, 1, 0, R.string.del);
    menu.add(0, 2, 0, R.string.delFromGroup);
    menu.add(0, 3, 0, R.string.setLeader);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case 1:
    {
      AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
      _people = peopleAdapter.getItem(acmi.position);
      rowId = _people.getId();
      deletePeople();
      break;
    }
    case 2:
    {
      AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
      _people = peopleAdapter.getItem(acmi.position);
      idP = _people.getId(); 
      deletePeopleFromGroup();
      break;
    }
    case 3:
    {
      AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
      _people = peopleAdapter.getItem(acmi.position);
      idP = _people.getId();
      setLeader();
      break;
    }
    }
    return super.onContextItemSelected(item);
  }
  
  private void setLeader()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(AddEditGroup.this);
    builder.setTitle(R.string.confirmTitle);
    builder.setMessage(R.string.setLeader);
    builder.setPositiveButton(R.string.saveCapture, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int button)
      {
        AsyncTask<Long, Object, Object> setLeaderTask = new AsyncTask<Long, Object, Object>()
        {
          @Override
          protected Object doInBackground(Long... params)
          {
            _group.setLeader(params[1]);
            return null;
          }

          @Override
          protected void onPostExecute(Object result)
          {
            onResume();
          }
        };

        setLeaderTask.execute(new Long[] { rowId, (long) idP });
      }
    });

    builder.setNegativeButton(R.string.button_cancel, null);
    builder.show();    
  }

  private void deletePeople()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(AddEditGroup.this);
    builder.setTitle(R.string.confirmTitle);
    builder.setMessage(R.string.confirmMessage);
    builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int button)
      {
        final GLDBAdapter glAdapter = new GLDBAdapter(AddEditGroup.this);

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

        deleteTask.execute(new Long[] { (long) idP });
      }
    });

    builder.setNegativeButton(R.string.button_cancel, null);
    builder.show();
  }
  
  private void deletePeopleFromGroup()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(AddEditGroup.this);
    builder.setTitle(R.string.confirmTitle);
    builder.setMessage(R.string.delFromGroup);
    builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int button)
      {
        final GLDBAdapter glAdapter = new GLDBAdapter(AddEditGroup.this);
        
        glAdapter.setPeopleGroup(idP, 0);
        if (idP == _group.getLeader())
        {
          _group.setLeader(0);
        }
        fillList();

        /*AsyncTask<Long, Object, Object> deleteFGTask = new AsyncTask<Long, Object, Object>()
        {
          @Override
          protected Object doInBackground(Long... params)
          {
            glAdapter.setPeopleGroup(params[0], 0);
            if (params[0] == _group.getLeader())
            {
              _group.setLeader(0);
            }
            return null;
          }

          @Override
          protected void onPostExecute(Object result)
          {
            onResume();
          }
        };

        deleteFGTask.execute(new Long[] { (long) idP });*/
      }
    });

    builder.setNegativeButton(R.string.button_cancel, null);
    builder.show();
  }
}
