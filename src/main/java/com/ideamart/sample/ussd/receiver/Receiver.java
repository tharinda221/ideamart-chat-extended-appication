package com.ideamart.sample.ussd.receiver;

import com.ideamart.sample.common.Constants;
import com.ideamart.sample.registeredUserMgt.RegisteredUser;
import com.ideamart.sample.sms.send.ScheduledMessage;
import com.ideamart.sample.sms.send.SendMessage;
import com.ideamart.sample.subcription.Subscription;
import com.ideamart.sample.usermgt.User;
import com.ideamart.sample.usermgt.UserDAO;
import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.StatusCodes;
import hms.kite.samples.api.ussd.MoUssdListener;
import hms.kite.samples.api.ussd.UssdRequestSender;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdResp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class is created to receive USSD messages
 */
public class Receiver implements MoUssdListener {


    private MoUssdReq moUssdReq;

    public MoUssdReq getMoUssdReq() {
        return moUssdReq;
    }

    public void setMoUssdReq(MoUssdReq moUssdReq) {
        this.moUssdReq = moUssdReq;
    }

    private UssdRequestSender ussdMtSender;

    public static String detailsSendToUser(String address, int a1, int a2) {
        try {
            UserDAO userDAO = new UserDAO();
            String range = String.valueOf(a1) + "<=age and age<=" + String.valueOf(a2);
            String sex = userDAO.getSearchTableSex(address);
            userDAO.updateSearchTableAge(address, range);
            ArrayList<RegisteredUser> list;
            list = userDAO.getUsersByAgeRange(range, sex);
            String finalMessage = "";
            RegisteredUser registeredUser;
            for (int i = 0; i < list.size(); i++) {
                registeredUser = list.get(i);
                finalMessage = finalMessage + String.valueOf(i + 1) + "." + registeredUser.getName() + " Wayasa " + registeredUser.getAge();
            }
            return finalMessage;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "null";
    }

    @Override
    public void init() {
        try {
            ussdMtSender = new UssdRequestSender(new URL(Constants.ApplicationConstants.USSD_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedUssd(MoUssdReq moUssdReq) {
        UserDAO userDAO = new UserDAO();

        if (Constants.ApplicationConstants.USSD_OP_MO_INIT.equals(moUssdReq.getUssdOperation())) {

            try {
                if (!userDAO.userAvailability(moUssdReq.getSourceAddress())) {
                    User user = new User(moUssdReq.getSourceAddress(), null, "1", moUssdReq.getMessage(), 1, 2);
                    userDAO.AddUser(user);
                } else {
                    userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "1");
                    userDAO.updateUserMessage(moUssdReq.getSourceAddress(), "dummy");
                    userDAO.updateCount(moUssdReq.getSourceAddress());
                }
                Subscription subscription = new Subscription();
                if (subscription.getStatus(moUssdReq.getSourceAddress())) {
                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.WELCOME_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                    sendRequest(request);
                    userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                } else {
                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.REGISTER_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                    sendRequest(request);
                }

            } catch (SdpException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String message = moUssdReq.getMessage();
                String flow = userDAO.getFlow(moUssdReq.getSourceAddress());
                if (flow.equals("1")) {
                    if ((message.equals("1") || userDAO.getMessage(moUssdReq.getSourceAddress()).equals("Registering"))) {

                        String userMessage = Constants.MessageConstants.REG_MSG;
                        if (!userDAO.getMessage(moUssdReq.getSourceAddress()).equals("Registering")) {
                            userDAO.updateUserMessage(moUssdReq.getSourceAddress(), "Registering");
                            userDAO.updateUserFlowStageNumberByInput(moUssdReq.getSourceAddress(), 0);
                            Subscription subscription = new Subscription();
                            subscription.subscribeUser(moUssdReq.getSourceAddress());
                            userDAO.RegisterUser(moUssdReq.getSourceAddress());
                            userMessage = userMessage + Constants.MessageConstants.REG_MSG_NAME;
                            MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                            sendRequest(request);
                            userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                            setMoUssdReq(moUssdReq);
                            new java.util.Timer().schedule(
                                    new java.util.TimerTask() {

                                        private MoUssdReq moUssdReq;

                                        @Override
                                        public void run() {
                                            // your code here
                                            System.out.println("Send welcome sms");
                                            moUssdReq = getMoUssdReq();
                                            SendMessage sendMessage = new SendMessage();
                                            sendMessage.SendMessage(Constants.MessageConstants.HELP_MENU, moUssdReq.getApplicationId(),
                                                    moUssdReq.getSourceAddress(), Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
                                        }
                                    },
                                    15000
                            );

                        } else {
                            int stage = userDAO.getUserFlowStageNumber(moUssdReq.getSourceAddress());
                            if (stage == 1) {
                                //UpdateUserName
                                userDAO.updateUserName(moUssdReq.getSourceAddress(), message);
                                userMessage = userMessage + Constants.MessageConstants.REG_MSG_SEX;
                                MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                sendRequest(request);
                                userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());

                            } else if (stage == 2) {
                                if (message.equals("1")) {
                                    userDAO.updateUserSex(moUssdReq.getSourceAddress(), "male");
                                    userMessage = userMessage + Constants.MessageConstants.REG_MSG_BIRTHDATE;
                                    MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else if (message.equals("2")) {
                                    userDAO.updateUserSex(moUssdReq.getSourceAddress(), "female");
                                    userMessage = userMessage + Constants.MessageConstants.REG_MSG_BIRTHDATE;
                                    MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else {
                                    userMessage = Constants.MessageConstants.REG_MSG_SEX_ERROR + Constants.MessageConstants.REG_MSG_SEX;
                                    MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);

                                }
                            } else if (stage == 3) {
                                userDAO.updateUserBirthDate(moUssdReq.getSourceAddress(), message);
                                userMessage = userMessage + Constants.MessageConstants.REG_MSG_AGE;
                                MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                sendRequest(request);
                                userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                            } else if (stage == 4) {
                                try {
                                    userDAO.updateUserAge(moUssdReq.getSourceAddress(), message);
                                    userMessage = userMessage + Constants.MessageConstants.REG_MSG_USERNAME;
                                    MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } catch (Exception e) {
                                    userMessage = Constants.MessageConstants.REG_MSG_AGE + Constants.MessageConstants.AGE_ERROR;
                                    MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumberByInput(moUssdReq.getSourceAddress(), 4);
                                }
                            } else if (stage == 5) {
                                try {
                                    userDAO.updateUserUseName(moUssdReq.getSourceAddress(), message);
                                    userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                                    userDAO.updateUserMessage(moUssdReq.getSourceAddress(), "RegisteringFinished");
                                    userMessage = userMessage + Constants.MessageConstants.REG_MSG_FINISHED;
                                    MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);


                                } catch (Exception e) {
                                    userMessage = Constants.MessageConstants.REG_MSG_USERNAME_ERROR +
                                            Constants.MessageConstants.REG_MSG_USERNAME;
                                    MtUssdReq request = createRequest(moUssdReq, userMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                }
                            } else {
                                userDAO.updateUserMessage(moUssdReq.getSourceAddress(), "RegisteringFinished");
                                MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.REG_MSG_FINISHED, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                sendRequest(request);
                                userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                            }
                        }

                    } else {
                        MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.EXIT_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_FIN);
                        sendRequest(request);
                    }
                } else if (flow.equals("2")) {
                    if (message.equals("1") || userDAO.getMessage(moUssdReq.getSourceAddress()).equals("Searching")) {
                        if (!userDAO.getMessage(moUssdReq.getSourceAddress()).equals("Searching")) {
                            userDAO.AddSearchTable(moUssdReq.getSourceAddress());
                            userDAO.updateUserMessage(moUssdReq.getSourceAddress(), "Searching");
                            userDAO.updateUserFlowStageNumberByInput(moUssdReq.getSourceAddress(), 1);
                            MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.SEARCH_MSG_SEX, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                            sendRequest(request);
                        } else {
                            int stage = userDAO.getUserFlowStageNumber(moUssdReq.getSourceAddress());
                            if (stage == 1) {
                                if (message.equals("1")) {
                                    userDAO.updateSearchTableSex(moUssdReq.getSourceAddress(), "sex = male");
                                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.SEARCH_MSG_AGE_LIST, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else if (message.equals("2")) {
                                    userDAO.updateSearchTableSex(moUssdReq.getSourceAddress(), "sex = female");
                                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.SEARCH_MSG_AGE_LIST, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else {

                                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.SEARCH_MSG_SEX_ERROR, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                }
                            } else if (stage == 2) {
                                if (message.equals("1")) {
                                    String finalMessage = detailsSendToUser(moUssdReq.getSourceAddress(), 18, 25);
                                    MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else if (message.equals("2")) {
                                    String finalMessage = detailsSendToUser(moUssdReq.getSourceAddress(), 25, 30);
                                    MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else if (message.equals("3")) {
                                    String finalMessage = detailsSendToUser(moUssdReq.getSourceAddress(), 30, 35);
                                    MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else if (message.equals("4")) {
                                    String finalMessage = detailsSendToUser(moUssdReq.getSourceAddress(), 35, 40);
                                    MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else if (message.equals("5")) {
                                    String finalMessage = detailsSendToUser(moUssdReq.getSourceAddress(), 40, 45);
                                    MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else if (message.equals("6")) {
                                    String finalMessage = detailsSendToUser(moUssdReq.getSourceAddress(), 45, 50);
                                    MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else if (message.equals("7")) {
                                    String finalMessage = detailsSendToUser(moUssdReq.getSourceAddress(), 50, 100);
                                    MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else {
                                    String finalMessage = Constants.MessageConstants.SEARCH_MSG_AGE_LIST_ERROR + "\n"
                                            + Constants.MessageConstants.SEARCH_MSG_AGE_LIST;
                                    MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                }
                            } else if (stage == 3) {
                                String sex = userDAO.getSearchTableSex(moUssdReq.getSourceAddress());
                                String range = userDAO.getSearchTableAge(moUssdReq.getSourceAddress());
                                ArrayList<RegisteredUser> list;
                                list = userDAO.getUsersByAgeRange(range, sex);
                                int index = Integer.valueOf(message);
                                RegisteredUser registeredUser = list.get(index - 1);
                                String finalMessage = registeredUser.getSex() + "\n" + "Name:-" + registeredUser.getName() + "\n" +
                                        "                                     Wayasa:-" + registeredUser.getAge() + "\n" +
                                        "                                     Upan Dinaya:-" + registeredUser.getBirthdate();
                                SendMessage sendMessage = new SendMessage();
                                sendMessage.SendMessage(finalMessage, Constants.ApplicationConstants.APP_ID,
                                        moUssdReq.getSourceAddress(), Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
                                MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.SEARCH_MSG_RESULT, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                sendRequest(request);
                                userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                            } else if (stage == 4) {
                                if (message.equals("1")) {
                                    SendMessage sendMessage = new SendMessage();
                                    String userName = userDAO.getUserNameByAddress(moUssdReq.getSourceAddress());
                                    String finalMessage = userName + ":" + Constants.MessageConstants.CHAT_REQUEST_MSG_SMS;
                                    sendMessage.SendMessage(finalMessage, Constants.ApplicationConstants.APP_ID,
                                            moUssdReq.getSourceAddress(), Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
                                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.CHAT_REQUEST_MSG, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateUserFlowStageNumber(moUssdReq.getSourceAddress());
                                } else {
                                    userDAO.updateUserMessage(moUssdReq.getSourceAddress(), "SearchingFinished");
                                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.WELCOME_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                                }
                            } else if (stage == 5) {
                                if (message.equals("0")) {
                                    userDAO.updateUserMessage(moUssdReq.getSourceAddress(), "SearchingFinished");
                                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.WELCOME_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");

                                } else {
                                    userDAO.updateUserMessage(moUssdReq.getSourceAddress(), "SearchingFinished");
                                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.WELCOME_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                                    sendRequest(request);
                                    userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                                }
                            }
                        }

                    } else if (message.equals("2")) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.SendMessage(Constants.MessageConstants.HELP_MENU, moUssdReq.getApplicationId(),
                                moUssdReq.getSourceAddress(), Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
                        MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.REG_MSG_FINISHED, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                        sendRequest(request);
                        userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");

                    } else if (message.equals("3")) {
                        String userName = userDAO.getUserNameByAddress(moUssdReq.getSourceAddress());
                        String finalMessage = userName + "\n" + "0.back";
                        MtUssdReq request = createRequest(moUssdReq, finalMessage, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                        sendRequest(request);
                        userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                    } else if (message.equals("4")) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.SendMessage(Constants.MessageConstants.HELP_MENU, moUssdReq.getApplicationId(),
                                moUssdReq.getSourceAddress(), Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
                        MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.REG_MSG_FINISHED, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                        sendRequest(request);
                        userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                    } else if (message.equals("5")) {
                        MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.DETAILS_MENU, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                        sendRequest(request);
                        userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                    } else {
                        MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.WELCOME_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                        sendRequest(request);
                        userDAO.updateMessageFlow(moUssdReq.getSourceAddress(), "2");
                    }
                }

            } catch (SdpException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public MtUssdReq createRequest(MoUssdReq moUssdReq, String menuContent, String ussdOperation) {

        MtUssdReq request = new MtUssdReq();
        request.setApplicationId(moUssdReq.getApplicationId());
        request.setEncoding(moUssdReq.getEncoding());
        request.setMessage(menuContent);
        request.setPassword(Constants.ApplicationConstants.PASSWORD);
        request.setSessionId(moUssdReq.getSessionId());
        request.setUssdOperation(ussdOperation);
        request.setVersion(moUssdReq.getVersion());
        request.setDestinationAddress(moUssdReq.getSourceAddress());
        return request;
    }

    public MtUssdResp sendRequest(MtUssdReq request) throws SdpException {
        // sending request to service
        MtUssdResp response = null;
        try {
            System.out.println();
            response = ussdMtSender.sendUssdRequest(request);
        } catch (SdpException e) {
            throw e;
        }

        // response status
        String statusCode = response.getStatusCode();
        String statusDetails = response.getStatusDetail();
        //System.out.println(statusDetails);
        if (StatusCodes.SuccessK.equals(statusCode)) {
            System.out.println("Message sent succeeded");
        } else {
            System.out.println("Message sent failed");
        }
        return response;
    }
}
