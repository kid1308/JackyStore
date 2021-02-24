package com.thangtd.jackystore;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.thangtd.jackystore.commons.MySQLiteOpenHelper;

import java.util.Calendar;
import java.util.Locale;

public class BookListFilter extends AppCompatActivity {
    private ListView lvBooks;
    private TextView tvHeader;
    private Spinner spType;
    private Spinner spYear;

    int mType;
    int mYear;

    private MySQLiteOpenHelper mDataHelper;
    private BookFilterAdapter mAdapter;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_filter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        tvHeader = findViewById(R.id.listFilterHeader);
        lvBooks = findViewById(android.R.id.list);
        spType = findViewById(R.id.listFilterType);
        spYear = findViewById(R.id.listFilterYear);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mType = position;
                loadList(mType, mYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mYear = position;
                loadList(mType, mYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void onResume() {
        super.onResume();

        Intent intent = this.getIntent();
        mType = intent.getIntExtra("FILTER_TYPE", 0);
        mYear = intent.getIntExtra("FILTER_YEAR", 0);

        spType.setSelection(mType);
        spYear.setSelection(mYear);

        mDataHelper = new MySQLiteOpenHelper(getApplicationContext());
        loadList(mType, mYear);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String mISBN = (String) ((TextView) view.findViewById(R.id.filterISBN)).getText();

                Intent intent = new Intent(getApplicationContext(), BookDetails.class);
                intent.putExtra("ISBN", mISBN);
                intent.putExtra("FROM_ACTIVITY", "Filter");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadList(int type, int year) {
        String sql = "";
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, mYear * (-1));
        int filterYear = instance.get(Calendar.YEAR);

        if (type == 0) {            // filter by purchased date
            sql = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS
                    + " where substr(" + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", -4) = '" + filterYear + "'"
                    + " order by (substr(" + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", 7, 4) || '-' || "
                    + "           substr(" + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", 4, 2) || '-' || "
                    + "           substr(" + MySQLiteOpenHelper.BOOK_PURCHASED_DATE + ", 1, 2)) DESC, TITLE_BOOK";
        } else if (type == 1) {     // filter by finished date
            sql = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS
                    + " where substr(" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + ", -4) = '" + filterYear + "'"
                    + " order by (substr(" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + ", 7, 4) || '-' || "
                    + "           substr(" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + ", 4, 2) || '-' || "
                    + "           substr(" + MySQLiteOpenHelper.BOOK_FINISHED_DATE + ", 1, 2)) DESC, TITLE_BOOK";
        }

        mCursor = mDataHelper.SELECTSQL(sql);
        mAdapter = new BookFilterAdapter(getApplicationContext(), mCursor, mType);
        lvBooks.setAdapter(mAdapter);

        tvHeader.setText(String.format(Locale.GERMANY, "%d %s", mCursor.getCount(), getString(R.string.str_list_header)));
    }
}
