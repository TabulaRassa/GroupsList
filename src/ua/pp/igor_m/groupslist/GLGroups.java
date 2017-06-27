package ua.pp.igor_m.groupslist;

import android.content.Context;
import android.database.Cursor;

public class GLGroups
{
  private Integer id;
  private String name;
  private Integer idP;
  private Context context;
  private Cursor cursor;

  public void setId(int _id)
  {
    id = _id;
  }
  
  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }
  
  public void setLeader(long _id)
  {
    idP = (int)_id;
  }

  public int getLeader()
  {
    return idP;
  }

  public GLGroups(Context _context, int _id, String _name, int _idP)
  {
    context = _context;
    id = _id;
    name = _name;
    idP = _idP;
  }

  public GLGroups(Context _context, String _name, int _idP)
  {
    context = _context;
    id = 0;
    name = _name;
    idP = _idP;
  }

  public GLGroups(Context _context)
  {
    context = _context;
    id = 0;
    name = "Новая группа";
    idP = 0;
  }

  @Override
  public String toString()
  {
    GLPeople _people;
    try
    {
      GLDBAdapter glAdapter = new GLDBAdapter(context);
      glAdapter.open();
      cursor = glAdapter.getOnePeople(idP);
      if (cursor.moveToFirst())
      {
        _people = new GLPeople(context, cursor.getString(2));
        glAdapter.close();
        cursor.close();
        return "Группа: " + name + ", надзиратель " + _people.getName();
      }
      else
      {
        glAdapter.close();
        cursor.close();
        return "Группа: " + name;
      }      
    } catch (Exception e)
    {
      return "Группа: " + name;
    }
  }
}
