package com.adelmotechnology.bakery_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showReportActivity extends AppCompatActivity {

    private DatabaseReference db_ref;
    Spinner shopspinner,monthspinner;
    ListView itemlist;
    ArrayList<String> shops = new ArrayList<>();
    ArrayList<String> showItemList = new ArrayList<String>();
    String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    Button genaratebtn,clearbtn;
    EditText datetext;
    TextView pricetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report);




        //getting DB Refference
        db_ref = FirebaseDatabase.getInstance().getReference();

        //creating variables
        shopspinner = (Spinner) findViewById(R.id.shopspinner);
        monthspinner = (Spinner) findViewById(R.id.monthspinner);
        genaratebtn = (Button) findViewById(R.id.genaratebtn);
        datetext = (EditText) findViewById(R.id.datetext);
        itemlist = (ListView) findViewById(R.id.itemlist);
        pricetext = (TextView) findViewById(R.id.pricetext);
        clearbtn = (Button) findViewById(R.id.clearbtn);

        //creating adapters
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,shops);
        final ArrayAdapter<String> montharrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,months);
        final ArrayAdapter<String> showItemListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,showItemList);
        //setting adapter
        shopspinner.setAdapter(arrayAdapter);
        itemlist.setAdapter(showItemListAdapter);

        //add data to spinner month
        monthspinner.setAdapter(montharrayAdapter);

        //getting values from items
        db_ref.child("shop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    shopclass name = ds.getValue(shopclass.class);
                    shops.add(name.getName());
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(showReportActivity.this,"Error In Database",Toast.LENGTH_LONG).show();
            }
        });

        //generate btn click event
        genaratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shopname = shopspinner.getSelectedItem().toString();
                String monthname = monthspinner.getSelectedItem().toString();


                Query query = db_ref.child("sales").orderByChild("name").equalTo(shopname);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            salesDetailsClass salesDetails = ds.getValue(salesDetailsClass.class);
                            pricetext.setText("Total :"+String.valueOf(salesDetails.getTotal()));
                            for(DataSnapshot ds1 : ds.child("Items").getChildren()){
                                salesDetailsPriceClass salesP = ds1.getValue(salesDetailsPriceClass.class);
                                int totalTemp =  salesP.getPrice() * salesP.getQty();
                                String itemdata = salesP.getName()+"   Rs:"+String.valueOf(salesP.getPrice())+"   *"+String.valueOf(salesP.getQty())+"   Amount:"+totalTemp;
                                showItemList.add(itemdata);
                            }
                        }
                        showItemListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemList.clear();
                showItemListAdapter.notifyDataSetChanged();
                pricetext.setText("Total Price");
            }
        });


    }
}
