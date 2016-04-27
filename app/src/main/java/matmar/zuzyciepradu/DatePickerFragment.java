package matmar.zuzyciepradu;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import static android.app.DatePickerDialog.*;

/**
 * Created by timo on 27.04.16.
 */
public class DatePickerFragment extends DialogFragment {
    OnDateSetListener dateListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dateListener = (OnDateSetListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnDateSetListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new android.app.DatePickerDialog(getActivity(),(MainActivity) getActivity(), year,month,day);
    }
}
