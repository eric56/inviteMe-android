package br.com.android.invviteme.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.android.invviteme.R;

/**
 * Created by Eric on 26/08/2016.
 */
public class FragmentNewEventStep1 extends Fragment {

    private DatePickerDialog dateInitEventPickerDialog;
    private DatePickerDialog dateFinishEventPickerDialog;
    private SimpleDateFormat dateFormatter;

    private TextView initEvent, finishEvent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_event_step1,container,false);
        referenceUI(view);
        return view;
    }

    private void referenceUI(View view){
        initEvent = (TextView) view.findViewById(R.id.dateInitEvent);
        finishEvent = (TextView) view.findViewById(R.id.dateFinishEvent);
    }

    private void configureDatePickerDialogs(){
        Calendar newCalendar = Calendar.getInstance();
        dateInitEventPickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                initEvent.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

}
