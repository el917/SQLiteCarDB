package el917.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import el917.car.R;

public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Context context;

    private final ArrayList<String> title;
    private final ArrayList<String> item;


    public MyArrayAdapter(Context context, ArrayList<String> title, ArrayList<String> item) {
        super(context, R.layout.item_info_all, title);
        this.context = context;
        this.title = title;
        this.item = item;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_info_all, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.tv1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.tv2);


        String mTitle = title.get(position);

        textView1.setText(item.get(position));

        textView2.setText(mTitle);


        return rowView;
    }
}
