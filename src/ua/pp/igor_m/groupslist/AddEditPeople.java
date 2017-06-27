package ua.pp.igor_m.groupslist;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEditPeople extends Activity implements Constants
{
  private long rowId;
  private int rowIdG;

  private EditText editName;
  private EditText editAddress;
  private EditText editPhone;
  
  private GLPeople _people;
  private GLPhone _phone;

  Cursor cursor;
  Cursor cursorPh;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    if (Build.VERSION.SDK_INT <= 11)
    {
      setContentView(R.layout.add_people_8);
      editName = (EditText) findViewById(R.id.editName_2);
      editAddress = (EditText) findViewById(R.id.editAddress_2);
      editPhone = (EditText) findViewById(R.id.editPhone_2);
      Button saveButton = (Button) findViewById(R.id.buttonAdd_2);
      saveButton.setOnClickListener(savePeopleButtonClicked);
    } else
    {
      setContentView(R.layout.add_people);
      editName = (EditText) findViewById(R.id.editName);
      editAddress = (EditText) findViewById(R.id.editAddress);
      editPhone = (EditText) findViewById(R.id.editPhone);
      Button saveButton = (Button) findViewById(R.id.buttonAdd);
      saveButton.setOnClickListener(savePeopleButtonClicked);
    }

    GLDBAdapter dbAdapter = new GLDBAdapter(this);
    Bundle extras = getIntent().getExtras();

    if (extras != null)
    {
      rowId = extras.getLong("row_id");
      rowIdG = (int)extras.getLong("row_idG");
            
      if (rowId != 0)
      {
        dbAdapter.open();
        cursor = dbAdapter.getOnePeople(rowId);
        cursor.moveToFirst();
        editName.setText(cursor.getString(cursor.getColumnIndex(_NAME)));
        cursorPh = dbAdapter.getPhonesByPeople(rowId);
        cursorPh.moveToFirst();
        editPhone.setText(cursorPh.getString(cursorPh.getColumnIndex(_PHONE)));
        //editAddress.setText(cursor.getString(cursor.getColumnIndex(_ADDRESS)));
        cursor.close();
        dbAdapter.close();
      }
    }

  }

  OnClickListener savePeopleButtonClicked = new OnClickListener()
  {

    @Override
    public void onClick(View v)
    {
      if (editName.getText().length() != 0)
      {
        AsyncTask<Object, Object, Object> savePeopleTask = new AsyncTask<Object, Object, Object>()
        {

          @Override
          protected Object doInBackground(Object... params)
          {
            savePeople();
            return null;
          }

          @Override
          protected void onPostExecute(Object result)
          {
            finish();
          }
        };
        savePeopleTask.execute((Object[]) null);
      } else
      {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddEditPeople.this);

        builder.setTitle(R.string.errorTitle);
        builder.setMessage(R.string.errorMessage);
        builder.setPositiveButton(R.string.errorButton, null);
        builder.show();
      }
    }

  };

  private void savePeople()
  {
    GLDBAdapter dbAdapter = new GLDBAdapter(this);

    //Log.d(_TAG, "rowIdG " + rowIdG);
    if (rowIdG==0)
      _people = new GLPeople(AddEditPeople.this, editName.getText().toString());
    else
      _people = new GLPeople(AddEditPeople.this, editName.getText().toString(), rowIdG);
    //Log.d(_TAG,  "name " + _people.getName() + ", idG " + _people.getIdGroup() + ", rowId " + rowId);
    
    // need add phone and address!!!
    if (rowId==0)
    {
      dbAdapter.insertPeople(_people);
    } else
    {
      _phone = new GLPhone((int)rowId, editPhone.getText().toString());
      dbAdapter.updatePeople(rowId, _people);
    }
    Log.d(_TAG, "succes");
  }
}
