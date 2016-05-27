package ca.lakeland.plantsd.flightlogger;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by plantsd on 5/19/2016.
 */
public class FlightLog {

    // weird order for declaration of variables to keep in sync with design of New Flight Log Activity
    private String serialNum;
    private String date;
    private String location;
    private Pilot pilot;
    private String spotter;
    private float windSpeed;
    private float temperature;
    private String weatherConditions;
    private String purposeOfFlight;
    private String payloadType;
    private ArrayList<Flight> flights;
    private String comments;
    private int flightLogNum, agencyNum;
    private float accumFlightTime, maxAltitude;


    public FlightLog() {
    }

    public FlightLog(String serialNum, String date, String location, Pilot pilot, String spotter,
                     float windSpeed, float temperature, String weatherConditions, String purposeOfFlight,
                     String payloadType, ArrayList<Flight> flights, String comments, int flightLogNum,
                     float maxAltitude) {
        this.serialNum = serialNum;
        this.date = date;
        this.location = location;
        this.pilot = pilot;
        this.spotter = spotter;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.weatherConditions = weatherConditions;
        this.purposeOfFlight = purposeOfFlight;
        this.payloadType = payloadType;
        this.flights = flights;
        this.comments = comments;
        this.flightLogNum = flightLogNum;
        this.maxAltitude = maxAltitude;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpotter() {
        return spotter;
    }

    public void setSpotter(String spotter) {
        this.spotter = spotter;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getPurposeOfFlight() {
        return purposeOfFlight;
    }

    public void setPurposeOfFlight(String purposeOfFlight) {
        this.purposeOfFlight = purposeOfFlight;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public float  getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getFlightLogNum() {
        return flightLogNum;
    }

    public void setFlightLogNum(int flightLogNum) {
        this.flightLogNum = flightLogNum;
    }

    public int getAgencyNum() {
        return agencyNum;
    }

    public void setAgencyNum(int agencyNum) {
        this.agencyNum = agencyNum;
    }

    public float getAccumFlightTime() {
        return accumFlightTime;
    }

    public void setAccumFlightTime(int accumFlightTime) {
        this.accumFlightTime = accumFlightTime;
    }

    public float getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(float maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    public void setAccumFlightTime(float accumFlightTime) {
        this.accumFlightTime = accumFlightTime;
    }
}
