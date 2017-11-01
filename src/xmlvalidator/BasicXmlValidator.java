package xmlvalidator;

public class BasicXmlValidator implements XmlValidator {

	@Override
	public String[] validate(String xmlDocument) {
		String[] xmlErrors = new String[5];

		int LineNum = 1;
		int lastOpeningNum = 0;
		int length = xmlDocument.length();
		BasicStringStack Stack = new BasicStringStack();

		for (int i = 0; i < length; i++) {
			if (xmlDocument.charAt(i) == '\n') {
				LineNum++;
			}
			if (xmlDocument.charAt(i) == '<') {
				String[] attVal = getAttribute(i, xmlDocument);

				if (attVal[0].equals("ignore")) {
					i++;
				} else if (attVal[1].equals("opening")) {
					Stack.push(attVal[0]);
					lastOpeningNum = LineNum;
					if (attVal[2].equals("unquoted")) {
						xmlErrors[0] = "Attribute not quoted";
						xmlErrors[1] = attVal[0];
						xmlErrors[2] = Integer.toString(LineNum);
						xmlErrors[3] = attVal[3];
						xmlErrors[4] = Integer.toString(LineNum);
						return xmlErrors;
					} else
						;
				} else if (attVal[1].equals("closing")) {
					if (Stack.getCount() == 0) {
						xmlErrors[0] = "Orphan closing tag";
						xmlErrors[1] = attVal[0];
						xmlErrors[2] = Integer.toString(LineNum);
						return xmlErrors;
					}
					if (Stack.peek(0).equals(attVal[0])) {
						Stack.pop();
					} else if (!Stack.peek(0).equals(attVal[0])) {
						xmlErrors[0] = "Tag mismatch";
						xmlErrors[1] = Stack.peek(0);
						xmlErrors[2] = Integer.toString(lastOpeningNum);
						xmlErrors[3] = attVal[0];
						xmlErrors[4] = Integer.toString(LineNum);
						return xmlErrors;
					}
				}
			}

		}
		if (Stack.getCount() != 0) {
			xmlErrors[0] = "Unclosed tag at end";
			xmlErrors[1] = Stack.peek(0);
			xmlErrors[2] = Integer.toString(lastOpeningNum);
			return xmlErrors;
		} else
			return null;
	}

	private String[] getAttribute(int i, String Doc) {
		String Attribute = null;
		String type;
		String[] attVal = new String[4];
		if (Character.isAlphabetic(Doc.charAt(i + 1)) || Doc.charAt(i + 1) == '/') {

			for (int x = i + 1; i < Doc.length();) {
				if (Doc.charAt(x) == '>') {
					Attribute = Doc.substring(i + 1, x);
					break;
				} else
					x++;
			}

			if (Attribute.contains("=")) {
				for (int x = 1; x < Attribute.length();) {
					if (Attribute.charAt(x) == ' ') {
						attVal[3] = Attribute.substring(x + 1, Attribute.indexOf("="));
						break;
					} else
						x++;
				}
				if (Attribute.charAt(Attribute.indexOf("=") + 1) == '\"') {
					attVal[2] = "quoted";
				} else {
					attVal[2] = "unquoted";
				}
			} else
				attVal[2] = "skip";

			for (int x = 1; x < Attribute.length();) {
				if (Attribute.charAt(x) == ' ') {
					Attribute = Attribute.substring(0, x);
					break;
				} else
					x++;
			}

			if (Attribute.charAt(0) == '/') {
				type = "closing";
				Attribute = Attribute.substring(1);
			} else {
				type = "opening";
			}

			attVal[0] = Attribute;
			attVal[1] = type;
			return attVal;
		} else
			attVal[0] = "ignore";
		attVal[1] = "ignore";
		return attVal;
	}

}
