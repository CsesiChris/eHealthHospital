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

public class Tab4Fragment extends Fragment {
    private static final String TAG = "Tab4Fragment";

    private static TextView textOut4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab4_fragment, container, false);

        if (textOut4 == null) {
            textOut4 = (TextView) view.findViewById(R.id.textout4);
            textOut4.setMovementMethod(new ScrollingMovementMethod());

            Log.d(TAG, "text initalized: " + textOut4);
            Log.d(TAG, "ID: " + this);
        }


        return view;
    }

    public void appendToTextInfo(String i) {
        textOut4.append(i);
    }
}
