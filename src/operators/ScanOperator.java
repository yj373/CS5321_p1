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
	
	private Operator parent;
	private String tableName;
	private String tableAddress;
	private File tableFile;
	private RandomAccessFile readPointer;
	private String tableAliase;
	private LinkedList<String> attributes;
	

	@Override
	public Tuple getNextTuple() {
		try {
			String data = readPointer.readLine();
			if (data!=null) {
				//Handle aliases
				Tuple tuple = new Tuple(data, tableAliase, attributes);
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
			this.readPointer.seek(0);
		} catch (IOException e) {
			e.printStackTrace();
			e.getMessage();
		}
		
	}
	
	//Constructors
	public ScanOperator() {
		
	}
	public ScanOperator(String tableInfo) {
		String[] aimTable = tableInfo.split("\\s+");
		if (aimTable.length<1) {
			this.tableName = null;
			return;
		}
		this.tableName = aimTable[0];
		this.tableAddress = DataBase.getInstance().getAddresses(tableName);
		this.tableFile = new File(tableAddress);
		try {
			this.readPointer = new RandomAccessFile(this.tableFile, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
		}
		this.tableAliase = aimTable[aimTable.length-1];
		this.attributes = DataBase.getInstance().getSchema(tableName);
	}
	
	public ScanOperator(String tableName, String tableAliase) {
		this.tableName = tableName;
		this.tableAddress = DataBase.getInstance().getAddresses(tableName);
		this.tableFile = new File(tableAddress);
		try {
			this.readPointer = new RandomAccessFile(this.tableFile, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
		}
		this.tableAliase = tableAliase;
		this.attributes = DataBase.getInstance().getSchema(tableName);
	}
	
	public ScanOperator(String tableName, String tableAliase, Operator op) {
		this.parent = op;
		this.tableName = tableName;
		this.tableAddress = DataBase.getInstance().getAddresses(tableName);
		this.tableFile = new File(tableAddress);
		try {
			this.readPointer = new RandomAccessFile(this.tableFile, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
		}
		this.tableAliase = tableAliase;
		this.attributes = DataBase.getInstance().getSchema(tableName);
	}
	
	//Getters
	public String getTableAliase() {
		return tableAliase;
	}
	public LinkedList<String> getAttributes(){
		return attributes;
	}

	public String getTable_name() {
		return tableName;
	}

	public String getTable_address() {
		return tableAddress;
	}

	public File getTable_file() {
		return tableFile;
	}

	public RandomAccessFile getRead_pointer() {
		return readPointer;
	}
	
	public Operator getParent() {
		return parent;
	}
	
	//Update parent
	public void setParent(Operator op) {
		this.parent = op;
	}

	
	

}
