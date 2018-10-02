package App;

import data.Dynamic_properties;

public class SQLInterpreter {

	public static void init (String[] args) {
		
		Dynamic_properties.INPUT_PATH = args[0];
		Dynamic_properties.OUTPUT_PATH = args[1];
		Dynamic_properties.DATA_PATH = Dynamic_properties.INPUT_PATH + "/db/data/";
		Dynamic_properties.QUERY_PATH = Dynamic_properties.INPUT_PATH + "/queries.sql";
		Dynamic_properties.SCHEMA_PATH = Dynamic_properties.INPUT_PATH +"/db/schema.txt";
		
		
		
		
	}
}
