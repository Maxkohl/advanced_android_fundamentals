/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.walkmyandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.PlaceDetectionClient;
import com.google.android.libraries.places.compat.PlaceLikelihood;
import com.google.android.libraries.places.compat.PlaceLikelihoodBuffer;
import com.google.android.libraries.places.compat.PlaceLikelihoodBufferResponse;
import com.google.android.libraries.places.compat.Places;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {

    private Button mLocationButton;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private TextView mLocationTextView;
    private FusedLocationProviderClient mFusedLocationClient;

    private ImageView mAndroidImageView;
    private AnimatorSet mRotateAnim;

    private boolean mTrackingLocation = false;
    private LocationCallback mLocationCallback;

    private PlaceDetectionClient mPlaceDetectionClient;

    private static final String TRACKING_LOCATION_KEY = "trackingLocation";
    private String mLastPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mTrackingLocation = savedInstanceState.getBoolean(TRACKING_LOCATION_KEY);
        }

        mLocationTextView = findViewById(R.id.textview_location);
        mLocationButton = findViewById(R.id.button_location);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTrackingLocation) {
                    startTrackingLocation();
                } else {
                    stopTrackingLocation();
                }
            }
        });

        mAndroidImageView = findViewById(R.id.imageview_android);
        mRotateAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (mTrackingLocation) {
                    //if Tracking is true, reverse geocode into address
                    new FetchAddressTask(MainActivity.this, MainActivity.this).execute(locationResult.getLastLocation());
                }
            }
        };
    }

    public void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.requestLocationUpdates(getLocationRequest(), mLocationCallback,
                    null);
            //This is for getting last location. Replaced by above for constant location updates.
//            mFusedLocationClient.getLastLocation().addOnSuccessListener(new
//            OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        new FetchAddressTask(MainActivity.this, MainActivity.this).execute
//                        (location);
//                    } else {
//                        mLocationTextView.setText(R.string.no_location);
//                    }
//                }
//            });
        }
        mLocationTextView.setText(getString(R.string.address_text,
                getString(R.string.loading),// Name
                getString(R.string.loading),// Address
                new Date())); // Timestamp

        mRotateAnim.start();
        mTrackingLocation = true;
        mLocationButton.setText(R.string.stop_tracking_location);
    }

    private void stopTrackingLocation() {
        if (mTrackingLocation) {
            mRotateAnim.end();
            mTrackingLocation = false;
            mLocationButton.setText(R.string.get_location);
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTrackingLocation();
                } else {
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(final String result) {
        if (mTrackingLocation) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    if (task.isSuccessful()) {
                        PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                        float maxLikelihood = 0;
                        Place currentPlace = null;
                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            if (maxLikelihood < placeLikelihood.getLikelihood()) {
                                maxLikelihood = placeLikelihood.getLikelihood();
                                currentPlace = placeLikelihood.getPlace();
                            }
                        }
                        if (currentPlace != null) {
                            mLocationTextView.setText(getString(R.string.address_text_places,
                                    currentPlace.getName(), result, System.currentTimeMillis()));
                        }
                        likelyPlaces.release();
                    } else {
                        mLocationTextView.setText(
                                getString(R.string.address_text,
                                        "No Place name found!",
                                        result, System.currentTimeMillis()));
                    }
                }
            });
        }
    }

    public LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTrackingLocation) {
            startTrackingLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTrackingLocation) {
            stopTrackingLocation();
            mTrackingLocation = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TRACKING_LOCATION_KEY, mTrackingLocation);
        super.onSaveInstanceState(outState);
    }
}
