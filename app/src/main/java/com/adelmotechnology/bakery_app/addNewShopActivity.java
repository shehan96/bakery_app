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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addNewShopActivity extends AppCompatActivity {

    ListView shoplist;
    ArrayList<String> shopnamelist = new ArrayList<>();
    ArrayAdapter<String> shopnamelistadapter;
    DatabaseReference db_ref;
    Button addnewitembtn;
    EditText shopnameedittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shop);

        db_ref = FirebaseDatabase.getInstance().getReference();

        shoplist = (ListView) findViewById(R.id.shoplist);
        addnewitembtn = (Button) findViewById(R.id.addnewitembtn);
        shopnameedittext = (EditText) findViewById(R.id.shopnameedittext);

        shopnamelistadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,shopnamelist);
        shoplist.setAdapter(shopnamelistadapter);

        //getting values from items
        db_ref.child("shop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopnamelist.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    addFoodItemClass name = ds.getValue(addFoodItemClass.class);
                    shopnamelist.add(name.getName());
                    shopnamelistadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addnewitembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String shopName = shopnameedittext.getText().toString();

                addShopClass addShop = new addShopClass();

                addShop.setName(shopName);

                DatabaseReference new_ref = db_ref.child("shop").push();

                new_ref.setValue(addShop);

            }
        });




    }
}
