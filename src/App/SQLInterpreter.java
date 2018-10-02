package App;

import java.io.FileNotFoundException;
import java.io.FileReader;

import data.Dynamic_properties;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.*;
import operators.Operator;
import visitors.BasicVisitor;



public class SQLInterpreter {

	public static void init (String[] args) {
		Dynamic_properties.setPath(args[0], args[1]);
	}

	public static void BuildQueryPlan () {
		
		String queriesFile = Dynamic_properties.queryPath;
		try {
			Operator root = null;
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			net.sf.jsqlparser.statement.Statement statement;
			int index = 1;

			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				BasicVisitor visitor = new BasicVisitor();
				try {
					root = visitor.getQueryPlan(select);
					writeToFile (index, root);
				} catch (Exception e) {
					System.err.println("Exception occurred during paring query" + index);
			        e.printStackTrace();
				}
				index++;	
			}

		} catch (Exception e){
			 System.err.println("Exception occurred during parsing");
	         e.printStackTrace();
		}

	}
	
	public static void writeToFile (int index, Operator root) {
		root.dump(index);
		System.out.println("end");
	}
	
}
