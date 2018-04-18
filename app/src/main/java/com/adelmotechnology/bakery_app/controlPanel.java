package com.adelmotechnology.bakery_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class controlPanel extends AppCompatActivity {

    Button addnewuserbtn,addnewshopbtn,addnewitembtn,showreportbtn,salesbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        addnewuserbtn = (Button) findViewById(R.id.addnewuserbtn);
        addnewshopbtn = (Button) findViewById(R.id.addnewshopbtn);
        addnewitembtn = (Button) findViewById(R.id.addnewitembtn);
        showreportbtn = (Button) findViewById(R.id.showreportbtn);
        salesbtn = (Button) findViewById(R.id.salesbtn);

        addnewuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addnewuserinten = new Intent(controlPanel.this,RegisterUserActivity.class);
                startActivity(addnewuserinten);
            }
        });

        addnewitembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addnewitemintent = new Intent(controlPanel.this,addNewItemActivity.class);
                startActivity(addnewitemintent);
            }
        });

        addnewshopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addnewshopintent = new Intent(controlPanel.this,addNewShopActivity.class);
                startActivity(addnewshopintent);
            }
        });

        salesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent salesintent = new Intent(controlPanel.this,salesActivity.class);
                startActivity(salesintent);
            }
        });

        showreportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showReportIntent = new Intent(controlPanel.this,showReportActivity.class);
                startActivity(showReportIntent);
            }
        });

    }
}
