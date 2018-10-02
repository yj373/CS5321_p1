package data;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/*This class act as a catalog storing the information of the database.
 *The information includes:
 *where the table is and the table schema
 * */

public class DataBase {
	//Get the needed paths
	private String query_path = Dynamic_properties.queryPath;
	private String data_path = Dynamic_properties.dataPath;
	private String schema_path = Dynamic_properties.schemaPath;
	
	//Track the address of each table
	//key:the name of the table, value: the address of the table
	private Map<String, String> addresses = new HashMap<String, String>();
	
	//Store the schemas of all the tables
	//key: the name of each table, value: table column names
	private Map<String, LinkedList<String>> schemas = new HashMap<String, LinkedList<String>>();
	
	//Singleton pattern
	private static volatile DataBase Instance =null;
	
	//Private constructor
	private DataBase() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(schema_path));
			String line = br.readLine();
			while(line !=null) {
				String[] res = line.split("\\s+");
				addresses.put(res[0], data_path+res[0]);
				LinkedList<String> columns = new LinkedList<String>();
				for (int i = 1; i<res.length; i++) {
					columns.add(res[i]);
				}
				schemas.put(res[0], columns);
				line = br.readLine();
			}
			br.close();
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	//Thread safe instance return
	public static DataBase getInstance() {
		if (Instance == null) {
			synchronized (DataBase.class) {
				if (Instance == null) {
					Instance = new DataBase();
				}
				
			}
		}
		return Instance;
	}
	
	//Getters
	public String getAddresses(String str){
		return addresses.get(str);
	}
	public LinkedList<String> getSchema(String str){
		return schemas.get(str);
	}
	

}
