package cvut.fel.pda2013;

import java.util.Comparator;

/**
 * porovna dve Message podle datumu
 * @author vojta
 *
 */
public class MessageComparator implements Comparator<Message> {

	@Override
	public int compare(Message lhs, Message rhs) {
		return lhs.getDatetime().compareTo(rhs.getDatetime());
	}

}
