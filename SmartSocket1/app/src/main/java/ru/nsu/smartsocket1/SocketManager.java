package ru.nsu.smartsocket1;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.yandex.mapkit.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SocketManager {
    boolean socketsIsReady = false;
    private ArrayList<Socket> socketArray = new ArrayList<Socket>();
    private final OkHttpClient client = new OkHttpClient();
    private final String SERVER_URL = "http://134.209.22.90:80/sockets";

    public ArrayList<Socket> getSocketArray() {
        return socketArray;
    }

    public boolean isSocketsIsReady() {
        return socketsIsReady;
    }

    public void setSocketArray(SocketResponse[] socketResponseArray) {

        socketArray.clear();
        for (SocketResponse socketResponse : socketResponseArray) {
            Socket tmpSocket = new Socket();
            tmpSocket.setFreeSocket(socketResponse.getFree_sockets());
            tmpSocket.setPosition(new Point(socketResponse.getLatitude(), socketResponse.getLongitude()));
            socketArray.add(tmpSocket);
        }
        socketsIsReady = true;
    }

    public void updateSocketArray(final double userLatitude, final double userLongitude)
    {
        socketsIsReady = false;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SERVER_URL).newBuilder();
        urlBuilder.addQueryParameter("latitude", Double.toString(userLatitude));
        urlBuilder.addQueryParameter("longitude", Double.toString(userLongitude));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
            {
                try {
                    if(response.isSuccessful()) {
                        String stringResponse = response.body().string();
                        Log.i(Helper.TAG, "Had server response: " + stringResponse);
                        SocketResponse[] socketResponseArray = new GsonBuilder().create().fromJson(stringResponse, SocketResponse[].class);
                        setSocketArray(socketResponseArray);
                    }
                    else // error on server
                    {
                        getServerDefaultValues(userLatitude, userLongitude);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call call, IOException e) {
                Log.i(Helper.TAG, "Fail with the server");
                getServerDefaultValues(userLatitude, userLongitude);

            }
        });
    }

    private void getServerDefaultValues(final double userLatitude, final double userLongitude)
    {
        Log.i(Helper.TAG, "Use default values");
        SocketResponse[] socketResponseArray = new SocketResponse[6];
        for (int i = 0; i < 6; i ++) {
            socketResponseArray[i] = new SocketResponse();
            socketResponseArray[i].setFree_sockets(i % 5);
            socketResponseArray[i].setLatitude(userLatitude + i * 0.01);
            socketResponseArray[i].setLongitude(userLongitude + i * 0.01);
        }
        setSocketArray(socketResponseArray);
    }
}
