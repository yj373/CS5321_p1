package data;

public class Tuple {
	//Store the data of each tuple
	private long[] data;
	
	//Constructors
	public Tuple() {
		
	}
	public Tuple(String str) {
		String[] temp = str.split(",");
		data = new long[temp.length];
		for (int i=0; i<temp.length; i++) {
			data[i] = Long.parseLong(temp[i]);
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
	
	public int getSize() {
		if (data == null) {
			return 0;
		}else {
			return data.length;
		}
	}
	

}
