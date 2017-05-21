package com.example.andriod.steeltrueturtle;

import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by bola on 5/20/2017.
 */

public class googleLogOut {
    googleLogOut()
    {}
    //logout of google(GMAIL)

    public static boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();

                return true;

        }
        return false;
    }
}
