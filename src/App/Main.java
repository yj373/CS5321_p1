package App;


public class Main {

	public static void main(String[] args) {
		
		SQLInterpreter.init(args);
		//SQLInterpreter.init();
		SQLInterpreter.BuildQueryPlan();
		
		
		
//		String queriesFile = Dynamic_properties.TEST_QUERY_PATH;
//		try {
//			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
//			Statement statement = parser.Statement();
//			Select select = (Select)statement;
//			PlainSelect ps = (PlainSelect)select.getSelectBody();
//			String table_info = ps.getFromItem().toString();
//			List table_info1 = ps.getJoins();
//			List list = ps.getJoins();
//			if (list != null){
//				System.out.println(list.get(0).toString());
//			}
//			ScanOperator scanOp = new ScanOperator(table_info);
//
//			SelectOperator selectOp = new SelectOperator(ps,scanOp);
//			ProjectOperator projectOp = new ProjectOperator(ps, selectOp);
//			SortOperator sortOp = new SortOperator(ps, projectOp);
//			sortOp.dump();
//
//			ScanOperator scanOp1 = new ScanOperator(table_info1.get(0).toString());
//			JoinOperator joinOp = new JoinOperator(scanOp, scanOp1);
////			SelectOperator selectOp = new SelectOperator(ps,scanOp);
////			ProjectOperator projectOp = new ProjectOperator(ps, joinOp);
////			projectOp.dump();
//
//		} catch (Exception e) {
//			System.err.println("Exception occurred during parsing");
//			e.printStackTrace();
//		}
//
	}

}
