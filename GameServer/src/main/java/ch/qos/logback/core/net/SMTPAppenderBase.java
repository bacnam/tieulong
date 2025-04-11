package ch.qos.logback.core.net;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.helpers.CyclicBuffer;
import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.sift.DefaultDiscriminator;
import ch.qos.logback.core.sift.Discriminator;
import ch.qos.logback.core.spi.CyclicBufferTracker;
import ch.qos.logback.core.util.ContentTypeUtil;
import ch.qos.logback.core.util.OptionHelper;

import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.*;

public abstract class SMTPAppenderBase<E>
        extends AppenderBase<E> {
    static final int MAX_DELAY_BETWEEN_STATUS_MESSAGES = 1228800000;
    static InternetAddress[] EMPTY_IA_ARRAY = new InternetAddress[0];
    protected Layout<E> subjectLayout;
    protected Layout<E> layout;
    protected Session session;
    protected EventEvaluator<E> eventEvaluator;
    protected Discriminator<E> discriminator = (Discriminator<E>) new DefaultDiscriminator();
    protected CyclicBufferTracker<E> cbTracker;
    long lastTrackerStatusPrint = 0L;
    int delayBetweenStatusMessages = 300000;
    String username;
    String password;
    String localhost;
    boolean asynchronousSending = true;
    private List<PatternLayoutBase<E>> toPatternLayoutList = new ArrayList<PatternLayoutBase<E>>();
    private String from;
    private String subjectStr = null;
    private String smtpHost;
    private int smtpPort = 25;
    private boolean starttls = false;
    private boolean ssl = false;
    private boolean sessionViaJNDI = false;
    private String jndiLocation = "java:comp/env/mail/Session";
    private String charsetEncoding = "UTF-8";
    private int errorCount = 0;

    protected abstract Layout<E> makeSubjectLayout(String paramString);

    public void start() {
        if (this.cbTracker == null) {
            this.cbTracker = new CyclicBufferTracker();
        }

        if (this.sessionViaJNDI) {
            this.session = lookupSessionInJNDI();
        } else {
            this.session = buildSessionFromProperties();
        }
        if (this.session == null) {
            addError("Failed to obtain javax.mail.Session. Cannot start.");

            return;
        }
        this.subjectLayout = makeSubjectLayout(this.subjectStr);

        this.started = true;
    }

    private Session lookupSessionInJNDI() {
        addInfo("Looking up javax.mail.Session at JNDI location [" + this.jndiLocation + "]");
        try {
            Context initialContext = new InitialContext();
            Object obj = initialContext.lookup(this.jndiLocation);
            return (Session) obj;
        } catch (Exception e) {
            addError("Failed to obtain javax.mail.Session from JNDI location [" + this.jndiLocation + "]");
            return null;
        }
    }

    private Session buildSessionFromProperties() {
        Properties props = new Properties(OptionHelper.getSystemProperties());
        if (this.smtpHost != null) {
            props.put("mail.smtp.host", this.smtpHost);
        }
        props.put("mail.smtp.port", Integer.toString(this.smtpPort));

        if (this.localhost != null) {
            props.put("mail.smtp.localhost", this.localhost);
        }

        LoginAuthenticator loginAuthenticator = null;

        if (this.username != null) {
            loginAuthenticator = new LoginAuthenticator(this.username, this.password);
            props.put("mail.smtp.auth", "true");
        }

        if (isSTARTTLS() && isSSL()) {
            addError("Both SSL and StartTLS cannot be enabled simultaneously");
        } else {
            if (isSTARTTLS()) {
                props.put("mail.smtp.starttls.enable", "true");
            }
            if (isSSL()) {
                String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
                props.put("mail.smtp.socketFactory.port", Integer.toString(this.smtpPort));
                props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
                props.put("mail.smtp.socketFactory.fallback", "true");
            }
        }

        return Session.getInstance(props, loginAuthenticator);
    }

    protected void append(E eventObject) {
        if (!checkEntryConditions()) {
            return;
        }

        String key = this.discriminator.getDiscriminatingValue(eventObject);
        long now = System.currentTimeMillis();
        CyclicBuffer<E> cb = (CyclicBuffer<E>) this.cbTracker.getOrCreate(key, now);
        subAppend(cb, eventObject);

        try {
            if (this.eventEvaluator.evaluate(eventObject)) {

                CyclicBuffer<E> cbClone = new CyclicBuffer(cb);

                cb.clear();

                if (this.asynchronousSending) {

                    SenderRunnable senderRunnable = new SenderRunnable(cbClone, eventObject);
                    this.context.getExecutorService().execute(senderRunnable);
                } else {

                    sendBuffer(cbClone, eventObject);
                }
            }
        } catch (EvaluationException ex) {
            this.errorCount++;
            if (this.errorCount < 4) {
                addError("SMTPAppender's EventEvaluator threw an Exception-", (Throwable) ex);
            }
        }

        if (eventMarksEndOfLife(eventObject)) {
            this.cbTracker.endOfLife(key);
        }

        this.cbTracker.removeStaleComponents(now);

        if (this.lastTrackerStatusPrint + this.delayBetweenStatusMessages < now) {
            addInfo("SMTPAppender [" + this.name + "] is tracking [" + this.cbTracker.getComponentCount() + "] buffers");
            this.lastTrackerStatusPrint = now;

            if (this.delayBetweenStatusMessages < 1228800000) {
                this.delayBetweenStatusMessages *= 4;
            }
        }
    }

    protected abstract boolean eventMarksEndOfLife(E paramE);

    protected abstract void subAppend(CyclicBuffer<E> paramCyclicBuffer, E paramE);

    public boolean checkEntryConditions() {
        if (!this.started) {
            addError("Attempting to append to a non-started appender: " + getName());

            return false;
        }

        if (this.eventEvaluator == null) {
            addError("No EventEvaluator is set for appender [" + this.name + "].");
            return false;
        }

        if (this.layout == null) {
            addError("No layout set for appender named [" + this.name + "]. For more information, please visit http:");

            return false;
        }
        return true;
    }

    public synchronized void stop() {
        this.started = false;
    }

    InternetAddress getAddress(String addressStr) {
        try {
            return new InternetAddress(addressStr);
        } catch (AddressException e) {
            addError("Could not parse address [" + addressStr + "].", (Throwable) e);
            return null;
        }
    }

    private List<InternetAddress> parseAddress(E event) {
        int len = this.toPatternLayoutList.size();

        List<InternetAddress> iaList = new ArrayList<InternetAddress>();

        for (int i = 0; i < len; i++) {
            try {
                PatternLayoutBase<E> emailPL = this.toPatternLayoutList.get(i);
                String emailAdrr = emailPL.doLayout(event);
                if (emailAdrr != null && emailAdrr.length() != 0) {
                    InternetAddress[] tmp = InternetAddress.parse(emailAdrr, true);
                    iaList.addAll(Arrays.asList(tmp));
                }
            } catch (AddressException e) {
                addError("Could not parse email address for [" + this.toPatternLayoutList.get(i) + "] for event [" + event + "]", (Throwable) e);
                return iaList;
            }
        }

        return iaList;
    }

    public List<PatternLayoutBase<E>> getToList() {
        return this.toPatternLayoutList;
    }

    protected void sendBuffer(CyclicBuffer<E> cb, E lastEventObject) {
        try {
            MimeBodyPart part = new MimeBodyPart();

            StringBuffer sbuf = new StringBuffer();

            String header = this.layout.getFileHeader();
            if (header != null) {
                sbuf.append(header);
            }
            String presentationHeader = this.layout.getPresentationHeader();
            if (presentationHeader != null) {
                sbuf.append(presentationHeader);
            }
            fillBuffer(cb, sbuf);
            String presentationFooter = this.layout.getPresentationFooter();
            if (presentationFooter != null) {
                sbuf.append(presentationFooter);
            }
            String footer = this.layout.getFileFooter();
            if (footer != null) {
                sbuf.append(footer);
            }

            String subjectStr = "Undefined subject";
            if (this.subjectLayout != null) {
                subjectStr = this.subjectLayout.doLayout(lastEventObject);

                int newLinePos = (subjectStr != null) ? subjectStr.indexOf('\n') : -1;
                if (newLinePos > -1) {
                    subjectStr = subjectStr.substring(0, newLinePos);
                }
            }

            MimeMessage mimeMsg = new MimeMessage(this.session);

            if (this.from != null) {
                mimeMsg.setFrom((Address) getAddress(this.from));
            } else {
                mimeMsg.setFrom();
            }

            mimeMsg.setSubject(subjectStr, this.charsetEncoding);

            List<InternetAddress> destinationAddresses = parseAddress(lastEventObject);
            if (destinationAddresses.isEmpty()) {
                addInfo("Empty destination address. Aborting email transmission");

                return;
            }
            InternetAddress[] toAddressArray = destinationAddresses.<InternetAddress>toArray(EMPTY_IA_ARRAY);
            mimeMsg.setRecipients(Message.RecipientType.TO, (Address[]) toAddressArray);

            String contentType = this.layout.getContentType();

            if (ContentTypeUtil.isTextual(contentType)) {
                part.setText(sbuf.toString(), this.charsetEncoding, ContentTypeUtil.getSubType(contentType));
            } else {

                part.setContent(sbuf.toString(), this.layout.getContentType());
            }

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart((BodyPart) part);
            mimeMsg.setContent((Multipart) mimeMultipart);

            mimeMsg.setSentDate(new Date());
            addInfo("About to send out SMTP message \"" + subjectStr + "\" to " + Arrays.toString((Object[]) toAddressArray));
            Transport.send((Message) mimeMsg);
        } catch (Exception e) {
            addError("Error occurred while sending e-mail notification.", e);
        }
    }

    protected abstract void fillBuffer(CyclicBuffer<E> paramCyclicBuffer, StringBuffer paramStringBuffer);

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return this.subjectStr;
    }

    public void setSubject(String subject) {
        this.subjectStr = subject;
    }

    public String getSMTPHost() {
        return getSmtpHost();
    }

    public void setSMTPHost(String smtpHost) {
        setSmtpHost(smtpHost);
    }

    public String getSmtpHost() {
        return this.smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public int getSMTPPort() {
        return getSmtpPort();
    }

    public void setSMTPPort(int port) {
        setSmtpPort(port);
    }

    public int getSmtpPort() {
        return this.smtpPort;
    }

    public void setSmtpPort(int port) {
        this.smtpPort = port;
    }

    public String getLocalhost() {
        return this.localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    public CyclicBufferTracker<E> getCyclicBufferTracker() {
        return this.cbTracker;
    }

    public void setCyclicBufferTracker(CyclicBufferTracker<E> cbTracker) {
        this.cbTracker = cbTracker;
    }

    public Discriminator<E> getDiscriminator() {
        return this.discriminator;
    }

    public void setDiscriminator(Discriminator<E> discriminator) {
        this.discriminator = discriminator;
    }

    public boolean isAsynchronousSending() {
        return this.asynchronousSending;
    }

    public void setAsynchronousSending(boolean asynchronousSending) {
        this.asynchronousSending = asynchronousSending;
    }

    public void addTo(String to) {
        if (to == null || to.length() == 0) {
            throw new IllegalArgumentException("Null or empty <to> property");
        }
        PatternLayoutBase<E> plb = makeNewToPatternLayout(to.trim());
        plb.setContext(this.context);
        plb.start();
        this.toPatternLayoutList.add(plb);
    }

    protected abstract PatternLayoutBase<E> makeNewToPatternLayout(String paramString);

    public List<String> getToAsListOfString() {
        List<String> toList = new ArrayList<String>();
        for (PatternLayoutBase<E> plb : this.toPatternLayoutList) {
            toList.add(plb.getPattern());
        }
        return toList;
    }

    public boolean isSTARTTLS() {
        return this.starttls;
    }

    public void setSTARTTLS(boolean startTLS) {
        this.starttls = startTLS;
    }

    public boolean isSSL() {
        return this.ssl;
    }

    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

    public void setEvaluator(EventEvaluator<E> eventEvaluator) {
        this.eventEvaluator = eventEvaluator;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCharsetEncoding() {
        return this.charsetEncoding;
    }

    public void setCharsetEncoding(String charsetEncoding) {
        this.charsetEncoding = charsetEncoding;
    }

    public String getJndiLocation() {
        return this.jndiLocation;
    }

    public void setJndiLocation(String jndiLocation) {
        this.jndiLocation = jndiLocation;
    }

    public boolean isSessionViaJNDI() {
        return this.sessionViaJNDI;
    }

    public void setSessionViaJNDI(boolean sessionViaJNDI) {
        this.sessionViaJNDI = sessionViaJNDI;
    }

    public Layout<E> getLayout() {
        return this.layout;
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    class SenderRunnable
            implements Runnable {
        final CyclicBuffer<E> cyclicBuffer;
        final E e;

        SenderRunnable(CyclicBuffer<E> cyclicBuffer, E e) {
            this.cyclicBuffer = cyclicBuffer;
            this.e = e;
        }

        public void run() {
            SMTPAppenderBase.this.sendBuffer(this.cyclicBuffer, this.e);
        }
    }
}

