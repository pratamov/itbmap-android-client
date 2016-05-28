package id.ac.itb.map.rest;

import java.util.HashMap;

/**
 * Created by Profisien on 5/27/2016.
 */
public interface GetMapsListener {
    void onGetMapsListenerComplete(HashMap<Integer, String> maps);
    void onGetMapsListenerError(String message);
}
