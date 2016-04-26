package matmar.zuzyciepradu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by timo on 25.04.16.
 */
public class DeviceDialogFragment extends DialogFragment {
    public interface DeviceDialogListener {
        public void onDialogPositiveClick(DeviceDialogFragment dialog);
    }

    DeviceDialogListener dialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (DeviceDialogListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement DeviceDialogListener");
        }
    }
    ArrayList<Integer> selectedDevices;
    boolean[] checkedItems;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        checkedItems = new boolean[getResources().getStringArray(R.array.devices).length];
        if(getArguments() == null){
            /*All devices are selected by default*/
            selectedDevices = new ArrayList();
            selectedDevices.addAll(Arrays.asList(0, 1, 2));

        /*Get number of devices, and create boolean array to have them enabled by default*/
            Arrays.fill(checkedItems,true);
        }
        else {
            selectedDevices = getArguments().getIntegerArrayList("selected_devices");
            for (int device : selectedDevices) {
                checkedItems[device]=true;
            }
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.check_devices)
            .setMultiChoiceItems(R.array.devices, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if(b)
                        selectedDevices.add(i);
                    else if(selectedDevices.contains(i))
                        selectedDevices.remove(Integer.valueOf(i));
                }
            })
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogListener.onDialogPositiveClick(DeviceDialogFragment.this);
                }
            });
        return builder.create();
    }
    public ArrayList getSelectedDevices(){
        return selectedDevices;
    }
}
