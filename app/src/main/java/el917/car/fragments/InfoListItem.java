package el917.car.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import el917.car.R;
import el917.car.utils.Cons;


public class InfoListItem extends Fragment implements AdapterView.OnItemClickListener {
    private int page;
    private ListView listInfo;
    private SharedPreferences sharedPreferences;

    public InfoListItem newInstance(ArrayList<String> arrayList, int page, String title) {

        InfoListItem mFragment = new InfoListItem();
        Bundle mBundle = new Bundle();
        mBundle.putStringArrayList(Cons.BUNDLE_ARRAY_STRING_1, arrayList);
        mBundle.putString(Cons.BUNDLE_STRING, title);
        mBundle.putInt(Cons.BUNDLE_INTEGER, page);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    public interface ChoiceColumnCar {
        public void choiceColumnCar(String carMake, int page);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {
            choiceColumnCar = (ChoiceColumnCar) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    private ChoiceColumnCar choiceColumnCar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        page = getArguments().getInt(Cons.BUNDLE_INTEGER);
        View rootView = inflater.inflate(R.layout.info_list, container, false);
        listInfo = (ListView) rootView.findViewById(R.id.listInfo);
        TextView tvHeader = (TextView) rootView.findViewById(R.id.tvHeader);
        tvHeader.setText(getArguments().getString(Cons.BUNDLE_STRING));
        listInfo.setOnItemClickListener(this);
        showMake();
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        switch (page) {
            case 1:
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
                ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                ((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
                listInfo.setSelection(loadPosition(page));

                break;
            case 2:
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.select_car_model));
                listInfo.setSelection(loadPosition(page));
                break;
            case 3:
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.select_car_year));

                listInfo.setSelection(loadPosition(page));
                break;

        }


        return rootView;
    }

    private void showMake() {
        ArrayList<String> arrayList;

        arrayList = getArguments().getStringArrayList(Cons.BUNDLE_ARRAY_STRING_1);

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listInfo.setAdapter(arrayAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (page) {
            case 1:
                choiceColumnCar.choiceColumnCar(((TextView) view).getText().toString(), page);
                saveTextInFile(listInfo.getFirstVisiblePosition(), page);
                break;
            case 2:
                choiceColumnCar.choiceColumnCar(((TextView) view).getText().toString(), page);
                saveTextInFile(listInfo.getFirstVisiblePosition(), page);
                break;
            case 3:
                choiceColumnCar.choiceColumnCar(((TextView) view).getText().toString(), page);
                saveTextInFile(listInfo.getFirstVisiblePosition(), page);
                break;
            case 4:
                // choiceColumnCar.choiceColumnCar(((TextView) view).getText().toString(), page);
                break;

        }


    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void saveTextInFile(int position, int page) {
        sharedPreferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        switch (page) {
            case 1:
                editor.putInt("SAVED_POSITION" + page, position);
                editor.apply();
                break;
            case 2:
                editor.putInt("SAVED_POSITION" + page, position);
                editor.apply();
                break;
            case 3:
                editor.putInt("SAVED_POSITION" + page, position);
                editor.apply();
                break;


        }


    }

    private int loadPosition(int page) {
        sharedPreferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        int position = 0;
        if (sharedPreferences.contains("SAVED_POSITION" + page)) {
            switch (page) {
                case 1:
                    position = sharedPreferences.getInt("SAVED_POSITION" + page, position);
                    break;
                case 2:
                    position = sharedPreferences.getInt("SAVED_POSITION" + page, position);
                    break;
                case 3:
                    position = sharedPreferences.getInt("SAVED_POSITION" + page, position);
                    break;
            }


        }
        return position;

    }
}