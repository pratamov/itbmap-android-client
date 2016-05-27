package id.ac.itb.map.rest;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Profisien on 5/27/2016.
 */
public class GetMapList extends AsyncTask<Void, Integer, HashMap<String, String>> {
    GetMapListListener getMapListListener;

    public GetMapList(GetMapListListener getMapListListener){
        this.getMapListListener = getMapListListener;
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... params) {

        String url = "http://216.126.192.36/itbmap/v1/beacons";
        try {
            HashMap<String, String> beaconList = new HashMap<>();

            String json = IOUtils.toString(new URL(url));
            JsonElement jelement = new JsonParser().parse(json);
            JsonObject jobject = jelement.getAsJsonObject();

            jobject = jobject.getAsJsonObject("data");
            JsonArray beacon_list = jobject.getAsJsonArray("beacon_list");

            for(int i = 0; i<beacon_list.size(); i++){
                jobject = beacon_list.get(i).getAsJsonObject();
                String uuid = jobject.get("uuid").toString().replace("\"", "");;
                String map_name = jobject.get("map_name").toString().replace("\"", "");
                beaconList.put(uuid, map_name);
            }

            return beaconList;

        } catch (IOException e) {

            getMapListListener.onGetMapListListenerError(e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(HashMap<String, String> beaconList) {
        getMapListListener.onGetMapListListenerComplete(beaconList);
    }
}
