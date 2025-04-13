package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.EmailException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {

    private EmailConcrete email; 

    @Before
    public void setUpEmailTest() {
        email = new EmailConcrete(); 
    }

    @After
    public void tearDownEmailTest() throws Exception {
        // Cleanup logic if needed
    }

    @Test
    public void testAddBcc() throws EmailException {
        email.addBcc("test1@example.com");
        email.addBcc("test2@example.com");
        assertEquals(2, email.getBccAddresses().size());
    }

    @Test
    public void testAddCc() throws EmailException {
        email.addCc("test@example.com");
        assertEquals(1, email.getCcAddresses().size());
    }

    @Test
    public void testAddHeader() throws Exception {
        email.addHeader("To", "Reem@gmail.com");
        assertEquals("Reem@gmail.com", email.getHeaders().get("To"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAddHeader() throws Exception {
        email.addHeader("To", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAddNameToHeader() throws Exception {
        email.addHeader("", "Reem@gmail.com");
    }

    @Test
    public void testAddReplyTo() throws EmailException {
        email.addReplyTo("reply@example.com", "Reply User");
        assertEquals(1, email.getReplyToAddresses().size());
    }

    @Test(expected = RuntimeException.class)
    public void testBuildMimeMessage() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.addTo("hi@gmail.com");
        email.setSubject("test mail");
        email.setCharset("ISO-8859-1");
        email.setContent("test content", "test/plain");
        email.buildMimeMessage();
        email.buildMimeMessage(); // This should throw RuntimeException
    }

    @Test
    public void testSuccessfulBuildMimeMessage() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.addTo("hi@gmail.com");
        email.setSubject("Test Email");
        email.setContent("Hello, this is a test.", "text/plain");
        email.buildMimeMessage();
        assertNotNull(email.getMimeMessage());
    }

    @Test(expected = EmailException.class)
    public void testBuildMimeMessageWithoutFrom() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
        email.addTo("hi@gmail.com");
        email.setSubject("Test Email");
        email.setContent("This email has no sender.", "text/plain");
        email.buildMimeMessage();
    }

    @Test(expected = EmailException.class)
    public void testBuildMimeMessageWithoutRecipients() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.setSubject("Test Email");
        email.setContent("This email has no recipients.", "text/plain");
        email.buildMimeMessage();
    }

    @Test
    public void testBuildMimeMessageWithMultipleRecipients() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.addTo("hi@gmail.com");
        email.addCc("cc@example.com");
        email.addBcc("bcc@example.com");
        email.setSubject("Test with multiple recipients");
        email.setContent("Testing To, CC, and BCC.", "text/plain");
        email.buildMimeMessage();
        MimeMessage message = email.getMimeMessage();
        assertEquals(1, message.getRecipients(Message.RecipientType.TO).length);
        assertEquals(1, message.getRecipients(Message.RecipientType.CC).length);
        assertEquals(1, message.getRecipients(Message.RecipientType.BCC).length);
    }

    @Test
    public void testBuildMimeMessageWithReplyTo() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.addTo("hi@gmail.com");
        email.setSubject("Reply-To Test");
        email.addReplyTo("reply@example.com", "Reply User");
        email.buildMimeMessage();
        MimeMessage message = email.getMimeMessage();
        InternetAddress[] replyTo = (InternetAddress[]) message.getReplyTo();
        assertNotNull(replyTo);
        assertEquals("reply@example.com", replyTo[0].getAddress());
    }

    @Test
    public void testBuildMimeMessageWithHeaders() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.addTo("hi@gmail.com");
        email.setSubject("Header Test");
        email.addHeader("X-Test-Header", "HeaderValue");
        email.buildMimeMessage();
        assertEquals("HeaderValue", email.getMimeMessage().getHeader("X-Test-Header")[0]);
    }

    @Test
    public void testBuildMimeMessageWithSentDate() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.addTo("hi@gmail.com");
        email.setSubject("Sent Date Test");
        email.buildMimeMessage();
        assertNotNull(email.getMimeMessage().getSentDate());
    }

    @Test
    public void testGetHostName() throws Exception {
        assertNull(email.getHostName());
    }

    @Test
    public void testGetHostNameWithSession() throws Exception {
        email.setHostName(null);
        Properties p = new Properties();
        p.setProperty("mail.host", "smtp.test.com");
        Session s = Session.getInstance(p);
        email.setMailSession(s);
        assertEquals("smtp.test.com", email.getHostName());
    }

    @Test
    public void testGetMailSessionCreatesNewSession() throws Exception {
        email.setHostName("localhost");
        Session session = email.getMailSession();
        assertNotNull(session);
    }

    @Test
    public void testGetSentDate() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.MARCH, 20, 12, 0, 0);
        java.util.Date date = calendar.getTime();
        email.setSentDate(date);
        assertEquals(date, email.getSentDate());
    }

    @Test
    public void testGetSocketConnectionTimeout() throws Exception {
        assertEquals(60000, email.getSocketConnectionTimeout());
    }

    @Test
    public void testSetFrom() throws EmailException {
        email.setFrom("from@example.com");
        assertEquals("from@example.com", email.getFromAddress().getAddress());
    }
}
