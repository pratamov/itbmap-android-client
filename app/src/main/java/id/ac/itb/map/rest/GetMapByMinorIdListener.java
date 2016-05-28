package id.ac.itb.map.rest;

import id.ac.itb.map.domain.Map;

/**
 * Created by Profisien on 5/27/2016.
 */
public interface GetMapByMinorIdListener {

    void onGetMapByMinorIdListenerComplete(Map map);
    void onGetMapByMinorIdListenerError(String message);
}
