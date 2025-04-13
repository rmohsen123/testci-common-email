package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.junit.After;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmailTest {

    private EmailConcrete email; 

    /*
     * The setup procedure is executed before each test case.
     * Creates a new EmailConcrete instance.
     */
    
    @Before
    public void setUpEmailTest() {
        email = new EmailConcrete();
    }
    /*
     * The tear down procedure is executed after each test case.
     * Can be used for cleanup if necessary.
     */

    @After
    public void tearDownEmailTest() throws Exception {
        // Cleanup logic if needed
    }
    /*
     *A test scenario for adding BCC (Blind Carbon Copy) receivers.
     */
    @Test
    public void testAddBcc() throws EmailException {
        email.addBcc("test1@example.com");
        email.addBcc("test2@example.com");

        System.out.println("BCC Addresses: " + email.getBccAddresses());
        assertEquals(2, email.getBccAddresses().size()); // Ensure that both addresses were added.
    }

    /*
     * Test case for verifying the addition of a CC address.
     */
    @Test
    public void testAddCc() throws EmailException {
        email.addCc("test@example.com");
        assertEquals(1, email.getCcAddresses().size());
    }

    /*
     * Test case for ensuring that addHeader() does not throw an error.
     */
    @Test
    public void testAddHeader() throws Exception {
       email.addHeader("To", "Reem@gmail.com");
       assertEquals("Reem@gmail.com", email.getHeaders().get("To"));
    }
    /*
     * Test scenario for dealing with an invalid header value (empty string).
     * Expected to throw an IllegalArgumentException.
     */
    
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidAddHeader() throws Exception {
       email.addHeader("To", "");
   
    }
    /*
     * Test case for dealing with an invalid header name (empty string).
     * Expected to throw IllegalArgumentException.
     */
    
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidAddNameToHeader() throws Exception {
       email.addHeader("", "Reem@gmail.com");
   
    }

    /*
     * Test case for adding a Reply-To address.
     */
    @Test
    public void testAddReplyTo() throws EmailException {
        email.addReplyTo("reply@example.com", "Reply User");
        assertEquals(1, email.getReplyToAddresses().size());
    }
    /*
     * A test scenario for using buildMimeMessage() twice.
     * It was expected that attempting to reconstruct an existing MimeMessage would result in a RuntimeException.
     */

    @Test (expected = RuntimeException.class)
    public void testBuildMimeMessage() throws Exception {
        try {
       email.setHostName("localhost");
       email.setSmtpPort(1234);
       email.setFrom("a@gmail.com");
       email.addTo("hi@gmail.com");
       email.setSubject("test mail");
       email.setCharset("ISO-8859-1");
       email.setContent("test content", "test/plain");;
       email.buildMimeMessage();
       email.buildMimeMessage(); // Should throw an exception

        }
       catch (RuntimeException re)
       {
           String message = "The MimeMessage is already built.";
           assertEquals(message, re);
           throw re;
       }
    }
    /*
     * Test case for successfully creating a MimeMessage.
     */
    @Test
    public void testSuccessfulBuildMimeMessage() throws Exception {
         email.setHostName("localhost");
         email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.addTo("hi@gmail.com");
        email.setSubject("Test Email");
        email.setContent("Hello, this is a test.", "text/plain");
        
        email.buildMimeMessage();
        
        assertNotNull(email.getMimeMessage()); // Ensure message is built
    }
    /*
     * Test case for a missing "From" address, which should result in an EmailException.
     */
    @Test(expected = EmailException.class)
    public void testBuildMimeMessageWithoutFrom() throws Exception {
        email.setHostName("localhost");
        email.setSmtpPort(1234);
       
        email.addTo("hi@gmail.com");
        email.setSubject("Test Email");
        email.setContent("This email has no sender.", "text/plain");

        email.buildMimeMessage(); // Should throw EmailException
    }
    
    /*
     * Test case for missing receivers (To, CC, and BCC).
     *Expected to throw an email exception.
     */
    @Test(expected = EmailException.class)
    public void testBuildMimeMessageWithoutRecipients() throws Exception {
         email.setHostName("localhost");
         email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.setSubject("Test Email");
        email.setContent("This email has no recipients.", "text/plain");

        email.buildMimeMessage(); // Should throw EmailException
    }

    /*
     *Test case for specifying multiple recipients (To, CC, and BCC).
     */
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

    /*
     * A test scenario for specifying the 'Reply-To' address.
     */
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

    /*
     * A test case for setting email headers.
     */
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

    /*
     * Test case for manually setting a send date.
     */
    @Test
    public void testBuildMimeMessageWithSentDate() throws Exception {
         email.setHostName("localhost");
         email.setSmtpPort(1234);
        email.setFrom("a@gmail.com");
        email.addTo("hi@gmail.com");
        email.setSubject("Sent Date Test");
        
        email.buildMimeMessage();

        assertNotNull(email.getMimeMessage().getSentDate()); // Should have a valid sent date
    }
    
    /*
     * Test case to ensure that getHostName() always returns null.
     */
    @Test
    public void testGetHostName() throws Exception{
        assertNull(email.getHostName());
    }

    @Test
    public void testGetHostNameWithSession()throws Exception {
        email.setHostName(null);
       Properties p = new Properties();
       p.setProperty("mail.host", "smtp.test.com");
       Session s = Session.getInstance(p);
       email.setMailSession(s);
       assertEquals("smtp.test.com", email.getHostName());
    }

    
    @Test
    public void testGetMailSession()throws Exception {
        
    }

    @Test
    public void testGetMailSessionCreatesNewSession() throws Exception {
        Session session = email.getMailSession();
        assertNotNull(session); // Ensure session is created
    }

    
    /*
     * Test case to ensure that getSentDate() does not return null.
     */
    @Test
    public void testGetSentDate() throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.MARCH, 20, 12, 0, 0);
        java.util.Date date = calendar.getTime();

        email.setSentDate(date);
        assertEquals(date, email.getSentDate());
    }


    
    /*
     * A test case to verify the default socket connection timeout value.
     */
    @Test
    public void testGetSocketConnectionTimeout()throws Exception {
        assertEquals(60000, email.getSocketConnectionTimeout());
    }

    /*
     * A test case for checking the From address in an email.
     */
    @Test
    public void testSetFrom() throws EmailException {
        email.setFrom("from@example.com");
        assertEquals("from@example.com", email.getFromAddress().getAddress());
    }
}
