package ca.lakeland.plantsd.flightlogger;

import java.util.Date;

/**
 * Created by plantsd on 5/19/2016.
 */
public class FlightLog {

    private Pilot pilot;
    private String serialNum, location,spotter, weatherConditions,
    purposeOfFlight, payloadType, battPackNum, comments, takeoffTime, landTime;
    private int totalFlightTime,windSpeed, temperature, flightPlanNum, flightLogNum, agencyNum,
            accumFlightTime, maxAltitude, battStartVoltage, battEndVoltage;
    private Date date;

    public FlightLog() {
    }

    public FlightLog(String serialNum, String location, String spotter,
                     String weatherConditions, String purposeOfFlight, String payloadType,
                     String battPackNum, String comments, String takeoffTime, String landTime,
                     int totalFlightTime, int windSpeed, int temperature, int flightPlanNum,
                     int flightLogNum, int agencyNum, int accumFlightTime, int maxAltitude,
                     int battStartVoltage, int battEndVoltage, Date date) {
        this.serialNum = serialNum;
        this.location = location;
        this.spotter = spotter;
        this.weatherConditions = weatherConditions;
        this.purposeOfFlight = purposeOfFlight;
        this.payloadType = payloadType;
        this.battPackNum = battPackNum;
        this.comments = comments;
        this.takeoffTime = takeoffTime;
        this.landTime = landTime;
        this.totalFlightTime = totalFlightTime;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.flightPlanNum = flightPlanNum;
        this.flightLogNum = flightLogNum;
        this.agencyNum = agencyNum;
        this.accumFlightTime = accumFlightTime;
        this.maxAltitude = maxAltitude;
        this.battStartVoltage = battStartVoltage;
        this.battEndVoltage = battEndVoltage;
        this.date = date;
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

    public String getBattPackNum() {
        return battPackNum;
    }

    public void setBattPackNum(String battPackNum) {
        this.battPackNum = battPackNum;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTakeoffTime() {
        return takeoffTime;
    }

    public void setTakeoffTime(String takeoffTime) {
        this.takeoffTime = takeoffTime;
    }

    public String getLandTime() {
        return landTime;
    }

    public void setLandTime(String landTime) {
        this.landTime = landTime;
    }

    public int getTotalFlightTime() {
        return totalFlightTime;
    }

    public void setTotalFlightTime(int totalFlightTime) {
        this.totalFlightTime = totalFlightTime;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getFlightPlanNum() {
        return flightPlanNum;
    }

    public void setFlightPlanNum(int flightPlanNum) {
        this.flightPlanNum = flightPlanNum;
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

    public int getAccumFlightTime() {
        return accumFlightTime;
    }

    public void setAccumFlightTime(int accumFlightTime) {
        this.accumFlightTime = accumFlightTime;
    }

    public int getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(int maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    public int getBattStartVoltage() {
        return battStartVoltage;
    }

    public void setBattStartVoltage(int battStartVoltage) {
        this.battStartVoltage = battStartVoltage;
    }

    public int getBattEndVoltage() {
        return battEndVoltage;
    }

    public void setBattEndVoltage(int battEndVoltage) {
        this.battEndVoltage = battEndVoltage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
