package com.headdetect.android.mocklocation.Tasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AddressLookupTask extends AsyncTask<LatLng, Void, String> {

    Context mContext;
    GoogleMap mMap;
    MarkerOptions mMarkerOptions;


    public AddressLookupTask(Context context, GoogleMap map, MarkerOptions markerOptions) {

        super();
        this.mContext = context;
        this.mMap = map;
        this.mMarkerOptions = markerOptions;

    }

    @Override
    protected String doInBackground(LatLng... params) {
        Geocoder geocoder = new Geocoder(mContext);
        double lat = params[0].latitude;
        double lng = params[0].longitude;

        List<Address> addresses = null;
        StringBuilder actualAddress = new StringBuilder();

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null) {
            for (Address address : addresses) {
                actualAddress.append(String.format("%s, %s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality(), address.getCountryName()) + "\n");
            }
        }

        return actualAddress.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        mMarkerOptions.title(result);
        mMarkerOptions.visible(true);

        Log.d("MockLocation", result);

        mMap.addMarker(mMarkerOptions).showInfoWindow();
    }

}