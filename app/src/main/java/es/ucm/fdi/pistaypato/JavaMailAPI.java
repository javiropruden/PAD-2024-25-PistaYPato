package es.ucm.fdi.pistaypato;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI {

    private String correoRemitente;
    private String contraseñaRemitente;

    public JavaMailAPI(String correoRemitente, String contraseñaRemitente) {
        this.correoRemitente = correoRemitente;
        this.contraseñaRemitente = contraseñaRemitente;
    }

    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        // Configuración del servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP
        props.put("mail.smtp.port", "587"); // Puerto SMTP
        props.put("mail.smtp.auth", "true"); // Requiere autenticación
        props.put("mail.smtp.starttls.enable", "true"); // Habilitar TLS

        // Crear una sesión con autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, contraseñaRemitente);
            }
        });

        try {
            // Crear un mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoRemitente)); // Remitente
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatario)); // Destinatario
            message.setSubject(asunto); // Asunto del correo
            message.setText(mensaje); // Mensaje

            // Enviar el correo
            Transport.send(message);
            System.out.println("Correo enviado correctamente.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}
