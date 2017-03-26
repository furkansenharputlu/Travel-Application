package com.harputyazilim.gezdir;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.SearchManager;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds mBounds = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));

    private SearchView searchView;
    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TimePicker timePicker;
    private Button chooseTime, back, add, match;
    private ToggleButton startAndFinish;
    private LinearLayout timeManager;
    private boolean turnPosition;
    private SeekBar radius;
    private double radiusValue;

    Event tripEvent = new Event();
    RequestQueue queue;
    View infoWindow;


    private static final String TAG="MyTag";
    private static final int MAX_RADIUS=25;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        turnPosition=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        buildGoogleApiClient();
       // mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchView = (SearchView) findViewById(R.id.search);
        mPlacesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.search_row, mGoogleApiClient, mBounds, null);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mPlacesAutoCompleteAdapter);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        chooseTime = (Button) findViewById(R.id.chooseTime);
        back = (Button) findViewById(R.id.back);
        match= (Button) findViewById(R.id.match);
        radius = (SeekBar) findViewById(R.id.radius);

        startAndFinish = (ToggleButton) findViewById(R.id.startAndFinish);
        startAndFinish.setTextOff("BAŞLANGIÇ");
        startAndFinish.setTextOn("BİTİŞ");
        timeManager = (LinearLayout) findViewById(R.id.timeManager);

        this.infoWindow =getLayoutInflater().inflate(R.layout.info_window, null);

        chooseTime.setOnClickListener(this);
        back.setOnClickListener(this);
        match.setOnClickListener(this);
        searchView.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("") && mGoogleApiClient.isConnected()) {
                    mPlacesAutoCompleteAdapter.getFilter().filter(newText);

                } else if (!mGoogleApiClient.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Google API Client  not connected", Toast.LENGTH_SHORT).show();

                }
                mRecyclerView.setVisibility(View.VISIBLE);
                return false;
            }
        });
        radius.setMax(MAX_RADIUS);
        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusValue=progress;
                Log.d("FS","radius "+radiusValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        LinearLayout searchMechanism = (LinearLayout) findViewById(R.id.searchMechanism);
        searchMechanism.requestFocus();


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAutoCompleteAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            //Do the things here on Click.....
                            LatLng latlng = places.get(0).getLatLng();
                            mMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(mPlacesAutoCompleteAdapter.mResultList.get(position).description.toString()));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                            mRecyclerView.setVisibility(View.INVISIBLE);
                            searchView.clearFocus();
                            //mPlacesAutoCompleteAdapter.getFilter().filter("");


                            //  Toast.makeText(getApplicationContext(), String.valueOf(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something goes wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Log.i("TAG", "Clicked: " + item.description);
                Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
            }
        }
        ));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(this);


        GoogleMap.OnMyLocationChangeListener locationListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                double latitude = location.getLatitude();
                double longitude =location.getLongitude();
                if(turnPosition) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),
                            15));
                    turnPosition=false;
                }

            }
        };
        mMap.setOnMyLocationChangeListener(locationListener);




        // Add a marker in Sydney and move the camera
      //  LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions());


       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.chooseTime :
                if(!startAndFinish.isChecked()){
                    tripEvent.setStartTime(timePicker.getCurrentHour()+"."+timePicker.getCurrentMinute());
                    startAndFinish.setChecked(true);
                }else{
                    tripEvent.setEndTime(timePicker.getCurrentHour()+"."+timePicker.getCurrentMinute());
                    timeManager.animate()
                            .translationY(timeManager.getHeight())
                            .alpha(0.0f)
                            .setDuration(300);
                }
                break;
            case R.id.search:
                mRecyclerView.setVisibility(View.VISIBLE);
                searchView.onActionViewExpanded();
                break;
            case R.id.back:
                timeManager.animate()
                        .translationY(0)
                        .alpha(1.0f)
                        .setDuration(300);
                break;
            case R.id.match:
                postEvent();

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.layout_main, new Matches())
                        .addToBackStack(null)
                        .commit();


        }

    }

    public void match(){

    }

    public void postEvent(){
        queue = Volley.newRequestQueue(this);
        String url = "http://192.168.88.149:3000/events/addEvent";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("userId", new Random().nextInt(10000));
            jsonBody.put("startTime", tripEvent.getStartTime());
            jsonBody.put("endTime", tripEvent.getEndTime());


            JSONArray coordinates = new JSONArray();
            JSONArray points = new JSONArray();
            for(int i=0;i<tripEvent.coordinates.size();i++){

                JSONArray point= new JSONArray();
                point.put(tripEvent.coordinates.get(i).first);
                point.put(tripEvent.coordinates.get(i).second);

                points.put(point);
            }


            //last pair of coordinate pairs will be first coordinate point so that we can draw polynome
            if(tripEvent.coordinates.size()>=3) {
                JSONArray point = new JSONArray();
                point.put(tripEvent.coordinates.get(0).first);
                point.put(tripEvent.coordinates.get(0).second);
                points.put(point);
            }

            coordinates.put(points);
            jsonBody.put("coordinates", coordinates);
            jsonBody.put("radius",radiusValue);

            System.out.println(jsonBody.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


// Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       // mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("VOLLEY", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("VOLLEY", error.getMessage());
                    }
                });



        // Set the tag on the request.
// Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(queue!=null){
            queue.cancelAll(TAG);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        Log.d("FS","getInfoWindow");
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.d("FS","getInfoContents");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        infoWindow=inflater.inflate(R.layout.info_window, null);


        TextView poiName= (TextView) infoWindow.findViewById(R.id.poiName);
        poiName.setText(marker.getTitle());
        Button add= (Button) infoWindow.findViewById(R.id.add);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                tripEvent.addCoordinates(marker.getPosition().latitude,marker.getPosition().longitude);
                marker.hideInfoWindow();
            }
        });


        return infoWindow;

    }
}
