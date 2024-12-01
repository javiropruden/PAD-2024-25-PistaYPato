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
    private String contrasenaRemitente;

    public JavaMailAPI(String correoRemitente, String contrasenaRemitente) {
        this.correoRemitente = correoRemitente;
        this.contrasenaRemitente = contrasenaRemitente;
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
                return new PasswordAuthentication(correoRemitente, contrasenaRemitente);
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

            // Enviar el mensaje como HTML
            message.setContent(mensaje, "text/html; charset=utf-8"); // Especificar el tipo de contenido como HTML

            // Enviar el correo
            Transport.send(message);
            System.out.println("Correo enviado correctamente.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    /**
     * Método para enviar un correo con los detalles de la reserva
     *
     * @param destinatario Correo electrónico del destinatario
     * @param asunto       Asunto del correo
     * @param sitio        Nombre del sitio o instalación
     * @param hora         Hora de la reserva
     * @param pista        Pista reservada
     */
    public void enviarCorreo(String destinatario, String asunto, String sitio, String hora, String pista) {
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
                return new PasswordAuthentication(correoRemitente, contrasenaRemitente);
            }
        });
        String mensaje = "<html><body>"
                + "<h3>Reserva Confirmada</h3>"
                + "<p><strong>Sitio:</strong> " + sitio + "</p>"
                + "<p><strong>Hora:</strong> " + hora + "</p>"
                + "<p><strong>Pista:</strong> " + pista + "</p>"
                + "<p>Gracias por realizar tu reserva.</p>"
                + "</body></html>";

        try {
            // Crear un mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoRemitente)); // Remitente
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatario)); // Destinatario
            message.setSubject(asunto); // Asunto del correo

            // Enviar el mensaje como HTML
            message.setContent(mensaje, "text/html; charset=utf-8"); // Especificar el tipo de contenido como HTML

            // Enviar el correo
            Transport.send(message);
            System.out.println("Correo enviado correctamente.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

}

