package matmar.zuzyciepradu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements
        DeviceDialogFragment.DeviceDialogListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private static final int START_DATE = 1;
    private static final int START_TIME = 2;
    private static final int FINAL_DATE = 3;
    private static final int FINAL_TIME = 4;
    private String sessionCookie;
    Toolbar toolbar;
    ArrayList<Values> values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionCookie = getIntent().getExtras().getString("SESSION_COOKIE");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    DeviceDialogFragment deviceDialogFragment;
    int dateElementWritten;
    DatePickerFragment datePickerFragment;
    TimePickerFragment timePickerFragment;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_date):
                datePickerFragment = new DatePickerFragment();
                dateElementWritten = START_DATE;
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
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
                deviceDialogFragment.show(getSupportFragmentManager(), "DeviceDialogFragment");//TODO: proof for opening many fragments
        }
        return super.onOptionsItemSelected(item);
    }

    ArrayList<Integer> selectedDevices;
    @Override
    public void onDialogPositiveClick(DeviceDialogFragment dialog) {
        selectedDevices = dialog.getSelectedDevices();
    }
    //TODO:Second date cannot be older than first one
    //TODO:Set checked dates and times on second time
    int startYear, startMonth, startDay;
    int finalYear, finalMonth, finalDay;
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if(dateElementWritten == START_DATE){
            startYear = i;
            startMonth = i1;
            startDay = i2;
            dateElementWritten = START_TIME;
            timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), "timePicker");
        } else if(dateElementWritten == FINAL_DATE){
            finalYear = i;
            finalMonth = i1;
            finalDay = i2;
            dateElementWritten = FINAL_TIME;
            timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), "timePicker");
        }
    }

    int startHour, startMinute;
    int finalHour, finalMinute;
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if(dateElementWritten == START_TIME){
            startHour = i;
            startMinute = i1;
            dateElementWritten = FINAL_DATE;
            datePickerFragment = new DatePickerFragment();
            datePickerFragment.getDialog();
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        } else if(dateElementWritten == FINAL_TIME){
            finalHour = i;
            finalMinute = i1;
        }
    }

    public boolean requestData(MenuItem item) {
        long startTime = new DateTime(startYear,startMonth,startDay,startHour,startMinute).getMillis() / 1000; // UNIX TIMESTAMP
        long finalTime = new DateTime(finalYear,finalMonth,finalDay,finalHour,finalMinute).getMillis() / 1000;

        ValuesRequest valuesRequest = new ValuesRequest(startTime, finalTime, selectedDevices, this, sessionCookie);
        valuesRequest.sendRequest(this);
        return false;
    }

    public void addValues(Values values){
        this.values.add(values);
    }
}
