package App;

import java.io.FileNotFoundException;
import java.io.FileReader;

import data.Dynamic_properties;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

public class SQLInterpreter {
	public static void init(String[] args) {
		Dynamic_properties.setPath(args[0], args[1]);
	}
	
	public static void process() throws FileNotFoundException, ParseException {
		CCJSqlParser parser = new CCJSqlParser(new FileReader(Dynamic_properties.queryPath));
		Statement statement = parser.Statement();
		int i = 0;
		while (statement != null) {
			Select select = (Select)statement;
			
			
		}
	}
	
}
