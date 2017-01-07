package com.ideamart.sample.common;

/**
 * Created by Ehelepola on 20/01/2016.
 */
public final class Constants {

    public static final class ApplicationConstants {
        public static final String USSD_OP_MO_INIT = "mo-init";
        public static final String USSD_OP_MT_CONT = "mt-cont";
        public static final String USSD_OP_MT_FIN = "mt-fin";

        //local app apis
        public static final String USSD_URL = "http://127.0.0.1:7000/ussd/send";
        public static final String SMS_URL = "http://127.0.0.1:7000/sms/send";
        public static final String SUBSCRIPTION_MESSAGE_URL = "http://127.0.0.1:7000/subscription/send";
        public static final String SUBSCRIPTION_BASESIZE_URL = "http://127.0.0.1:7000/subscription/query-base";
        public static final String SUBSCRIPTION_STATUS_URL = "http://127.0.0.1:7000/subscription/getStatus";

        //IdeaMart APIs public
//        public static final String USSD_URL = "https://api.dialog.lk/ussd/send";
//        public static final String SMS_URL = "https://api.dialog.lk/sms/send";
//        public static final String SUBSCRIPTION_MESSAGE_URL = "https://api.dialog.lk/subscription/send";
//        public static final String SUBSCRIPTION_BASESIZE_URL = "https://api.dialog.lk/subscription/query-base";
//        public static final String SUBSCRIPTION_STATUS_URL = "https://api.dialog.lk/subscription/getStatus";

        // For registration action should be 1 (0 - Opt Out | 1 - Opt In)
        public static final String REG_ACTION = "1";

        //Specific Version
        public static final String VERSION = "1.0";

        //local app credentials
        public static final String PASSWORD = "password";
        public static final String APP_ID = "APP_00001";

        //hosted app credentials
//        public static final String PASSWORD = "";
//        public static final String APP_ID = "";

        //local database
        public static final String JDBC_URL = "jdbc:mysql://localhost:3306/mangalam";
        public static final String DATABASE_USERNAME = "root";
        public static final String DATABASE_PASSWORD = "";

        //hosted database credentials
//        public static final String JDBC_URL = "jdbc:mysql://localhost:3306/{dbname}";
//        public static final String DATABASE_USERNAME = "";
//        public static final String DATABASE_PASSWORD = "";

        public static final String DATABASE_TRAFFIC_TABLE_NAME = "mangalam";
        public static final String DATABASE_USER_TABLE_NAME = "mangalam_users";

        public static final String DATABASE_DASHBOARD_TABLE_NAME = "mangalam_dashboard";


    }

    public static final class MessageConstants {
        public static final String WELCOME_MESSAGE = "1.liyapadinchi wenna\n2. Obata Galapena Kenaa Dan Soyaganna" +
                "\n3.Yaluwek Samaga Chat Kirimata.\n4.Obe Username Eka Nawatha Labaganimata.\n5. Udaw\n" +
                "6.Apa Gana Thathu\n99.Exit";
        public static final String HELP_MENU = "Welcome to (App Name) USSD Application\n" +
                "      Chat Kirimata Upades.\n" +
                " \n" +
                "                   (Appname) Ha Ekathuwu Obata Isthuthi. \n" +
                "                      \n" +
                "                    (UssdCode) Dial Kara Anaka 2 Oba, Galapene Kenek Soyaganna\n" +
                "\n" +
                "                    Obe Yahaluwanwath (UssdCode) Dial Kara Oba Samaga Chat Kirimata Pulwan.  \n" +
                " \n" +
                "                     ***Chat Kirimata Upades***\n" +
                " \n" +
                "                    Keyword <histhanak> Yaluwage Username <histhanak> Oyage Message Eka Sadahan kara (Number)ta Yawanna. \n" +
                " \n" +
                "                   Udaharana:-Keyword Nimesha kohomada oyata? Sent Number. ";
        public static final String REG_MSG = "\"Welcome to (App Name) USSD Application  \n" +
                "     \n" +
                "        Obata Galapenama Kenaa Dan Lesiyenma Soyaganna Puluwan\n\"";
        public static final String REG_MSG_NAME = "Obe Name Sadahan Karanna.\n";
        public static final String REG_MSG_SEX = "Oba,\n" +
                "                Male(Purushayek) Nam Anka 1 \n" +
                "                Female(Isthri) Anka 2\n" +
                "               Adala ankaya Thoranna.";
        public static final String REG_MSG_SEX_ERROR = "Please choose 1 or 2";
        public static final String REG_MSG_BIRTHDATE = "Obe Upan Dinaya Sadahan Karanna.\n" +
                "               (Udaharana:1900.01.01)";
        public static final String REG_MSG_AGE = "Obe Danata Wayasa Sadahan Karanna.\n";
        public static final String REG_MSG_USERNAME = "Oba Kamathi Usename ekak Sadanna\n" +
                "               (Udaharana:Nim,Madu,nuwan).";
        public static final String REG_MSG_USERNAME_ERROR = "UserName exist. Choose another username\n";
        public static final String DETAILS_MENU = "Sp Name:{} \nDeveloper: tharinda221@gmail.com\n" +
                "0. Back\n99. Exit";
        public static final String REG_MSG_FINISHED = "Keti paniwidayak magin labena upades pilipadinna.\n0.Main menu";
        public static final String SEARCH_MSG_SEX = "Oba Soyanne\n1.Male(Purusha) Nam\n2.Female(Isthri) Nam";
        public static final String EXIT_MESSAGE = "Mema Sewawa Bavitha Kara Obata Isthuthu.Nawathath Paminenna (APP Name) Wetha";

    }
}
