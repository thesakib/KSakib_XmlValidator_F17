package xmlvalidator;

import static java.lang.System.*;

public class BasicStringStack implements StringStack {

	private int count = 0;
	private String[] items = new String[0];

	// public BasicStringStack(int initialSize) {
	// count = 0;
	// items = new String[initialSize];
	// }

	@Override
	public void push(String item) {
		if (count == items.length) {
			int newLength = (int) (items.length * 1.25) + 1;
			String[] tempAry = new String[newLength];
			arraycopy(items, 0, tempAry, 0, items.length);
			items = tempAry;
		}
		items[count++] = item;
	}

	@Override
	public String pop() {
		String tempItem;
		if (count == 0)
			return null;
		else
			tempItem = items[count - 1];
		items[count - 1] = null;
		count--;
		return tempItem;

	}

	@Override
	public String peek(int position) {
		if (count == 0)
			return null;
		if ((position > count - 1) || (position < 0)) {
			return null;
		} else
			return items[count - position - 1];

	}

	@Override
	public int getCount() {

		return count;
	}

}
