package com.example.buddyppb.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "result_journal")
public class ResultJournal implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "result_id")
    private int resultId;

    @ColumnInfo(name = "journal_id")
    private int journalId;

    @ColumnInfo(name = "positive_percentage")
    private String positivePercentage;

    @ColumnInfo(name = "negative_percentage")
    private String negativePercentage;

    @ColumnInfo(name = "positive_count")
    private int positiveCount;

    @ColumnInfo(name = "negative_count")
    private int negativeCount;

    @ColumnInfo(name = "positive_words")
    private String positiveWords;

    @ColumnInfo(name = "negative_words")
    private String negativeWords;

    // Empty constructor (Room needs this)
    public ResultJournal() {}

    // Full constructor (optional)
    public ResultJournal(int resultId, Integer id, String positivePercentage, String negativePercentage,
                         int positiveCount, int negativeCount,
                         String positiveWords, String negativeWords) {
        this.resultId = resultId;
        this.journalId = id;
        this.positivePercentage = positivePercentage;
        this.negativePercentage = negativePercentage;
        this.positiveCount = positiveCount;
        this.negativeCount = negativeCount;
        this.positiveWords = positiveWords;
        this.negativeWords = negativeWords;
    }

    // Parcelable constructor
    protected ResultJournal(Parcel in) {
        resultId = in.readInt();
        journalId = in.readInt();
        positivePercentage = in.readString();
        negativePercentage = in.readString();
        positiveCount = in.readInt();
        negativeCount = in.readInt();
        positiveWords = in.readString();
        negativeWords = in.readString();
    }

    public static final Creator<ResultJournal> CREATOR = new Creator<ResultJournal>() {
        @Override
        public ResultJournal createFromParcel(Parcel in) {
            return new ResultJournal(in);
        }

        @Override
        public ResultJournal[] newArray(int size) {
            return new ResultJournal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resultId);
        dest.writeInt(journalId);
        dest.writeString(positivePercentage);
        dest.writeString(negativePercentage);
        dest.writeInt(positiveCount);
        dest.writeInt(negativeCount);
        dest.writeString(positiveWords);
        dest.writeString(negativeWords);
    }

    // Getters & Setters

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getJournalId() {
        return journalId;
    }

    public void setJournalId(int journalId) {
        this.journalId = journalId;
    }


    public String getPositivePercentage() {
        return positivePercentage;
    }

    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    public String getNegativePercentage() {
        return negativePercentage;
    }

    public void setNegativePercentage(String negativePercentage) {
        this.negativePercentage = negativePercentage;
    }

    public int getPositiveCount() {
        return positiveCount;
    }

    public void setPositiveCount(int positiveCount) {
        this.positiveCount = positiveCount;
    }

    public int getNegativeCount() {
        return negativeCount;
    }

    public void setNegativeCount(int negativeCount) {
        this.negativeCount = negativeCount;
    }

    public String getPositiveWords() {
        return positiveWords;
    }

    public void setPositiveWords(String positiveWords) {
        this.positiveWords = positiveWords;
    }

    public String getNegativeWords() {
        return negativeWords;
    }

    public void setNegativeWords(String negativeWords) {
        this.negativeWords = negativeWords;
    }
}
