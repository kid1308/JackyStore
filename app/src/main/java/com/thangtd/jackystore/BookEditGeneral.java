package com.thangtd.jackystore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.thangtd.jackystore.commons.MySQLiteOpenHelper;
import com.thangtd.jackystore.commons.Utility;
import com.thangtd.jackystore.objects.AuthorObj;
import com.thangtd.jackystore.objects.PublisherObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.thangtd.jackystore.commons.Constants.CAMERA;
import static com.thangtd.jackystore.commons.Constants.FOREIGN;
import static com.thangtd.jackystore.commons.Constants.GALLERY;
import static com.thangtd.jackystore.commons.Constants.PERMISSIONS;
import static com.thangtd.jackystore.commons.Constants.PERMISSION_ALL;
import static com.thangtd.jackystore.commons.Constants.SCAN;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookEditGeneral.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookEditGeneral#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookEditGeneral extends Fragment {
    public static String TAG = "FragmentGeneral";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "bookISBN";
    private static final String ARG_PARAM2 = "bookTitle";
    private static final String ARG_PARAM3 = "bookAuthor";
    private static final String ARG_PARAM4 = "bookTranslator";
    private static final String ARG_PARAM5 = "bookPublisher";
    private static final String ARG_PARAM6 = "bookPages";
    private static final String ARG_PARAM7 = "bookPublishedDate";
    private static final String ARG_PARAM8 = "bookKind";
    private static final String ARG_PARAM9 = "bookCover";

    // TODO: Rename and change types of parameters
    private String mISBN;
    private String mTitle;
    private String mAuthor;
    private String mTranslator;
    private String mPublisher;
    private String mPages;
    private String mPublishedDate;
    private int mKind;
    private byte[] mCover;
    private String imageAction[];

    private LinearLayout layoutTranslator;
    private EditText etTitle;
    private EditText etISBN;
    private AutoCompleteTextView actvAuthor;
    private EditText etTranslator;
    private AutoCompleteTextView actvPublisher;
    private EditText etPublishedDate;
    private EditText etPages;
    private ImageView imgCover;
    private ImageButton btnScanISBN;
    private Spinner spKind;
    private URL url;

    private static AsyncHttpClient sClient = new AsyncHttpClient();
    private OnFragmentInteractionListener mListener;

    private MySQLiteOpenHelper dataHelper;
    private ArrayList<String> mListPublisher = new ArrayList<>();
    private ArrayAdapter<String> mAdapterPublisher;
    private ArrayList<String> mListAuthor = new ArrayList<>();
    private ArrayAdapter<String> mAdapterAuthor;

    public BookEditGeneral() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BookEditGeneral.
     */
    // TODO: Rename and change types and number of parameters
    public static BookEditGeneral newInstance(String param1, String param2, String param3, String param4,
                                              String param5, String param6, String param7, int param8, byte[] param9) {
        BookEditGeneral fragment = new BookEditGeneral();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        args.putString(ARG_PARAM7, param7);
        args.putInt(ARG_PARAM8, param8);
        args.putByteArray(ARG_PARAM9, param9);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mISBN = getArguments().getString(ARG_PARAM1);
            mTitle = getArguments().getString(ARG_PARAM2);
            mAuthor = getArguments().getString(ARG_PARAM3);
            mTranslator = getArguments().getString(ARG_PARAM4);
            mPublisher = getArguments().getString(ARG_PARAM5);
            mPages = getArguments().getString(ARG_PARAM6);
            mPublishedDate = getArguments().getString(ARG_PARAM7);
            mKind = getArguments().getInt(ARG_PARAM8);
            mCover = getArguments().getByteArray(ARG_PARAM9);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_edit_general, container, false);

        layoutTranslator = v.findViewById(R.id.layout_TRANSLATOR);
        etISBN = v.findViewById(R.id.book_ISBN);
        etTitle = v.findViewById(R.id.book_TITLE);
        actvAuthor = v.findViewById(R.id.book_AUTHOR);
        etTranslator = v.findViewById(R.id.book_TRANSLATOR);
        actvPublisher = v.findViewById(R.id.book_PUBLISHER);
        etPages = v.findViewById(R.id.book_PAGES);
        etPublishedDate = v.findViewById(R.id.book_PUBLISHED_DATE);
        imgCover = v.findViewById(R.id.book_COVER);
        btnScanISBN = v.findViewById(R.id.btnScanISBN);
        spKind = v.findViewById(R.id.book_KIND);

        btnScanISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, SCAN);
            }
        });

        imageAction = getResources().getStringArray(R.array.imageActions);
        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setItems(imageAction, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        boolean result = Utility.hasPermissions(getActivity(), PERMISSIONS);
                        if (result) {
                            if (imageAction[item].equals(getString(R.string.action_image_camera))) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAMERA);
                            } else if (imageAction[item].equals(getString(R.string.action_image_gallery))) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY);
                            }
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                        }
                    }
                });
                builder.show();
            }
        });

        spKind.setSelection(0);
        layoutTranslator.setVisibility(View.GONE);
        spKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mKind = spKind.getSelectedItemPosition();
                if (mKind == FOREIGN) {
                    layoutTranslator.setVisibility(View.VISIBLE);
                } else {
                    layoutTranslator.setVisibility(View.GONE);
                }

                mListener.onFragmentInteraction(mISBN, mTitle, mAuthor, mTranslator, mPublisher, mPages, mPublishedDate, mKind, mCover);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (dataHelper == null) {
            dataHelper = new MySQLiteOpenHelper(getContext());
            dataHelper.onUpgrade(dataHelper.database, 1, 2);
        }


        Cursor cursorAuthor = dataHelper.SELECT_ALL_AUTHORS();
        if ((cursorAuthor != null) && (cursorAuthor.getCount() > 0)) {
            while (cursorAuthor.moveToNext()) {
                String strAuthor = cursorAuthor.getString(cursorAuthor
                        .getColumnIndex(MySQLiteOpenHelper.AUTHOR_NAME));
                if (!mListAuthor.contains(strAuthor)) {
                    mListAuthor.add(strAuthor);
                }
            }
        }
        mAdapterAuthor = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_layout_01, mListAuthor);
        actvAuthor.setAdapter(mAdapterAuthor);
        actvAuthor.setThreshold(1);
        actvAuthor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mAuthor = actvAuthor.getText().toString();

                if (mKind == FOREIGN) {
                    etTranslator.requestFocus();
                } else {
                    actvPublisher.requestFocus();
                }
            }
        });

        actvAuthor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (actvAuthor.getText().toString().trim().length() > 0) {
                        mAuthor = actvAuthor.getText().toString().trim();

                        // insert into device local db
                        AuthorObj mDatabase = new AuthorObj(mAuthor);
                        dataHelper.INSERT_AUTHOR(mDatabase);

                        if (mKind == FOREIGN) {
                            etTranslator.requestFocus();
                        } else {
                            actvPublisher.requestFocus();
                        }
                    }
                }
                return true;
            }
        });

        Cursor cursorPublisher = dataHelper.SELECT_ALL_PUBLISHERS();
        if ((cursorPublisher != null) && (cursorPublisher.getCount() > 0)) {
            while (cursorPublisher.moveToNext()) {
                String strPub = cursorPublisher.getString(cursorPublisher
                        .getColumnIndex(MySQLiteOpenHelper.PUBLISHER_NAME));
                if (!mListPublisher.contains(strPub)) {
                    mListPublisher.add(strPub);
                }
            }
        }
        mAdapterPublisher = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_layout_01, mListPublisher);
        actvPublisher.setAdapter(mAdapterPublisher);
        actvPublisher.setThreshold(1);
        actvPublisher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mPublisher = actvPublisher.getText().toString();
                etPages.requestFocus();
            }
        });

        actvPublisher.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (actvPublisher.getText().toString().trim().length() > 0) {
                        mPublisher = actvPublisher.getText().toString().trim();

                        // insert into device local db
                        PublisherObj mDatabase = new PublisherObj(mPublisher);
                        dataHelper.INSERT_PUBLISHER(mDatabase);

                        etPages.requestFocus();
                    }
                }
                return true;
            }
        });

        if ((mISBN != null) && !mISBN.equals("")) {
            etTitle.setText(mTitle);
            etISBN.setText(mISBN);
            actvAuthor.setText(mAuthor);
            etTranslator.setText(mTranslator);
            actvPublisher.setText(mPublisher);
            etPages.setText(mPages);
            etPublishedDate.setText(mPublishedDate);
            spKind.setSelection(mKind);

            if (mCover != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(mCover, 0, mCover.length);
                imgCover.setImageBitmap(bmp);
            }
        } else {
            etTitle.requestFocus();
        }

        etISBN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etTitle.getText().toString().equals("")) {
                        getBookDetails(etISBN.getText().toString());
                    } else {
                        sendDataToActivity();
                    }
                }
            }
        });
        etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sendDataToActivity();
                }
            }
        });
        actvAuthor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sendDataToActivity();
                }
            }
        });
        etTranslator.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sendDataToActivity();
                }
            }
        });
        actvPublisher.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sendDataToActivity();
                }
            }
        });
        etPages.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sendDataToActivity();
                }
            }
        });
        etPublishedDate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    sendDataToActivity();
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == GALLERY)
                onSelectFromGalleryResult(data);
            else {
                // Scan ISBN
                String mResult = data.getStringExtra("SCAN_RESULT").replace("-", "");
                etISBN.setText(mResult);
                getBookDetails(mResult);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgCover.setImageBitmap(bm);
        uploadImage(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        imgCover.setImageBitmap(thumbnail);
        uploadImage(thumbnail);
    }

    private void uploadImage(Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (image != null) {
            image.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            mCover = bytes.toByteArray();
            mListener.onFragmentInteraction(mISBN, mTitle, mAuthor, mTranslator, mPublisher, mPages, mPublishedDate, mKind, mCover);
        }
    }

    private void getBookDetails(String isbn) {
        // Query the book database by ISBN code.
        String mURL = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, mURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("items")) {
                JSONArray jArr = jsonObject.getJSONArray("items");
                jsonObject = jArr.getJSONObject(0);

                JSONObject objVolumeInfo = jsonObject.getJSONObject("volumeInfo");
                String title = (objVolumeInfo.getString("title"));
                String author = (objVolumeInfo.getString("authors"));
                author = author.substring(2, author.length() - 2);
                etTitle.setText(title);
                actvAuthor.setText(author);

                if (objVolumeInfo.has("publisher")) {
                    String publisher = (objVolumeInfo.getString("publisher")).replace("\"", "");
                    actvPublisher.setText(publisher);
//                            etPublisher.setText(publisher);
                }

                if (objVolumeInfo.has("publishedDate")) {
                    String publishedDate = (objVolumeInfo.getString("publishedDate"));
                    etPublishedDate.setText(publishedDate);
                }

                if (objVolumeInfo.has("pageCount")) {
                    String pageCount = (objVolumeInfo.getString("pageCount"));
                    etPages.setText(pageCount);
                }

                if (objVolumeInfo.has("imageLinks")) {
                    JSONObject objCoverImage = objVolumeInfo.getJSONObject("imageLinks");
                    if (objCoverImage.has("thumbnail")) {
                        url = null;
                        try {
                            url = new URL(objCoverImage.getString("thumbnail"));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        if (url != null) {
                            getImageFromUrl mAsyncTask = new getImageFromUrl();
                            mAsyncTask.execute(url);
                        }
                    }
                }

                sendDataToActivity();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_isbn_mismatch), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class getImageFromUrl extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... aURL) {

            // compress image in background thread
            Bitmap result =  getImage(aURL[0]);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            mCover = bytes.toByteArray();

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            // update UI in main thread
            imgCover.setImageBitmap(result);

            sendDataToActivity();
        }

        private Bitmap getImage(URL aURL) {

            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(aURL.openConnection().getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    private void sendDataToActivity() {
        mISBN = etISBN.getText().toString();
        mTitle = etTitle.getText().toString();
        mAuthor = actvAuthor.getText().toString();
        mTranslator = etTranslator.getText().toString();
        mPublisher = actvPublisher.getText().toString();
        mPages = etPages.getText().toString();
        if (mPages.equals("")) {
            mPages = "0";
        }
        mPublishedDate = etPublishedDate.getText().toString();

        if (mISBN.equals("")) {
            etISBN.setError(getString(R.string.error_isbn_missing));
        } else {
            mListener.onFragmentInteraction(mISBN, mTitle, mAuthor, mTranslator, mPublisher, mPages, mPublishedDate, mKind, mCover);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String isbn, String title, String author, String translator,
                                   String publisher, String pages, String releasedDate, int kind, byte[] cover);
    }
}
