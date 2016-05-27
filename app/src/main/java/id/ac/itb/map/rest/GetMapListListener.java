package id.ac.itb.map.rest;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Profisien on 5/27/2016.
 */
public interface GetMapListListener {
    void onGetMapListListenerComplete(HashMap<String, String> mapList);
    void onGetMapListListenerError(String message);
}
