package com.github.hynra.wortel.consumer;


import com.rabbitmq.client.QueueingConsumer;

public interface ConsumerListener {
    public void onMessageReceived(QueueingConsumer.Delivery delivery);
}
