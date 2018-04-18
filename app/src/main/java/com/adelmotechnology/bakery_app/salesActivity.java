package com.adelmotechnology.bakery_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class salesActivity extends AppCompatActivity {

    private DatabaseReference db_ref;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter3,arrayAdapter4,arrayAdapter5;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> shops = new ArrayList<>();
    ArrayList<String> items_selected = new ArrayList<>();
    ArrayList<Integer> items_selected_qty = new ArrayList<>();
    ArrayList<String> items_selected_with_price = new ArrayList<>();
    ArrayList<Integer> items_selected_price = new ArrayList<>();
    Spinner shopspinner1,itemspinner1;
    TextView datetext,pricetext;
    Button itemaddbtn1,printbtn;
    ListView itemlist1;
    EditText qtyno1,priceno1;

    int TotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        //creating database reference
        db_ref = FirebaseDatabase.getInstance().getReference();
        //creating variables for views
        shopspinner1 = (Spinner) findViewById(R.id.shopspinner);
        itemspinner1 = (Spinner) findViewById(R.id.itemspinner);
        datetext = (TextView) findViewById(R.id.datetext);
        itemaddbtn1 = (Button) findViewById(R.id.itemaddbtn);
        itemlist1 = (ListView) findViewById(R.id.itemlist);
        qtyno1 = (EditText) findViewById(R.id.qtyno);
        printbtn = (Button) findViewById(R.id.printbtn);
        pricetext = (TextView) findViewById(R.id.pricetext);
        //creating variable
        //int Total = 0;
        //displaying date
        datetext.setText(getDate());
        //creating adapters
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,shops);
        arrayAdapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items_selected_with_price);
        //setting adapters
        itemspinner1.setAdapter(arrayAdapter);
        shopspinner1.setAdapter(arrayAdapter2);
        itemlist1.setAdapter(arrayAdapter4);

        //getting values from items
        db_ref.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot ds:dataSnapshot.getChildren()){
                   addFoodItemClass name = ds.getValue(addFoodItemClass.class);
                   items.add(name.getName());
                   arrayAdapter.notifyDataSetChanged();
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //getting values from items
        db_ref.child("shop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    shopclass name = ds.getValue(shopclass.class);
                    shops.add(name.getName());
                    arrayAdapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //add items button event
        itemaddbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String itemTemp = getItems();
                String itemQty = qtyno1.getText().toString();
                String temp = itemTemp+"   "+itemQty;
                if(items_selected_with_price.contains(temp)){
                    Toast.makeText(salesActivity.this,"Item Already Added!!!",Toast.LENGTH_LONG).show();
                }
                else{
                    items_selected_with_price.add(temp);
                    arrayAdapter4.notifyDataSetChanged();
                    //getting price
                    Query query = db_ref.child("Items").orderByChild("name").equalTo(itemTemp);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                addFoodItemClass price = ds.getValue(addFoodItemClass.class);
                                Toast.makeText(salesActivity.this,String.valueOf(price.getPrice()),Toast.LENGTH_LONG).show();
                                updateprice(price.getPrice(),Integer.valueOf(qtyno1.getText().toString()),itemTemp);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        //print button event
        printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salesDetailsClass salesdetails = new salesDetailsClass();

                salesdetails.setMonth(getMonth());
                salesdetails.setDate(getDateOnly());
                salesdetails.setName(shopspinner1.getSelectedItem().toString());
                salesdetails.setTotal(TotalPrice);

                DatabaseReference new_ref = db_ref.child("sales").push();
                new_ref.setValue(salesdetails);
                Toast.makeText(salesActivity.this,""+items_selected.size(),Toast.LENGTH_LONG).show();
                int i;
                for(i=0;i<items_selected.size();i++){
                    salesDetailsPriceClass salesDetailsPrice = new salesDetailsPriceClass();
                    salesDetailsPrice.setName(items_selected.get(i));
                    salesDetailsPrice.setPrice(items_selected_price.get(i));
                    salesDetailsPrice.setQty(items_selected_qty.get(i));
                    DatabaseReference new_ref2 = new_ref.child("Items").push();
                    new_ref2.setValue(salesDetailsPrice);
                }
                //asking for print this
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(salesActivity.this);
                builder.setTitle("Confirm Print");
                builder.setMessage("Would you like to print this sale bill?");

                // add the buttons
                builder.setPositiveButton("Yes! Print", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent printingIntent = new Intent(salesActivity.this,printActivity.class);
                        startActivity(printingIntent);
                    }
                });
                builder.setNegativeButton("Cancel", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                // Clear All Array Lists
                items_selected_with_price.clear();
                items_selected.clear();
                items_selected_price.clear();
                //notify the adapter
                arrayAdapter4.notifyDataSetChanged();
            }
        });

    }

    //method to show date
    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    //method to get only date
    private int getDateOnly(){
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return Integer.valueOf(dateFormat.format(date));
    }

    //method to get month
    private String getMonth(){
        String monthName = "";
        Calendar calendar = Calendar.getInstance();
        int monthValue = calendar.get(Calendar.MONTH);
        if(monthValue == 0){
            monthName = "January";
        }
        else if(monthValue == 1){
            monthName = "February";
        }
        else if(monthValue == 2){
            monthName = "March";
        }
        else if(monthValue == 3){
            monthName = "April";
        }
        else if(monthValue == 4){
            monthName = "May";
        }
        else if(monthValue == 5){
            monthName = "June";
        }
        else if(monthValue == 6){
            monthName = "July";
        }
        else if(monthValue == 7){
            monthName = "August";
        }
        else if(monthValue == 8){
            monthName = "September";
        }
        else if(monthValue == 9){
            monthName = "October";
        }
        else if(monthValue == 10){
            monthName = "November";
        }
        else if(monthValue == 11){
            monthName = "December";
        }

        return monthName;

    }

    //method to get selected items
    private String getItems(){
        return itemspinner1.getSelectedItem().toString();
    }

    //method to get selected items Qty
    private String getItemsQty(){
        return qtyno1.getText().toString();
    }

    //update price
    private void updateprice(int newprice,int newqty,String itemTemp){
        //adding data to array lists
        items_selected.add(itemTemp);
        items_selected_price.add(newprice);
        items_selected_qty.add(newqty);
        //calculating Total
        TotalPrice = TotalPrice + (newprice * newqty);
        String tempPriceString = "Rs:"+String.valueOf(TotalPrice);
        pricetext.setText(tempPriceString);
    }



        //query.addListenerForSingleValueEvent(new ValueEventListener() {
           // @Override
            //public void onDataChange(DataSnapshot dataSnapshot) {
                //if (dataSnapshot.exists()) {
                    //for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //addFoodItemClass name = ds.getValue(addFoodItemClass.class);
                        //items_selected.add(String.valueOf(name.getPrice()));
                        //arrayAdapter3.notifyDataSetChanged();
                        //int temp = Integer.valueOf(qtyno1.getText().toString());
                        //Total = name.getPrice() * temp;
                   // }
                //}
            //}

            //@Override
            //public void onCancelled(DatabaseError databaseError) {

            //}
        //});
   // }
}
