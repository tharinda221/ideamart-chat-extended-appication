package com.ideamart.sample.usermgt;

/**
 * Created by tharinda on 10/20/16.
 */
public class User {

    private String address;
    private String name;
    private String flow;
    private String message;
    private int subscription;
    private int status;

    public User(String address, String name, String flow, String message, int subcription, int status) {
        this.address = address;
        this.name = name;
        this.flow = flow;
        this.message = message;
        this.subscription = subcription;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSubscription() {
        return subscription;
    }

    public void setSubscription(int subscription) {
        this.subscription = subscription;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
