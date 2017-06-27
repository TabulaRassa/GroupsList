package ua.pp.igor_m.groupslist;

import ua.pp.igor_m.groupslist.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEditBook extends Activity implements Constants
{
  private long rowId;

  private EditText editName;
  private EditText editCount;
  
  Cursor cursor;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT <= 11)
    {
      setContentView(R.layout.add_book_8);
      editName = (EditText) findViewById(R.id.editNameBook_2);      
      editCount = (EditText) findViewById(R.id.editCountBook_2);
      Button saveButton = (Button) findViewById(R.id.buttonAdd_2);
      saveButton.setOnClickListener(saveBookButtonClicked);
    }
    else
    {
      setContentView(R.layout.add_book);    
      editName = (EditText) findViewById(R.id.editNameBook);      
      editCount = (EditText) findViewById(R.id.editCountBook);
      Button saveButton = (Button) findViewById(R.id.buttonAdd);
      saveButton.setOnClickListener(saveBookButtonClicked);
    }

    GLDBAdapter dbAdapter = new GLDBAdapter(this);
    Bundle extras = getIntent().getExtras();
    
    if (extras != null)
    {
      rowId = extras.getLong("row_id");
      dbAdapter.open();
      cursor = dbAdapter.getOneBook(rowId);      
      cursor.moveToFirst();
      int nameIndex = cursor.getColumnIndex(_NAME);
      int countIndex = cursor.getColumnIndex(_COUNT);
      editName.setText(cursor.getString(nameIndex));
      editCount.setText(cursor.getString(countIndex));
      cursor.close();
      dbAdapter.close();     
    }   
    

  }

  OnClickListener saveBookButtonClicked = new OnClickListener()
  {

    @Override
    public void onClick(View v)
    {
      if (editName.getText().length() != 0)
      {
        AsyncTask<Object, Object, Object> saveBookTask = new AsyncTask<Object, Object, Object>()
        {

          @Override
          protected Object doInBackground(Object... params)
          {
            saveBook();
            return null;
          }
          
          @Override
          protected void onPostExecute(Object result)
          {
            finish();
          }
        };
        saveBookTask.execute((Object[]) null);
      }
      else
      {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddEditBook.this);

        builder.setTitle(R.string.errorTitle);
        builder.setMessage(R.string.errorMessage);
        builder.setPositiveButton(R.string.errorButton, null);
        builder.show();
      }
    }

  };
  
  private void saveBook()
  {
    GLDBAdapter dbAdapter = new GLDBAdapter(this);
    GLBooks _book;
    int count;    
    
    try
    {
      count = Integer.parseInt(editCount.getText().toString());
    }  
    catch (Exception e)
    {
      count = 0;
    }  
    
    _book = new GLBooks(editName.getText().toString(), count );
        
    if (getIntent().getExtras() == null)
    {
      dbAdapter.insertBook(_book);      
    } 
    else
    {
      dbAdapter.updateBook(rowId, _book);
    }
  }
}
