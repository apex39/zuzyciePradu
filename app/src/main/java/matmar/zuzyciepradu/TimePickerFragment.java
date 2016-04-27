package matmar.zuzyciepradu;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import static android.app.DatePickerDialog.*;

/**
 * Created by timo on 27.04.16.
 */
public class TimePickerFragment extends DialogFragment {
    TimePickerDialog.OnTimeSetListener timeListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            timeListener = (TimePickerDialog.OnTimeSetListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnTimeSetListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new android.app.TimePickerDialog(getActivity(),(MainActivity) getActivity(), 0,0, true);
    }
}
