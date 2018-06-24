package at.co.cc.ehealthhospital;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Tab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";

    private static ImageView pic;

    private static boolean picSet = false;

    private static byte[] fullBytes = new byte[0];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab3_fragment, container, false);
        pic = (ImageView) view.findViewById(R.id.pic);
        if (!picSet) {
            pic.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
        }

        Log.d(TAG, "pic initalized: " + pic);
        Log.d(TAG, "ID: " + this);

        return view;
    }

    public void showPictureDummy() {
        picSet = true;
        pic.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        pic.setVisibility(ImageView.VISIBLE);
    }

    public void hidePictureDummy() {
        pic.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
        picSet = false;
    }

    public void appendPictureData(byte[] newBytes) {

        Log.d(TAG, "\nNew Bytes:\n" + newBytes);

        byte[] one = fullBytes;
        //byte[] two = getBytesForTwo();

        byte[] combined = new byte[one.length + newBytes.length];

        System.arraycopy(one,0,combined,0, one.length);
        System.arraycopy(newBytes,0,combined,one.length,newBytes.length);

        fullBytes = combined;
        Log.d(TAG, "\nNew Bytes size: " + newBytes.length);
        Log.d(TAG, "\nFull Bytes size: " + fullBytes.length);
    }

    public void showPicture (byte[] lastBytes) {

        byte[] one = fullBytes;
        //byte[] two = getBytesForTwo();

        Log.d(TAG, "Last size: " + new String(lastBytes));

        List<byte[]> restBytes = tokens(lastBytes, MainActivity.STREAM_STOP.getBytes());

        Log.d(TAG, "Last size 2: " + new String(restBytes.get(0)));

        byte[] combined = new byte[one.length + restBytes.get(0).length];

        System.arraycopy(one,0,combined,0, one.length);
        System.arraycopy(restBytes.get(0),0,combined,one.length,restBytes.get(0).length);


        Log.d(TAG, "Bytes size: " + combined.length);
        Log.d(TAG, "\nBytes:\n" + combined);
        // Convert bytes data into a Bitmap
        Bitmap bmp = BitmapFactory.decodeByteArray(combined, 0, combined.length);
        if (bmp != null && pic != null) {
            pic.setImageBitmap(bmp);
        }
    }

    private static List<byte[]> tokens(byte[] array, byte[] delimiter) {
        List<byte[]> byteArrays = new LinkedList();
        if (delimiter.length == 0) {
            return byteArrays;
        }
        int begin = 0;

        outer:
        for (int i = 0; i < array.length - delimiter.length + 1; i++) {
            for (int j = 0; j < delimiter.length; j++) {
                if (array[i + j] != delimiter[j]) {
                    continue outer;
                }
            }
            byteArrays.add(Arrays.copyOfRange(array, begin, i));
            begin = i + delimiter.length;
        }
        byteArrays.add(Arrays.copyOfRange(array, begin, array.length));
        return byteArrays;
    }
}
