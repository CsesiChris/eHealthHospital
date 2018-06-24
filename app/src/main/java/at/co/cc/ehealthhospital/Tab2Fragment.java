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

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private static String text = "";

    private static TextView textOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);

        textOut = (TextView) view.findViewById(R.id.textout);
        textOut.setMovementMethod(new ScrollingMovementMethod());

            Log.d(TAG, "text initalized: " + textOut);
            Log.d(TAG, "ID: " + this);

        textOut.setVisibility(TextView.VISIBLE);
        textOut.setText(text);
        return view;
    }

    public void appendToTextInfo(String i) {
        text = text + "\n" + i;
        textOut.setText(text);
        textOut.setVisibility(TextView.VISIBLE);
        //textInfo.append(i + "\n");
    }

    public void hideText() {
        text = "";
        if (textOut != null) {
            textOut.setText(text);
        }
    }
}
