package android.bignerdranch.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME="time";

    private TimePicker mTimePicker;

    public  static final String EXTRA_TIME= "com.bignerdranch.android.criminalintent.time";


    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME,date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;


    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState){

        Date date = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        int minutes = calendar.get(Calendar.MINUTE);
        int hours = calendar.get(Calendar.HOUR);




        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
           mTimePicker.setHour(hours);
           mTimePicker.setMinute(minutes);
        } else {
            mTimePicker.setCurrentHour(hours);
            mTimePicker.setCurrentMinute(minutes);

        }







        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_time).setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int hour;
                        int minutes;

                        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                            hour= mTimePicker.getHour();
                            minutes = mTimePicker.getMinute();
                        } else {
                            hour =mTimePicker.getCurrentHour();
                            minutes = mTimePicker.getCurrentMinute();

                        }

                        Date date = (Date) getArguments().getSerializable(ARG_TIME);

                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR,hour);
                        calendar.set(Calendar.MINUTE,minutes);

                        date= calendar.getTime();




                        sendResult(Activity.RESULT_OK, date);

                    }
                }).create();

    }


    private  void sendResult(int resultCode,Date date){

        if(getTargetFragment()==null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);


    }

}
