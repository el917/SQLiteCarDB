package el917.car;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import el917.car.database.ExternalDbOpenHelper;
import el917.car.fragments.InfoListItem;
import el917.car.fragments.InfoListAll;


public class MainActivity extends ActionBarActivity implements InfoListItem.ChoiceColumnCar {
    private SQLiteDatabase database;
    private FragmentManager mFragmentManager;
    private ProgressDialog progress;
    private String carMake;
    private String carModelCondensed;
    private String carModel;
    private String carYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        OpenDataBase openDataBase = new OpenDataBase();
        openDataBase.execute();


    }


    class OpenDataBase extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage(getString(R.string.load_data_base));
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(MainActivity.this);
            database = dbOpenHelper.openDataBase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            queryMake();

        }
    }


    private void setFragmentList(ArrayList<String> arrayList, int pos) {
        String title = "";
        switch (pos) {
            case 1:
                title = getString(R.string.select_make);
                break;
            case 2:
                title = getString(R.string.selected) + " -> " + carMake;
                break;
            case 3:
                title = getString(R.string.selected) + " -> "+carMake + " -> " +carModelCondensed;
                break;

        }


        Fragment mFragment;
        mFragmentManager = getSupportFragmentManager();

        mFragment = new InfoListItem().newInstance(arrayList, pos, title);

        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }
    }

    @Override
    public void choiceColumnCar(String columCar, int page) {
        switch (page) {
            case 1:
                this.carMake = columCar;
                queryAllModel();
                break;
            case 2:
                this.carModelCondensed = columCar;
                queryYearAndModel();
                break;
            case 3:
                String[] partYear = columCar.split(" ", 2);
                this.carYear = partYear[0];
                this.carModel = partYear[1];
                queryAll();

                break;
        }


    }


    private void queryMake() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor;
        cursor = database.rawQuery(
                "SELECT  DISTINCT a.[Make] from [Auto_1941_2009] a", null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) != null) {
                    arrayList.add(cursor.getString(0));
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //  database.close();

        setFragmentList(arrayList, 1);

    }



    private void queryAllModel() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor;
        cursor = database.rawQuery(
                "SELECT a.[Model] from [Auto_1941_2009] a where a.[Make] LIKE \"" + carMake + "\" ORDER BY a.[Model]  ASC", null);
        String[] model;
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) != null) {
                    model = cursor.getString(0).split(" ");
                    arrayList.add(model[0]);
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        arrayList = new ArrayList<String>(new HashSet<String>(arrayList));
        Collections.sort(arrayList);
        setFragmentList(arrayList, 2);

    }

    private void queryYearAndModel() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor;
        cursor = database.rawQuery(
                "SELECT a.[Year], a.[Model]  from [Auto_1941_2009] a" +
                        " where a.[Make] LIKE \"" + carMake + "\" and a.[Model] LIKE \"" + carModelCondensed + "%\" ORDER BY a.[Year]  desc,  a.[Model]", null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) != null) {
                    arrayList.add((cursor.getString(0) + " " + cursor.getString(1)));
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //  database.close();

        setFragmentList(arrayList, 3);


    }

    private void queryAll() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor;
        cursor = database.rawQuery(
                "SELECT  * from [Auto_1941_2009] a where a.[Make] LIKE \"" + carMake + "\" and a.[Model] LIKE \"" + carModel + "\" and a.[Year] like \"" + carYear + "\"", null);


        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    if (cursor.getString(i) != null) {
                        // arrayList.add(cursor.getColumnName(i));
                        arrayList.add(getResources().getStringArray(R.array.name_column)[i]);
                        arrayList.add(cursor.getString(i));
                    }


                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();


        int i = 0;
        ArrayList<String> title = new ArrayList<String>();
        ArrayList<String> item = new ArrayList<String>();
        for (String word : arrayList) {
            if (i % 2 != 0) {
                title.add(word);
            } else {
                item.add(word);
            }
            i++;
        }


        setFragmentListAllInfo(title, item);


    }

    private void setFragmentListAllInfo(ArrayList<String> title, ArrayList<String> item) {
        String titleHeader = carYear + " " + carMake + " " + carModel;

        Fragment mFragment;
        mFragmentManager = getSupportFragmentManager();

        mFragment = new InfoListAll().newInstance(title, item, titleHeader);

        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }

    }

    void backButton(String title) {
        if (title.equals(getString(R.string.base_param))) {
            queryYearAndModel();
        } else if (title.equals(getString(R.string.select_car_model))) {
            queryMake();
        } else if (title.equals(getString(R.string.select_car_year))) {
            queryAllModel();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String  titleBar = item.getTitle().toString();
                backButton(titleBar);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        ActionBar actionBar = getSupportActionBar();
        String titleBar = (String) actionBar.getTitle();
        assert titleBar != null;
        if (titleBar.equals(getString(R.string.app_name))) {
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        } else {
            backButton(titleBar);
        }
    }
}



