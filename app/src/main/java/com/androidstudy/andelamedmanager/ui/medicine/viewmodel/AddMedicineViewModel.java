package com.androidstudy.andelamedmanager.ui.medicine.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import com.androidstudy.andelamedmanager.data.AppDatabase;
import com.androidstudy.andelamedmanager.data.model.Medicine;


public class AddMedicineViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    public AddMedicineViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

    }

    public void addMedicine(final Medicine medicine) {
        new addAsyncTask(appDatabase).execute(medicine);
    }

    private static class addAsyncTask extends AsyncTask<Medicine, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Medicine... params) {
            db.medicineDao().insertData(params[0]);
            return null;
        }
    }
}
