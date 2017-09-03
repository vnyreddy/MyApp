package com.vinay.wizdem.myapp.FireBase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vinay.wizdem.myapp.Interfaces.FetchData;
import com.vinay.wizdem.myapp.Models.AppData;

/**
 * Created by vinay_1 on 8/31/2017.
 */

public class LoadData {

    private DatabaseReference mFirebaseReference, ref;
    private AppData mAppData;

    public void getAssignData(final FetchData fetchData){
        mFirebaseReference = FirebaseDatabase.getInstance().getReference();
        ref = mFirebaseReference.child("app_info");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                        mAppData = dataSnapshot.getValue(AppData.class);
                        fetchData.assignData(mAppData);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fetchData.databaseError(); //not working need to check
            }
        });

    }
}
