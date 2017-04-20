package com.xipherlabs.podcastr;

import com.google.firebase.database.FirebaseDatabase;

public class Utils {
    private static FirebaseDatabase mDatabase;
    public static void initFirebasePersistence(){
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
    }
    public static FirebaseDatabase getDb() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
