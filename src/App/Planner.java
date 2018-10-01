package App;

import data.Dynamic_properties;
import java.io.FileReader;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import operators.Operator;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class Planner {
	
	public void parse_queries() {
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(Dynamic_properties.QUERY_PATH));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				PlainSelect ps = (PlainSelect)select.getSelectBody();
				Operator op = generatePlan(ps);
				System.out.println("Select body is " + select.getSelectBody());
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}
	public Operator generatePlan(PlainSelect ps) {
		return null;
	}

}
