package at.co.cc.ehealthhospital;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private static String text = "";

    private static TextView textInfo;
//    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

 //       if (view == null) {

        View view = inflater.inflate(R.layout.tab1_fragment, container, false);

        textInfo = (TextView) view.findViewById(R.id.info);
        textInfo.setMovementMethod(new ScrollingMovementMethod());

            Log.d(TAG, "textInfo initalized: " + textInfo);
            Log.d(TAG, "ID: " + this);
   //     }

        Log.d(TAG, "textInfo visibility: " + textInfo.getVisibility());
        textInfo.setVisibility(TextView.VISIBLE);
        textInfo.setText(text);
        return view;
    }

    public void appendToTextInfo(String i) {
        text = text + "\n" + i;
        textInfo.setText(text);
        textInfo.setVisibility(TextView.VISIBLE);
        //textInfo.append(i + "\n");
    }

    public void hideText() {
        text = "";
        if (textInfo != null) {
            textInfo.setText(text);
        }
    }
}
