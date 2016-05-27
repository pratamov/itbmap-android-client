package id.ac.itb.map.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;

import id.ac.itb.map.R;
import id.ac.itb.map.Service.Ranging;
import id.ac.itb.map.event.MovingListener;
import id.ac.itb.map.event.MovingModel;
import id.ac.itb.map.utils.BitmapProviderPicasso;

public class MapActivity extends TileViewActivity implements MovingListener {

    private ImageView user_marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TileView tileView = getTileView();

        tileView.setBitmapProvider(new BitmapProviderPicasso());

        tileView.setSize(
                Ranging.getInstance().getMap().getMapWidth() * 2,
                Ranging.getInstance().getMap().getMapHeight() * 2);

        tileView.addDetailLevel(1.000f, Ranging.getInstance().getMap().getMapTileImageUrl() + "1000/%d_%d.jpg");
        tileView.addDetailLevel(0.500f, Ranging.getInstance().getMap().getMapTileImageUrl() + "500/%d_%d.jpg");
        tileView.addDetailLevel(0.250f, Ranging.getInstance().getMap().getMapTileImageUrl() + "250/%d_%d.jpg");
        tileView.addDetailLevel(0.125f, Ranging.getInstance().getMap().getMapTileImageUrl() + "125/%d_%d.jpg");

        tileView.setShouldRenderWhilePanning(true);

        tileView.defineBounds(0, 0, 1, 1);

        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);

        addUserMarker(0, 0);

        double real_width = Ranging.getInstance().getMap().getMapRealWidth();


        ImageView marker_bluetooth_1 = new ImageView(this);

        marker_bluetooth_1.setImageResource(R.drawable.beacon_1_marker);

        getTileView().addMarker(marker_bluetooth_1,
                Ranging.getInstance().getMap().getPositionXBeacon1() / real_width,
                Ranging.getInstance().getMap().getPositionYBeacon1() / real_width,
                null, null);


        ImageView marker_bluetooth_2 = new ImageView(this);

        marker_bluetooth_2.setImageResource(R.drawable.beacon_2_marker);

        getTileView().addMarker(marker_bluetooth_2,
                Ranging.getInstance().getMap().getPositionXBeacon2() / real_width,
                Ranging.getInstance().getMap().getPositionYBeacon2() / real_width,
                null, null);


        ImageView marker_bluetooth_3 = new ImageView(this);

        marker_bluetooth_3.setImageResource(R.drawable.beacon_3_marker);

        getTileView().addMarker(marker_bluetooth_3,
                Ranging.getInstance().getMap().getPositionXBeacon3() / real_width,
                Ranging.getInstance().getMap().getPositionYBeacon3() / real_width,
                null, null);

        tileView.setMarkerTapListener(mMarkerTapListener);

        tileView.setScale(0f);

        frameTo(0.5, 0.5);

        MovingModel.getInstance().setMovingListener(this);
    }

    private void addUserMarker(double x, double y) {

        user_marker = new ImageView(this);
        user_marker.setImageResource(R.drawable.user_marker);
        getTileView().addMarker(user_marker, x, y, null, null);

    }

    private void setPin(final double x, final double y) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getTileView() != null)
                    getTileView().moveMarker(user_marker, x, y);
            }
        });

    }

    private MarkerLayout.MarkerTapListener mMarkerTapListener = new MarkerLayout.MarkerTapListener() {
        @Override
        public void onMarkerTap(View v, int x, int y) {

            Toast.makeText(getApplicationContext(), "Position : " + x + "," + y, Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void onMoving(double x, double y) {

        Log.w("BEACON PIN X", x + "");
        Log.w("BEACON PIN Y", y + "");
        setPin(x, y);
    }
}
