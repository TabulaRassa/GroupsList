package ua.pp.igor_m.groupslist;

import android.content.Context;
import android.database.Cursor;

public class GLPeople implements Constants
{
  private String name;
  private Integer idG;
  private Integer id;
  
  private GLGroups _group;
  private Context context;
  private Cursor cursor;

  public String getName()
  {
    return name;
  }

  public int getIdGroup()
  {
    return idG;
  }

  public int getId()
  {
    return id;
  }

  public GLPeople(Context _context, int _id, String _name, int _idG)
  {
    context = _context;
    id = _id;
    name = _name;
    idG = _idG;
  }

  public GLPeople(Context _context, String _name,  int _idG)
  {
    this(_context, 0, _name, _idG);
  }

  public GLPeople(Context _context, String _name)
  {
    this(_context, 0, _name, 0);
  }

  @Override
  public String toString()
  {
    try 
    {
      GLDBAdapter glAdapter = new GLDBAdapter(context);
      glAdapter.open();
      cursor = glAdapter.getOneGroup(idG);
      if (cursor.moveToFirst())
      {
        _group = new GLGroups(context, cursor.getString(1), cursor.getInt(2));
        glAdapter.close();
        cursor.close();
        return name + ", группа: " + _group.getName(); 
      }
      else
      {
        glAdapter.close();
        cursor.close();
        return name;
      }      
    } catch(Exception e)
    {
      return name;
    }    
  }

}
