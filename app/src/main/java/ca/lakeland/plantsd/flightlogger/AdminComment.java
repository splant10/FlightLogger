package ca.lakeland.plantsd.flightlogger;

import java.io.Serializable;

/**
 * Created by plantsd on 6/15/2016.
 */
public class AdminComment implements Serializable {
    private String comment;
    private String date;

    public AdminComment(String c, String d) {
        this.comment = c;
        this.date = d;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}
