package ru.nsu.smartsocket1;

import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;

public class SocketManager {
    private ArrayList<Socket> socketArray = new ArrayList<Socket>();

    public ArrayList<Socket> getSocketArray() {
        return socketArray;
    }

    public void setSocketArray(ArrayList<Socket> socketArray) {
        this.socketArray = socketArray;
    }
    public void updateSocketArray(double userLatitude, double userLongitude)
    {
        //http-запрос на сервер
        for (int i = 0; i < socketArray.size() - 2; i ++)
        {
            socketArray.get(i).setFreeSocket(i);
            socketArray.get(i).setPosition(new Point(userLatitude + i * 0.001 + 0.003, userLongitude + i * 0.001));
        }
        for (int i = socketArray.size() - 2; i < socketArray.size(); i ++)
        {
            socketArray.get(i).setFreeSocket(i);
            socketArray.get(i).setPosition(new Point(userLatitude - i * 0.001, userLongitude - i * 0.001));
        }
    }
    public void initSocketArray(double userLatitude, double userLongitude)
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
    }
}
