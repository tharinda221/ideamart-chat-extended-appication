/*
 *Copyright 2015 Tharinda Dilshan Ehelepola
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ideamart.sample.sms.send;

import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.sms.SmsRequestSender;
import hms.kite.samples.api.sms.messages.MtSmsReq;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * This class is created for send messages.
 */
public class SendMessage {

    private SmsRequestSender requestSender;

    public void SendMessage(String message, String ApplicationID, String address, String password, String url) {

        MtSmsReq mtSmsReq = new MtSmsReq();
        mtSmsReq.setMessage(message);
        mtSmsReq.setApplicationId(ApplicationID);
        mtSmsReq.setPassword(password);
        mtSmsReq.setDestinationAddresses(Arrays.asList(address));

        try {
            requestSender = new SmsRequestSender(new URL(url));
            requestSender.sendSmsRequest(mtSmsReq);
        }
        catch (SdpException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
