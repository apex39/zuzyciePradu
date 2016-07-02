package matmar.zuzyciepradu;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by timo on 03.07.16.
 */
public class Values {
    int deviceId;

    public Values(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setValues(ArrayList<Value> values) {

        this.values = values;
    }

    ArrayList<Value> values;
    public class Value{
        DateTime timestamp;
        Integer value;
    }
}
