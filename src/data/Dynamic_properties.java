package data;

public class Dynamic_properties {
	public static String inputPath = "src/samples/input";
	public static String dataPath = inputPath + "/db/data/";
	public static String queryPath = inputPath + "/queries.sql";
	public static String schemaPath = inputPath + "/db/schema.txt";
	public static String outputPath = "src/samples/output";
	public static String testQueryPath = inputPath + "/test_query.sql";
	
	public static void setPath(String p0, String p1) {
		inputPath = p0;
		outputPath = p1;
	}
	
}
