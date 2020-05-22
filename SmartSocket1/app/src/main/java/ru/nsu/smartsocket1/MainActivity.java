package ru.nsu.smartsocket1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private double userLatitude = 59.9386;
    private double userLongitude = 30.3141;
    private float ZOOM_INCREMENT = 10.0f;
    private float ZOOM_DEFAULT = 10.05f;
    private static final int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 112;
    private static final int REQUEST_CODE_PERMISSION_ACCESS_COARSE_LOCATION = 113;
    private static final String ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private LocationManager manager;
    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
                moveCameraToUser();
            } else {
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

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(ACCESS_FINE_LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(ACCESS_COARSE_LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            // Проверка наличия разрешений
            // Если нет разрешения на использование соответсвующих разркешений выполняем какие-то действия
            System.out.println("have not got permissions");
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION_PERMISSION}, REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION_PERMISSION}, REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
            //return;
        } else {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }

        mapView = (MapView) findViewById(R.id.mapview);

        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), ZOOM_DEFAULT, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        System.out.println("in onCreate");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("got Fine Location");
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
           }
       }else
        if (requestCode == REQUEST_CODE_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("got Coarse Location");
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

   }
    protected void moveCameraToUser()
    {
        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), ZOOM_DEFAULT, 0.0f, 0.0f),
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

        System.out.println("in onStart");
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
