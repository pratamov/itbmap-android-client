package id.ac.itb.map.rest;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

import id.ac.itb.map.domain.Geofence;
import id.ac.itb.map.domain.Map;

/**
 * Created by Profisien on 5/27/2016.
 */
public class GetMapByMinorId extends AsyncTask<Integer, Integer, Map> {

    GetMapByMinorIdListener getMapByMinorIdListener;

    public GetMapByMinorId(GetMapByMinorIdListener getMapByMinorIdListener) {

        this.getMapByMinorIdListener = getMapByMinorIdListener;

    }

    @Override
    protected Map doInBackground(Integer... params) {
        int minorId = params[0];
        String url = "http://216.126.192.36/itbmap/v1/maps/"+minorId;
        try {

            String json = IOUtils.toString(new URL(url));

            JsonElement jelement = new JsonParser().parse(json);
            JsonObject jobject = jelement.getAsJsonObject();

            jobject = jobject.getAsJsonObject("data");
            jobject = jobject.getAsJsonObject("map");

            Map map = new Map();

            map.setMinorId(minorId);
            map.setUuid(jobject.get("uuid").toString().replace("\"", ""));
            map.setMapDescription(jobject.get("map_description").toString().replace("\"", ""));
            map.setMapName(jobject.get("map_name").toString().replace("\"", ""));
            map.setMapRawImageFilename(jobject.get("map_raw_image_filename").toString().replace("\"", ""));
            map.setMapTileImageUrl(jobject.get("map_tile_image_url").toString().replace("\"", ""));

            map.setPositionXBeacon1(jobject.get("position_x_beacon_1").getAsDouble());
            map.setPositionYBeacon1(jobject.get("position_y_beacon_1").getAsDouble());

            map.setPositionXBeacon2(jobject.get("position_x_beacon_2").getAsDouble());
            map.setPositionYBeacon2(jobject.get("position_y_beacon_2").getAsDouble());

            map.setPositionXBeacon3(jobject.get("position_x_beacon_3").getAsDouble());
            map.setPositionYBeacon3(jobject.get("position_y_beacon_3").getAsDouble());

            map.setMapHeight(jobject.get("map_height").getAsInt());
            map.setMapWidth(jobject.get("map_width").getAsInt());

            map.setMapRealHeight(jobject.get("map_real_width").getAsDouble());
            map.setMapRealWidth(jobject.get("map_real_height").getAsDouble());

            JsonArray geofences = jobject.getAsJsonArray("geofences");

            for(int i = 0; i<geofences.size(); i++){
                jobject = geofences.get(i).getAsJsonObject();
                Geofence geofence = new Geofence();
                geofence.setMinorId(minorId);

                String name = jobject.get("name").toString().replace("\"", "");
                double x1 = jobject.get("x1").getAsDouble();
                double y1 = jobject.get("y1").getAsDouble();
                double x2 = jobject.get("x2").getAsDouble();
                double y2 = jobject.get("y2").getAsDouble();
                double x3 = jobject.get("x3").getAsDouble();
                double y3 = jobject.get("y3").getAsDouble();

                map.addGeofence(geofence);
            }

            return map;

        } catch (IOException e) {

            getMapByMinorIdListener.onGetMapByMinorIdListenerError(e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(Map map) {
        getMapByMinorIdListener.onGetMapByMinorIdListenerComplete(map);
    }
}
