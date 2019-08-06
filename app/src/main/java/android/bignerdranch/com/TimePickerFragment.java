package android.bignerdranch.com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState){
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_time).
                setPositiveButton(android.R.string.ok,null).create();

    }


}
