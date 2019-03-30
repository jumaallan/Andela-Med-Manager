package com.androidstudy.andelamedmanager.ui.medicine.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.androidstudy.andelamedmanager.R;
import com.androidstudy.andelamedmanager.data.model.MedData;
import com.androidstudy.andelamedmanager.data.model.Medicine;
import com.androidstudy.andelamedmanager.ui.medicine.adapter.MedicineSparkAdapter;
import com.robinhood.spark.SparkView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicineActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textViewMedicineName)
    TextView textViewMedicineName;
    @BindView(R.id.sparkView)
    SparkView sparkView;
    @BindView(R.id.textViewOne)
    TextView textViewOne;
    @BindView(R.id.textViewTwo)
    TextView textViewTwo;
    @BindView(R.id.textViewThree)
    TextView textViewThree;
    @BindView(R.id.textViewFour)
    TextView textViewFour;
    @BindView(R.id.textViewInterval)
    TextView textViewInterval;
    @BindView(R.id.textViewMedPills)
    TextView textViewMedPills;
    @BindView(R.id.textViewMedicinePercentage)
    TextView textViewMedicinePercentage;
    @BindView(R.id.textViewMedDescription)
    TextView textViewMedDescription;

    private List<MedData> medDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.activity_add_medicine));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        medDataList = getDummyMedData();

        setupSparkView();

        Intent in = getIntent();
        Bundle b = in.getExtras();

        assert b != null;
        Medicine medicine = b.getParcelable("MEDICINE");
        assert medicine != null;
        textViewMedicineName.setText(medicine.getName());
        //Calculate Percentage
        int takePercentage = (Integer.parseInt(medicine.getPillsTaken()) * 100 / Integer.parseInt(medicine.getPills()));
        textViewMedicinePercentage.setText(String.format("%d%s", 100 - takePercentage, getString(R.string.percentage)));

        textViewMedDescription.setText(medicine.getDescription());
        textViewInterval.setText(medicine.getInterval() + " Times");
        textViewMedPills.setText(medicine.getPills());

        switch (medicine.getInterval()) {
            case "1":
                textViewOne.setText("8:00 AM");
                break;
            case "2":
                textViewOne.setText("8:00 AM");
                textViewThree.setText("5:00 PM");
                break;
            case "3":
                textViewOne.setText("8:00 AM");
                textViewTwo.setText("12:00 Noon");
                textViewThree.setText("5:00 PM");
                break;
            case "4":
                textViewOne.setText("8:00 AM");
                textViewTwo.setText("12:00 Noon");
                textViewThree.setText("5:00 PM");
                textViewFour.setText("10:00 PM");
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }

    private void setupSparkView() {
        MedicineSparkAdapter adapter = new MedicineSparkAdapter(medDataList);
        sparkView.setAdapter(adapter);
    }

    private List<MedData> getDummyMedData() {
        List<MedData> listViewItems = new ArrayList<>();
        listViewItems.add(new MedData(2L, 5L));
        listViewItems.add(new MedData(3L, 5L));
        listViewItems.add(new MedData(5L, 8L));
        listViewItems.add(new MedData(4L, 8L));
        listViewItems.add(new MedData(8L, 14L));
        listViewItems.add(new MedData(9L, 13L));
        listViewItems.add(new MedData(10L, 16L));
        return listViewItems;
    }
}
