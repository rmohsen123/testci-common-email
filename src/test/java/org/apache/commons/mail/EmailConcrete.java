package org.apache.commons.mail;

import java.util.Map;

import javax.mail.Session;

public class EmailConcrete extends MultiPartEmail {

    @Override
    public Email setMsg(String msg) throws EmailException {
        return super.setMsg(msg); // Properly forward to parent implementation
    }

    /**
     * @return headers
     */
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getContentType() {
        return this.contentType;
    }

    @Override
public String getHostName() {
    String hostName = super.getHostName();
    try {
        if (hostName == null && getMailSession() != null) {
            return getMailSession().getProperty("mail.host");
        }
    } catch (EmailException e) {
        e.printStackTrace(); // or log it
    }
    return hostName;
}


    @Override
    public Session getMailSession() throws EmailException {
        return super.getMailSession(); // Allow tests to access real session behavior
    }
}

