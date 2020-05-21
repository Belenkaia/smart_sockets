package ru.nsu.smartsocket1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;

/*

import java.util.Date;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.CoordinatorLayout;
*/
public class MainActivity extends AppCompatActivity {

    private final String MAPKIT_API_KEY = "38f7019b-3523-494e-a2dc-088ce94d1298";
    private MapView mapView;
    private double userLatitude = 0;
    private double userLongitude = 0;
    private double ZOOM_INCREMENT = 5.0;

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
            }
            else{
                System.out.println("Sorry, location unavailable");
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Проверка наличия разрешений
            // Если нет разрешения на использование соответсвующих разркешений выполняем какие-то действия
            System.out.println("have not got permissions");
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,listener);

        // Укажите имя activity вместо map.
        setContentView(R.layout.activity_main);
        mapView = (MapView)findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    protected void moveCameraToUser()
    {
        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), 31.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }
    public void onButtonPlusClick(View view)
    {
        float zoom = mapView.getMap().getCameraPosition().getZoom();
        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), ((float) (zoom + ZOOM_INCREMENT)), 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

    }
    public void onButtonMinusClick(View view)
    {
        float zoom = mapView.getMap().getCameraPosition().getZoom();
        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), ((float) (zoom - ZOOM_INCREMENT)), 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

}
