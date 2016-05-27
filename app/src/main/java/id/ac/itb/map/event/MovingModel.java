package id.ac.itb.map.event;

/**
 * Created by Profisien on 5/26/2016.
 */
public class MovingModel {

    private static MovingModel instance;
    private MovingListener movingListener;


    public static MovingModel getInstance() {
        if (instance == null)
            instance = new MovingModel();
        return instance;
    }

    public void setMovingListener(MovingListener movingListener) {
        this.movingListener = movingListener;
    }

    public void setPosition(double x, double y) {
        if (movingListener != null)
            movingListener.onMoving(x, y);
    }
}
