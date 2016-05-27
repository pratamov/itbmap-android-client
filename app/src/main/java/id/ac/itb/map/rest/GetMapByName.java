package id.ac.itb.map.rest;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

import id.ac.itb.map.domain.Map;

/**
 * Created by Profisien on 5/27/2016.
 */
public class GetMapByName extends AsyncTask<String, Integer, Map> {

    GetMapByNameListener getMapByNameListener;

    public GetMapByName(GetMapByNameListener getMapByNameListener) {

        this.getMapByNameListener = getMapByNameListener;

    }

    @Override
    protected Map doInBackground(String... params) {
        String mapName = params[0];
        String url = "http://216.126.192.36/itbmap/v1/beacons/0?map_name="+mapName;
        try {

            String json = IOUtils.toString(new URL(url));

            JsonElement jelement = new JsonParser().parse(json);
            JsonObject jobject = jelement.getAsJsonObject();

            jobject = jobject.getAsJsonObject("data");
            jobject = jobject.getAsJsonObject("map");

            Map map = new Map();

            map.setUuid(jobject.get("uuid").toString().replace("\"", ""));
            map.setMapDescription(jobject.get("map_description").toString().replace("\"", ""));
            map.setMapName(jobject.get("map_name").toString().replace("\"", ""));
            map.setMapRawImageFilename(jobject.get("map_raw_image_filename").toString().replace("\"", ""));
            map.setMapTileImageUrl(jobject.get("map_tile_image_url").toString().replace("\"", ""));
            map.setRegionName(jobject.get("region_name").toString().replace("\"", ""));

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

            return map;

        } catch (IOException e) {

            getMapByNameListener.onGetMapByNameListenerError(e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(Map map) {
        getMapByNameListener.onGetMapByNameListenerComplete(map);
    }
}
