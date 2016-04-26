package matmar.zuzyciepradu;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DeviceDialogFragment.DeviceDialogListener {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    SublimePicker sublimePicker;
    SublimeListenerAdapter pickerListener = new SublimeListenerAdapter() {
        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {

        }

        @Override
        public void onCancelled() {
        }
    };

    DeviceDialogFragment deviceDialogFragment;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_date):
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE,0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
                break;

            case (R.id.action_device):
                if(deviceDialogFragment == null){
                    deviceDialogFragment = new DeviceDialogFragment();
                }
                if(selectedDevices != null){
                    Bundle bundle = new Bundle();
                    bundle.putIntegerArrayList("selected_devices",selectedDevices);
                    deviceDialogFragment.setArguments(bundle);
                }
                deviceDialogFragment.show(getSupportFragmentManager(), "DeviceDialogFragment");

        }
        return super.onOptionsItemSelected(item);
    }

    ArrayList selectedDevices;
    @Override
    public void onDialogPositiveClick(DeviceDialogFragment dialog) {
        selectedDevices = dialog.getSelectedDevices();
    }
}
