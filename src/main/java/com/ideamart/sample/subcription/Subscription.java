package com.ideamart.sample.subcription;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ideamart.sample.common.Constants;
import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.subscription.SubscriptionRequestSender;
import hms.kite.samples.api.subscription.messages.SubscriptionRequest;
import hms.kite.samples.api.subscription.messages.SubscriptionResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by tharinda on 10/21/16.
 */
public class Subscription {

    private String subscriptionStatus;

    public String subscribeUser(String address) throws MalformedURLException {
        SubscriptionRequestSender subscriptionRequestSender =
                new SubscriptionRequestSender(new URL(Constants.ApplicationConstants.SUBSCRIPTION_MESSAGE_URL));

        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setApplicationId(Constants.ApplicationConstants.APP_ID);
        subscriptionRequest.setPassword(Constants.ApplicationConstants.PASSWORD);
        subscriptionRequest.setSubscriberId(address);
        subscriptionRequest.setAction(Constants.ApplicationConstants.REG_ACTION);
        subscriptionRequest.setVersion(Constants.ApplicationConstants.VERSION);

        try {
            // Get SubscriptionResponse and assign Subscription Register Status to display
            SubscriptionResponse subscriptionResponse
                    = subscriptionRequestSender.sendSubscriptionRequest(subscriptionRequest);
            return subscriptionResponse.getStatusDetail();
        } catch (SdpException e) {
            System.out.println("Error Occurred due to" + e);
        }
        return null;
    }

    public boolean getStatus(String address) throws IOException {
        StatusBean statusBean = new StatusBean();
        statusBean.setSubscriberId(address);
        statusBean.setPassword(Constants.ApplicationConstants.PASSWORD);
        statusBean.setApplicationId(Constants.ApplicationConstants.APP_ID);
        String postUrl = Constants.ApplicationConstants.SUBSCRIPTION_STATUS_URL;
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(statusBean));//gson.tojson() converts your pojo to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.setHeader("Accept", "application/json");
        org.apache.http.HttpResponse response = httpClient.execute(post);
        System.out.println(response.getStatusLine());
        InputStream inputStream = response.getEntity().getContent();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        System.out.println("Subscription status:");
        System.out.println(result);
        JsonElement jelement = new JsonParser().parse(result);
        JsonObject jobject = jelement.getAsJsonObject();
        String subscriptionStatus = String.valueOf(jobject.get("subscriptionStatus")).replaceAll("['\"]", "");
        System.out.println(subscriptionStatus);
        return subscriptionStatus.equals("REGISTERED");
    }
}
