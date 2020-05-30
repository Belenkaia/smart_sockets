package ru.nsu.smartsocket1;

import com.yandex.mapkit.geometry.Point;

public class Socket {
    private Point position;
    private int freeSocket = 4;

    public int getFreeSocket() {
        return freeSocket;
    }

    public Point getPosition() {
        return position;
    }

    public void setFreeSocket(int freeSocket) {
        this.freeSocket = freeSocket;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
