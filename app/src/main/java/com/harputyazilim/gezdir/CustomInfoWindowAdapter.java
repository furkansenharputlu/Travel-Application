package com.harputyazilim.gezdir;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by furkan on 26.03.2017.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    View view;

    public CustomInfoWindowAdapter(Context context){
        this.context=context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Log.d("FS","getInfoWindow");
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.d("FS","getInfoContents");
        TextView poiName= (TextView) view.findViewById(R.id.poiName);
        Button add= (Button) view.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FS","addddddddd");
            }
        });
        return view;
    }
}
