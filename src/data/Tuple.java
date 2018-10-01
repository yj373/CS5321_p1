package data;

import java.util.LinkedList;
import java.util.Map;

public class Tuple {
	//Store the data of each tuple
	private long[] data;
	private Map<String, Integer> schema;
	
	//Constructors
	public Tuple() {
		
	}
	//Construct a tuple of a single table
	public Tuple(String str, String table_aliase, LinkedList<String> attributes) {
		String[] temp = str.split(",");
		data = new long[temp.length];
		if (data.length!=attributes.size()) return;
		for (int i=0; i<temp.length; i++) {
			data[i] = Long.parseLong(temp[i]);
			StringBuilder sb = new StringBuilder();
			sb.append(table_aliase);
			sb.append(".");
			sb.append(attributes.get(i));
			schema.put(sb.toString(), i);
		}
	}
	
	// Construct a tuple from previous tuple
	public Tuple(long[] data, Map<String, Integer> schema) {
		this.data = data;
		this.schema = schema;
	
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
	
	public Map<String, Integer> getSchema() {
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
