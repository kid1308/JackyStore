package com.thangtd.jackystore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.thangtd.jackystore.commons.MyApplication;
import com.thangtd.jackystore.commons.MySQLiteOpenHelper;
import com.thangtd.jackystore.commons.Utility;
import com.thangtd.jackystore.objects.BookObj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class BookHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int REQUEST_CODE = 1;
    public static final String SHOWCASE_ID = "HOME_SHOWCASE";

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //creating fragment object
    Fragment mFragment = null;

    private int mKind;
    private MyApplication mApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = findViewById(R.id.container);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generalIntent = new Intent(getApplicationContext(), BookEdit.class);
                generalIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(generalIntent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mApp = ((MyApplication) getApplicationContext());
        loadSavedPreferences();

//        Log.d("HASH_KEY", getApplicationHashKey(getApplicationContext()));
    }

    protected void onResume() {
        super.onResume();

        Intent intent = this.getIntent();
        mKind = intent.getIntExtra("BOOK_KIND", 0);
        if (mKind == 0) {
            displaySelectedScreen(R.id.nav_books_vn);
        } else if (mKind == 1) {
            displaySelectedScreen(R.id.nav_books_foreign);
        } else if (mKind == 2) {
            displaySelectedScreen(R.id.nav_books_manga);
        }

//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        // Set up the ViewPager with the sections adapter.
//        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public static String getApplicationHashKey(Context ctx) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sig = Base64.encodeToString(md.digest(), Base64.DEFAULT).trim();
                if (sig.trim().length() > 0) {
                    return sig;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        String name = sharedPreferences.getString("storedName", "");
        if (name.equals("")) {
            showFirstTimeTutorial();
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(name + getString(R.string.app_name).substring(5));
            mApp.setName(name);
        }
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void showFirstTimeTutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);

        FloatingActionButton mFabButton = findViewById(R.id.fab);
        sequence.addSequenceItem(mFabButton,
                getString(R.string.showcase_add_button), getString(R.string.showcase_begin))
                .setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
                    @Override
                    public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                        showFirstTimeDialog();
                    }
                });

        sequence.start();
    }

    public void showFirstTimeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.first_time_dialog, null);
        final EditText edt = dialogView.findViewById(R.id.NAME_STORE);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        dialogBuilder.setTitle(getString(R.string.dialog_welcome_title));
        dialogBuilder.setMessage(getString(R.string.dialog_welcome_message));
        dialogBuilder.setPositiveButton(getString(R.string.text_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String name;
                if (!edt.getText().toString().trim().equals("")) {
                    name = edt.getText().toString().trim();
                } else {
                    name = "Jacky";
                }

                savePreferences("storedName", name);
                mApp.setName(name);
                Objects.requireNonNull(getSupportActionBar()).setTitle(name + getString(R.string.app_name).substring(5));
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displaySelectedScreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());

        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_books_vn:
                mFragment = PlaceholderFragment.newInstance(0, 0);
                break;
            case R.id.nav_books_foreign:
                mFragment = PlaceholderFragment.newInstance(1, 1);
                break;
            case R.id.nav_books_manga:
                mFragment = PlaceholderFragment.newInstance(2, 2);
                break;
            case R.id.action_statistics:
                Intent intent = new Intent(getApplicationContext(), Statistics.class);
                startActivity(intent);
                break;
            case R.id.action_import_list:
                importFrom();
                break;
            case R.id.action_export_list:
                AlertDialog.Builder alert = new AlertDialog.Builder(BookHome.this);
                alert.setTitle(getResources().getString(R.string.dialog_export_title));
                alert.setMessage(getResources().getString(R.string.dialog_export_message));

                // Set an EditText view to get user input
                final EditText input = new EditText(BookHome.this);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(340)});
                alert.setView(input);

                alert.setPositiveButton(getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String fileName = input.getText().toString();
                        exportTo(fileName);
                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();

                break;
            case R.id.action_about:
                Intent intentAbout = new Intent(getApplicationContext(), About.class);
                startActivity(intentAbout);
                break;
        }

        //replacing the fragment
        if (mFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, mFragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_alert_circle_outline_black_48dp).setTitle(getString(R.string.dialog_exit_title))
                    .setMessage(getString(R.string.confirm_exit))
                    .setPositiveButton(getString(R.string.text_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            System.exit(0);
                        }
                    }).setNegativeButton(getString(R.string.text_no), null).show();
        }
    }

    private void exportTo(String filename) {
        MySQLiteOpenHelper mDataHelper = new MySQLiteOpenHelper(getApplicationContext());
        String sql = "select " + MySQLiteOpenHelper.BOOK_ISBN + ", " + MySQLiteOpenHelper.BOOK_TITLE + ", "
                + MySQLiteOpenHelper.BOOK_AUTHOR + ", " + MySQLiteOpenHelper.BOOK_TRANSLATOR + ", "
                + MySQLiteOpenHelper.BOOK_PUBLISHER + ", " + MySQLiteOpenHelper.BOOK_PAGES + ", "
                + MySQLiteOpenHelper.BOOK_KIND + ", " + MySQLiteOpenHelper.BOOK_RELEASED_DATE + ", "
                + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", " + MySQLiteOpenHelper.BOOK_FINISHED_DATE
                + " from " + MySQLiteOpenHelper.TB_BOOK_DETAILS;

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        filename = filename + ".csv";
        File saveFile = new File(exportDir, filename);
        try {
            saveFile.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(saveFile));
            SQLiteDatabase sql_db = mDataHelper.getReadableDatabase();//here create a method ,and return SQLiteDatabaseObject.getReadableDatabase();
            Cursor curCSV = sql_db.rawQuery(sql, null);
            csvWrite.writeNext(curCSV.getColumnNames());

            while (curCSV.moveToNext()) {
                //Which column you want to export you can add over here...
                String[] arrStr = {curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_ISBN)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_TITLE)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_AUTHOR)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_TRANSLATOR)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_PUBLISHER)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_PAGES)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_KIND)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_RELEASED_DATE)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_PURCHASED_DATE)),
                        curCSV.getString(curCSV.getColumnIndex(MySQLiteOpenHelper.BOOK_FINISHED_DATE))};
                csvWrite.writeNext(arrStr);
            }

            // Open exported file
