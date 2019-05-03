package util;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by s911415 on 7/12/2016.
 */
public class Mail {
    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
    private Session session;
    private Properties props;
    private InternetAddress _from = null, _reply = null;
    private InternetAddress[] _to = null, _cc = null, _bcc = null;
    private InternetAddress[] _forceSendToAddresses;
    private String _subject, _content;
    private List<String> _attachment = new ArrayList<>();
    private static final String EMAIL_DELIMITER = ",";
    private boolean _isDebug;

    public Mail() {
        props = config.Config.getMailProperties();

        if (props == null) {
            throw new NullPointerException("Failed to load mail props.");
        }

        LogUtility.debugLog("user=" + props.getProperty("mail.smtp.username") + ",pwd=" + props.getProperty("mail.smtp.password"));
        _isDebug = Boolean.parseBoolean(props.getProperty("mail.debug", "false"));
        {
            _forceSendToAddresses = Stream.of(props.getProperty("mail.force_send_to", "")
                    .trim()
                    .split(","))
                    .filter(s -> s.length() > 0)
                    .map(address -> {
                        try {
                            return new InternetAddress(address.trim());
                        } catch (AddressException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .toArray(InternetAddress[]::new);
        }

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                props.getProperty("mail.smtp.username"),
                                props.getProperty("mail.smtp.password")
                        );
                    }
                });
    }

    /**
     * 取得寄信人
     *
     * @return
     */
    private InternetAddress getFrom() {
        if (this._from != null) return this._from;
        String email = props.getProperty("mail.smtp.sender");
        String name = props.getProperty("mail.smtp.sender_name");

        if (name == null) {
            setFrom(email);
            if (this._from != null) return getFrom();
        } else {
            setFrom(email, name);
            if (this._from != null) return getFrom();
        }

        return null;
    }

    /**
     * 設定寄信人
     *
     * @param emailAddress 寄件人Email
     * @return
     */
    public Mail setFrom(String emailAddress) {
        try {
            this._from = new InternetAddress(emailAddress);
        } catch (Exception e) {
            LogUtility.errorLog(e);
        }

        return this;
    }

    /**
     * 設定寄信人
     *
     * @param emailAddress 寄件人Email
     * @param userName     寄件人名稱
     * @return
     */
    public Mail setFrom(String emailAddress, String userName) {
        try {
            this._from = new InternetAddress(emailAddress, MimeUtility.encodeText(userName, DEFAULT_ENCODING.displayName(), "B"));
        } catch (Exception e) {
            LogUtility.errorLog(e);
        }
        Message message = new MimeMessage(session);
        return this;
    }

    /**
     * 設定收信人
     *
     * @param emailAddress 收信人Email
     * @return
     */
    public Mail setTo(String emailAddress) {
        return setTo(Arrays.asList(emailAddress));
    }

    /**
     * 設定收信人
     *
     * @param list 所有收信人Email
     * @return
     */
    public Mail setTo(List<String> list) {
        String toAddress = String.join(EMAIL_DELIMITER, list.toArray(new String[list.size()]));
        try {
            this._to = InternetAddress.parse(toAddress);
        } catch (AddressException e) {
            LogUtility.errorLog(e);
        }

        return this;
    }

    /**
     * 設定副本
     *
     * @param emailAddress 副本Email
     * @return
     */
    public Mail setCc(String emailAddress) {
        return setCc(Arrays.asList(emailAddress));
    }

    /**
     * 設定副本
     *
     * @param list 所有副本Email
     * @return
     */
    public Mail setCc(List<String> list) {
        String toAddress = String.join(EMAIL_DELIMITER, list.toArray(new String[list.size()]));
        try {
            this._cc = InternetAddress.parse(toAddress);
        } catch (AddressException e) {
            LogUtility.errorLog(e);
        }

        return this;
    }

    /**
     * 設定密件副本
     *
     * @param emailAddress 密件副本Email
     * @return
     */
    public Mail setBcc(String emailAddress) {
        return setBcc(Arrays.asList(emailAddress));
    }

    /**
     * 設定密件副本
     *
     * @param list 所有副本Email
     * @return
     */
    public Mail setBcc(List<String> list) {
        String toAddress = String.join(EMAIL_DELIMITER, list.toArray(new String[list.size()]));
        try {
            this._bcc = InternetAddress.parse(toAddress);
        } catch (AddressException e) {
            LogUtility.errorLog(e);
        }

        return this;
    }

    /**
     * 設定信件主旨
     *
     * @param title
     * @return
     */
    public Mail setSubject(String title) {
        this._subject = title;

        return this;
    }

    /**
     * 設定信件內容
     *
     * @param content
     * @return
     */
    public Mail setContent(String content) {
        this._content = content;

        return this;
    }

    /**
     * 新增附件
     *
     * @param path
     * @return
     */
    public Mail addAttachment(String path) {
        _attachment.add(path);

        return this;
    }

    /**
     * 傳送信件
     */
    public void send() {
        MimeMessage message = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();

        try {
            if (getFrom() != null) {
                message.setFrom(getFrom());
            }

            if (_isDebug || _forceSendToAddresses.length == 0) {
                if (this._to != null) message.setRecipients(Message.RecipientType.TO, this._to);
                if (this._cc != null) message.setRecipients(Message.RecipientType.CC, this._cc);
                if (this._bcc != null) message.setRecipients(Message.RecipientType.BCC, this._bcc);
            } else {
                message.setRecipients(Message.RecipientType.BCC, this._forceSendToAddresses);
            }

            message.setSubject(this._subject, DEFAULT_ENCODING.displayName());
            messageBodyPart.setContent(this._content, "text/html;charset=\"" + DEFAULT_ENCODING.displayName() + "\"");
            multipart.addBodyPart(messageBodyPart);
            for (String attachPath : this._attachment) {
                try {
                    File attachment = new File(attachPath);
                    if (!attachment.exists()) continue;

                    MimeBodyPart mimeBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachPath);
                    mimeBodyPart.setDataHandler(new DataHandler(source));
                    mimeBodyPart.setFileName(MimeUtility.encodeText(attachment.getName(), DEFAULT_ENCODING.displayName(), "B"));
                    multipart.addBodyPart(mimeBodyPart);
                } catch (Exception e) {
                    LogUtility.errorLog(e);
                }
            }

            message.setContent(multipart);

            if (_isDebug) {
                try {
                    File eml = File.createTempFile("mail-output", ".eml");
                    try (FileOutputStream out = new FileOutputStream(eml)) {
                        message.writeTo(out);
                    }
                    LogUtility.infoLog("Email write to " + eml.getAbsolutePath());
                } catch (IOException e) {
                    LogUtility.errorLog(e);
                }
            } else {
                new Thread(() -> {
                    try {
                        Transport.send(message);
                        LogUtility.debugLog("寄信成功");

                    } catch (MessagingException e) {
                        LogUtility.errorLog(e);
                    }
                }).start();
            }

        } catch (MessagingException e) {
            LogUtility.errorLog(e);
        }
    }

    /*
     * Just for test code
     *
    public static void main(){
        Mail mail = new Mail();
        mail.setTo("to@email.com")
                .setFrom("from@email.com", "Name")
                .setSubject("Subject")
                .setContent("Content")
                .addAttachment("Path for attachment");
        mail.send();
    }
    */

}
