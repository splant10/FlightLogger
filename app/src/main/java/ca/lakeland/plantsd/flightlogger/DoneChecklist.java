package ca.lakeland.plantsd.flightlogger;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by plantsd on 6/3/2016.
 */
public class DoneChecklist implements Serializable {
    private String date;
    private Pilot pilot;
    private Bitmap signature;

    public DoneChecklist(String date, Pilot pilot, Bitmap bmp) {
        this.date = date;
        this.pilot = pilot;
        this.signature = bmp;
    }

    public String getDate() {
        return date;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public Bitmap getSignature() {
        return signature;
    }
}
