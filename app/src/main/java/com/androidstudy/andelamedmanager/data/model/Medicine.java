package com.androidstudy.andelamedmanager.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity
public class Medicine implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long medicineId;
    // private long userId;
    private String name;
    private String description;
    private String interval;
    private String pills;
    private String pillsTaken;
    private boolean hasNotification;  //User can switch them on/off per Med //Next Version - Hahaha, No time :)
    private Date startDate;
    private Date endDate;
    private int days;

    public Medicine(String name, String description, String interval, String pills, String pillsTaken, boolean hasNotification, Date startDate, Date endDate, int days) {
        this.name = name;
        this.description = description;
        this.interval = interval;
        this.pills = pills;
        this.pillsTaken = pillsTaken;
        this.hasNotification = hasNotification;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
    }

    @Ignore
    protected Medicine(Parcel in) {
        medicineId = in.readLong();
        name = in.readString();
        description = in.readString();
        interval = in.readString();
        pills = in.readString();
        pillsTaken = in.readString();
        hasNotification = in.readByte() != 0;
        days = in.readInt();
    }

    @Ignore
    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getPills() {
        return pills;
    }

    public void setPills(String pills) {
        this.pills = pills;
    }

    public String getPillsTaken() {
        return pillsTaken;
    }

    public void setPillsTaken(String pillsTaken) {
        this.pillsTaken = pillsTaken;
    }

    public boolean isHasNotification() {
        return hasNotification;
    }

    public void setHasNotification(boolean hasNotification) {
        this.hasNotification = hasNotification;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(medicineId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(interval);
        dest.writeString(pills);
        dest.writeString(pillsTaken);
        dest.writeByte((byte) (hasNotification ? 1 : 0));
        dest.writeInt(days);
    }
}
