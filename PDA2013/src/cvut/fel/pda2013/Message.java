package cvut.fel.pda2013;

import java.util.Date;

/**
 * Trida pouzita k reprezentaci zpravy
 * @author vojta
 *
 */
public class Message {

	private User from;
	private User to;
	private String message;
	private String datetime; 
	public User getFrom() {
		return from;
	}
	public void setFrom(User from) {
		this.from = from;
	}
	public User getTo() {
		return to;
	}
	public void setTo(User to) {
		this.to = to;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public Message(User from, User to, String message, String datetime) {
		super();
		this.from = from;
		this.to = to;
		this.message = message;
		this.datetime = datetime;
	}
	
	@Override
	public String toString() {
		return "From: " + from.getName() + ", to: " + to.getName() + ", date: " + datetime + ", msg: " + message;
	}
	
	
	
	
}
