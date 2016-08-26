package ca.lakeland.plantsd.flightlogger.Background;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import ca.lakeland.plantsd.flightlogger.Activities.FlightLogInfoActivity;
import ca.lakeland.plantsd.flightlogger.Objects.Flight;
import ca.lakeland.plantsd.flightlogger.Objects.FlightLog;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Format;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by plantsd on 8/25/2016.
 *
 * Tutorial from http://www.kylebeal.com/blog/2011/10/using-jexcelapi-in-an-android-app/
 */
public class ExcelFromFlightLog {

    private String filename;
    private FlightLog fl;
    private File excelFile;
    private WritableWorkbook wb;
    private WritableSheet ws;


    /**
     *
     * @param context - App context when creating this excel file
     * @param saveAs - Name to save excel file as
     * @param flightLog - Flight log to convert to excel format
     */
    public ExcelFromFlightLog(Context context, String saveAs, FlightLog flightLog) {
        this.filename = saveAs;
        this.fl = flightLog;

        this.wb = createWorkbook(this.filename);
        this.ws = createSheet(this.wb, "Flight log", 0);

        // Widen column widths
        this.ws.setColumnView(0,19);
        this.ws.setColumnView(1,19);
        this.ws.setColumnView(2,19);
        this.ws.setColumnView(3,19);
        this.ws.setColumnView(4,19);
        this.ws.setColumnView(5,19);
        this.ws.setColumnView(6,19);


        try {
            WritableCellFormat borderFormat = new WritableCellFormat();
            borderFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat emptyFormat = new WritableCellFormat();

            // "Headers"
            writeCell(0, 0, "Flight Log Number", this.ws, borderFormat);
            writeCell(0, 1, "Date", this.ws, borderFormat);
            writeCell(0, 2, "Serial Number", this.ws, borderFormat);
            writeCell(0, 3, "Location", this.ws, borderFormat);
            writeCell(0, 4, "Pilot", this.ws, borderFormat);
            writeCell(0, 5, "Spotter", this.ws, borderFormat);
            writeCell(0, 6, "Wind Speed", this.ws, borderFormat);
            writeCell(0, 7, "Temperature", this.ws, borderFormat);

            writeCell(3, 0, "Weather Conditions", this.ws, borderFormat);
            writeCell(3, 1, "Payload", this.ws, borderFormat);
            writeCell(3, 2, "Number of Flights", this.ws, borderFormat);
            writeCell(3, 3, "Agency", this.ws, borderFormat);
            writeCell(3, 4, "Total Flight Time", this.ws, borderFormat);
            writeCell(3, 5, "Altitude", this.ws, borderFormat);

            writeCell(0, 9, "Purpose of Flight", this.ws, emptyFormat);
            writeCell(0, 10, "Comments", this.ws, emptyFormat);
            writeCell(0, 11, "Admin Comments", this.ws, emptyFormat);


            // Fill in flight log data
            writeCell(1, 0, String.valueOf(fl.getFlightLogNum()), this.ws, borderFormat);
            writeCell(1, 1, fl.getDate(), this.ws, borderFormat);
            writeCell(1, 2, fl.getSerialNum(), this.ws, borderFormat);
            writeCell(1, 3, fl.getLocation(), this.ws, borderFormat);
            writeCell(1, 4, fl.getPilot().getName(), this.ws, borderFormat);
            writeCell(1, 5, fl.getSpotter(), this.ws, borderFormat);
            writeCell(1, 6, String.valueOf(fl.getWindSpeed()), this.ws, borderFormat);
            writeCell(1, 7, String.valueOf(fl.getTemperature()), this.ws, borderFormat);

            writeCell(4, 0, fl.getWeatherConditions(), this.ws, borderFormat);
            writeCell(4, 1, fl.getPayloadType(), this.ws, borderFormat);
            writeCell(4, 2, String.valueOf(fl.getFlights().size()), this.ws, borderFormat);
            writeCell(4, 3, "LLC", this.ws, borderFormat); // temp
            writeCell(4, 4, String.valueOf(fl.getAccumFlightTime()), this.ws, borderFormat);
            writeCell(4, 5, String.valueOf(fl.getMaxAltitude()), this.ws, borderFormat);

            writeCell(1, 9, fl.getPurposeOfFlight(), this.ws, emptyFormat);
            writeCell(1, 10, fl.getComments(), this.ws, emptyFormat);
            int row = 11;
            for (int i = 0; i < fl.getAdminComments().size(); ++i) {
                row = 11+i; // 11 because it shows up in the 11th row and on.
                writeCell(1, row, fl.getAdminComments().get(i).getDate(), this.ws, emptyFormat);
                writeCell(2, row, fl.getAdminComments().get(i).getComment(), this.ws, emptyFormat);
            }

            row += 2; // add a space below the admin comments

            writeCell(0, row, "Flights:", this.ws, emptyFormat);
            writeCell(1, row, "Takeoff Time", this.ws, borderFormat);
            writeCell(2, row, "Land Time", this.ws, borderFormat);
            writeCell(3, row, "Flight Time", this.ws, borderFormat);
            writeCell(4, row, "Battery Pack Number", this.ws, borderFormat);
            writeCell(5, row, "Battery Start Voltage", this.ws, borderFormat);
            writeCell(6, row, "Battery End Voltage", this.ws, borderFormat);

            row += 1;
            int flight_start_row = row;

            // fill in the flights
            for (int i = 0; i < fl.getFlights().size(); ++i) {
                row = flight_start_row + i;
                Flight flight = fl.getFlights().get(i);
                writeCell(1, row, flight.getTakeoffTime(), this.ws, borderFormat);
                writeCell(2, row, flight.getLandTime(), this.ws, borderFormat);
                writeCell(3, row, String.valueOf(flight.getFlightTime()), this.ws, borderFormat);
                writeCell(4, row, flight.getBattPackNum(), this.ws, borderFormat);
                writeCell(5, row, String.valueOf(flight.getBattStartVoltage()), this.ws, borderFormat);
                writeCell(6, row, String.valueOf(flight.getBattEndVoltage()), this.ws, borderFormat);
            }

            wb.write();
            SettingsMenu.emailFile(context, excelFile);

            wb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public File getExcelFile() {
        return this.excelFile;
    }


    /**
     *
     * @param fileName - the name to give the new workbook file
     * @return - a new WritableWorkbook with the given fileName
     */
    public WritableWorkbook createWorkbook(String fileName){
        // exports must use a temp file while writing to avoid memory hogging
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setUseTemporaryFileDuringWrite(true);

        // get the sdcard's directory
        File sdCard = Environment.getExternalStorageDirectory();
        // add on the your app's path
        File dir = new File(sdCard.getAbsolutePath() + "/JExcelApiTest");
        // make them in case they're not there
        dir.mkdirs();
        // create a standard java.io.File object for the Workbook to use
        this.excelFile = new File(dir,fileName);

        WritableWorkbook wb = null;

        try {
            // create a new WritableWorkbook using the java.io.File and
            // WorkbookSettings from above
            wb = Workbook.createWorkbook(this.excelFile,wbSettings);
        } catch(IOException ex) {
            Log.e("----------", ex.getStackTrace().toString());
            Log.e("----------", ex.getMessage());
        }

        return wb;
    }


    /**
     *
     * @param wb - WritableWorkbook to create new sheet in
     * @param sheetName - name to be given to new sheet
     * @param sheetIndex - position in sheet tabs at bottom of workbook
     * @return - a new WritableSheet in given WritableWorkbook
     */
    public WritableSheet createSheet(WritableWorkbook wb,
                                     String sheetName, int sheetIndex){
        //create a new WritableSheet and return it
        return wb.createSheet(sheetName, sheetIndex);
    }


    /**
     *
     * @param columnPosition - column to place new cell in
     * @param rowPosition - row to place new cell in
     * @param contents - string value to place in cell
     * @param sheet - WritableSheet to place cell in
     * @throws RowsExceededException - thrown if adding cell exceeds .xls row limit
     * @throws WriteException - Idunno, might be thrown
     */
    public void writeCell(int columnPosition, int rowPosition, String contents,
                          WritableSheet sheet, WritableCellFormat cellFormat) throws RowsExceededException, WriteException {
        //create a new cell with contents at position
        Label newCell = new Label(columnPosition,rowPosition,contents);
        newCell.setCellFormat(cellFormat);

        sheet.addCell(newCell);
    }

}
