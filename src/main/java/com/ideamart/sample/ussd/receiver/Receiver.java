package com.ideamart.sample.ussd.receiver;

import com.ideamart.sample.common.Constants;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

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
                    User user = new User(moUssdReq.getSourceAddress(), null, "1", moUssdReq.getMessage(), 1, 0);
                    userDAO.AddUser(user);
                } else {
                    userDAO.updateCount(moUssdReq.getSourceAddress());
                }

                MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.WELCOME_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                sendRequest(request);
            } catch (SdpException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String message = moUssdReq.getMessage();
                if (message.equals("1")) {
                    Subscription subscription = new Subscription();
                    subscription.subscribeUser(moUssdReq.getSourceAddress());
                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.REGISTER_MENU, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                    sendRequest(request);
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
                                    sendMessage.SendMessage(Constants.MessageConstants.HELP_SMS, moUssdReq.getApplicationId(),
                                            moUssdReq.getSourceAddress(), Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
                                }
                            },
                            15000
                    );
                    return;
                } else if (message.equals("2")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.SendMessage(Constants.MessageConstants.HELP_SMS, moUssdReq.getApplicationId(),
                            moUssdReq.getSourceAddress(), Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.HELP_MENU, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                    sendRequest(request);
                    return;
                } else if (message.equals("3")) {
                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.DETAILS_MENU, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                    sendRequest(request);
                } else if (message.equals("0")) {
                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.WELCOME_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                    sendRequest(request);
                } else if (message.equals("99")) {
                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.EXIT_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_FIN);
                    sendRequest(request);
                } else {
                    MtUssdReq request = createRequest(moUssdReq, Constants.MessageConstants.WELCOME_MESSAGE, Constants.ApplicationConstants.USSD_OP_MT_CONT);
                    sendRequest(request);
                }

            } catch (SdpException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
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
