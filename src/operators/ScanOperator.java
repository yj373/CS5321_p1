package operators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import data.DataBase;

import data.Tuple;

//Deal with Select * From Table
public class ScanOperator extends Operator{
	
	private String table_name;
	private String table_address;
	private File table_file;
	private RandomAccessFile read_pointer;
	private String table_aliase;
	private LinkedList<String> attributes;
	

	@Override
	public Tuple getNextTuple() {
		try {
			String data = read_pointer.readLine();
			if (data!=null) {
				Tuple tuple = new Tuple(data);
				return tuple;
			}
		} catch (IOException e) {
			e.printStackTrace();
			e.getMessage();
		}
		
		return null;
	}

	@Override
	public void reset() {
		try {
			this.read_pointer.seek(0);
		} catch (IOException e) {
			e.printStackTrace();
			e.getMessage();
		}
		
	}
	
	//Constructors
	public ScanOperator() {
		
	}
	public ScanOperator(String table_info) {
		String[] aim_table = table_info.split("\\s+");
		if (aim_table.length<1) {
			this.table_name = null;
			return;
		}
		this.table_name = aim_table[0];
		this.table_address = DataBase.getInstance().getAddresses(table_name);
		this.table_file = new File(table_address);
		try {
			this.read_pointer = new RandomAccessFile(this.table_file, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
		}
		this.table_aliase = aim_table[aim_table.length-1];
		this.attributes = DataBase.getInstance().getSchema(table_name);
	}
	
	//Getters
	public String getTableAliase() {
		return table_aliase;
	}
	public LinkedList<String> getAttributes(){
		return attributes;
	}

	public String getTable_name() {
		return table_name;
	}

	public String getTable_address() {
		return table_address;
	}

	public File getTable_file() {
		return table_file;
	}

	public RandomAccessFile getRead_pointer() {
		return read_pointer;
	}

	
	

}
