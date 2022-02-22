package com.ecc;


/**
 * Acknowledgement interface to do manually acknowledge
 * !!! It should be used as parameter with
 * {@link com.ecc.annotation.Ack @Ack} annotation in the method annotated with
 * {@link com.ecc.annotation.MqListener @MqListener}
 *
 * @see com.ecc.annotation.Ack @Ack
 * @author Emin Cem Canoglu
 */

public interface Acknowledgement {
    void doAck();
}
