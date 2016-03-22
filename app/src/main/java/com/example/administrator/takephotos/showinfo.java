package com.example.administrator.takephotos;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class showinfo extends Activity {

    private TextView Height;
    private TextView Breast;
    private TextView Waist;
    private TextView Hipshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showinfo);

        Bundle bundle = new Bundle();
        Double height = bundle.getDouble("height");
        Double breast = bundle.getDouble("breast");
        Double waist = bundle.getDouble("waist");
        Double hipshot = bundle.getDouble("hipshot");

        Height = (TextView)findViewById(R.id.height);
        Breast = (TextView)findViewById(R.id.breast);
        Waist = (TextView)findViewById(R.id.waist);
        Hipshot = (TextView)findViewById(R.id.hipshot);

        Height.setText(height.toString());
        Breast.setText(breast.toString());
        Waist.setText(waist.toString());
        Hipshot.setText(hipshot.toString());
    }
}
