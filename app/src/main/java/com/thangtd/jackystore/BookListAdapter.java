package com.thangtd.jackystore;

import android.content.Context;
import android.database.Cursor;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thangtd.jackystore.commons.MySQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by thangtd2016 on 13/03/2018.
 */

public class BookListAdapter extends CursorAdapter {

    private MySQLiteOpenHelper mDataHelper;
    private Cursor cursor;
    private Context context;
    private ViewHolder holder;

    public BookListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
        this.cursor = cursor;
        mDataHelper = new MySQLiteOpenHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_main_item, parent, false);
        bindView(view, context, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Do nothing
    }

    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.content_main_item, null);

            holder = new ViewHolder();
            holder.mView = convertView;
            holder.tvISBN = convertView.findViewById(R.id.item_ISBN);
            holder.tvTitle = convertView.findViewById(R.id.item_TITLE);
            holder.tvAuthor = convertView.findViewById(R.id.item_AUTHOR);
            holder.tvYear = convertView.findViewById(R.id.item_YEAR);
            holder.tvFinishedDate = convertView.findViewById(R.id.item_FINISHED_DATE);
            holder.imgStatus = convertView.findViewById(R.id.item_STATUS);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        cursor.moveToPosition(position);

        holder.tvISBN.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_ISBN)));
        holder.tvTitle.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_TITLE)));
        holder.tvAuthor.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_AUTHOR)));
        holder.tvYear.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_RELEASED_DATE)));
        holder.tvFinishedDate.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_FINISHED_DATE)));

        String purchaseDate = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_PURCHASED_DATE));
        final String finishDate = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_FINISHED_DATE));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = sdf.parse(purchaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long days = endDate.getTime() - startDate.getTime();

        holder.imgStatus.setVisibility(View.GONE);
        if (!finishDate.equals("")) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_checked_green));
        } else if (finishDate.equals("") && (TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS) > 365)) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_remind_red));
        }

        return convertView;
    }

    public static class ViewHolder {
        View mView;
        TextView tvISBN;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvYear;
        TextView tvFinishedDate;
        ImageView imgStatus;
    }
}
