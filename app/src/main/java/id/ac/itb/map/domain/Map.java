package id.ac.itb.map.domain;

import java.util.List;

/**
 * Created by Profisien on 5/25/2016.
 */
public class Map {
    private String uuid;
    private double positionXBeacon1;
    private double positionXBeacon2;
    private double positionXBeacon3;
    private double positionYBeacon1;
    private double positionYBeacon2;
    private double positionYBeacon3;
    private String mapRawImageFilename;
    private String mapTileImageUrl;
    private String mapName;
    private String regionName;
    private String mapDescription;
    private double mapRealWidth;
    private double mapRealHeight;
    private int mapWidth;
    private int mapHeight;

    public Map(){
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public double getPositionXBeacon1() {
        return positionXBeacon1;
    }

    public void setPositionXBeacon1(double positionXBeacon1) {
        this.positionXBeacon1 = positionXBeacon1;
    }

    public double getPositionXBeacon2() {
        return positionXBeacon2;
    }

    public void setPositionXBeacon2(double positionXBeacon2) {
        this.positionXBeacon2 = positionXBeacon2;
    }

    public double getPositionXBeacon3() {
        return positionXBeacon3;
    }

    public void setPositionXBeacon3(double positionXBeacon3) {
        this.positionXBeacon3 = positionXBeacon3;
    }

    public double getPositionYBeacon1() {
        return positionYBeacon1;
    }

    public void setPositionYBeacon1(double positionYBeacon1) {
        this.positionYBeacon1 = positionYBeacon1;
    }

    public double getPositionYBeacon2() {
        return positionYBeacon2;
    }

    public void setPositionYBeacon2(double positionYBeacon2) {
        this.positionYBeacon2 = positionYBeacon2;
    }

    public double getPositionYBeacon3() {
        return positionYBeacon3;
    }

    public void setPositionYBeacon3(double positionYBeacon3) {
        this.positionYBeacon3 = positionYBeacon3;
    }

    public String getMapRawImageFilename() {
        return mapRawImageFilename;
    }

    public void setMapRawImageFilename(String mapRawImageFilename) {
        this.mapRawImageFilename = mapRawImageFilename;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getMapDescription() {
        return mapDescription;
    }

    public void setMapDescription(String mapDescription) {
        this.mapDescription = mapDescription;
    }

    public double getMapRealWidth() {
        return mapRealWidth;
    }

    public void setMapRealWidth(double mapRealWidth) {
        this.mapRealWidth = mapRealWidth;
    }

    public double getMapRealHeight() {
        return mapRealHeight;
    }

    public void setMapRealHeight(double mapRealHeight) {
        this.mapRealHeight = mapRealHeight;
    }

    public String getMapTileImageUrl() {
        return mapTileImageUrl;
    }

    public void setMapTileImageUrl(String mapTileImageUrl) {
        this.mapTileImageUrl = mapTileImageUrl;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    @Override
    public String toString() {
        return "Map{" +
                "uuid='" + uuid + '\'' +
                ", positionXBeacon1=" + positionXBeacon1 +
                ", positionXBeacon2=" + positionXBeacon2 +
                ", positionXBeacon3=" + positionXBeacon3 +
                ", positionYBeacon1=" + positionYBeacon1 +
                ", positionYBeacon2=" + positionYBeacon2 +
                ", positionYBeacon3=" + positionYBeacon3 +
                ", mapRawImageFilename='" + mapRawImageFilename + '\'' +
                ", mapTileImageUrl='" + mapTileImageUrl + '\'' +
                ", mapName='" + mapName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", mapDescription='" + mapDescription + '\'' +
                ", mapRealWidth=" + mapRealWidth +
                ", mapRealHeight=" + mapRealHeight +
                ", mapWidth=" + mapWidth +
                ", mapHeight=" + mapHeight +
                '}';
    }
}
