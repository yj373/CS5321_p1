package App;

import data.Dynamic_properties;

public class SQLInterpreter {

	public static void init (String[] args) {
		
		Dynamic_properties.setPath(args[0], args[1]);
		
		
	}
}
