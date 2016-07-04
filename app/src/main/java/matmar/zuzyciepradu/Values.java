package matmar.zuzyciepradu;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by timo on 03.07.16.
 */
public class Values {
    int deviceId;
    Value[] values;
    public Values(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }

    public ArrayList<Integer> getValues(){
        ArrayList<Integer> meter_values = new ArrayList<>();
        for(Value value : values){
            meter_values.add(value.meter_value);
        }
        return meter_values;
    }

   public ArrayList<String> getTime(){
       ArrayList<String> time = new ArrayList<>();
       for(Value value : values){
           time.add(value.TIME);
       }
       return time;
   }
    public class Value{
        String TIME;
        Integer meter_value;
    }
}
