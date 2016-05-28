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
public class GetMaps extends AsyncTask<Void, Integer, HashMap<Integer, String>> {
    GetMapsListener getMapsListener;

    public GetMaps(GetMapsListener getMapsListener){
        this.getMapsListener = getMapsListener;
    }

    @Override
    protected HashMap<Integer, String> doInBackground(Void... params) {

        String url = "http://216.126.192.36/itbmap-webservice/v1/maps";

        try {

            HashMap<Integer, String> maps = new HashMap<>();

            String json = IOUtils.toString(new URL(url));
            JsonElement jelement = new JsonParser().parse(json);
            JsonObject jobject = jelement.getAsJsonObject();

            jobject = jobject.getAsJsonObject("data");
            JsonArray beacon_list = jobject.getAsJsonArray("maps");

            for(int i = 0; i<beacon_list.size(); i++){

                jobject = beacon_list.get(i).getAsJsonObject();
                int major_id = jobject.get("minor_id").getAsInt();
                String map_name = jobject.get("map_name").toString().replace("\"", "");
                maps.put(major_id, map_name);

            }

            return maps;

        } catch (IOException e) {

            getMapsListener.onGetMapsListenerError(e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(HashMap<Integer, String> beaconList) {
        getMapsListener.onGetMapsListenerComplete(beaconList);
    }
}
