//Reference:
// deleting
//https://firebase.google.com/docs/firestore/manage-data/delete-data


package com.example.simpleparadox.listycity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView cityList;
    ArrayAdapter<City> cityAdapter;
    ArrayList<City> cityDataList;

    CustomList customList;
    Button addCityButton;
    Button deleteCityButton;
    EditText addCityEditText;
    EditText addProvinceEditText;
    FirebaseFirestore db;
    final String TAG = "Sample";
    int Selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);




        cityDataList = new ArrayList<>();
        addCityButton = findViewById(R.id.add_city_button);
        deleteCityButton = findViewById(R.id.delete_button);
        addCityEditText = findViewById(R.id.add_city_field);
        addProvinceEditText = findViewById(R.id.add_province_edit_text);

        cityAdapter = new CustomList(this, cityDataList);

        cityList.setAdapter(cityAdapter);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Cities");

        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected = i;
            }
        });

        addCityButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cityName = addCityEditText.getText().toString();
                final String provinceName = addProvinceEditText.getText().toString();
                HashMap<String, String> data = new HashMap<>();
                if (cityName.length()>0 && provinceName.length()>0) {
                    data.put("Province Name", provinceName);
                    collectionReference
                            .document(cityName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
// These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been added successfully!");
                                    addCityEditText.setText("");
                                    addProvinceEditText.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
// These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });

                }
            }
        });
        deleteCityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cityDataList.size() >= 1){
                    //removing  selected Medicine


                    collectionReference.document(cityDataList.get(Selected).getCityName())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });

                    cityDataList.remove(Selected);
                    //Refresh the screen
                    cityAdapter.notifyDataSetChanged();
                }
                // Code here executes on main thread after user presses button
            }
        });
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                cityDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("Province Name")));
                    String city = doc.getId();
                    String province = (String) doc.getData().get("Province Name");
                    cityDataList.add(new City(city, province)); // Adding the cities and provinces from FireStore
                }
                cityAdapter.notifyDataSetChanged();
            }
        });

//        dataList = new ArrayList<>();
//        dataList.addAll(Arrays.asList(cities));
//
//        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
//
//        cityList.setAdapter(cityAdapter);



    }


}
