package com.ideamart.sample.sms.send;

import com.ideamart.sample.common.Constants;
import hms.kite.samples.api.ussd.messages.MoUssdReq;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by tharinda on 10/26/16.
 */
public class ScheduledMessage {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private String message;
    private MoUssdReq moUssdReq;
    private int time;
    public void SendScheduledMessage(final String msg, final MoUssdReq Req, int t) {
        message = msg;
        moUssdReq = Req;
        time = t;
        System.out.println("Starting one-second countdown now...");
        ScheduledFuture<?> countdown = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                SendMessage sendMessage = new SendMessage();
                sendMessage.SendMessage(message, moUssdReq.getApplicationId(), moUssdReq.getSourceAddress()
                        , Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
                System.out.println("Out of time!");
            }}, time, TimeUnit.SECONDS);

        while (!countdown.isDone()) {
            try {
                Thread.sleep(1000);
                System.out.println("do other stuff here");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        scheduler.shutdown();
    }
}
