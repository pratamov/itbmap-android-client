package id.ac.itb.map.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Profisien on 5/25/2016.
 */
public class MapDaoImpl implements MapDao{
    private List<String> listBeaconUuid;
    private HashMap<String, String> mapUuidToName;
    private HashMap<String, String> mapNameToUuid;

    @Override
    public List<String> getListBeaconUuid() {

        if (listBeaconUuid == null)
            listBeaconUuid = Arrays.asList("cb10023f-a318-3394-4199-a8730c7c1aec");
        return listBeaconUuid;

    }

    @Override
    public String getMapNameByBeaconUuid(String uuid) {

        if (mapUuidToName == null) {
            mapUuidToName = new HashMap<>();
            mapNameToUuid = new HashMap<>();
        }
        if (mapUuidToName.containsKey(uuid))
            return mapUuidToName.get(uuid);
        else{

            //TODO get map name by uuid
            String mapName = "[NAME]"+uuid;

            mapUuidToName.put(uuid, mapName);
            mapNameToUuid.put(mapName, uuid);
            return mapName;
        }

    }

    @Override
    public String getBeaconUuidByMapName(String mapName) {

        if (mapNameToUuid == null) {
            mapUuidToName = new HashMap<>();
            mapNameToUuid = new HashMap<>();
        }
        if (mapNameToUuid.containsKey(mapName))
            return mapNameToUuid.get(mapName);
        else{

            //TODO get uuid by map name
            String uuid = "[UUID]"+mapName;

            mapUuidToName.put(uuid, mapName);
            mapNameToUuid.put(mapName, uuid);
            return uuid;
        }
    }

    @Override
    public id.ac.itb.map.domain.Map getMap(String uuid) {

        id.ac.itb.map.domain.Map labtek_v_lt_3 = new id.ac.itb.map.domain.Map();

        labtek_v_lt_3.setUuid(uuid);
        labtek_v_lt_3.setPositionXBeacon1(1.0);
        labtek_v_lt_3.setPositionYBeacon1(1.0);

        labtek_v_lt_3.setPositionXBeacon2(3.0);
        labtek_v_lt_3.setPositionYBeacon2(1.0);

        labtek_v_lt_3.setPositionXBeacon3(1.0);
        labtek_v_lt_3.setPositionYBeacon3(3.0);
        labtek_v_lt_3.setMapRawImageFilename("painting");
        labtek_v_lt_3.setMapTileImageUrl("http://216.126.192.36/maps/ITB_LABTEK_V_LANTAI_3_TIMUR/");
        labtek_v_lt_3.setMapName("Boston");
        labtek_v_lt_3.setRegionName("USA");
        labtek_v_lt_3.setMapDescription("");
        labtek_v_lt_3.setMapRealHeight(8);
        labtek_v_lt_3.setMapRealWidth(8);
        labtek_v_lt_3.setMapWidth(4704);
        labtek_v_lt_3.setMapHeight(4704);

        return labtek_v_lt_3;
    }
}
