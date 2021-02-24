package com.thangtd.jackystore;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookEditPersonal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookEditPersonal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookEditPersonal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "bookPurchasedDate";
    private static final String ARG_PARAM2 = "bookFinishedDate";
    private static final String ARG_PARAM3 = "bookLocation";
    private static final String ARG_PARAM4 = "bookNote";

    // TODO: Rename and change types of parameters
    private String mPurchasedDate;
    private String mFinishedDate;
    private String mLocation;
    private String mNote;

    private EditText etPurchasedDate;
    private EditText etFinishedDate;
    private EditText etLocation;
    private EditText etNote;

    private OnFragmentInteractionListener mListener;

    public BookEditPersonal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BookEditPersonal.
     */
    // TODO: Rename and change types and number of parameters
    public static BookEditPersonal newInstance(String param1, String param2, String param3, String param4) {
        BookEditPersonal fragment = new BookEditPersonal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPurchasedDate = getArguments().getString(ARG_PARAM1);
            mFinishedDate = getArguments().getString(ARG_PARAM2);
            mLocation = getArguments().getString(ARG_PARAM3);
            mNote = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_edit_personal, container, false);

        etPurchasedDate = v.findViewById(R.id.book_PURCHASED_DATE);
        etFinishedDate = v.findViewById(R.id.book_FINISHED_DATE);
        etLocation = v.findViewById(R.id.book_LOCATION);
        etNote = v.findViewById(R.id.book_NOTE);

        etPurchasedDate.setText(mPurchasedDate);
        etFinishedDate.setText(mFinishedDate);
        etLocation.setText(mLocation);
        etNote.setText(mNote);

        etPurchasedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFromCalendar(1);
            }
        });
        etFinishedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFromCalendar(2);
            }
        });
        etLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mLocation = etLocation.getText().toString();
                    mListener.onFragmentInteraction(mPurchasedDate, mFinishedDate, mLocation, mNote);
                }
            }
        });
        etNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mNote = etNote.getText().toString();
                    mListener.onFragmentInteraction(mPurchasedDate, mFinishedDate, mLocation, mNote);
                }
            }
        });

        etNote.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mNote = etNote.getText().toString();
                    mListener.onFragmentInteraction(mPurchasedDate, mFinishedDate, mLocation, mNote);
                }

                return false;
            }
        });
        return v;
    }

    private void setDateFromCalendar(int i) {
        final Calendar mCalendar = Calendar.getInstance();
        final String mFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(mFormat, Locale.US);
        final int ordinal = i;

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (ordinal == 1) {
                    etPurchasedDate.setText(sdf.format(mCalendar.getTime()));
                    mPurchasedDate = etPurchasedDate.getText().toString();
                } else if (ordinal == 2) {
                    etFinishedDate.setText(sdf.format(mCalendar.getTime()));
                    mFinishedDate = etFinishedDate.getText().toString();
                }

                mListener.onFragmentInteraction(mPurchasedDate, mFinishedDate, mLocation, mNote);
            }
        };

        new DatePickerDialog(getContext(), dateListener, mCalendar
                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(String PurchasedDate, String finishedDate, String location, String note);
    }
}
