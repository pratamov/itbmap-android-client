package id.ac.itb.map.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * @author Mike Dunn, 2/19/16.
 */
public class BitmapProviderPicasso implements BitmapProvider {
    public Bitmap getBitmap(Tile tile, Context context) {
        Object data = tile.getData();
        if (data instanceof String) {
            String unformattedFileName = (String) tile.getData();
            String formattedFileName = String.format(unformattedFileName, tile.getColumn(), tile.getRow());
            try {
                //return Picasso.with( context ).load( formattedFileName ).memoryPolicy( MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE ).get();
                return Picasso.with(context).load(formattedFileName).get();
            } catch (IOException e) {
                // probably couldn't find the file
            }
        }
        return null;
    }
}
