package ca.lakeland.plantsd.flightlogger;

/**
 * Created by plantsd on 5/25/2016.
 */
public class Battery {

    private int packNum;
    private int startVoltage;
    private int endVoltage;

    public Battery(int packNum, int startVoltage, int endVoltage) {
        this.packNum = packNum;
        this.startVoltage = startVoltage;
        this.endVoltage = endVoltage;
    }

    public int getPackNum() {
        return packNum;
    }

    public int getStartVoltage() {
        return startVoltage;
    }

    public int getEndVoltage() {
        return endVoltage;
    }
}
