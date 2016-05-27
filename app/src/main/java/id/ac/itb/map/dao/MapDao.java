package id.ac.itb.map.dao;

import java.util.List;

/**
 * Created by Profisien on 5/25/2016.
 */
public interface MapDao {
    id.ac.itb.map.domain.Map getMap(String uuid);
    List<String> getListBeaconUuid();
    String getMapNameByBeaconUuid(String uuid);
    String getBeaconUuidByMapName(String mapName);

}
