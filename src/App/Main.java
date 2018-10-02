package App;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import data.Dynamic_properties;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import operators.JoinOperator;
import operators.ProjectOperator;
import operators.ScanOperator;
import operators.SelectOperator;
import operators.SortOperator;

public class Main {

	public static void main(String[] args) {
		
		SQLInterpreter.init(args);
		
		try {
			SQLInterpreter.process();
			
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}

	}
}
