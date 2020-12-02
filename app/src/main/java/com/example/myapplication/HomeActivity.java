package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private Button select_bt;
    private Button create_bt;
    private SharedPreferences prefs;
    private ArrayList<Profile> list;
    private DatabaseOpener opener;
    private SQLiteDatabase db;
    private View profileListView;
    public static long currentProfileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        select_bt = findViewById(R.id.select_btn);
        create_bt = findViewById(R.id.create_btn);

        opener = new DatabaseOpener(this);
        db = opener.getWritableDatabase();


        select_bt.setOnClickListener(v -> {
            generateListOfProfilesDialog();
        });

        create_bt.setOnClickListener(v -> {
            Intent goToProfile = new Intent(HomeActivity.this, ProfileCreateActivity.class);
            startActivityForResult(goToProfile, 10);
        });

    }

    private void loadProfiles() {
        list = new ArrayList<>();
        Cursor results = opener.selectAll(db);
        int idIndex = results.getColumnIndex(DatabaseOpener.COL_ID);
        int firstNameIndex = results.getColumnIndex(DatabaseOpener.COL_FIRST_NAME);
        int lastNameIndex = results.getColumnIndex(DatabaseOpener.COL_LAST_NAME);

        while (results.moveToNext()) {
            long id = results.getLong(idIndex);
            String firstName = results.getString(firstNameIndex);
            String lastName = results.getString(lastNameIndex);
            Log.d("HomePage", "Loaded profile" + firstName + " " + lastName );
            list.add(new Profile(id, firstName, lastName));
        }
    }

    private void generateListOfProfilesDialog() {
        profileListView = getLayoutInflater().inflate(R.layout.activity_profiles_list_view,null);

        loadProfiles();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(list.size() > 0) {
            builder.setView(profileListView);
            ListView profileList = profileListView.findViewById(R.id.profileList);
            MyListAdapter adapter = new MyListAdapter(this, list);
            profileList.setAdapter(adapter);
            profileList.setOnItemClickListener( (parent,view,pos,id) -> login(list.get(pos)));//generateProfileAlert(list.get(pos)));

        } else {
            TextView noProfile = new TextView(this);
            noProfile.setText("No profiles created");
            builder.setView(noProfile);
        }
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void generateProfileAlert(Profile profile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        builder.setTitle("What would you like to do?");
        builder.setNegativeButton("DELETE", (click,arg) ->  generateDeleteAlert(profile));
        builder.setPositiveButton("LOGIN", (click,arg) ->  {login(profile);});
        dialog = builder.create();
        dialog.show();
    }

    public void generateDeleteAlert(Profile profile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm").setMessage("Are you sure you wish to delete the profile " + profile.getFirstName() + " " + profile.getLastName() + "?");
        builder.setPositiveButton("Yes", (e,i) -> {});
        builder.setNegativeButton("No",(e,i)->{});
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteProfile(Profile profile) {
        //TODO: Implement delete
    }

    private void login(Profile profile) {
        Intent goToMain = new Intent(HomeActivity.this, MainActivity.class);
        currentProfileId = profile.getId();
        goToMain.putExtra(MainActivity.PROFILE, currentProfileId);
        startActivity(goToMain);
        Toast.makeText(this, "Logged in as " + profile.getFirstName() + " " + profile.getLastName(), Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     * MyListAdapter is an inner class created to help display ListView
     */
    public static class MyListAdapter extends BaseAdapter {

        /**
         * Context for which the list is on
         */
        private Context context;

        /**
         * List of summary objects
         */
        private ArrayList<Profile> list;

        /**
         * Two Arg constructor
         */
        protected MyListAdapter(Context context, ArrayList<Profile> list) {
            setContext(context);
            setList(list);
        }

        /**
         * Returns size of list
         * @return size of list
         */
        @Override
        public int getCount() {
            return getList().size();
        }

        /**
         * Returns item at position in list
         * @param position position of item
         * @return item at position
         */
        @Override
        public Object getItem(int position) {
            return getList().get(position);
        }

        /**
         *  Override currently does nothing
         */
        @Override
        public long getItemId(int position) {
            return -1;
        }

        /**
         * Inflates the view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.profile_list_design,parent,false);

            TextView profileId= convertView.findViewById(R.id.profileId);

            profileId.setText((position+1)+")\t" +getList().get(position).getFirstName() + " " + getList().get(position).getLastName());

            return convertView;
        }

        /**
         * Mutator for context
         */
        private void setContext(Context context) { this.context = context; }

        /**
         * Accessor for context
         */
        private Context getContext() { return this.context; }

        /**
         * Mutator for list
         */
        private void setList(ArrayList<Profile> list) { this.list = list; }

        /**
         * Accessor for list
         */
        private ArrayList<Profile> getList() { return this.list; }
    }
}