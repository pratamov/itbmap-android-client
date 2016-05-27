package id.ac.itb.map.activity;

import id.ac.itb.map.R;
import id.ac.itb.map.Service.Ranging;
import id.ac.itb.map.domain.Position;
import id.ac.itb.map.exception.BeaconOutOfRangeException;
import id.ac.itb.map.exception.MapException;
import id.ac.itb.map.event.MovingModel;
import id.ac.itb.map.rest.GetMapByName;
import id.ac.itb.map.rest.GetMapByNameListener;
import id.ac.itb.map.rest.GetMapList;
import id.ac.itb.map.rest.GetMapListListener;

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

public class VisibleMapActivity extends ListActivity implements BeaconConsumer, GetMapByNameListener, GetMapListListener {
    public static int VISIBLE_BEACON_TTL = 5000;

    List<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    BeaconManager beaconManager;
    HashMap<String, Long> visibleBeaconUuid;

    HashMap<String, String> mapList;
    boolean serviceConnected;
    List<String> mapName;

    private double x;
    private double y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visible_map);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

        visibleBeaconUuid = new HashMap<>();
        serviceConnected = false;

        new GetMapList(this).execute();

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

        new GetMapByName(this).execute(listItems.get(position));

    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon bcn : beacons) {
                    String uuid = bcn.getId1().toString();
                    String majorId = bcn.getId2().toString();
                    String minorId = bcn.getId3().toString();
                    double distance = bcn.getDistance();
                    visibleBeaconUuid.put(uuid, System.currentTimeMillis() + VISIBLE_BEACON_TTL);
                    if (Ranging.getInstance().isMapSelected())
                        Ranging.getInstance().setDistance(uuid, Integer.parseInt(majorId), distance);
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
        if (mapList != null)
            rangingBeaconInRegion();
    }

    public void updateList() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapName = getVisibleMapName();
                listItems.clear();
                listItems.addAll(mapName);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private List<String> getVisibleMapName() {

        List<String> visibleMapName = new ArrayList<>();

        Iterator it = visibleBeaconUuid.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry pair = (Map.Entry) it.next();
            if (System.currentTimeMillis() <= (long) pair.getValue()) {
                if (mapList != null) {
                    String mapName = mapList.get(pair.getKey());
                    visibleMapName.add(mapName);
                }
            }
            //it.remove();
        }
        return visibleMapName;

    }

    @Override
    public void onGetMapByNameListenerComplete(final id.ac.itb.map.domain.Map map) {

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
    public void onGetMapByNameListenerError(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VisibleMapActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onGetMapListListenerComplete(HashMap<String, String> mapList) {

        this.mapList = mapList;
        if (serviceConnected)
            rangingBeaconInRegion();

    }

    @Override
    public void onGetMapListListenerError(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VisibleMapActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void rangingBeaconInRegion(){
        try {
            List<String> uuidList = new ArrayList<>(mapList.keySet());
            for (String uuid : uuidList) {
                beaconManager.startRangingBeaconsInRegion(new Region(uuid, null, null, null));
            }
        } catch (RemoteException e) {
        }
    }
}
