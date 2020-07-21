import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

public class FetchAddressTask extends AsyncTask<Location, Void, String> {

    private Context context;
    private static final String TAG = FetchAddressTask.class.getSimpleName();

    public FetchAddressTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Location... locations) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
