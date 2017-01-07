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
package com.ideamart.sample.sms.operations;

import com.ideamart.sample.sms.send.SendMessage;
import com.ideamart.sample.usermgt.User;
import com.ideamart.sample.usermgt.UserDAO;
import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.sms.messages.MoSmsReq;

import java.sql.SQLException;

import com.ideamart.sample.common.Constants;

/**
 * This class is created for do operations for messages.
 */
public class Operations {

//    public void passToDatabase(MoSmsReq moSmsReq) throws SQLException, ClassNotFoundException, SdpException {
//        UserDAO userDAO = new UserDAO();
//        User user = new User(moSmsReq.getSourceAddress(), null, "1", moSmsReq.getMessage(), "no");
//        userDAO.AddUser(user);
//        System.out.println(userDAO.userAvailability(moSmsReq.getSourceAddress()));
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.SendMessage("Your Message Received",moSmsReq.getApplicationId(),moSmsReq.getSourceAddress()
//                , Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
//    }

    public void chat(String name, String message, String userAddress) throws ClassNotFoundException, SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateCount(userAddress);
        String userName = userDAO.getUserNameByAddress(userAddress);
        if (userName.equals("null")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.SendMessage(Constants.MessageConstants.HELP_SMS, Constants.ApplicationConstants.APP_ID,
                    userAddress, Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
            return;
        }
        String address = userDAO.getUserAddressByName(name);
        SendMessage sendMessage = new SendMessage();
        if(address == null) {
            sendMessage.SendMessage("The name you entered was wrong. Try again", Constants.ApplicationConstants.APP_ID,
                    userAddress, Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
        } else {
            String finalMessage = userName + ":" + message;
            sendMessage.SendMessage(finalMessage, Constants.ApplicationConstants.APP_ID,
                    address, Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
        }
    }

    public void register(String name, String address) throws ClassNotFoundException, SQLException {
        UserDAO userDAO = new UserDAO();
        if(!userDAO.userAvailability(address)) {
            User user = new User(address, null, "1", "sms", 1, 0);
            userDAO.AddUser(user);
        }
        SendMessage sendMessage = new SendMessage();
        if(userDAO.RegisterUserName(address, name)) {
            String finalMesage = "You have successfully registered.\n Your use name is " + name;
            sendMessage.SendMessage(finalMesage, Constants.ApplicationConstants.APP_ID,
                    address, Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
        } else {
            sendMessage.SendMessage("The name you entered is already exist. Please enter another one.", Constants.ApplicationConstants.APP_ID,
                    address, Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
        }
    }

    public void find(String address) throws ClassNotFoundException, SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateCount(address);
        String name = userDAO.getUserNameByAddress(address);
        if(name.equals("null")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.SendMessage(Constants.MessageConstants.HELP_SMS, Constants.ApplicationConstants.APP_ID,
                    address, Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
            return;
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.SendMessage(name +":wisin echat magein chat kirimata illum kara atha.", Constants.ApplicationConstants.APP_ID,
                    "tel:all", Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
            return;
        }
    }

    public void sendErrorMessage(String address) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.SendMessage("Oba kala yomu kireema waradiya", Constants.ApplicationConstants.APP_ID,
                address, Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
    }
}
