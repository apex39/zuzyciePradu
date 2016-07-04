package matmar.zuzyciepradu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by timo on 30.06.16.
 */
public class ValuesRequest {
    private static final String VALUES_URL = "http://electricity-testing9569.rhcloud.com/?action=value&controller=index";
    private static final String KEY_METER = "meter_id";
    private static final String KEY_STARTTIME = "from_time";
    private static final String KEY_ENDTIME = "to_time";
    private static final String TAG = ValuesRequest.class.getSimpleName();

    private final ArrayList<Integer> devices;
    private final long startTime;
    private final long endTime;
    Context context;
    RequestQueue requestQueue;
    String sessionCookie;
    private MainActivity mainActivity;

    public ValuesRequest(final long startTime, final long endTime, ArrayList<Integer> devices, Context context, String sessionCookie) {
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        this.devices = devices;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionCookie = sessionCookie;

    }

    private void sendRequest(final long startTime, final long endTime, final int device){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VALUES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Values values = new Values(device);
                        Values.Value[] receivedValues = gson.fromJson(response,Values.Value[].class);
                        values.setValues(receivedValues);
                        sendValues(values);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ValuesRequest.this.context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_METER, String.valueOf(device));
                params.put(KEY_STARTTIME, String.valueOf(startTime));
                params.put(KEY_ENDTIME, String.valueOf(endTime));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                    HashMap<String, String> headers = new HashMap<>();
                    Log.d(TAG, sessionCookie);
                    headers.put("Cookie", sessionCookie);
                    return headers;
                    }
        };
        requestQueue.add(stringRequest);
    }

    public void sendRequest(MainActivity mainActivity) {
        if(this.mainActivity == null) this.mainActivity = mainActivity;
        for(int element : devices){
            sendRequest(startTime, endTime, element+1);
        }
    }
    private void sendValues(Values values){
        mainActivity.addValues(values);
    }
}
