package com.github.hynra.wortel;

import android.os.AsyncTask;

import com.github.hynra.wortel.models.FactoryModel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by hynra on 5/30/17.
 */

public class MQConnection  {

    private String hostName;
    private String virtualHostName;
    private String username;
    private String password;
    private int port;
    private int requestTimeOut = 30 * 1000;
    private int requestHeartBeat = 30;
    private BrokerCallback mCallback;
    private Connection mConnection;
    private Channel mChannel;


    /** Constructor **/

    public MQConnection(
            String hostName, String virtualHostName, String username, String password, int port,
            int requestTimeOut, int requestHeartBeat, BrokerCallback mCallback) {

        this.hostName = hostName;
        this.virtualHostName = virtualHostName;
        this.username = username;
        this.password = password;
        this.port = port;
        this.requestTimeOut = requestTimeOut;
        this.requestHeartBeat = requestHeartBeat;
        this.mCallback = mCallback;
    }

    public MQConnection(){

    }

    public MQConnection(FactoryModel factoryModel){
        this.hostName = factoryModel.getHostName();
        this.virtualHostName = factoryModel.getVirtualHostName();
        this.username = factoryModel.getUsername();
        this.password = factoryModel.getPassword();
        this.port = factoryModel.getPort();
        this.requestTimeOut = factoryModel.getRequestTimeOut();
        this.requestHeartBeat = factoryModel.getRequestHeartBeat();
        this.mCallback = factoryModel.getmCallback();
    }

    public void  initConnection() throws IOException, TimeoutException {
        new AsyncTask<Void,Void,Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids) {
                try{

                    ConnectionFactory connectionFactory = new ConnectionFactory();
                    connectionFactory.setUsername(username);
                    connectionFactory.setPassword(password);
                    connectionFactory.setVirtualHost(virtualHostName);
                    connectionFactory.setHost(hostName);
                    connectionFactory.setPort(port);
                    connectionFactory.setConnectionTimeout(requestTimeOut);
                    connectionFactory.setRequestedHeartbeat(requestHeartBeat);
                    mConnection = connectionFactory.newConnection();
                 //   mChannel = mConnection.createChannel();
                    mConnection.addShutdownListener(cause -> {
                        String errorMessage = cause.getMessage() == null ? "connection was shutdown" : "shutdown : " + cause.getMessage();
                        mCallback.onConnectionClosed(errorMessage);
                    });
                    return true;
                } catch (Exception e){
                    e.printStackTrace();
                    mCallback.onConnectionFailure(e.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean){
                    mCallback.onConnectionSuccess(mChannel);
                }
            }
        }.execute();
    }





    /** getter & setter **/


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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


    public BrokerCallback getBrokerCallback() {
        return mCallback;
    }

    public void setBrokerCallback(BrokerCallback mCallback) {
        this.mCallback = mCallback;
    }


}
