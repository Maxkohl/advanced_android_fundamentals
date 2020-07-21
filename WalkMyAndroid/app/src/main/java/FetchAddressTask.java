import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.walkmyandroid.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FetchAddressTask extends AsyncTask<Location, Void, String> {

    private Context context;
    private static final String TAG = FetchAddressTask.class.getSimpleName();

    public FetchAddressTask(Context context) {
        this.context = context;
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
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultMessage = context.getString(R.string.service_not_available);
            Log.e(TAG, resultMessage, e);
        } catch (IllegalArgumentException iAE) {
            resultMessage = context.getString(R.string.invalid_lat_long);
            Log.e(TAG, resultMessage, iAE);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
