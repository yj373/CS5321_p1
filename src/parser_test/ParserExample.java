package parser_test;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Table;
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
//				
//				Expression test = ps.getWhere();
//				System.out.println(test.toString());
				//List<> test = ps.getOrderByElements();
				
				
				//test project operator
//				long[] data1 = new long[] {1,2,3};
//				Map<String, Integer> schema1 = new HashMap<String, Integer>();
//				schema1.put("Sailors.A",0);
//				schema1.put("S.C",1);
//				schema1.put("S.B",2);
//				Tuple current = new Tuple(data1,schema1);
//				long[] data = new long[2];
//				Map<String, Integer> schema = new HashMap<String, Integer>();
//				
//				List<SelectItem> selectItems = ps.getSelectItems();
//				if (selectItems.get(0).toString() == "*") {
//					System.out.println("current value is *");
//				} else {
//					int index = 0;
//					for (SelectItem expre : selectItems) {
//						String attributeName = expre.toString();
//						Integer dataIndex = current.getSchema().get(attributeName);
//						if (dataIndex!=null) {
//							data[index] = current.getTupleData()[dataIndex];
//							schema.put(attributeName, index);
//							index++;
//						}	
//					}
//					System.out.println(current.getTupleData().toString());
//				}
				
				
				
				
				if (ps.getDistinct()!=null) {
					System.out.println(ps.getDistinct().toString());
				}
				if (ps.getJoins()!=null) {
					System.out.println(ps.getJoins().size());
					System.out.println(ps.getJoins().toString());
				}
				if (ps.getFromItem()!=null) {
					System.out.println(ps.getFromItem().toString());
					Table t = (Table)ps.getFromItem();
					System.out.println(t.getWholeTableName());
					System.out.println(t.getAlias());
					System.out.println(t.getName());
					
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
