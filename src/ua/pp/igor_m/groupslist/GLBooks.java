package ua.pp.igor_m.groupslist;

public class GLBooks
{

  String book;
  int count;

  public String getBook()
  {
    return book;
  }

  public int getCount()
  {
    return count;
  }

  public GLBooks()
  {
    this("new book", 12);
  }
  
  public GLBooks(String _book)
  {
    this(_book, 12);
  }

  public GLBooks(String _book, int _count)
  {
    book = _book;
    count = _count;
  }

  @Override
  public String toString()
  {    
    return book + "(" + count + ")";
  }
}
