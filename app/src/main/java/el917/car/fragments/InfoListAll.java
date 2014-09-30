package el917.car.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import el917.car.R;
import el917.car.adapter.MyArrayAdapter;
import el917.car.utils.Cons;

public class InfoListAll extends Fragment {
    private ListView listInfo;

    public InfoListAll newInstance(ArrayList<String> title, ArrayList<String> item, String titleHeader) {

        InfoListAll mFragment = new InfoListAll();
        Bundle mBundle = new Bundle();
        mBundle.putStringArrayList(Cons.BUNDLE_ARRAY_STRING_1, title);
        mBundle.putStringArrayList(Cons.BUNDLE_ARRAY_STRING_2, item);
        mBundle.putString(Cons.BUNDLE_STRING, titleHeader);
        mBundle.putInt(Cons.BUNDLE_INTEGER, 4);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.base_param));
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        View rootView = inflater.inflate(R.layout.info_list, container, false);
        listInfo = (ListView) rootView.findViewById(R.id.listInfo);
        TextView tvHeader = (TextView) rootView.findViewById(R.id.tvHeader);
        tvHeader.setText(getArguments().getString(Cons.BUNDLE_STRING));
        showAll();

        return rootView;
    }

    void showAll() {
        ArrayList<String> title;
        ArrayList<String> item;

        title = getArguments().getStringArrayList(Cons.BUNDLE_ARRAY_STRING_1);
        item = getArguments().getStringArrayList(Cons.BUNDLE_ARRAY_STRING_2);

        MyArrayAdapter arrayAdapter = new MyArrayAdapter(getActivity(), title, item);
        listInfo.setAdapter(arrayAdapter);

    }


}