//            try {
//                File csvfile = new File(Environment.getExternalStorageDirectory() + "/JackyStore.csv");
//                CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));
//                String[] nextLine;
//                while ((nextLine = reader.readNext()) != null) {
//                    // nextLine[] is an array of values from the line
//                    System.out.println(nextLine[0] + nextLine[1] + "etc...");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
//            }

            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("Error:", sqlEx.getMessage(), sqlEx);
        } finally {
            Toast.makeText(getApplicationContext(), "Export completed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void importFrom() {
        Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        fileintent.setType("gagt/sdf");
        try {
            startActivityForResult(fileintent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No activity can handle picking a file. Showing alternatives.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (requestCode) {
            case REQUEST_CODE:
                MySQLiteOpenHelper mDataHelper = new MySQLiteOpenHelper(getApplicationContext());
                SQLiteDatabase db = mDataHelper.getWritableDatabase();
                String tableName = MySQLiteOpenHelper.TB_BOOK_DETAILS;
                db.execSQL("delete from " + tableName);

                if (Utility.checkPermission(this)) {
                    if (data.getData() != null) {
                        String filepath = data.getData().getPath();
                        try {
                            if (resultCode == RESULT_OK) {
                                try {
                                    FileReader file = new FileReader(filepath);
                                    BufferedReader buffer = new BufferedReader(file);
                                    String line;
                                    db.beginTransaction();

                                    while ((line = buffer.readLine()) != null) {
                                        String[] str = line.split(",", 10);  // defining 10 columns with null or blank field //values acceptance

                                        String isbn = str[0].substring(1, str[0].length() - 1);
                                        String title = str[1].substring(1, str[1].length() - 1);
                                        String author = str[2].substring(1, str[2].length() - 1);
                                        String translator = str[3].substring(1, str[3].length() - 1);
                                        String publisher = str[4].substring(1, str[4].length() - 1);
                                        String pages = str[5].substring(1, str[5].length() - 1);
                                        String kind = str[6].substring(1, str[6].length() - 1);
                                        String released_date = str[7].substring(1, str[7].length() - 1);
                                        String purchased_date = str[8].substring(1, str[8].length() - 1);
                                        String finished_date = str[9].substring(1, str[9].length() - 1);

                                        try {
                                            int numPages = Integer.parseInt(pages);
                                            int numKind = Integer.parseInt(kind);

                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault());
                                            Date currentTime = Calendar.getInstance().getTime();
                                            String mTime = sdf.format(currentTime);

                                            BookObj mBook = new BookObj(isbn, title, author, translator, publisher, numPages,
                                                    released_date, mTime, purchased_date, finished_date, "", "", numKind, null);
                                            mDataHelper.INSERT_BOOK(mBook);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Toast.makeText(getApplicationContext(), "Import completed.", Toast.LENGTH_SHORT).show();
                                    db.setTransactionSuccessful();
                                    db.endTransaction();
                                } catch (IOException e) {
                                    if (db.inTransaction())
                                        db.endTransaction();
                                    Dialog d = new Dialog(this);
                                    d.setTitle(e.getMessage() + "first");
                                    d.show();
                                    // db.endTransaction();
                                }
                            } else {
                                if (db.inTransaction())
                                    db.endTransaction();
                                Dialog d = new Dialog(this);
                                d.setTitle("Only CSV files allowed");
                                d.show();
                            }
                        } catch (Exception ex) {
                            if (db.inTransaction())
                                db.endTransaction();
                            Dialog d = new Dialog(this);
                            d.setTitle(ex.getMessage() + "second");
                            d.show();
                            // db.endTransaction();
                        }
                    }
                }
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_KIND_NUMBER = "kind_number";

        private ListView lvBooks;
        private TextView tvSummary;
        private SearchView searchView = null;
        private SearchView.OnQueryTextListener queryTextListener;

        private int mSection;
        private int mKindFragment;

        private MySQLiteOpenHelper mDataHelper;
        private BookListAdapter mAdapter;
        private Cursor mCursor;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, int kindBook) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_KIND_NUMBER, kindBook);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mSection = getArguments().getInt(ARG_SECTION_NUMBER);
                mKindFragment = getArguments().getInt(ARG_KIND_NUMBER);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_book_home, container, false);
            tvSummary = rootView.findViewById(R.id.tvSummary);
            lvBooks = rootView.findViewById(android.R.id.list);

            mDataHelper = new MySQLiteOpenHelper(getActivity().getApplicationContext());
            String sql = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where "
                    + MySQLiteOpenHelper.BOOK_KIND + " = " + mKindFragment
                    + " order by " + MySQLiteOpenHelper.BOOK_TITLE;

            mCursor = mDataHelper.SELECTSQL(sql);
            mAdapter = new BookListAdapter(getActivity().getApplicationContext(), mCursor);
            lvBooks.setAdapter(mAdapter);

            lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String mISBN = (String) ((TextView) view.findViewById(R.id.item_ISBN)).getText();

                    Intent intent = new Intent(getActivity().getApplicationContext(), BookDetails.class);
                    intent.putExtra("ISBN", mISBN);
                    intent.putExtra("BOOK_KIND", mKindFragment);
                    startActivity(intent);
                }
            });

            lvBooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final String mFinished = (String) ((TextView) view.findViewById(R.id.item_FINISHED_DATE)).getText();

                    if (mFinished.equals("")) {
                        final String mISBN = (String) ((TextView) view.findViewById(R.id.item_ISBN)).getText();
                        final String mTitle = (String) ((TextView) view.findViewById(R.id.item_TITLE)).getText();

                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle(getResources().getString(R.string.dialog_header_confirm));
                        alert.setMessage(getResources().getString(R.string.confirm_finish) + ' ' + mTitle + " ?");

                        alert.setPositiveButton(getResources().getString(R.string.text_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String finishDate = Utility.getCurrentDate("dd/MM/yyyy");
                                BookObj mBook = new BookObj(finishDate);
                                mDataHelper.UPDATE_FINISHED_DATE_BY_ISBN(mBook, mISBN);

                                // Refresh listview
                                String sql = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where "
                                        + MySQLiteOpenHelper.BOOK_KIND + " = " + mKindFragment;
                                mCursor = mDataHelper.SELECTSQL(sql);
                                mAdapter = new BookListAdapter(getActivity().getApplicationContext(), mCursor);
                                lvBooks.setAdapter(mAdapter);
                            }
                        });

                        alert.setNegativeButton(getResources().getString(R.string.text_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });

                        alert.show();
                    }

                    return true;
                }
            });

            String mSum = mCursor.getCount() + " " + getString(R.string.str_books_in_store);
            tvSummary.setText(mSum);
            setHasOptionsMenu(true);

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_book_home, menu);

            final MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            if (searchItem != null) {
                searchView = (SearchView) searchItem.getActionView();
            }
            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

                queryTextListener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // re-query list
                        mDataHelper = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                        String sql = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where "
                                + MySQLiteOpenHelper.BOOK_KIND + " = " + mKindFragment + " and "
                                + MySQLiteOpenHelper.BOOK_TITLE + " like '%" + newText + "%'";

                        mCursor = mDataHelper.SELECTSQL(sql);
                        mAdapter = new BookListAdapter(getActivity().getApplicationContext(), mCursor);
                        lvBooks.setAdapter(mAdapter);

                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return true;
                    }
                };
                searchView.setOnQueryTextListener(queryTextListener);
            }

            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_search:
                    // Not implemented here
                    return false;
                default:
                    break;
            }
            searchView.setOnQueryTextListener(queryTextListener);
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position, mKind);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
