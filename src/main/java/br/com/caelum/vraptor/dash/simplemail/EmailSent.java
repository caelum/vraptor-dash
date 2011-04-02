package br.com.caelum.vraptor.dash.simplemail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.commons.mail.Email;

@Entity
public class EmailSent {
	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private String addresses;
	@Column(length = 255)
	private String from;
	@Lob
	private String subject;

	private Date createdAt;

	@Lob
	private String problem;

	public EmailSent(String from, String subject) {
		this.from = from;
		this.subject = subject;
		this.addresses = "";
		this.createdAt = new Date();
	}

	public static EmailSent from(Email email) {
		EmailSent sent = new EmailSent(email.getFromAddress().getAddress(),
				email.getSubject());
		sent.addWithPrefix("to", email.getToAddresses());
		sent.addWithPrefix("cc", email.getCcAddresses());
		sent.addWithPrefix("bcc", email.getBccAddresses());
		return sent;
	}

	private void addWithPrefix(String prefix, List<InternetAddress> toAddresses) {
		for (InternetAddress addr : toAddresses) {
			addresses += "|" + prefix + ":" + addr.getAddress() + "|";
		}
	}

	public static EmailSent from(Email email, Exception ex) {
		EmailSent sent = from(email);
		sent.problem = toStringEx(ex);
		return sent;
	}

	private static String toStringEx(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		ex.printStackTrace(writer);
		writer.flush();
		return sw.getBuffer().toString();
	}

}
