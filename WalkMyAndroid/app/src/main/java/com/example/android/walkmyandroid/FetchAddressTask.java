package com.example.android.walkmyandroid;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressTask extends AsyncTask<Location, Void, String> {

    private Context context;
    private static final String TAG = FetchAddressTask.class.getSimpleName();

    private OnTaskCompleted mListener;

    public FetchAddressTask(Context context, OnTaskCompleted listener) {
        this.context = context;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Location location = locations[0];
        List<Address> addresses = null;
        String resultMessage = "";
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            if (addresses == null || addresses.size() == 0) {
                if (resultMessage.isEmpty()) {
                    resultMessage = context.getString(R.string.address_not_found);
                    Log.e(TAG, resultMessage);
                }
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressParts = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressParts.add(address.getAddressLine(i));
                }
                resultMessage = TextUtils.join("\n", addressParts);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultMessage = context.getString(R.string.service_not_available);
            Log.e(TAG, resultMessage, e);
        } catch (IllegalArgumentException iAE) {
            resultMessage = context.getString(R.string.invalid_lat_long);
            Log.e(TAG, resultMessage, iAE);
        }
        return resultMessage;
    }

    @Override
    protected void onPostExecute(String address) {
        super.onPostExecute(address);
        mListener.onTaskCompleted(address);
    }

    interface  OnTaskCompleted {
        void onTaskCompleted(String result);
    }
}
