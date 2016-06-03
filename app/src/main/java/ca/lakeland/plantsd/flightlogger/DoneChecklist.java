package ca.lakeland.plantsd.flightlogger;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by plantsd on 6/3/2016.
 */
public class DoneChecklist implements Serializable {
    private String date;
    private String pilot;
    private Bitmap signature;

    public DoneChecklist(String date, String pilot, Bitmap bmp) {
        this.date = date;
        this.pilot = pilot;
        this.signature = bmp;
    }

    public String getDate() {
        return date;
    }

    public String getPilot() {
        return pilot;
    }

    public Bitmap getSignature() {
        return signature;
    }
}
