package com.androidstudy.andelamedmanager.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.androidstudy.andelamedmanager.data.model.Medicine;

import java.util.List;

@Dao
public abstract class MedicineDao implements BaseDao<Medicine> {

    //Get all Medicine
    @Query("SELECT * FROM Medicine")
    public abstract LiveData<List<Medicine>> getAllMedicine();

    @Query("DELETE FROM Medicine")
    public abstract void deleteAll();
}
