package common.pojos;

import java.io.Serializable;

public class Message implements Serializable, Conventions {

    private static final long serialVersionUID = 1L;
    private int code;
    private Object content;

    public Message() {
    }

    public Message(int code) {
        this();
        setCode(code);

    }

    public Message(int code, Object obj) {
        setCode(code);
        setContent(obj);
    }

    public int getCode() {
        return code;
    }

    void setCode(int messageCode) {
        this.code = messageCode;

    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object messageContent) {
        this.content = messageContent;
    }


    /*	
	
     public static void main(String[] args) {
		
     String s = "<conv_id>231432432erg  3t4t 44 t34t tw t4 tfj hejh 53412324134231</conv_id>";
     String res = Message.extract(0, "conv_id", s);
		
     System.out.println("Extracted value = "+res);
     System.out.println("Still have "+s);
     }
	
	
	
     */
    /**
     * Extracts the requested property from the given string The type of the
     * property is either field, record or sth else and it defines how the
     * property can be extracted
     *
     * @param typeOfProperty
     * @param property
     * @param string
     * @return
     */
    public static String extract(int typeOfProperty, String property, String string) {
        // TODO Auto-generated method stub

        // Match regexx "<property> ... </property>

        String extracted = "";


        // Extract the text between the two title elements
        String pattern = "(?i)(<" + property + ".*?>)(.+?)(</" + property + ">)";
        String updated = string.replaceAll(pattern, "$2");
        //string = string.substring(extracted.length(), string.length());

        System.out.println(string);
        System.out.println(pattern);
        System.out.println(updated);

        return updated;
    }
    //m.append("user_id", R.getU().getId());

