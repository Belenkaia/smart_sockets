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
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;


import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;
import android.graphics.Color;
import android.graphics.PointF;

import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {

    private final String MAPKIT_API_KEY = "38f7019b-3523-494e-a2dc-088ce94d1298";
    private MapView mapView;
    private double userLatitude = 59.9386;
    private double userLongitude = 30.3141;
    private float ZOOM_INCREMENT = 1.0f;
    private float ZOOM_DEFAULT = 10.05f;
    private final int MIN_TIME_LOCATION_UPDATE = 1000;
    private final int MIN_DISTANCE_LOCATION_UPDATE = 10;
    private float tmpZoom = ZOOM_DEFAULT;
    private static final int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 112;
    private static final int REQUEST_CODE_PERMISSION_ACCESS_COARSE_LOCATION = 113;
    private static final String ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private LocationManager manager = null;
    private PlacemarkMapObject userMark;
    private ArrayList<PlacemarkMapObject> socketPlacemarks = new ArrayList<PlacemarkMapObject>();
    private SocketManager socketManager = new SocketManager();

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
                moveCameraToUser();
                userMark.setGeometry(new Point(userLatitude, userLongitude));

                socketManager.updateSocketArray(userLatitude, userLongitude);
                while(!socketManager.isSocketsIsReady())
                {
                    System.out.println("wait for socket list");
                }
                updateSocketMarks();

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


        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapview);
                mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), ZOOM_DEFAULT, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        if (checkSelfPermission(ACCESS_FINE_LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(ACCESS_COARSE_LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            // Проверка наличия разрешений
            // Если нет разрешения на использование соответсвующих разркешений выполняем какие-то действия
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION_PERMISSION}, REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION_PERMISSION}, REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            registerLocationManager();
        }
        userMark = mapView.getMap().getMapObjects().addPlacemark(new Point(userLatitude, userLongitude), ImageProvider.fromResource(this, R.drawable.location2));

        socketManager.updateSocketArray(userLatitude, userLongitude);
        while(!socketManager.isSocketsIsReady())
        {
            System.out.println("wait for socket list");
        }
        setSocketMarks();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                registerLocationManager();
           }
       }else
        if (requestCode == REQUEST_CODE_PERMISSION_ACCESS_COARSE_LOCATION) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                registerLocationManager();
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

   }
   private void registerLocationManager()
   {
       if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
               checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
       {
           if(manager == null)
           {
               manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
               manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_LOCATION_UPDATE, MIN_DISTANCE_LOCATION_UPDATE, listener);
           }

       }
   }
    private void moveCameraToUser()
    {
        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), tmpZoom, 0.0f, 0.0f),
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
        tmpZoom = mapView.getMap().getCameraPosition().getZoom();
        tmpZoom += ZOOM_INCREMENT;
        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), tmpZoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

    }
    public void onButtonMinusClick(View view)
    {

        tmpZoom = mapView.getMap().getCameraPosition().getZoom();
        tmpZoom -= ZOOM_INCREMENT;
        mapView.getMap().move(
                new CameraPosition(new Point(userLatitude, userLongitude), tmpZoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    public void onButtonUserLocationClick(View view)
    {
        moveCameraToUser();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {
    }

    private void setSocketIcon(int countFreeSocket, int i)
    {
        switch (countFreeSocket) //update icon
        {
            case 0:
            {
                socketPlacemarks.get(i).setIcon(ImageProvider.fromResource(this, R.drawable.socket_0_1));
                break;
            }
            case 1:
            {
                socketPlacemarks.get(i).setIcon(ImageProvider.fromResource(this, R.drawable.socket_1_1));
                break;
            }
            case 2:
            {
                socketPlacemarks.get(i).setIcon(ImageProvider.fromResource(this, R.drawable.socket_2_1));
                break;
            }
            case 3:
            {
                socketPlacemarks.get(i).setIcon(ImageProvider.fromResource(this, R.drawable.socket_3_1));
                break;
            }
            case 4:
            {
                socketPlacemarks.get(i).setIcon(ImageProvider.fromResource(this, R.drawable.socket_4_2));
                break;
            }
        };
    }
    public void onButtonUpdateClick(View view)
    {
        if(socketManager != null)
        {
            socketManager.updateSocketArray(userLatitude, userLongitude);
            while(!socketManager.isSocketsIsReady())
            {
                System.out.println("wait for socket list");
            }
            updateSocketMarks();
        }
    }
    private void updateSocketMarks()
    {
        if(socketManager != null) {
            ArrayList<Socket> tmpSocketList = socketManager.getSocketArray();
            for (int i = 0; i < socketPlacemarks.size(); i++) {
                socketPlacemarks.get(i).setGeometry(tmpSocketList.get(i).getPosition()); //update position

                setSocketIcon(tmpSocketList.get(i).getFreeSocket(), i);//update icon
            }
        }
    }
    private void setSocketMarks()
    {
        if(socketManager != null)
        {
            ArrayList<Socket> tmpSocketList = socketManager.getSocketArray();
            for (Socket socket : tmpSocketList) {
             socketPlacemarks.add(mapView.getMap().getMapObjects().addPlacemark(socket.getPosition(), ImageProvider.fromResource(this, R.drawable.socket_4_2)));
            }

        }
    }
}

