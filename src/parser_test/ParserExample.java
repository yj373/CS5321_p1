package parser_test;
import java.io.FileReader;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class ParserExample {
	
	private static final String queriesFile = "queries.sql";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				PlainSelect ps = (PlainSelect)select.getSelectBody();
				
				List<SelectItem> test = ps.getSelectItems();
				for (SelectItem expre : test) {
					String attributeName = expre.toString();
					System.out.println(attributeName);
						
				}
				
				
				if (ps.getDistinct()!=null) {
					System.out.println(ps.getDistinct().toString());
				}
				if (ps.getJoins()!=null) {
					System.out.println(ps.getJoins().size());
					System.out.println(ps.getJoins().toString());
				}
				if (ps.getFromItem()!=null) {
					System.out.println(ps.getFromItem().toString());
				}
				if(ps.getFromItem().getAlias()!=null) {
					System.out.println(ps.getFromItem().getAlias());
				}
				if (ps.getWhere()!=null) {
					System.out.println(ps.getWhere().toString());
				}
				
				System.out.println("Select body is " + select.getSelectBody());
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}

	}

}