    public String getDescription() {
        String s = null;
        switch (code) {

            case LOGIN_REQUEST:
                s = "LOGIN_REQUEST";
                break;
            case LOGIN_RESPONSE:
                s = "LOGIN_RESPONSE";
                break;
            case BAD_PASSWORD:
                s = "BAD_PASSWORD";
                break;
            case BAD_USERNAME:
                s = "BAD_USERNAME";
                break;
            case SIGNUP_REQUEST:
                s = "SIGNUP_REQUEST";
                break;
            case SIGNUP_RESPONSE:
                s = "SIGNUP_RESPONSE";
                break;
            case USERNAME_UNAVAILABLE:
                s = "USERNAME_UNAVAILABLE";
                break;
            case SIGNUP_SUCCESS:
                s = "SIGNUP_SUCCESS";
                break;
            case GET_CONTACTS_REQUEST:
                s = "GET_CONTACTS_REQUEST";
                break;
            case GET_CONTACTS_RESPONSE:
                s = "GET_CONTACTS_RESPONSE";
                break;
            case SEARCH_FOR_USER_REQUEST:
                s = "SEARCH_FOR_USER_REQUEST";
                break;
            case SEARCH_FOR_USER_RESPONSE:
                s = "SEARCH_FOR_USER_RESPONSE";
                break;
            case ADD_CONTACT_REQUEST:
                s = "ADD_CONTACT_REQUEST";
                break;
            case ADD_CONTACT_REQ_ACK:
                s = "ADD_CONTACT_REQ_ACK";
                break;
            case ACR_DELIVERY:
                s = "ACR_DELIVERY";
                break;
            case ACR_DELIVERY_ACK:
                s = "ACR_DELIVERY_ACK";
                break;
            case ACR_DECISION:
                s = "ACR_DECISION";
                break;
            case ACR_DECISION_ACK:
                s = "ACR_DECISION_ACK ";
                break;
            case ACR_DECISION_DELIVERY:
                s = "ACR_DECISION_DELIVERY";
                break;
            case ACR_DECISION_DELIVERY_ACK:
                s = "ACR_DECISION_DELIVERY_ACK";
                break;
            case CONVERSATION_ID_REQUEST:
                s = "CONVERSATION_ID_REQUEST";
                break;
            case CONVERSATION_ID_RESPONSE:
                s = "CONVERSATION_ID_RESPONSE";
                break;
            case CHAT_MSG_SEND_REQ:
                s = "CHAT_MSG_SEND_REQ";
                break;
            case CHAT_MSG_SEND_ACK:
                s = "CHAT_MSG_SEND_ACK";
                break;
            case CHAT_MSG_DELIVERY:
                s = "CHAT_MSG_DELIVERY";
                break;
            case CHAT_MSG_DELIVERY_ACK:
                s = "CHAT_MSG_DELIVERY_ACK";
                break;
            case CHAT_MSG_DELIVERY_REPORT:
                s = "CHAT_MSG_DELIVERY_REPORT";
                break;
            case CHAT_MSG_DELIVERY_REPORT_ACK:
                s = "CHAT_MSG_DELIVERY_REPORT_ACK";
                break;
            case FILE_SEND_REQ:
                s = "FILE_SEND_REQ";
                break;
            case FILE_SEND_ACK:
                s = "FILE_SEND_ACK";
                break;
            case FILE_SEND_REJECTION:
                s = "FILE_SEND_REJECTION";
                break;
            case FILE_DELIVERY:
                s = "FILE_DELIVERY";
                break;
            case FILE_DELIVERY_ACK:
                s = "FILE_DELIVERY_ACK";
                break;
            case FILE_DELIVERY_REPORT:
                s = "FILE_DELIVERY_REPORT";
                break;
            case FILE_DELIVERY_REPORT_ACK:
                s = "FILE_DELIVERY_REPORT_ACK";
                break;
            case FILE_DOWNLOAD_COMPLETED:
                s = "FILE_DOWNLOAD_COMPLETED";
                break;
            case FILE_START_DOWNLOAD_OK:
                s = "FILE_START_DOWNLOAD_OK";
                break;
            case FILE_START_UPLOAD_OK:
                s = "START_FILE_UPLOAD_OK";
                break;
            case FILE_UPLOAD_COMPLETED:
                s = "FILE_UPLOAD_COMPLETED";
                break;
            case USER_STATUS_UPDATE:
                s = "USER_STATUS_UPDATE";
                break;
            case CONTACT_STATUS_UPDATE:
                s = "CONTACT_STATUS_UPDATE";
                break;
            case CONTACT_DELETION:
                s = "CONTACT_DELETION";
                break;
            case CONTACT_DELETED_ACK:
                s = "CONTACT_DELETED_ACK";
                break;
            case CONTACT_ADDITION:
                s = "CONTACT_ADDITION";
                break;
            case CONTACT_ADDED_ACK:
                s = "CONTACT_ADDED_ACK";
                break;
            case MISSED_ACRS_REQUEST:
                s = "MISSED_ACRS_REQUEST";
                break;
            case MISSED_ACR_RESPONSE:
                s = "MISSED_ACR_RESPONSE";
                break;
            case MISSED_ACR_REPORTS_REQUEST:
                s = "MISSED_ACR_REPORTS_REQUEST";
                break;
            case MISSED_ACR_DECISION_DELIVERY:
                s = "MISSED_ACR_DECISION_DELIVERY";
                break;
            case MISSED_CALLS_REQUEST:
                s = "MISSED_CALLS_REQUEST";
                break;
            case MISSED_CALLS_RESPONSE:
                s = "MISSED_CALLS_RESPONSE";
                break;
            case MISSED_CHAT_REQUEST:
                s = "MISSED_CHAT_REQUEST";
                break;
            case MISSED_CHAT_RESPONSE:
                s = "MISSED_CHAT_RESPONSE";
                break;
            case MISSED_FILE_REQUEST:
                s = "MISSED_FILE_REQUEST";
                break;
            case MISSED_FILE_RESPONSE:
                s = "MISSED_FILE_RESPONSE";
                break;
            case LOGOUT_REQUEST:
                s = "LOGOUT_REQUEST";
                break;
            case LOGOUT_RESPONSE:
                s = "LOGOUT_RESPONSE";
                break;
            case COMMUNICATION_TERMINATION_REQUEST:
                s = "COMMUNICATION_TERMINATION_REQUEST";
                break;
            case COMMUNICATION_TERMINATION_RESPONSE:
                s = "COMMUNICATION_TERMINATION_RESPONSE";
                break;
        }
        return s;
    }
}
