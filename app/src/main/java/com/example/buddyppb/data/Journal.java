package com.example.buddyppb.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journal")
public class Journal implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "initialTimestamp")
    private Long initialTimestamp;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "isAnalyzed")
    private boolean isAnalyzed;

    // ===== Default Constructor (Required by Room) =====
    public Journal() { }

    // ===== Full Constructor =====
    public Journal(int id, String title, String image, String description,
                   Long initialTimestamp, String timestamp, boolean isAnalyzed) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.initialTimestamp = initialTimestamp;
        this.timestamp = timestamp;
        this.isAnalyzed = isAnalyzed;
    }

    // ===== Getter & Setter =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getInitialTimestamp() { return initialTimestamp; }
    public void setInitialTimestamp(Long initialTimestamp) { this.initialTimestamp = initialTimestamp; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isAnalyzed() { return isAnalyzed; }
    public void setAnalyzed(boolean analyzed) { isAnalyzed = analyzed; }

    // ===== Parcelable Constructor =====
    protected Journal(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        description = in.readString();

        if (in.readByte() == 0) {
            initialTimestamp = null;
        } else {
            initialTimestamp = in.readLong();
        }

        timestamp = in.readString();
        isAnalyzed = in.readByte() != 0;
    }

    // ===== Parcelable Implementation =====
    public static final Creator<Journal> CREATOR = new Creator<Journal>() {
        @Override
        public Journal createFromParcel(Parcel in) {
            return new Journal(in);
        }

        @Override
        public Journal[] newArray(int size) {
            return new Journal[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(description);

        if (initialTimestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(initialTimestamp);
        }

        dest.writeString(timestamp);
        dest.writeByte((byte) (isAnalyzed ? 1 : 0));
    }
}
