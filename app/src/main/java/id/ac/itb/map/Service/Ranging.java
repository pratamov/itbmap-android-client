package id.ac.itb.map.Service;

import android.util.Log;

import id.ac.itb.map.domain.Map;
import id.ac.itb.map.domain.Position;
import id.ac.itb.map.exception.BeaconOutOfRangeException;
import id.ac.itb.map.exception.MapException;

public class Ranging {
    public static int DISTANCE_TTL = 10000;

    public static Ranging instance;
    private Map map;
    private double distanceBeacon1;
    private double distanceBeacon2;
    private double distanceBeacon3;
    private long distanceBeacon1Validity;
    private long distanceBeacon2Validity;
    private long distanceBeacon3Validity;


    public static Ranging getInstance() {

        if (Ranging.instance == null)
            Ranging.instance = new Ranging();
        return Ranging.instance;

    }

    public Ranging() {


    }

    public void unselectMap(){

        this.map = null;

    }

    public boolean isMapSelected(){

        return map != null;

    }

    public Map getMap(){

        return this.map;

    }


    public void selectMap(Map map)  {

        this.map = map;

    }

    public void setDistance(int minorID, int major_id, double distance) {

        if (!(map == null) && map.getMinorId() == minorID) {
            Log.w("BEACON DIST", major_id+"_"+distance);
            switch (major_id) {
                case 1: {
                    Log.w("BEACON DIST 1", major_id+"_"+distance);
                    distanceBeacon1 = distance;
                    distanceBeacon1Validity = System.currentTimeMillis() + DISTANCE_TTL;
                }
                case 2: {
                    Log.w("BEACON DIST 2", major_id+"_"+distance);
                    distanceBeacon2 = distance;
                    distanceBeacon2Validity = System.currentTimeMillis() + DISTANCE_TTL;
                }
                case 3: {
                    Log.w("BEACON DIST 3", major_id+"_"+distance);
                    distanceBeacon3 = distance;
                    distanceBeacon3Validity = System.currentTimeMillis() + DISTANCE_TTL;
                }
                default: Log.w("BEACON DIST DEF", major_id+"_"+distance);
            }
        }
    }

    public Position trilateration(boolean real_position) throws MapException, BeaconOutOfRangeException {

        if (map == null)
            throw new MapException("Map is not selected");

        long millis = System.currentTimeMillis();
        if (millis >= distanceBeacon1Validity ||
                millis >= distanceBeacon2Validity ||
                millis >= distanceBeacon3Validity)
            throw new BeaconOutOfRangeException("Beacon is out of range");

        double S = (
                Math.pow(map.getPositionXBeacon3(), 2.) -
                        Math.pow(map.getPositionXBeacon2(), 2.) +
                        Math.pow(map.getPositionYBeacon3(), 2.) -
                        Math.pow(map.getPositionYBeacon2(), 2.) +
                        Math.pow(distanceBeacon2, 2.) -
                        Math.pow(distanceBeacon3, 2.)
        ) / 2.0;
        double T = (
                Math.pow(map.getPositionXBeacon1(), 2.) -
                        Math.pow(map.getPositionXBeacon2(), 2.) +
                        Math.pow(map.getPositionYBeacon1(), 2.) -
                        Math.pow(map.getPositionYBeacon2(), 2.) +
                        Math.pow(distanceBeacon2, 2.) -
                        Math.pow(distanceBeacon1, 2.)
        ) / 2.0;
        double y = ((T * (map.getPositionXBeacon2() - map.getPositionXBeacon3())) - (S * (map.getPositionXBeacon2() - map.getPositionXBeacon1()))) /
                (((map.getPositionYBeacon1() - map.getPositionYBeacon2()) * (map.getPositionXBeacon2() - map.getPositionXBeacon3())) -
                        ((map.getPositionYBeacon3() - map.getPositionYBeacon2()) * (map.getPositionXBeacon2() - map.getPositionXBeacon1())));
        double x = ((y * (map.getPositionYBeacon1() - map.getPositionYBeacon2())) - T) /
                (map.getPositionXBeacon2() - map.getPositionXBeacon1());

        if (!real_position){
            y = y / map.getMapRealWidth();
            x = x / map.getMapRealWidth();
        }
        Position position = new Position(x, y);
        return position;

    }

    public String getInformationHtml(){
        String x = "?";
        String y = "?";
        try {
            Position position = trilateration(true);
            x = Math.round(position.getX()*100)/100+"";
            y = Math.round(position.getY()*100)/100+"";
        } catch (MapException e) {
            e.printStackTrace();
        } catch (BeaconOutOfRangeException e) {
            e.printStackTrace();
        }
        String s = "<h6>UUID : </h6>" + map.getUuid() +
                "<h6>Map Name : </h6>" + map.getMapName() +
                "<h6>Description : </h6>" + map.getMapDescription() +
                "<h6>User Position (Meter) [X,Y] : </h6>[" + x + "," + y + "]" +
                "<h6>Area Size (Meter) [W,H] : </h6>[" + map.getMapRealWidth() + "," + map.getMapRealHeight() + "]" +
                "<h6>Distance Beacon 1 (Meter) : </h6>" + distanceBeacon1 +
                "<h6>Distance Beacon 2 (Meter) : </h6>" + distanceBeacon2 +
                "<h6>Distance Beacon 3 (Meter) : </h6>" + distanceBeacon3 +
                "<h6>Map Size (Pizel) [W,H] : </h6>[" + map.getMapWidth() + "," + map.getMapHeight() + "]";
        return s;
    }
}
