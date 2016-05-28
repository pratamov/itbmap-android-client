package id.ac.itb.map.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
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
    private FloatingActionButton infoButton;
    private double x;
    private double y;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        infoButton = (FloatingActionButton) findViewById(R.id.infoButton);

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

        tileView.setOnLongClickListener(longClickListener);

        infoButton.setOnClickListener(infoButtonListener);

    }

    private void addUserMarker(double x, double y) {

        user_marker = new ImageView(this);
        user_marker.setImageResource(R.drawable.user_marker);
        getTileView().addMarker(user_marker, x, y, null, null);

    }

    private View.OnClickListener infoButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
                    alertDialog.setTitle("Positioning Information");
                    alertDialog.setMessage(Html.fromHtml(Ranging.getInstance().getInformationHtml()));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        }
    };

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener(){

        @Override
        public boolean onLongClick(View v) {

            Toast.makeText(getApplicationContext(), "Position : " + v.getScrollX() + "," + v.getScrollY(), Toast.LENGTH_LONG).show();
            return true;

        }
    };

    private MarkerLayout.MarkerTapListener mMarkerTapListener = new MarkerLayout.MarkerTapListener() {
        @Override
        public void onMarkerTap(View v, int x, int y) {

            Toast.makeText(getApplicationContext(), "Position : " + x + "," + y, Toast.LENGTH_LONG).show();

            geofence();

        }
    };

    private void geofence(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO set toast when passing geofence
            }
        });

    }

    @Override
    public void onMoving(final double x, final double y) {

        this.x = x;
        this.y = y;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getTileView() != null)
                    getTileView().moveMarker(user_marker, x, y);
            }
        });

    }

}
