package ca.lakeland.plantsd.flightlogger;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by plantsd on 6/8/2016.
 *
 * some code structure from
 * http://stackoverflow.com/questions/15792186/singleton-pattern-with-combination-of-lazy-loading-and-thread-safety
 */
public class Storage {

    private FlightNum flightNum;
    private List<Pilot> pilots;
    private List<String> spotters;
    private List<DoneChecklist> doneChecklists;
    private List<FlightLog> flightLogs;
    private List<String> emails;
    private String adminPassword;
    private File csvFile;
    static private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/flightlogs.csv";

    private List<String> payloads = Arrays.asList("QX100","RX100","ADC-Micro");
    public List<String> getPayloads() {
        return payloads;
    }

    private Storage() {
        flightNum = getFlightNum();
        pilots = getPilots();
        spotters = getSpotters();
        doneChecklists = getDoneChecklists();
        flightLogs = getFlightLogs();
        emails = getEmails();
        adminPassword = "admin"; // beaut
        csvFile = getCsvFile();
    }

    private static class LazyHolder {
        private static final Storage INSTANCE = new Storage();
    }

    public static Storage getInstance() {
        return LazyHolder.INSTANCE;
    }

    // Lazy singleton structure for global objects
    public FlightNum getFlightNum() {
        if (flightNum == null) {
            flightNum = new FlightNum();
        }
        return flightNum;
    }


    public List<Pilot> getPilots() {
        if (pilots == null) {
            pilots = new ArrayList<Pilot>();
        }
        return pilots;
    }

    public List<String> getSpotters() {
        if (spotters == null) {
            spotters = new ArrayList<String>();
        }
        return spotters;
    }


    public List<DoneChecklist> getDoneChecklists() {
        if (doneChecklists == null) {
            doneChecklists = new ArrayList<DoneChecklist>();
        }
        return doneChecklists;
    }


    public List<FlightLog> getFlightLogs() {
        if (flightLogs == null) {
            flightLogs = new ArrayList<FlightLog>();
        }
        return flightLogs;
    }

    public List<String> getEmails() {
        if (emails == null) {
            emails = new ArrayList<>();
        }
        return emails;
    }

    // Only the best security for us.
    public String getAdminPassword() {
        return this.adminPassword;
    }

    // Setters
    public void setFlightNum(FlightNum flightnum) {
        this.flightNum = flightnum;
    }

    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }

    public void setSpotters(List<String> spotters) {
        this.spotters = spotters;
    }

    public void setDoneChecklists(List<DoneChecklist> doneChecklists) {
        this.doneChecklists = doneChecklists;
    }

    public void setFlightLogs(List<FlightLog> flightLogs) {
        this.flightLogs = flightLogs;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public void setAdminPassword(String pw) {
        this.adminPassword = pw;
    }

    public File getCsvFile() {
        if (csvFile == null) {
            csvFile = new File(filePath);
        }
        return csvFile;
    }

    public void setCsvFile(File f) {
        this.csvFile = f;
    }

    public void setCsvFile(String s) {
        getCsvFile().delete();

        try {
            PrintWriter out = new PrintWriter(new FileWriter(getCsvFile(), false));
            out.append(s);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // http://stackoverflow.com/a/13357785
    // Need the next two methods for amending flight logs to have admin comments
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    // http://stackoverflow.com/a/13357785
    public static List<String> getStringListFromFile() throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();

        String[] temp = ret.split("\n");
        return new ArrayList<String>(Arrays.asList(temp));
    }

    public void clearAllData() {
        this.flightNum = null;
        this.pilots = null;
        this.spotters = null;
        this.doneChecklists = null;
        this.flightLogs = null;
        this.emails = null;
        this.csvFile = null;
        File file = new File(filePath);
        boolean deleted = file.delete();
    }
}
