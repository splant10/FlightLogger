package ca.lakeland.plantsd.flightlogger;

import java.util.Date;

/**
 * Created by plantsd on 5/19/2016.
 */
public class FlightLog {

    private String serialNum, location, pilot, spotter, weatherConditions,
    purposeOfFlight, payloadType, battPackNum, comments;
    private int takeoffHour, takeoffMinute, landHour, landMinute, totalFlightTime,
    windSpeed, temperature, flightPlanNum, flightLogNum, agencyNum, accumFlightTime,
    maxAltitude, battStartVoltage, battEndVoltage;
    private Date date;

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

    public String getPilot() {
        return pilot;
    }

    public void setPilot(String pilot) {
        this.pilot = pilot;
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

    public int getTakeoffHour() {
        return takeoffHour;
    }

    public void setTakeoffHour(int takeoffHour) {
        this.takeoffHour = takeoffHour;
    }

    public int getTakeoffMinute() {
        return takeoffMinute;
    }

    public void setTakeoffMinute(int takeoffMinute) {
        this.takeoffMinute = takeoffMinute;
    }

    public int getLandHour() {
        return landHour;
    }

    public void setLandHour(int landHour) {
        this.landHour = landHour;
    }

    public int getLandMinute() {
        return landMinute;
    }

    public void setLandMinute(int landMinute) {
        this.landMinute = landMinute;
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
