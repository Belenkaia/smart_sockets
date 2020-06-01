package ru.nsu.smartsocket1;

public class SocketResponse {
    private double latitude;
    private double longitude;
    private int free_sockets;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getFree_sockets() {
        return free_sockets;
    }

    public void setFree_sockets(int free_sockets) {
        this.free_sockets = free_sockets;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
