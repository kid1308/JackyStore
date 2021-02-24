package com.thangtd.jackystore;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thangtd.jackystore.commons.MySQLiteOpenHelper;
import com.thangtd.jackystore.objects.BookObj;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.thangtd.jackystore.commons.Constants.INSERT;
import static com.thangtd.jackystore.commons.Constants.LOCAL;
import static com.thangtd.jackystore.commons.Constants.UPDATE;

public class BookEdit extends AppCompatActivity
        implements BookEditGeneral.OnFragmentInteractionListener,
        BookEditPersonal.OnFragmentInteractionListener {

    public static final String SHOWCASE_ID = "EDIT_SHOWCASE";

    private MenuItem btnDone;

    private String mISBN;
    private String mTitle;
    private String mAuthor;
    private String mTranslator;
    private String mPublisher;
    private String mPages;
    private String mReleasedDate;
    private String mPurchasedDate;
    private String mFinishedDate;
    private String mLocation;
    private String mNote;
    private int mKind;
    private byte[] mCover;
    private String imageAction[];
    private int type;

    private MySQLiteOpenHelper mDataHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mDataHelper = new MySQLiteOpenHelper(BookEdit.this);

        Intent intent = this.getIntent();
        mISBN = intent.getStringExtra("ISBN");

        if ((mISBN != null) && !mISBN.equals("")) {
            type = UPDATE;
            String sql = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where "
                    + MySQLiteOpenHelper.BOOK_ISBN + " = " + mISBN;

            Cursor cursor = mDataHelper.SELECTSQL(sql);
            cursor.moveToNext();
            mTitle = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_TITLE));
            getSupportActionBar().setTitle(mTitle);
            mAuthor = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_AUTHOR));
            mTranslator = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_TRANSLATOR));
            mPublisher = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_PUBLISHER));
            mPages = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_PAGES));
            mReleasedDate = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_RELEASED_DATE));
            mPurchasedDate = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_PURCHASED_DATE));
            mFinishedDate = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_FINISHED_DATE));
            mLocation = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_LOCATION));
            mNote = cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_NOTE));
            mKind = Integer.parseInt(cursor.getString(cursor
                    .getColumnIndex(MySQLiteOpenHelper.BOOK_KIND)));
            mCover = cursor.getBlob(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_COVER));
        } else {
            type = INSERT;
            mISBN = "";
            mTitle = "";
            mAuthor = "";
            mTranslator = "";
            mPublisher = "";
            mPages = "0";
            mReleasedDate = "";
            mPurchasedDate = "";
            mFinishedDate = "";
            mLocation = "";
            mNote = "";
            mKind = LOCAL;
            mCover = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_edit, menu);
//        btnDone = menu.findItem(R.id.action_done);
//        showFirstTimeTutorial();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault());
                Date currentTime = Calendar.getInstance().getTime();
                String mTime = sdf.format(currentTime);

                if (!mISBN.equals("")) {
                    BookObj mBook = new BookObj(mISBN, mTitle, mAuthor, mTranslator, mPublisher, Integer.parseInt(mPages),
                            mReleasedDate, mTime, mPurchasedDate, mFinishedDate, mLocation, mNote, mKind, mCover);
                    if (type == INSERT) {
                        mDataHelper.INSERT_BOOK(mBook);
                    } else {
                        mDataHelper.UPDATE_BOOK_BY_ISBN(mBook, mISBN);
                    }

                    Intent intent = new Intent(getApplicationContext(), BookHome.class);
                    intent.putExtra("BOOK_KIND", mKind);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_isbn_missing), Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFirstTimeTutorial() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);

        sequence.addSequenceItem(btnDone.getActionView(),
                "This is button done", "GOT IT");

        sequence.start();
    }

    @Override
    public void onFragmentInteraction(String isbn, String title, String author, String translator,
                                      String publisher, String pages, String releasedDate, int kind, byte[] cover) {
        mISBN = isbn;
        mTitle = title;
        mAuthor = author;
        mTranslator = translator;
        mPublisher = publisher;
        mPages = pages;
        mReleasedDate = releasedDate;
        mKind = kind;
        mCover = cover;
    }

    @Override
    public void onFragmentInteraction(String purchasedDate, String finishedDate, String location, String note) {
        mPurchasedDate = purchasedDate;
        mFinishedDate = finishedDate;
        mLocation = location;
        mNote = note;

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_book_edit, container, false);
            return rootView;
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
            switch (position) {
                case 0:
                    BookEditGeneral fragmentGeneral = new BookEditGeneral();
                    getSupportFragmentManager().beginTransaction().add(fragmentGeneral, "FragmentGeneral").commit();
                    return BookEditGeneral.newInstance(mISBN, mTitle, mAuthor, mTranslator, mPublisher, mPages, mReleasedDate, mKind, mCover);
                case 1:
                    return BookEditPersonal.newInstance(mPurchasedDate, mFinishedDate, mLocation, mNote);
                default:
                    return BookEditGeneral.newInstance(mISBN, mTitle, mAuthor, mTranslator, mPublisher, mPages, mReleasedDate, mKind, mCover);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
