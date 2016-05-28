package id.ac.itb.map.rest;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by Profisien on 5/27/2016.
 */
public class GetRegions extends AsyncTask<Void, Integer, HashMap<String, String>> {
    GetRegionsListener getRegionsListener;

    public GetRegions(GetRegionsListener getRegionsListener){
        this.getRegionsListener = getRegionsListener;
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... params) {

        String url = "http://216.126.192.36/itbmap/v1/regions";
        try {
            HashMap<String, String> regions = new HashMap<>();

            String json = IOUtils.toString(new URL(url));
            JsonElement jelement = new JsonParser().parse(json);
            JsonObject jobject = jelement.getAsJsonObject();

            jobject = jobject.getAsJsonObject("data");
            JsonArray beacon_list = jobject.getAsJsonArray("regions");

            for(int i = 0; i<beacon_list.size(); i++){
                jobject = beacon_list.get(i).getAsJsonObject();
                String uuid = jobject.get("uuid").toString().replace("\"", "");;
                String region_name = jobject.get("region_name").toString().replace("\"", "");
                regions.put(uuid, region_name);
            }

            return regions;

        } catch (IOException e) {

            getRegionsListener.onGetRegionsListenerError(e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(HashMap<String, String> beaconList) {
        getRegionsListener.onGetRegionsListenerComplete(beaconList);
    }
}
