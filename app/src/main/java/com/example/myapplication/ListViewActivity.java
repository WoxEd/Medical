package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    /**
     * Database opener created for the purposes of prototype 1.
     */
    private PrototypeOneDBOpener opener;

    /**
     * DB which holds our entries
     */
    private SQLiteDatabase db;

    private ArrayList<SummaryObject> list = new ArrayList<>();

    /**
     * ListView which holds the saved entries
     */
    ListView savedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        loadEntries();
        inflateList();
    }

    /**
     * Loads entries from DB into ArrayList
     */
    private void loadEntries() {

        opener = new PrototypeOneDBOpener(this);
        db = opener.getWritableDatabase();
        String[] columns = {PrototypeOneDBOpener.COL_ID, PrototypeOneDBOpener.COL_DISABILITY,
                PrototypeOneDBOpener.COL_RATING, PrototypeOneDBOpener.COL_DATE};
        Cursor results = db.query(false, PrototypeOneDBOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int idIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_ID);
        int disabilityIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_DISABILITY);
        int ratingIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_RATING);
        int dateIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_DATE);

        while (results.moveToNext()) {
            long id = results.getLong(idIndex);
            String disability = results.getString(disabilityIndex);
            int rating = results.getInt(ratingIndex);
            String date = results.getString(dateIndex);
            Log.d("LOAD", "LOADED " + disability + " " + rating + " " + date);
            list.add(new SummaryObject(disability, rating, date));
        }
    }

    private void inflateList() {

        savedList = findViewById(R.id.savedList);
        MyListAdapter adapter = new MyListAdapter(this,list);
        savedList.setAdapter(adapter);
    }

    /**
     * MyListAdapter is an inner class created to help display ListView
     */
    public class MyListAdapter extends BaseAdapter {

        private Context context;

        private ArrayList<SummaryObject> list;

        protected MyListAdapter(Context context, ArrayList<SummaryObject> list) {
            setContext(context);
            setList(list);
        }

        @Override
        public int getCount() {
            return getList().size();
        }

        @Override
        public Object getItem(int position) {
            return getList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view,parent,false);

            TextView disabilityType = convertView.findViewById(R.id.entryType);
            TextView severity = convertView.findViewById(R.id.entryRating);
            TextView date = convertView.findViewById(R.id.entryDate);


            disabilityType.setText(getList().get(position).getDisabilityType());
            severity.setText(Integer.toString(getList().get(position).getRating()));
            date.setText(getList().get(position).getDate());

            return convertView;
        }

        private void setContext(Context context) {
            this.context = context;
        }

        private Context getContext() { return this.context; }

        private void setList(ArrayList<SummaryObject> list) {
            this.list = list;
        }

        private ArrayList<SummaryObject> getList() {
            return this.list;
        }
    }
}