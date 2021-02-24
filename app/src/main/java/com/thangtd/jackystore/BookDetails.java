package com.thangtd.jackystore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thangtd.jackystore.commons.MySQLiteOpenHelper;
import com.zing.zalo.zalosdk.oauth.LoginVia;
import com.zing.zalo.zalosdk.oauth.ValidateOAuthCodeCallback;
import com.zing.zalo.zalosdk.oauth.ZaloOpenAPICallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.thangtd.jackystore.commons.Constants.FOREIGN;

public class BookDetails extends AppCompatActivity {
    private String mISBN;
    private String mTitle;
    private String mAuthor;
    private String mTranslator;
    private String mPublisher;
    private String mReleasedDate;
    private String mPurchasedDate;
    private String mFinishedDate;
    private String mLocation;
    private String mNote;
    private String mPages;
    private byte[] mCover;
    private int mKind;
    private String mFrom;

    private TextView tvTitle;
    private TextView tvISBN;
    private TextView tvAuthor;
    private TextView tvTranslator;
    private TextView tvPublisher;
    private TextView tvPages;
    private TextView tvReleasedDate;
    private TextView tvPurchasedDate;
    private TextView tvFinishedDate;
    private TextView tvLocation;
    private TextView tvNote;
    private ImageView imgCover;
    private LinearLayout layoutTRANS;

    private MySQLiteOpenHelper mDataHelper;
    private LoginListener mLoginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDataHelper = new MySQLiteOpenHelper(BookDetails.this);
        mLoginListener = new LoginListener();

        Intent intent = this.getIntent();
        mISBN = intent.getStringExtra("ISBN");
        mFrom = intent.getStringExtra("FROM_ACTIVITY");

        String sql = "select * from " + MySQLiteOpenHelper.TB_BOOK_DETAILS + " where "
                + MySQLiteOpenHelper.BOOK_ISBN + " = " + mISBN;

        Cursor cursor = mDataHelper.SELECTSQL(sql);

        cursor.moveToNext();

        mTitle = cursor.getString(cursor
                .getColumnIndex(MySQLiteOpenHelper.BOOK_TITLE));
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
        mCover = cursor.getBlob(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_COVER));
        mKind = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.BOOK_KIND));

        tvTitle = findViewById(R.id.detail_TITLE);
        tvISBN = findViewById(R.id.detail_ISBN);
        tvAuthor = findViewById(R.id.detail_AUTHOR);
        tvTranslator = findViewById(R.id.detail_TRANSLATOR);
        tvPublisher = findViewById(R.id.detail_PUBLISHER);
        tvPages = findViewById(R.id.detail_PAGES);
        tvReleasedDate = findViewById(R.id.detail_RELEASED_DATE);
        tvPurchasedDate = findViewById(R.id.detail_PURCHASED_DATE);
        tvFinishedDate = findViewById(R.id.detail_FINISHED_DATE);
        tvLocation = findViewById(R.id.detail_LOCATION);
        tvNote = findViewById(R.id.detail_NOTE);
        imgCover = findViewById(R.id.detail_COVER);

        getSupportActionBar().setTitle(mTitle);
        tvTitle.setText(mTitle);
        tvISBN.setText(mISBN);
        tvAuthor.setText(mAuthor);
        tvTranslator.setText(mTranslator);
        tvPublisher.setText(mPublisher);
        tvReleasedDate.setText(mReleasedDate);
        tvPages.setText(mPages);
        tvPurchasedDate.setText(mPurchasedDate);
        tvFinishedDate.setText(mFinishedDate);
        tvLocation.setText(mLocation);
        tvNote.setText(mNote);

        if (mCover != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(mCover, 0, mCover.length);
            imgCover.setImageBitmap(bmp);
        }

        layoutTRANS = findViewById(R.id.detailLayout_TRANSLATOR);
        layoutTRANS.setVisibility(View.GONE);
        if (mKind == FOREIGN) {
            layoutTRANS.setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookEdit.class);
                intent.putExtra("ISBN", mISBN);
                startActivity(intent);
            }
        });

        FloatingActionButton fabZalo = findViewById(R.id.fabZalo);
        fabZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZaloSDK.Instance.isAuthenticate(new ValidateOAuthCodeCallback() {
                    @Override
                    public void onValidateComplete(boolean validated, int errorCode, long userId, String oauthCode) {
                        if (validated) {    //OAthCode con hieu luc
                            getAccessToken(oauthCode);
                        } else {      //Su dung OAthCode moi
                            ZaloSDK.Instance.authenticate(BookDetails.this, LoginVia.WEB, mLoginListener);
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions, menu);
        menu.findItem(R.id.action_done).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(BookDetails.this)
                        .setTitle(getString(R.string.dialog_header_confirm))
                        .setMessage(getString(R.string.confirm_delete))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDataHelper.DELETE_BOOK_BY_CODE(mISBN);
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFrom != null && mFrom.equals("Filter")) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(getApplicationContext(), BookHome.class);
            intent.putExtra("BOOK_KIND", mKind);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);

        ZaloSDK.Instance.onActivityResult(this, reqCode, resCode, data);

        String oathCode = data.getStringExtra("code");
        getAccessToken(oathCode);
    }

    private void getAccessToken(String code) {
        String appID = getString(R.string.appID);
        String appSecret = getString(R.string.appSec);

        String mURL = "https://oauth.zaloapp.com/v3/access_token?app_id=" + appID + "&app_secret=" + appSecret + "&code=" + code;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, mURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("access_token")) {
                String accessToken = jsonObject.getString("access_token");
                getFriendID();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_isbn_mismatch), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getFriendID() {
        ZaloSDK.Instance.getFriendListInvitable(this, 0, 100, new ZaloOpenAPICallback() {
            @Override
            public void onResult(JSONObject arg0) {
                try {
                    JSONArray jArr = arg0.getJSONArray("data");

                    for (int i = 0; i <= jArr.length(); i++) {
                        arg0 = jArr.getJSONObject(i);

                        if (arg0.getString("name").equals("Minh Hòa")) {
                            String name = (arg0.getString("name"));
                            String id = (arg0.getString("id"));

                            sendMessage(name, id);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new String[]{"id", "name"});
    }

    public void sendMessage(String aName, String aID) {
        Toast.makeText(getApplicationContext(), "Bạn muốn gửi tin nhắn đến " + aName + "?", Toast.LENGTH_LONG).show();
    }
}
