package id.ac.itb.map.rest;

import java.util.HashMap;

/**
 * Created by Profisien on 5/27/2016.
 */
public interface GetRegionsListener {
    void onGetRegionsListenerComplete(HashMap<String, String> regions);
    void onGetRegionsListenerError(String message);
}
