package ua.pp.igor_m.groupslist;

public class GLPhone
{
  private Integer id;
  private String phone;
  private Integer idP;
  
  public GLPhone(int _idP, String _phone)
  {
    phone = _phone;
    idP = _idP;
  }
  
  public int getId()
  {
    return id;
  }

  public String getPhone()
  {
    return phone;
  }
  
  public int getIdP()
  {
    return idP;
  }

  @Override
  public String toString()
  {
    // TODO Auto-generated method stub
    return phone;
  }
}
