package ru.nsu.smartsocket1;

import com.yandex.mapkit.geometry.Point;

class Socket {
    private Point position;
    private int freeSocket = 4;

    int getFreeSocket() {
        return freeSocket;
    }

    Point getPosition() {
        return position;
    }

    void setFreeSocket(int freeSocket) {
        this.freeSocket = freeSocket;
    }

    void setPosition(Point position) {
        this.position = position;
    }
}
