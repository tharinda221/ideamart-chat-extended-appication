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
package com.ideamart.sample.sms.receive;

import com.ideamart.sample.sms.operations.Operations;
import com.ideamart.sample.usermgt.UserDAO;
import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.sms.MoSmsListener;
import hms.kite.samples.api.sms.messages.MoSmsReq;

import java.net.MalformedURLException;
import java.sql.SQLException;

/**
 * This class is created for receive messages.
 */
public class Receiver implements MoSmsListener {
    @Override
    public void init() {

    }

    @Override
    public void onReceivedSms(MoSmsReq moSmsReq) {
        String message = moSmsReq.getMessage();
        UserDAO userDAO = new UserDAO();
        String [] messageParts = message.split(" ");
        Operations operations = new Operations();
        try {
            String operation = messageParts[1].toLowerCase();
            if(operation.equals("un")) {
                operations.register(messageParts[2], moSmsReq.getSourceAddress());
            } else if(operation.equals("chat")) {
                userDAO.updateCount(moSmsReq.getSourceAddress());
                String finalString = "";
                for (int i =3; i< messageParts.length; i++) {
                    finalString = finalString+messageParts[i]+" ";
                }
                operations.chat(messageParts[2], finalString, moSmsReq.getSourceAddress());
            } else if(operation.equals("find")) {
                operations.find(moSmsReq.getSourceAddress());
            } else {
                operations.sendErrorMessage(moSmsReq.getSourceAddress());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
