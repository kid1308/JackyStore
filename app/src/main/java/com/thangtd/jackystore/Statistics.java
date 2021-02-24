package com.thangtd.jackystore;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.thangtd.jackystore.commons.MyApplication;
import com.thangtd.jackystore.commons.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Statistics extends AppCompatActivity {

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

    private MyApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mApp = ((MyApplication) getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_activity_statistics));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_TYPE = "type";

        private PieChart pie_01;
        private PieChart pie_02;
        private Spinner spView;

        private ImageView imgLegend_01;
        private ImageView imgLegend_02;
        private ImageView imgLegend_03;
        private TextView tvLegend_01;
        private TextView tvLegend_02;
        private TextView tvLegend_03;

        private int mType;  /* 1: book kind
                               2: book status */

        private int mView;  /* 1: buying
                               2: reading */

        private ArrayList<Integer> mColors = new ArrayList<>();
        private ArrayList<Integer> mColors_02 = new ArrayList<>();
        private ArrayList<String> mLabels = new ArrayList<>();
        private ArrayList<String> mLabels_02 = new ArrayList<>();

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int type) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_TYPE, type);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mType = getArguments().getInt(ARG_TYPE);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

            pie_01 = rootView.findViewById(R.id.pieChart_01);
            pie_02 = rootView.findViewById(R.id.pieChart_02);
            spView = rootView.findViewById(R.id.spView);

            imgLegend_01 = rootView.findViewById(R.id.imgLegend_01);
            imgLegend_02 = rootView.findViewById(R.id.imgLegend_02);
            imgLegend_03 = rootView.findViewById(R.id.imgLegend_03);

            tvLegend_01 = rootView.findViewById(R.id.tvLegend_01);
            tvLegend_02 = rootView.findViewById(R.id.tvLegend_02);
            tvLegend_03 = rootView.findViewById(R.id.tvLegend_03);

            pie_01.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    Intent intent = new Intent(getContext(), BookListFilter.class);
                    intent.putExtra("FILTER_TYPE", 0);
                    intent.putExtra("FILTER_YEAR", 0);
                    startActivity(intent);
                }

                @Override
                public void onNothingSelected() {

                }
            });

            pie_02.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    Intent intent = new Intent(getContext(), BookListFilter.class);
                    intent.putExtra("FILTER_TYPE", 0);
                    intent.putExtra("FILTER_YEAR", 0);
                    startActivity(intent);
                }

                @Override
                public void onNothingSelected() {

                }
            });

            spView.setSelection(0);
            spView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mView = spView.getSelectedItemPosition();
                    loadPieChart(mView);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            return rootView;
        }

        public void loadPieChart(int view) {
            final int[] COLORS_BOOK_KIND = {ContextCompat.getColor(getContext(), R.color.colorRed),
                    ContextCompat.getColor(getContext(), R.color.colorBlue),
                    ContextCompat.getColor(getContext(), R.color.colorGreen)};

            final int[] COLORS_READ_STATUS = {ContextCompat.getColor(getContext(), R.color.colorGreen200),
                    ContextCompat.getColor(getContext(), R.color.colorRed200)};

            /* Tab 01 - Book kind */
            if (mType == 1) {
                /* Chart 01 - This year statistics*/

                float qtyLocal;
                float qtyForeign;
                float qtyManga;

                if (view == 0) {    // View statistics by buying
                    MySQLiteOpenHelper mDataLocal = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlLocal = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 0) " +
                            "and (substr(" + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", -4) = strftime('%Y','now'))";

                    Cursor mCursorLocal = mDataLocal.SELECTSQL(sqlLocal);
                    qtyLocal = mCursorLocal.getCount();

                    // Get quantity of foreign books that bought this year
                    MySQLiteOpenHelper mDataForeign = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlForeign = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 1) " +
                            "and (substr(" + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", -4) = strftime('%Y','now'))";

                    Cursor mCursorForeign = mDataForeign.SELECTSQL(sqlForeign);
                    qtyForeign = mCursorForeign.getCount();

                    // Get quantity of manga-comic books that bought this year
                    MySQLiteOpenHelper mDataManga = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlManga = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 2) " +
                            "and (substr(" + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", -4) = strftime('%Y','now'))";

                    Cursor mCursorManga = mDataManga.SELECTSQL(sqlManga);
                    qtyManga = mCursorManga.getCount();
                } else {    // View statistics by reading
                    MySQLiteOpenHelper mDataLocal = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlLocal = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 0) " +
                            "and (substr(" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + ", -4) = strftime('%Y','now'))";

                    Cursor mCursorLocal = mDataLocal.SELECTSQL(sqlLocal);
                    qtyLocal = mCursorLocal.getCount();

                    // Get quantity of foreign books that bought this year
                    MySQLiteOpenHelper mDataForeign = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlForeign = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 1) " +
                            "and (substr(" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + ", -4) = strftime('%Y','now'))";

                    Cursor mCursorForeign = mDataForeign.SELECTSQL(sqlForeign);
                    qtyForeign = mCursorForeign.getCount();

                    // Get quantity of manga-comic books that bought this year
                    MySQLiteOpenHelper mDataManga = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlManga = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 2) " +
                            "and (substr(" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + ", -4) = strftime('%Y','now'))";

                    Cursor mCursorManga = mDataManga.SELECTSQL(sqlManga);
                    qtyManga = mCursorManga.getCount();
                }

                int qtyTotal = Math.round(qtyLocal + qtyForeign + qtyManga);

                float percentLocal = qtyLocal / qtyTotal * 100;
                float percentForeign = qtyForeign / qtyTotal * 100;
                float percentManga = qtyManga / qtyTotal * 100;

                int xIndex = 0;
                List<PieEntry> entries = new ArrayList<>();
                mColors.clear();
                mLabels.clear();

                if (percentLocal > 0) {
                    entries.add(new PieEntry(percentLocal, String.format(Locale.GERMANY, "(%.0f)", qtyLocal)));
                    mColors.add(COLORS_BOOK_KIND[0]);    // Cyan
                    mLabels.add(getString(R.string.str_books_local));
                }

                if (percentForeign > 0) {
                    xIndex += 1;
                    entries.add(new PieEntry(percentForeign, String.format(Locale.GERMANY, "(%.0f)", qtyForeign)));
                    mColors.add(COLORS_BOOK_KIND[1]);    // Indigo
                    mLabels.add(getString(R.string.str_books_foreign));
                }

                if (percentManga > 0) {
                    xIndex += 1;
                    entries.add(new PieEntry(percentManga, String.format(Locale.GERMANY, "(%.0f)", qtyManga)));
                    mColors.add(COLORS_BOOK_KIND[2]);    // Amber
                    mLabels.add(getString(R.string.str_books_manga));
                }

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(mColors);
                dataSet.setSliceSpace(2f);
                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter(pie_01));
                data.setValueTextColor(Color.WHITE);
                data.setValueTextSize(11f);
                pie_01.setData(data);

                // display total in center circle
                pie_01.setCenterText(qtyTotal + "\n " + getString(R.string.center_total));
                pie_01.setCenterTextSize(12f);
                pie_01.setCenterTextColor(Color.BLACK);

                /* Chart 02 - All time statistics*/
                float qtyLocal_02;
                float qtyForeign_02;
                float qtyManga_02;

                if (view == 0) {        // View statistics by buying
                    // Get quantity of local books in library
                    MySQLiteOpenHelper mDataLocal_02 = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlLocal_02 = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 0) ";

                    Cursor mCursorLocal_02 = mDataLocal_02.SELECTSQL(sqlLocal_02);
                    qtyLocal_02 = mCursorLocal_02.getCount();

                    // Get quantity of foreign books in library
                    MySQLiteOpenHelper mDataForeign_02 = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlForeign_02 = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 1) ";

                    Cursor mCursorForeign_02 = mDataForeign_02.SELECTSQL(sqlForeign_02);
                    qtyForeign_02 = mCursorForeign_02.getCount();

                    // Get quantity of manga-comic books in library
                    MySQLiteOpenHelper mDataManga_02 = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlManga_02 = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 2) ";

                    Cursor mCursorManga_02 = mDataManga_02.SELECTSQL(sqlManga_02);
                    qtyManga_02 = mCursorManga_02.getCount();
                } else {                // View statistics by reading
                    // Get quantity of local books had read
                    MySQLiteOpenHelper mDataLocal_02 = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlLocal_02 = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS +
                            " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 0) " +
                            "   and (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " IS NOT NULL) " +
                            "   and (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " <> '') ";

                    Cursor mCursorLocal_02 = mDataLocal_02.SELECTSQL(sqlLocal_02);
                    qtyLocal_02 = mCursorLocal_02.getCount();

                    // Get quantity of foreign books had read
                    MySQLiteOpenHelper mDataForeign_02 = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlForeign_02 = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS +
                            " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 1) " +
                            "   and (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " IS NOT NULL) " +
                            "   and (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " <> '') ";

                    Cursor mCursorForeign_02 = mDataForeign_02.SELECTSQL(sqlForeign_02);
                    qtyForeign_02 = mCursorForeign_02.getCount();

                    // Get quantity of manga-comic books had read
                    MySQLiteOpenHelper mDataManga_02 = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                    String sqlManga_02 = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS +
                            " where (" + MySQLiteOpenHelper.BOOK_KIND + " = 2) " +
                            "   and (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " IS NOT NULL) " +
                            "   and (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " <> '') ";

                    Cursor mCursorManga_02 = mDataManga_02.SELECTSQL(sqlManga_02);
                    qtyManga_02 = mCursorManga_02.getCount();
                }

                int qtyTotal_02 = Math.round(qtyLocal_02 + qtyForeign_02 + qtyManga_02);

                float percentLocal_02 = qtyLocal_02 / qtyTotal_02 * 100;
                float percentForeign_02 = qtyForeign_02 / qtyTotal_02 * 100;
                float percentManga_02 = qtyManga_02 / qtyTotal_02 * 100;

                xIndex = 0;
                mColors_02.clear();
                mLabels_02.clear();
                List<PieEntry> entries_02 = new ArrayList<>();

                if (percentLocal_02 > 0) {
                    entries_02.add(new PieEntry(percentLocal_02, String.format(Locale.GERMANY, "(%.0f)", qtyLocal_02)));
                    mColors_02.add(COLORS_BOOK_KIND[0]);    // Cyan
                    mLabels_02.add(String.valueOf(qtyLocal_02));
                }

                if (percentForeign_02 > 0) {
                    xIndex += 1;
                    entries_02.add(new PieEntry(percentForeign_02, String.format(Locale.GERMANY, "(%.0f)", qtyForeign_02)));
                    mColors_02.add(COLORS_BOOK_KIND[1]);    // Indigo
                    mLabels_02.add(String.valueOf(qtyForeign_02));
                }

                if (percentManga_02 > 0) {
                    xIndex += 1;
                    entries_02.add(new PieEntry(percentManga_02, String.format(Locale.GERMANY, "(%.0f)", qtyManga_02)));
                    mColors_02.add(COLORS_BOOK_KIND[2]);    // Amber
                    mLabels_02.add(String.valueOf(qtyManga_02));
                }

                PieDataSet dataSet_02 = new PieDataSet(entries_02, "");
                dataSet_02.setColors(mColors_02);
                dataSet_02.setSliceSpace(2f);
                PieData data_02 = new PieData(dataSet_02);
                data_02.setValueFormatter(new PercentFormatter(pie_02));
                data_02.setValueTextColor(Color.WHITE);
                data_02.setValueTextSize(11f);
                pie_02.setData(data_02);

                // display total in center circle
                pie_02.setCenterText(qtyTotal_02 + "\n " + getString(R.string.center_total));
                pie_02.setCenterTextSize(12f);
                pie_02.setCenterTextColor(Color.BLACK);

                // display legend in a row between 2 charts
                imgLegend_03.setVisibility(View.VISIBLE);
                tvLegend_03.setVisibility(View.VISIBLE);

                imgLegend_01.setBackgroundColor(COLORS_BOOK_KIND[0]);
                imgLegend_02.setBackgroundColor(COLORS_BOOK_KIND[1]);
                imgLegend_03.setBackgroundColor(COLORS_BOOK_KIND[2]);

                tvLegend_01.setText(getString(R.string.str_books_local));
                tvLegend_02.setText(getString(R.string.str_books_foreign));
                tvLegend_03.setText(getString(R.string.str_books_manga));
            }
            /* Tab 02 - Book read/unread */
            else if (mType == 2) {
                spView.setVisibility(View.GONE);
                /* Chart 01 - This year statistics*/
                // Quantity of book that finished this year
                MySQLiteOpenHelper mDataRead = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                String sqlRead = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS +
                        " where (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " IS NOT NULL) " +
                        "   and (substr(" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + ", -4) = strftime('%Y','now'))";

                Cursor mCursorRead = mDataRead.SELECTSQL(sqlRead);
                int qtyRead = mCursorRead.getCount();

                // Quantity of books that bought this year
                MySQLiteOpenHelper mDataBuy = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                String sqlBuy = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS +
                        " where (substr(" + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", -4) = strftime('%Y','now'))";

                Cursor mCursorBuy = mDataBuy.SELECTSQL(sqlBuy);
                int qtyBuy = mCursorBuy.getCount();

                int qtyUnread = qtyBuy - qtyRead;
                float percentRead = (float) qtyRead / qtyBuy * 100;
                float percentUnread = (float) qtyUnread / qtyBuy * 100;

                List<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(percentRead, String.format(Locale.GERMANY, "(%d)", qtyRead)));
                entries.add(new PieEntry(percentUnread, String.format(Locale.GERMANY, "(%d)", qtyUnread)));

                ArrayList<String> labels = new ArrayList<>();
                labels.add(getString(R.string.label_read));
                labels.add(getString(R.string.label_unread));

                ArrayList<Integer> colors = new ArrayList<Integer>();
                for (int c : COLORS_READ_STATUS) colors.add(c);

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(colors);
                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter(pie_01));
                data.setValueTextColor(Color.WHITE);
                data.setValueTextSize(11f);
                pie_01.setData(data);

                // display total in center circle
                pie_01.setCenterText(qtyBuy + "\n " + getString(R.string.center_total));
                pie_01.setCenterTextSize(12f);
                pie_01.setCenterTextColor(Color.BLACK);

                /* Chart 02 - All time statistics*/
                // Quantity of books that finished
                MySQLiteOpenHelper mDataReadAll = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                String sqlReadAll = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS +
                        " where (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " IS NOT NULL) " +
                        "   and (" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + " <> '') ";

                Cursor mCursorReadAll = mDataReadAll.SELECTSQL(sqlReadAll);
                int qtyReadAll = mCursorReadAll.getCount();

                // Total books in the library
                MySQLiteOpenHelper mDataAll = new MySQLiteOpenHelper(getActivity().getApplicationContext());
                Cursor mCursorAll = mDataAll.SELECT_ALL_BOOKS();
                int qtyAll = mCursorAll.getCount();

                int qtyUnreadAll = qtyAll - qtyReadAll;
                float percentReadAll = (float) qtyReadAll / qtyAll * 100;
                float percentUnreadAll = (float) qtyUnreadAll / qtyAll * 100;

                List<PieEntry> entries_02 = new ArrayList<>();
                entries_02.add(new PieEntry(percentReadAll, String.format(Locale.GERMANY, "(%d)", qtyReadAll)));
                entries_02.add(new PieEntry(percentUnreadAll, String.format(Locale.GERMANY, "(%d)", qtyUnreadAll)));

                ArrayList<String> labels_02 = new ArrayList<>();
                labels_02.add(getString(R.string.label_read));
                labels_02.add(getString(R.string.label_unread));

                PieDataSet dataSet_02 = new PieDataSet(entries_02, "");
                dataSet_02.setColors(colors);
                PieData data_02 = new PieData(dataSet_02);
                data_02.setValueFormatter(new PercentFormatter(pie_02));
                data_02.setValueTextColor(Color.WHITE);
                data_02.setValueTextSize(11f);
                pie_02.setData(data_02);

                // display total in center circle
                pie_02.setCenterText(qtyAll + "\n " + getString(R.string.center_total));
                pie_02.setCenterTextSize(12f);
                pie_02.setCenterTextColor(Color.BLACK);

                // display legend in a row between 2 charts
                imgLegend_03.setVisibility(View.GONE);
                tvLegend_03.setVisibility(View.GONE);

                imgLegend_01.setBackgroundColor(COLORS_READ_STATUS[0]);
                imgLegend_02.setBackgroundColor(COLORS_READ_STATUS[1]);

                tvLegend_01.setText(getString(R.string.label_read));
                tvLegend_02.setText(getString(R.string.label_unread));
            }

            pie_01.animateY(1000);
            pie_01.setUsePercentValues(true);
            pie_01.getDescription().setText(getString(R.string.description_pie_01));
            pie_01.getDescription().setTextSize(11f);
            pie_01.getLegend().setEnabled(false);
            pie_01.invalidate(); // refresh


            pie_02.animateY(1000);
            pie_02.setUsePercentValues(true);
            pie_02.getDescription().setText(getString(R.string.description_pie_02));
            pie_02.getDescription().setTextSize(11f);
            pie_02.getLegend().setEnabled(false);
            pie_02.invalidate(); // refresh
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
