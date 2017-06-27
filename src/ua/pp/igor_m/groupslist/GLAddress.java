package ua.pp.igor_m.groupslist;

public class GLAddress
{

  private Integer id;
  private String address;
  private Integer idP;

  public GLAddress(int _idP, String _address)
  {
    address = _address;
    idP = _idP;
  }

  public int getId()
  {
    return id;
  }

  public String getAddress()
  {
    return address;
  }

  public int getIdP()
  {
    return idP;
  }

  @Override
  public String toString()
  {
    // TODO Auto-generated method stub
    return address;
  }

}
