package matmar.zuzyciepradu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements
        DeviceDialogFragment.DeviceDialogListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private static final int START_DATE = 1;
    private static final int START_TIME = 2;
    private static final int FINAL_DATE = 3;
    private static final int FINAL_TIME = 4;
    private static final LocalDateTime JAN_1_1970 = new LocalDateTime(1970, 1, 1, 2, 0);
    private String sessionCookie;
    Toolbar toolbar;
    ArrayList<Values> valuesArrayList;
    LineChart chart;
    ArrayList<ILineDataSet> dataSets;
    LineData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionCookie = getIntent().getExtras().getString("SESSION_COOKIE");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        valuesArrayList = new ArrayList<>();
        chart = (LineChart) findViewById(R.id.chart);
        dataSets = new ArrayList<ILineDataSet>();

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
        DateTime startJodaTime = new DateTime(startYear,startMonth+1,startDay,startHour,startMinute);
        DateTimeZone timeZone = DateTimeZone.getDefault();
        long startTime = new Duration(JAN_1_1970.toDateTime(timeZone), startJodaTime).getMillis() / 1000;// UNIX TIMESTAMP

        DateTime finalJodaTime = new DateTime(finalYear,finalMonth+1,finalDay,finalHour,finalMinute);
        long finalTime = new Duration(JAN_1_1970.toDateTime(timeZone), finalJodaTime).getMillis() / 1000;

        ValuesRequest valuesRequest = new ValuesRequest(startTime, finalTime, selectedDevices, this, sessionCookie);
        valuesRequest.sendRequest(this);
        return false;
    }


    private void createChart(){
        dataSets = new ArrayList<ILineDataSet>();;
        for(Values values:valuesArrayList){
            ArrayList<Entry> vals1 = new ArrayList<>();
            for(int i = 0;i<values.getValues().size();i++){
                vals1.add(new Entry(i,Float.valueOf(values.getValues().get(i))));
            }

            LineDataSet setComp1 = new LineDataSet(vals1,String.valueOf(values.deviceId));

            dataSets.add(setComp1);
            data = new LineData(dataSets);
            chart.setData(data);
            chart.invalidate();
        }

    }
    public void addValues(Values values){
        this.valuesArrayList.add(values);
        createChart();
    }
}
