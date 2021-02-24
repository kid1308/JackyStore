package com.thangtd.jackystore;

import android.annotation.SuppressLint;
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

public class BookFilterAdapter extends CursorAdapter {
    private Cursor cursor;
    private Context context;
    private int type;

    public BookFilterAdapter(Context context, Cursor cursor, int type) {
        super(context, cursor);
        this.context = context;
        this.cursor = cursor;
        this.type = type;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_filter_item, parent, false);
        bindView(view, context, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Do nothing
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.content_filter_item, null);

            holder = new ViewHolder();
            holder.mView = convertView;
            holder.tvISBN = convertView.findViewById(R.id.filterISBN);
            holder.tvTitle = convertView.findViewById(R.id.filterTITLE);
            holder.tvAuthor = convertView.findViewById(R.id.filterAUTHOR);
            holder.tvYear = convertView.findViewById(R.id.filterYEAR);
            holder.tvPurchasedDate = convertView.findViewById(R.id.filterPURCHASED_DATE);
            holder.tvFinishedDate = convertView.findViewById(R.id.filterFINISHED_DATE);
            holder.imgStatus = convertView.findViewById(R.id.filterSTATUS);

            if (type == 0) {
                holder.tvPurchasedDate.setVisibility(View.VISIBLE);
                holder.tvFinishedDate.setVisibility(View.GONE);
                holder.imgStatus.setVisibility(View.GONE);
            } else {
                holder.tvPurchasedDate.setVisibility(View.GONE);
                holder.tvFinishedDate.setVisibility(View.VISIBLE);
                holder.imgStatus.setVisibility(View.VISIBLE);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        cursor.moveToPosition(position);

        holder.tvISBN.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_ISBN)));
        holder.tvTitle.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_TITLE)));
        holder.tvAuthor.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_AUTHOR)));
        holder.tvYear.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_RELEASED_DATE)));
        holder.tvPurchasedDate.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_PURCHASED_DATE)));
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

        if (!finishDate.equals("")) {
            holder.imgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_checked_green));
        } else if (TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS) > 365) {
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
        TextView tvPurchasedDate;
        TextView tvFinishedDate;
        ImageView imgStatus;
    }
}
