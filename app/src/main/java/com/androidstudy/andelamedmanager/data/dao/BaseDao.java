package com.androidstudy.andelamedmanager.data.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * This is a base DAO that will hold common SQL Queries mapped to methods.
 * @param <T> The data type of the entity you want to work with.
 */
public interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    void insertData(T data);

    @Delete
    void deleteData(T data);
}
