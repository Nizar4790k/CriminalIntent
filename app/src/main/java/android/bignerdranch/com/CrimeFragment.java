package android.bignerdranch.com;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private  static  final  String ARG_CRIME_ID ="crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private  static final String DIALOG_TIME="DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME=2;
    private static final int REQUEST_CONTACT = 1;
    private static final int CALL_CONTACT = 3;




    private Crime mCrime;
    private EditText mTitleField;

    private CheckBox mSolvedCheckBox;

    private Button mDateButton;
    private Button mGoToFirst;
    private Button mGoToLast;
    private Button mTimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;

    public static CrimeFragment newInstance (UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return  crimeFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);






    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);


        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        mSuspectButton = v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });

        if(mCrime.getSuspect()!=null){
            mSuspectButton.setText(mCrime.getSuspect());
        }



        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                dialog.show(manager,DIALOG_TIME);


            }
        });



        mTitleField =  (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                mCrime.setTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton =(Button)v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);

            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mGoToFirst  = (Button) v.findViewById(R.id.btn_go_to_first);
        mGoToLast = (Button)v.findViewById(R.id.btn_go_to_last);

        mGoToFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCrime = CrimeLab.get(getContext()).getCrimes().get(0);
                Intent intent = CrimePagerActivity.newIntent(getContext(),mCrime.getId());
                startActivity(intent);



            }
        });



        mGoToLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Crime> crimes = CrimeLab.get(getContext()).getCrimes();

                mCrime = crimes.get(crimes.size()-1);
                Intent intent = CrimePagerActivity.newIntent(getContext(),mCrime.getId());
                startActivity(intent);

            }
        });

        List<Crime> crimes = CrimeLab.get(getContext()).getCrimes();

        if(mCrime.equals(crimes.get(0))){

            mGoToFirst.setEnabled(false);
        } else if(mCrime.equals(crimes.get(crimes.size()-1))){
            mGoToLast.setEnabled(false);
        }

        mTimeButton.setText(R.string.time_button);

        mReportButton = v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Activity activity = getActivity();

                Intent i = ShareCompat.IntentBuilder.from(activity)
                                                    .setType("text/plain")
                                                    .getIntent();



        //      i.setType("text/plain");

                i.putExtra(Intent.EXTRA_TEXT,getReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                i= Intent.createChooser(i,getString(R.string.send_report));
                if (i.resolveActivity(activity.getPackageManager()) != null){
                    startActivity(i);
                }



            }
        });


        mCallSuspectButton = v.findViewById(R.id.call_suspect);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+mCrime.getSuspectNumber()));
                startActivity(intent);



                
            }
        });





        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==REQUEST_DATE){

            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();

        } else if(requestCode==REQUEST_TIME){
            Date date = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateDate();

        }  else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();


            // Specify which fields you want your query to return
            // values for
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER
            };




            // Perform your query - the contactUri is like a "where"
            // clause here
            Cursor cursor = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                // Double-check that you actually got results
                if (cursor.getCount() == 0) {
                    return;
                }
                // Pull out the first column of the first row of data
                // that is your suspect's name
                cursor.moveToFirst();
                String suspect = cursor.getString(0);

                String number = cursor.getString(cursor.getColumnIndexOrThrow
                        (ContactsContract.CommonDataKinds.Phone.NUMBER));


                mCrime.setSuspectNumber(number);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                cursor.close();
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }



    @Override
   public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                Intent intent = new Intent(getContext(),CrimeListActivity.class);
                startActivity(intent);

                return true;

                default:
                    return super.onOptionsItemSelected(item);

        }
    }



    @Override
    public void onPause(){
        super.onPause();

        mCrime.setTitle(mTitleField.getText().toString());

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getReport(){

        String solvedString = null;

        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string  .crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);


        return report;
    }

}
