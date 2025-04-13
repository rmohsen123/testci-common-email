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
        if (hostName == null && getMailSession() != null) {
            return getMailSession().getProperty("mail.host");
        }
        return hostName;
    }

    @Override
    public Session getMailSession() {
        return super.getMailSession(); // Allow tests to access real session behavior
    }
}

