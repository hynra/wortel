package com.github.hynra.wortel.models;

import com.github.hynra.wortel.BrokerCallback;

/**
 * Created by hynra on 5/30/17.
 */

public class FactoryModel {

    private String hostName;
    private String virtualHostName;
    private String username;
    private String password;
    private int port;
    private int requestTimeOut = 30 * 1000;
    private int requestHeartBeat = 30;

    public BrokerCallback getmCallback() {
        return mCallback;
    }

    public void setmCallback(BrokerCallback mCallback) {
        this.mCallback = mCallback;
    }

    private BrokerCallback mCallback;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getVirtualHostName() {
        return virtualHostName;
    }

    public void setVirtualHostName(String virtualHostName) {
        this.virtualHostName = virtualHostName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRequestTimeOut() {
        return requestTimeOut;
    }

    public void setRequestTimeOut(int requestTimeOut) {
        this.requestTimeOut = requestTimeOut;
    }

    public int getRequestHeartBeat() {
        return requestHeartBeat;
    }

    public void setRequestHeartBeat(int requestHeartBeat) {
        this.requestHeartBeat = requestHeartBeat;
    }


}
