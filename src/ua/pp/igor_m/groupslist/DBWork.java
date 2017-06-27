package ua.pp.igor_m.groupslist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ua.pp.igor_m.groupslist.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class DBWork extends Activity implements Constants, OnClickListener
{
  private EditText editSave;
  private EditText editRead;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.db_work);
    
    editSave = (EditText) findViewById(R.id.editPathToSave);    
    editRead = (EditText) findViewById(R.id.editPathFromRestore);
    editSave.setText("/DownLoad/GL_copy.db");
    editRead.setText("/DownLoad/GL_copy.db");
    View buttonSave = findViewById(R.id.buttonSaveDB);
    buttonSave.setOnClickListener(this);
    View buttonRead = findViewById(R.id.buttonRestoreDB);
    buttonRead.setOnClickListener(this);
  }

  @Override
  public void onClick(View v)
  {
    // TODO Auto-generated method stub
    switch (v.getId())
    {
      case R.id.buttonSaveDB:
      {
        try
        {
          saveDB();
        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      }
      
      case R.id.buttonRestoreDB:
      {
        try
        {
          readDB();
        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
        
      }
      
    }
  }
  
  private void saveDB() throws IOException
  {
    File inFileName = this.getDatabasePath(DATABASE_NAME);
    FileInputStream fis = new FileInputStream(inFileName);
    String pathTo;
    
    pathTo = editSave.getText().toString();
    if (pathTo.length()==0)
      pathTo = "/Download/database_copy.db";
    String outFileName = Environment.getExternalStorageDirectory()+pathTo;
    OutputStream outputZ = new FileOutputStream(outFileName);
    byte[] buffer = new byte[1024];
    int length;
    while ((length = fis.read(buffer))>0){
        outputZ.write(buffer, 0, length);
    }
    
    outputZ.flush();
    outputZ.close();
    fis.close();
  }
  
  private void readDB() throws IOException
  {
    String pathFrom;
    pathFrom = editSave.getText().toString();
    if (pathFrom.length()==0)
      pathFrom = "/Download/database_copy.db";
    String inFileName = Environment.getExternalStorageDirectory()+pathFrom;
    FileInputStream fis = new FileInputStream(inFileName);
    
    File outFileName = this.getDatabasePath(DATABASE_NAME);
    OutputStream outputZ = new FileOutputStream(outFileName);
    byte[] buffer = new byte[1024];
    int length;
    while ((length = fis.read(buffer))>0){
        outputZ.write(buffer, 0, length);
    }
    
    outputZ.flush();
    outputZ.close();
    fis.close();
  }
}
