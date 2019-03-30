package com.androidstudy.andelamedmanager.ui.medicine.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidstudy.andelamedmanager.R;
import com.androidstudy.andelamedmanager.ui.main.ui.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMedicineActivity extends AppCompatActivity {

    @BindView(R.id.editTextMedicineName)
    EditText editTextMedicineName;
    @BindView(R.id.editTextMedicineDescription)
    EditText editTextMedicineDescription;
    @BindView(R.id.editTextMedicineStartDate)
    EditText editTextMedicineStartDate;
    @BindView(R.id.editTextMedicineEndDate)
    EditText editTextMedicineEndDate;
    @BindView(R.id.buttonContinue)
    Button buttonContinue;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textViewOne)
    TextView textViewOne;
    @BindView(R.id.textViewTwo)
    TextView textViewTwo;
    @BindView(R.id.textViewThree)
    TextView textViewThree;
    @BindView(R.id.textViewFour)
    TextView textViewFour;

    String name, description, startDate, endDate, pills;
    int interval, days;
    SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.activity_add_medicine));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final DatePickerDialog.OnDateSetListener startMedDate = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            AddMedicineActivity.this.updateStartMedLabel(startDate);
        };

        final DatePickerDialog.OnDateSetListener endMedDate = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            AddMedicineActivity.this.updateEndMedLabel(endDate);
        };

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = simpleDateFormat.format(calendar.getTime());
        editTextMedicineStartDate.setText(today);
        editTextMedicineEndDate.setText(today);

        interval = 1;

        editTextMedicineStartDate.setOnClickListener(v -> {
            new DatePickerDialog(AddMedicineActivity.this, startMedDate, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        editTextMedicineEndDate.setOnClickListener(v -> {
            new DatePickerDialog(AddMedicineActivity.this, endMedDate, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        buttonContinue.setOnClickListener(view -> getPills());
    }

    private void getPills() {
        //Get Number of Days * Intervals to get the Number of Pills
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDateMed = editTextMedicineStartDate.getText().toString().trim();
        String endDateMed = editTextMedicineEndDate.getText().toString().trim();

        try {
            Date date1 = myFormat.parse(startDateMed);
            Date date2 = myFormat.parse(endDateMed);
            long diff = date2.getTime() - date1.getTime();

            pills = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) * interval);
            days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            validateMedicalDetails();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateStartMedLabel(String start) {
        String date = simpleDateFormat.format(calendar.getTime());
        editTextMedicineStartDate.setText(date);
        startDate = start;
    }

    private void updateEndMedLabel(String end) {
        String date = simpleDateFormat.format(calendar.getTime());
        editTextMedicineEndDate.setText(date);
        endDate = end;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }

    @OnClick(R.id.textViewOne)
    public void textViewOne(View view) {
        textViewOne.setBackgroundResource(R.drawable.bg_blue_button_sharp);
        textViewOne.setTextColor(getResources().getColor(R.color.white));

        textViewTwo.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewTwo.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewThree.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewThree.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewFour.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewFour.setTextColor(getResources().getColor(R.color.bg_login_button));

        interval = 1;
    }

    @OnClick(R.id.textViewTwo)
    public void textViewTwo(View view) {
        textViewOne.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewOne.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewTwo.setBackgroundResource(R.drawable.bg_blue_button_sharp);
        textViewTwo.setTextColor(getResources().getColor(R.color.white));

        textViewThree.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewThree.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewFour.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewFour.setTextColor(getResources().getColor(R.color.bg_login_button));

        interval = 2;
    }

    @OnClick(R.id.textViewThree)
    public void textViewThree(View view) {
        textViewOne.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewOne.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewTwo.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewTwo.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewThree.setBackgroundResource(R.drawable.bg_blue_button_sharp);
        textViewThree.setTextColor(getResources().getColor(R.color.white));

        textViewFour.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewFour.setTextColor(getResources().getColor(R.color.bg_login_button));

        interval = 3;
    }

    @OnClick(R.id.textViewFour)
    public void textViewFour(View view) {
        textViewOne.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewOne.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewTwo.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewTwo.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewThree.setBackgroundResource(R.drawable.bg_white_button_sharp);
        textViewThree.setTextColor(getResources().getColor(R.color.bg_login_button));

        textViewFour.setBackgroundResource(R.drawable.bg_blue_button_sharp);
        textViewFour.setTextColor(getResources().getColor(R.color.white));

        interval = 4;
    }

    private void validateMedicalDetails() {

        name = editTextMedicineName.getText().toString().trim();
        description = editTextMedicineDescription.getText().toString().trim();
        startDate = editTextMedicineStartDate.getText().toString().trim();
        endDate = editTextMedicineEndDate.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextMedicineName.setError("Please Enter Medicine Name");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            editTextMedicineDescription.setError("Please Enter Medicine Description");
            return;
        }

        if (TextUtils.isEmpty(startDate)) {
            editTextMedicineStartDate.setError("Please Enter Medicine Start Date");
            return;
        }

        if (TextUtils.isEmpty(endDate)) {
            editTextMedicineEndDate.setError("Please Enter Medicine End Date");
            return;
        }

        saveMedicalData();
    }

    private void saveMedicalData() {
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);

        Intent medicine = new Intent(this, ConfirmMedicineActivity.class);
        Bundle bundle = new Bundle();
        //TODO :: Change this :)
        //TODO :: CALCULATE PILLS

        //Pass data via Bundle
        bundle.putString("name", name);
        bundle.putString("description", description);
        bundle.putString("interval", String.valueOf(interval));
        bundle.putString("startDate", startDate);
        bundle.putString("endDate", endDate);
        bundle.putString("pills", pills);
        bundle.putInt("days", days);
        medicine.putExtras(bundle);
        startActivity(medicine);
    }
}
