package test;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import operators.ProjectOperator;
import operators.ScanOperator;
import operators.SelectOperator;


class SelectOperatorTest {
	
	@Test
	public void getNextTupleTest() throws JSQLParserException {

		String[] statements = new String[10];
		
		statements[0] = "SELECT * FROM Sailors S WHERE S.B = 300;";
		statements[1] = "SELECT * FROM Sailors WHERE Sailors.B = 300;";
		statements[2] = "SELECT * FROM Sailors S WHERE S.B = 100 AND S.C = 50;";
		statements[3] = "SELECT * FROM Sailors S WHERE S.B = 100 AND S.A < 5 ;";
		statements[4] = "SELECT * FROM Sailors S WHERE S.B = 200 AND S.A > 1;";
		statements[5] = "SELECT * FROM Reserves WHERE Reserves.H < 105;";
		statements[6] = "SELECT * FROM Reserves R WHERE R.H = 103;";
		statements[7] = "SELECT * FROM Reserves R WHERE R.G > 3;";
		statements[8] = "SELECT * FROM Boats BO WHERE BO.D = 107;";
		statements[9] = "SELECT * FROM Boats BO WHERE BO.E = 1;";
		
		for (int i=0; i< statements.length; i++) {
			System.out.println("*******when statement is : " + statements[i]);
			String statement = statements[i];
			CCJSqlParserManager parserManager = new CCJSqlParserManager();
			PlainSelect ps = (PlainSelect) ((Select) parserManager.
					parse(new StringReader(statement))).getSelectBody();
			String table_info = ps.getFromItem().toString();
			ScanOperator scanOp = new ScanOperator(table_info);
			SelectOperator selectOp = new SelectOperator(ps,scanOp);
			//ProjectOperator projectOp = new ProjectOperator(ps, selectOp);
			selectOp.dump();
			System.out.println("*******end*********");
			System.out.println();
		}

	}

}
