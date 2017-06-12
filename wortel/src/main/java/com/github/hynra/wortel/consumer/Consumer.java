package com.github.hynra.wortel.consumer;

import android.os.Handler;
import android.util.Log;

import com.github.hynra.wortel.BrokerCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private Connection mConnection;
    private ConsumerListener mqConsumerListener;
    private Thread subscribeThread;
    private Handler mCallbackHandler = new Handler();
    private String TAG = this.getClass().getSimpleName();
    private boolean isRunning;
    private boolean queuing;
    private Channel mChannel;
    private QueueingConsumer mQueue;
    private BrokerCallback mCallback;

    public Consumer(Connection mConnection, BrokerCallback mCallback){
        this.mConnection = mConnection;
        this.mCallback = mCallback;
    }


    public void setMessageListener(ConsumerListener listner){
        mqConsumerListener = listner;
    }

    public void stop(){
        isRunning = false;
        queuing = false;
        if(isChannelAvailable()){
            try {
                mChannel.close();
                mChannel.abort();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }
    }

    public void consume(String queueName){
        isRunning = true;
        queuing = true;
        Log.d(TAG, "subscribe");
        subscribeThread = new Thread(() -> {
            Log.d(TAG, "subscribe -> run");
            while(isRunning) {
                try {
                    Log.d(TAG, "subscribe -> run -> subscribeRunning");
                    initchanenel();
                    String mQueueName = declareQueue(queueName);
                //    mChannel.queueBind(mQueueName, mExchange, mRoutingKey);
                    mQueue = new QueueingConsumer(mChannel);
                    mChannel.basicConsume(mQueueName, mQueue);
                    while(queuing){
                        Log.d(TAG, "subscribe -> run -> subscribeRunning -> queuing");
                        final QueueingConsumer.Delivery delivery;
                        delivery = mQueue.nextDelivery();
                        mChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        mCallbackHandler.post(() -> mqConsumerListener.onMessageReceived(delivery));
                    }
                } catch (InterruptedException | ConsumerCancelledException
                        | ShutdownSignalException | IOException e) {
                    sendBackErrorMessage(e);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        subscribeThread.start();
    }

    private void sendBackErrorMessage(Exception e) {
        final String errorMessage = e.getMessage() == null ? e.toString() : e.getMessage();
        mCallbackHandler.post(() -> mCallback.onConnectionFailure(errorMessage));
    }

    private String declareQueue(String queueName) throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x-expires", 2 * 60 * 60 * 1000);
        queueName = mChannel.queueDeclare(queueName, false, true, false, null).getQueue();
        Log.d(TAG, "Queue :" + "queue:name:" + queueName + " declared");

        return  queueName;
    }

    private void initchanenel(){
        if(!isChannelAvailable()){
            createChannel();
        }
    }


    private boolean isChannelAvailable() {
        if (mChannel != null && mChannel.isOpen()) {
            return true;
        }
        return false;
    }

    private void createChannel() {
        try {
            mChannel = this.mConnection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
