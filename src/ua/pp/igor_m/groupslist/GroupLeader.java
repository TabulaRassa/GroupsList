package ua.pp.igor_m.groupslist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

public class GroupLeader extends Activity implements Constants
{
  private Spinner spinnerGL;
  private Button buttonSave;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);    
    setContentView(R.layout.group_leader);
    
    spinnerGL = (Spinner) findViewById(R.id.spinnerGL);
    buttonSave = (Button) findViewById(R.id.saveGL);
    
    loadSpinnerData();
    
  }

  private void loadSpinnerData()
  {
    // TODO Auto-generated method stub
    GLDBAdapter glAdapter = new GLDBAdapter(this);
    
  }
  
}
