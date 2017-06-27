package ua.pp.igor_m.groupslist;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class booksListAdapter extends ArrayAdapter<GLBooks>
{

  int resource;
  
  public booksListAdapter(Context _context, int _resource, List<GLBooks> _book)
  {
    super(_context, _resource, _book);
    resource = _resource;
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LinearLayout todoView;

    GLBooks item = getItem(position);

    String bookString = item.book + " (" + item.count + ")"; 
    String bookGivenString = null;   
    

    if (convertView == null) {
      todoView = new LinearLayout(getContext());
      String inflater = Context.LAYOUT_INFLATER_SERVICE;
      LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
      vi.inflate(resource, todoView, true);
    } else {
      todoView = (LinearLayout) convertView;
    }

    TextView bookView = (TextView)todoView.findViewById(R.id.bookView);
    TextView bookGivenView = (TextView)todoView.findViewById(R.id.bookCheckBox);
      
    bookView.setText(bookString);
    bookGivenView.setText(bookGivenString);

    return todoView;
  }

}
