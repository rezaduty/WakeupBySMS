package com.rezaduty.mmmm;

/**
 * Created by client on 8/27/17.
 */


public interface SmsListener {

    public void messageReceived(String messageText,String messageAdress);

}