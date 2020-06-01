package ru.nsu.smartsocket1;

import com.google.gson.GsonBuilder;
import com.yandex.mapkit.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SocketManager {
    private boolean socketsIsReady = false;
    private ArrayList<Socket> socketArray = new ArrayList<Socket>();
    private final OkHttpClient client = new OkHttpClient();
    private final String SERVER_URL = "http://134.209.22.90:80/sockets";

    public ArrayList<Socket> getSocketArray() {
        return socketArray;
    }

   // private final Random random = new Random();
    public void setSocketArray(SocketResponse[] socketResponseArray) {

        socketArray.clear();
        System.out.println("json: ");
        for (SocketResponse socketResponse : socketResponseArray) {
            Socket tmpSocket = new Socket();
            System.out.print("| " + socketResponse.getLatitude() + ", " + socketResponse.getLongitude() + ", " + socketResponse.getFree_sockets() + " | ");
            tmpSocket.setFreeSocket(socketResponse.getFree_sockets());
            tmpSocket.setPosition(new Point(socketResponse.getLatitude(), socketResponse.getLongitude()));
            System.out.println(" sockets: ");
            System.out.print("{ " + tmpSocket.getFreeSocket() + ", " + tmpSocket.getPosition().getLatitude() + ", " + tmpSocket.getPosition().getLongitude() + " } ");
            socketArray.add(tmpSocket);
        }
        socketsIsReady = true;
    }

    public void updateSocketArray(double userLatitude, double userLongitude)
    {
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
                    String stringResponse = response.body().string();
                    System.out.println("String RESPONSE: " + stringResponse);
                    SocketResponse[] socketResponseArray = new GsonBuilder().create().fromJson(stringResponse, SocketResponse[].class);
                    setSocketArray(socketResponseArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call call, IOException e) {
                System.out.println("fail with the server");
            }
        });

        /*for (int i = 0; i < socketArray.size() - 2; i ++)
        {
            socketArray.get(i).setFreeSocket(random.nextInt(5));
            socketArray.get(i).setPosition(new Point(userLatitude + i * 0.001 + 0.003, userLongitude + i * 0.001));
        }
        for (int i = socketArray.size() - 2; i < socketArray.size(); i ++)
        {
            socketArray.get(i).setFreeSocket(random.nextInt(5));
            socketArray.get(i).setPosition(new Point(userLatitude - i * 0.001, userLongitude - i * 0.001));
        }*/
    }

   /* public void initSocketArray(double userLatitude, double userLongitude)
    {
        // http to server
        for( int i = 0 ; i < 3; i ++)
        {
            Socket tmpSocket = new Socket();
            tmpSocket.setPosition(new Point(userLatitude + i * 0.001 + 0.003, userLongitude + i * 0.001));
            socketArray.add(tmpSocket);
        }
        for( int i = 3 ; i < 5; i ++)
        {
            Socket tmpSocket = new Socket();
            tmpSocket.setPosition(new Point(userLatitude - i * 0.001, userLongitude - i * 0.001));
            socketArray.add(tmpSocket);
        }
    }*/
}
