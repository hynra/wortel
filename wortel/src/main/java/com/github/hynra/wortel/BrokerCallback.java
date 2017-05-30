package com.github.hynra.wortel;

import com.rabbitmq.client.Channel;

/**
 * Created by hynra on 5/29/17.
 */

public interface BrokerCallback {

    void onConnectionSuccess(Channel channel);

    void onConnectionFailure(String message);

    void onDisconnected();

    void onConnectionClosed(String message);
}
