package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

public class Messages {

	public static Map<User, ArrayList<Message>> messages = new HashMap<User, ArrayList<Message>>();
		
	public static List<Message> getMessagesForUser(User u) {				
		for (Entry<User, ArrayList<Message>> entry : messages.entrySet()) {
			if (entry.getKey() == u) return entry.getValue();
		}
		
		return null;
	}
	
	private static void addMessageForUser(Message msg, User u) {
		if (messages.get(u) != null) {
			messages.get(u).add(msg);
		}else {
			ArrayList<Message> list = new ArrayList<Message>();
			list.add(msg);
			messages.put(u, list);
		}
	}	
	
	public static void ParseMessages(List<Message> msgs) {
		for(Message m : msgs) {
			addMessageForUser(m, m.getFrom());
		}
	}
	
	public static List<Message> receiveMessages() {
		List<Message> list = new ArrayList<Message>();
		
		NetWorkers net = new NetWorkers();
		try {
			String str = net.receiveMessage(Login.loggedUser+"");			
			for(String s : str.split("#")) {
				String[] splt = s.split(";");
				User from = Users.getUserById(Integer.parseInt(splt[0])); 
				User to = Login.getLoggedUser();				
				
				Message msg = new Message(from, to, splt[1]+"", splt[2]+"");
				
				list.add(msg);
			}
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {}
		return list;
		
	}
	
}
