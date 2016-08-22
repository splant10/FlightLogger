package ca.lakeland.plantsd.flightlogger.Objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by plantsd on 6/3/2016.
 */
public class DoneChecklist implements Serializable {
    private String date;
    private String author;

    public DoneChecklist(String date, String author) {
        this.date = date;
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

}
