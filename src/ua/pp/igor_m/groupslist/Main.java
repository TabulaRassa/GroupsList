package ua.pp.igor_m.groupslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Main extends AppCompatActivity implements Constants, OnClickListener
{
  Intent intent;
  Animation anim = null;
  Animation anim1 = null;
  Animation anim2 = null;
  Animation anim3 = null;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    View buttonGroups = findViewById(R.id.buttonGroups);
    buttonGroups.setOnClickListener(this);
    View buttonExit = findViewById(R.id.buttonExit);
    buttonExit.setOnClickListener(this);
    View buttonPeople = findViewById(R.id.buttonPeople);
    buttonPeople.setOnClickListener(this);
    View buttonBooks = findViewById(R.id.buttonBooks);
    buttonBooks.setOnClickListener(this);

    anim = AnimationUtils.loadAnimation(this, R.anim.b_alfa);
    anim1 = AnimationUtils.loadAnimation(this, R.anim.b_alfa_1);
    anim2 = AnimationUtils.loadAnimation(this, R.anim.b_alfa_2);
    anim3 = AnimationUtils.loadAnimation(this, R.anim.b_alfa_3);
    buttonGroups.startAnimation(anim);
    buttonPeople.startAnimation(anim1);
    buttonBooks.startAnimation(anim2);
    buttonExit.startAnimation(anim3);
  }

  @Override
  public void onClick(View v)
  {
    // TODO Auto-generated method stub
    switch (v.getId())
    {
    case R.id.buttonExit:
      finish();
      break;
    case R.id.buttonPeople:
      intent = new Intent(this, PeopleList.class);
      startActivity(intent);
      break;
    case R.id.buttonBooks:
      intent = new Intent(this, BooksList.class);
      startActivity(intent);
      break;
    case R.id.buttonGroups:
      intent = new Intent(this, GroupsList.class);
      startActivity(intent);
      break;
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.groups_list, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    switch (item.getItemId())
    {
    case R.id.db_h:
    {
      Intent i = new Intent(this, DBWork.class);
      startActivity(i);
      break;
    }
    }

    return super.onOptionsItemSelected(item);
  }
}
