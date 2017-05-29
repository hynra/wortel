package com.github.hynra.wortel;

/**
 * Created by hynra on 5/29/17.
 */

public interface BrokerCallback {
    void onConnectionFailure(String message);

    void onDisconnected();

    void onConnectionClosed(String message);
}
