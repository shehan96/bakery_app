package com.adelmotechnology.bakery_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addNewItemActivity extends AppCompatActivity {

    private DatabaseReference db_ref;
    EditText itemnameedittext,priceedittext;
    Button addnewitembtn;
    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView currentitemlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        //creating database reference
        db_ref = FirebaseDatabase.getInstance().getReference();

        //creating variables for views
        itemnameedittext = (EditText) findViewById(R.id.itemnameedittext);
        priceedittext = (EditText) findViewById(R.id.priceedittext);
        addnewitembtn = (Button) findViewById(R.id.addnewitembtn);
        currentitemlist = (ListView) findViewById(R.id.currentitemlist);

        //setting adapters
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        currentitemlist.setAdapter(arrayAdapter);


        addnewitembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String itemName = itemnameedittext.getText().toString();
                final int itemPrice = Integer.valueOf(priceedittext.getText().toString());

                addFoodItemClass addfooditem = new addFoodItemClass();

                addfooditem.setName(itemName);
                addfooditem.setPrice(itemPrice);

                DatabaseReference new_ref = db_ref.child("Items").push();

                new_ref.setValue(addfooditem);

            }
        });

        //getting values from items
        db_ref.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
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


    }
}
