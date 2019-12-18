package android.bignerdranch.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private  static  final String ARG_DATE="date";

    private  DatePicker mDatePicker;

    public static final String EXTRA_DATE =
            "com.bignerdranch.android.criminalintent.date";

    private  int mHours;
    private int mMinutes;



    private  void sendResult(int resultCode,Date date){
        if(getTargetFragment()==null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);

    }



    public static DatePickerFragment newInstance(Date date){

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);


        
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState){

        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

         mMinutes =calendar.get(Calendar.MINUTE);
         mHours = calendar.get(Calendar.HOUR);



        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year,month,day,null);



        /*
        DatePicker datePicker = new DatePicker(getActivity()); //Deprecated


        datePicker.setCalendarViewShown(true); // Deprecated
        datePicker.setSpinnersShown(false);// Deprecated
        */
        return new AlertDialog.Builder(getActivity()).setView(v)
                .setTitle(R.string.date_picker_title).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
                        calendar.set(Calendar.HOUR,mHours);
                        calendar.set(Calendar.MINUTE,mMinutes);


                        Date date = calendar.getTime();
                        sendResult(Activity.RESULT_OK, date);

                    }
                }).create();
    }

}




