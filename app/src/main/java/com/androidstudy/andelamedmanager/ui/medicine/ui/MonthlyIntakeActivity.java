package com.androidstudy.andelamedmanager.ui.medicine.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.androidstudy.andelamedmanager.R;
import com.androidstudy.andelamedmanager.data.model.Medicine;
import com.androidstudy.andelamedmanager.ui.medicine.adapter.MonthlyMedicineAdapter;
import com.androidstudy.andelamedmanager.ui.medicine.viewmodel.MedicineViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthlyIntakeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_empty)
    FrameLayout emptyFrame;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_intake);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.activity_monthly_intake));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MedicineViewModel medicineViewModel = ViewModelProviders.of(this).get(MedicineViewModel.class);
        medicineViewModel.getMedicineList().observe(this, medicines -> {
            if (MonthlyIntakeActivity.this.medicineList == null) {
                setListData(medicines);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }

    public void setListData(final List<Medicine> medicineList) {
        this.medicineList = medicineList;

        if (medicineList.isEmpty()) {
            emptyFrame.setVisibility(View.VISIBLE);
        }

        MonthlyMedicineAdapter monthlyMedicineAdapter = new MonthlyMedicineAdapter(this, medicineList, (v, position) -> {
            Medicine medicine = medicineList.get(position);
            Intent intent = new Intent(getApplicationContext(), MedicineActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("MEDICINE", medicine);
            intent.putExtras(b);
            startActivity(intent);

        });

        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(monthlyMedicineAdapter);
    }
}
