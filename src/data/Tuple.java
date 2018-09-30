package data;

import java.util.LinkedList;

public class Tuple {
	//Store the data of each tuple
	private long[] data;
	private String[] schema;
	
	//Constructors
	public Tuple() {
		
	}
	//Construct a tuple of a single table
	public Tuple(String str, String table_name, LinkedList<String> attributes) {
		String[] temp = str.split(",");
		data = new long[temp.length];
		schema = new String[temp.length];
		if (data.length!=attributes.size()) return;
		for (int i=0; i<temp.length; i++) {
			data[i] = Long.parseLong(temp[i]);
			StringBuilder sb = new StringBuilder();
			sb.append(table_name);
			sb.append(".");
			sb.append(attributes.get(i));
			
		}
	}
	public Tuple(String[] str_arr) {
		data = new long[str_arr.length];
		for (int i=0; i<data.length; i++) {
			data[i] = Long.parseLong(str_arr[i]);
		}
	}
	
	//Getters
	public long[] getTupleData() {
		return data;
	}
	public String[] getSchema() {
		return schema;
	}
	
	public int getSize() {
		if (data == null) {
			return 0;
		}else {
			return data.length;
		}
	}
	

}
