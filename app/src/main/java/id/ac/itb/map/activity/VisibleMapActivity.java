package id.ac.itb.map.activity;

import id.ac.itb.map.R;
import id.ac.itb.map.Service.Ranging;
import id.ac.itb.map.domain.Position;
import id.ac.itb.map.exception.BeaconOutOfRangeException;
import id.ac.itb.map.exception.MapException;
import id.ac.itb.map.event.MovingModel;
import id.ac.itb.map.rest.GetMapByMinorId;
import id.ac.itb.map.rest.GetMapByMinorIdListener;
import id.ac.itb.map.rest.GetMaps;
import id.ac.itb.map.rest.GetMapsListener;
import id.ac.itb.map.rest.GetRegions;
import id.ac.itb.map.rest.GetRegionsListener;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VisibleMapActivity extends ListActivity implements BeaconConsumer, GetRegionsListener, GetMapsListener, GetMapByMinorIdListener {
    public static int VISIBLE_BEACON_TTL = 5000;

    List<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    BeaconManager beaconManager;

    /**
     * region = <uuid, region_name>
     */
    HashMap<String, String> regions;

    /**
     * map = <minor_id, map_name>
     */
    HashMap<Integer, String> maps;

    /**
     * map = <map_name, millis expired>
     */
    HashMap<Integer, Long> visibleMap;

    boolean serviceConnected;

    private double x;
    private double y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visible_map);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

        maps                = new HashMap<>();
        visibleMap          = new HashMap<>();

        serviceConnected    = false;

        new GetRegions(this).execute();
        new GetMaps(this).execute();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        beaconManager.setBackgroundScanPeriod(500);
        beaconManager.setBackgroundBetweenScanPeriod(20);
        beaconManager.bind(this);

    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String mapName = listItems.get(position);

        Iterator it = maps.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue().equals(mapName)) {
                int minorId = (int) pair.getKey();
                new GetMapByMinorId(this).execute(minorId);
            }
        }


    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon bcn : beacons) {
                    String uuid = bcn.getId1().toString();
                    int majorId = Integer.parseInt(bcn.getId2().toString());
                    int minorId = Integer.parseInt(bcn.getId3().toString());
                    double distance = bcn.getDistance();
                    visibleMap.put(minorId, System.currentTimeMillis() + VISIBLE_BEACON_TTL);
                    if (Ranging.getInstance().isMapSelected())
                        Ranging.getInstance().setDistance(minorId, majorId, distance);
                }

                try {
                    Position position = Ranging.getInstance().trilateration(false);
                    MovingModel.getInstance().setPosition(position.getX(), position.getY());
                } catch (MapException e) {
                    Log.w("Exception", e.getMessage());
                } catch (BeaconOutOfRangeException e) {
                    Log.w("Exception", e.getMessage());
                }

                updateList();
            }
        });

        serviceConnected = true;
        //Menunggu event onGetRegionsListenerComplete
        if (regions != null)
            rangingBeaconInRegion();
    }

    public void updateList() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listItems.clear();
                listItems.addAll(getVisibleAllMapName());
                adapter.notifyDataSetChanged();
            }
        });

    }

    private List<String> getVisibleAllMapName() {

        List<String> visibleMapName = new ArrayList<>();

        Iterator it = visibleMap.entrySet().iterator();

        while (it.hasNext()) {
            final Map.Entry pair = (Map.Entry) it.next();
            if (System.currentTimeMillis() <= (long) pair.getValue()) {
                int minorId = (int) pair.getKey();
                String mapName = maps.get(minorId);
                if (mapName != null)
                    visibleMapName.add(mapName);
            }
            //it.remove();
        }

        return visibleMapName;

    }

    private void rangingBeaconInRegion(){
        try {
            List<String> uuidList = new ArrayList<>(regions.keySet());
            for (String uuid : uuidList) {
                beaconManager.startRangingBeaconsInRegion(new Region(uuid, null, null, null));
            }
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onGetRegionsListenerComplete(HashMap<String, String> regions) {

        this.regions = regions;
        // Menunggu event onBeaconServiceConnect
        if (serviceConnected)
            rangingBeaconInRegion();

    }

    @Override
    public void onGetRegionsListenerError(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VisibleMapActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onGetMapsListenerComplete(HashMap<Integer, String> maps) {

        this.maps = maps;

    }

    @Override
    public void onGetMapsListenerError(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VisibleMapActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onGetMapByMinorIdListenerComplete(final id.ac.itb.map.domain.Map map) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Ranging.getInstance().selectMap(map);
                Intent myIntent = new Intent(VisibleMapActivity.this, MapActivity.class);
                VisibleMapActivity.this.startActivity(myIntent);
            }
        });

    }

    @Override
    public void onGetMapByMinorIdListenerError(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VisibleMapActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
