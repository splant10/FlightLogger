package ca.lakeland.plantsd.flightlogger;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by plantsd on 6/17/2016.
 * based off of http://stackoverflow.com/questions/3666007/how-to-serialize-object-to-csv-file
 */
public class FlightLogToCSV {

    private String csvSeparator;
    private Gson gson;

    public FlightLogToCSV(String separator) {
        csvSeparator = separator;
        gson = new Gson();
    }

    public String headerCSV() {

        String oneLine = new String();
        oneLine += "\"";
        oneLine += "Flight Log Number";
        oneLine += csvSeparator;
        oneLine += "Date";
        oneLine += csvSeparator;
        oneLine += "Serial Number";
        oneLine += csvSeparator;
        oneLine += "Location";
        oneLine += csvSeparator;
        oneLine += "Pilot";
        oneLine += csvSeparator;
        oneLine += "Spotter";
        oneLine += csvSeparator;
        oneLine += "Windspeed";
        oneLine += csvSeparator;
        oneLine += "Temperature";
        oneLine += csvSeparator;
        oneLine += "Weather Conditions";
        oneLine += csvSeparator;
        oneLine += "Purpose of Flight";
        oneLine += csvSeparator;
        oneLine += "Payload";
        oneLine += csvSeparator;
        oneLine += "# of Flights";
        oneLine += csvSeparator;

        oneLine += "Flights";
        oneLine += csvSeparator;

        oneLine += "Comments";
        oneLine += csvSeparator;
        oneLine += "Agency";
        oneLine += csvSeparator;
        oneLine += "Total Flight Time";
        oneLine += csvSeparator;
        oneLine += "Altitude";
        oneLine += csvSeparator;
        oneLine += "Admin Comments";
        oneLine += "\"";

        return oneLine;

    }

    public String flightToCSV(FlightLog flightLog){

        String oneLine = new String();
        oneLine += "\"";
        oneLine += flightLog.getFlightLogNum() <= 0 ? "" : flightLog.getFlightLogNum();
        oneLine += csvSeparator;

        String date = flightLog.getDate().trim();

        oneLine += date.length() <= 0 ? "" : date;
        oneLine += csvSeparator;
        oneLine += flightLog.getSerialNum().trim().length() <= 0 ? "" : flightLog.getSerialNum();
        oneLine += csvSeparator;
        oneLine += flightLog.getLocation().trim().length() <= 0 ? "" : flightLog.getLocation();
        oneLine += csvSeparator;
        oneLine += flightLog.getPilot().getName().trim().length() <= 0 ? "" : flightLog.getPilot().getName();
        oneLine += csvSeparator;
        oneLine += flightLog.getSpotter().trim().length() <= 0 ? "" : flightLog.getSpotter();
        oneLine += csvSeparator;
        oneLine += flightLog.getWindSpeed() <= 0 ? "0" : flightLog.getWindSpeed();
        oneLine += csvSeparator;
        oneLine += flightLog.getTemperature() <= 0 ? "0" : flightLog.getTemperature();
        oneLine += csvSeparator;

        String weather = flightLog.getWeatherConditions().trim();

        oneLine += weather.length() <= 0 ? "" : weather;
        oneLine += csvSeparator;

        String purpose = flightLog.getPurposeOfFlight().trim();

        oneLine += purpose.length() <= 0 ? "" : purpose;
        oneLine += csvSeparator;
        oneLine += flightLog.getPayloadType().trim().length() <= 0 ? "" : flightLog.getPayloadType();
        oneLine += csvSeparator;
        oneLine += flightLog.getFlights().size() <= 0 ? "" : flightLog.getFlights().size();
        oneLine += csvSeparator;

        try {
            String flightsJson = gson.toJson(flightLog.getFlights());
            flightsJson = flightsJson.replace("\"", "\"\""); // need to do this so csv knows to ignore double quotes in the field
            oneLine += flightsJson;
        } catch (Exception e) {

        }
        oneLine += csvSeparator;

        String comments = flightLog.getComments().trim();

        oneLine += comments.length() <= 0 ? "" : comments;
        oneLine += csvSeparator;
        oneLine += "LLC";
        oneLine += csvSeparator;
        oneLine += flightLog.getAccumFlightTime() <= 0 ? "0" : flightLog.getAccumFlightTime();
        oneLine += csvSeparator;
        oneLine += flightLog.getMaxAltitude() <= 0 ? "0" : flightLog.getMaxAltitude();
        oneLine += csvSeparator;

        try {
            for (AdminComment ac : flightLog.getAdminComments()) {
                String comment = ac.getComment().trim();

                oneLine += comment.length() <= 0 ? "" : comment;
                oneLine += "; ";
            }
        } catch (Exception e) {}

        oneLine += "\"";
        return oneLine;

    }
}
