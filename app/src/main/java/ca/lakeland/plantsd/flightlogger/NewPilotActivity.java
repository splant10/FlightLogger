package ca.lakeland.plantsd.flightlogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewPilotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pilot);
    }

    public void btnCancelPilotClick() {
        finish();
    }

    public void btnConfirmPilotClick() {
        Pilot p = new Pilot("larry");
    }
}
