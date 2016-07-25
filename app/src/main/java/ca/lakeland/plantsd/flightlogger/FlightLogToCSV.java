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
    private File file;

    public FlightLogToCSV(String separator, File file) {
        csvSeparator = separator;
        gson = new Gson();
        this.file = file;
    }

    public void writeHeaderToCSV() {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            StringBuffer oneLine = new StringBuffer();
            oneLine.append("\"");
            oneLine.append("Flight Log Number");
            oneLine.append(csvSeparator);
            oneLine.append("Date");
            oneLine.append(csvSeparator);
            oneLine.append("Serial Number");
            oneLine.append(csvSeparator);
            oneLine.append("Location");
            oneLine.append(csvSeparator);
            oneLine.append("Pilot");
            oneLine.append(csvSeparator);
            oneLine.append("Spotter");
            oneLine.append(csvSeparator);
            oneLine.append("Windspeed");
            oneLine.append(csvSeparator);
            oneLine.append("Temperature");
            oneLine.append(csvSeparator);
            oneLine.append("Weather Conditions");
            oneLine.append(csvSeparator);
            oneLine.append("Purpose of Flight");
            oneLine.append(csvSeparator);
            oneLine.append("Payload");
            oneLine.append(csvSeparator);
            oneLine.append("# of Flights");
            oneLine.append(csvSeparator);

            oneLine.append("Flights");
            oneLine.append(csvSeparator);

            oneLine.append("Comments");
            oneLine.append(csvSeparator);
            oneLine.append("Agency");
            oneLine.append(csvSeparator);
            oneLine.append("Total Flight Time");
            oneLine.append(csvSeparator);
            oneLine.append("Altitude");
            oneLine.append(csvSeparator);
            oneLine.append("Admin Comments");
            oneLine.append("\"");

            bw.write(oneLine.toString());
            bw.newLine();

            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {}
        catch (FileNotFoundException e){}
        catch (IOException e){}
    }

    public void addFlightToCSV(FlightLog flightLog){
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            // -- Add new flight
            StringBuffer oneLine = new StringBuffer();
            oneLine.append("\"");
            oneLine.append(flightLog.getFlightLogNum() <= 0 ? "" : flightLog.getFlightLogNum());
            oneLine.append(csvSeparator);

            String date = flightLog.getDate().trim();

            oneLine.append(date.length() <= 0 ? "" : date);
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getSerialNum().trim().length() <= 0 ? "" : flightLog.getSerialNum());
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getLocation().trim().length() <= 0 ? "" : flightLog.getLocation());
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getPilot().getName().trim().length() <= 0 ? "" : flightLog.getPilot().getName());
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getSpotter().trim().length() <= 0 ? "" : flightLog.getSpotter());
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getWindSpeed() <= 0 ? "0" : flightLog.getWindSpeed());
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getTemperature() <= 0 ? "0" : flightLog.getTemperature());
            oneLine.append(csvSeparator);

            String weather = flightLog.getWeatherConditions().trim();

            oneLine.append(weather.length() <= 0 ? "" : weather);
            oneLine.append(csvSeparator);

            String purpose = flightLog.getPurposeOfFlight().trim();

            oneLine.append(purpose.length() <= 0 ? "" : purpose);
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getPayloadType().trim().length() <= 0 ? "" : flightLog.getPayloadType());
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getFlights().size() <= 0 ? "" : flightLog.getFlights().size());
            oneLine.append(csvSeparator);

            try {
                String flightsJson = gson.toJson(flightLog.getFlights());
                flightsJson = flightsJson.replace("\"", "\"\""); // need to do this so csv knows to ignore double quotes in the field
                oneLine.append(flightsJson);
            } catch (Exception e) {

            }
            oneLine.append(csvSeparator);

            String comments = flightLog.getComments().trim();

            oneLine.append(comments.length() <= 0 ? "" : comments);
            oneLine.append(csvSeparator);
            oneLine.append("LLC");
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getAccumFlightTime() <= 0 ? "0" : flightLog.getAccumFlightTime());
            oneLine.append(csvSeparator);
            oneLine.append(flightLog.getMaxAltitude() <= 0 ? "0" : flightLog.getMaxAltitude());
            oneLine.append(csvSeparator);

            try {
                for (AdminComment ac : flightLog.getAdminComments()) {
                    String comment = ac.getComment().trim();

                    oneLine.append(comment.length() <= 0 ? "" : comment);
                    oneLine.append("; ");
                }
            } catch (Exception e) {}

            oneLine.append("\"");

            bw.write(oneLine.toString());
            bw.newLine();
            // --- end add new flight

            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {}
        catch (FileNotFoundException e){}
        catch (IOException e){}
    }

    public File getFile() {
        return file;
    }

}
