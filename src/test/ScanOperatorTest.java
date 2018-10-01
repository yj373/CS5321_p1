package test;

import operators.ScanOperator;
import data.Tuple;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

class ScanOperatorTest {

	// Test for Constructor: ScanOperator(String tableInfo)
	@Test 
	void testForAlias() {
		String tableInfo = "Sailors S1";
		ScanOperator sOp = new ScanOperator(tableInfo);
		assertEquals(sOp.getTableName(), "Sailors");
		assertEquals(sOp.getTableAliase(), "S1");
		
		LinkedList<String> schema = new LinkedList<>();
		schema.add("A");
		schema.add("B");
		schema.add("C");
		
		for (int i = 0; i < schema.size(); i++) {
			assertEquals(sOp.getAttributes().get(i), schema.get(i));
		}
		assertEquals(sOp.getAttributes().size(), schema.size());		
	}
	
	// Test for Constructor: ScanOperator(String tableName, String tableAliase)
	@Test
	void testForExplicitAlias() {
		String tableName = "Reserves";
		String tableAliase = "R";
		ScanOperator sOp= new ScanOperator(tableName, tableAliase);
			
		LinkedList<String> schema = new LinkedList<>();
		schema.add("G");
		schema.add("H");
		
		for (int i = 0; i < schema.size(); i++) {
			assertEquals(sOp.getAttributes().get(i), schema.get(i));
		}
		assertEquals(sOp.getAttributes().size(), schema.size());
	}
	
	// Test for getNextTuple() and reset()
	@Test
	void testForTuple() {
		ScanOperator sOp = new ScanOperator("Sailors S");
		
		Tuple tuple = sOp.getNextTuple();
		assertEquals(tuple.getTupleData(), "1 200 50 ");
		
		tuple = sOp.getNextTuple();
		assertEquals(tuple.getTupleData(), "2 200 200 ");
		
		tuple = sOp.getNextTuple();
		assertEquals(tuple.getTupleData(), "3 100 105 ");
		
		tuple = sOp.getNextTuple();
		assertEquals(tuple.getTupleData(), "4 100 50 ");
		
		tuple = sOp.getNextTuple();
		assertEquals(tuple.getTupleData(), "5 100 500 ");
		
		tuple = sOp.getNextTuple();
		assertEquals(tuple.getTupleData(), "6 300 400 ");
		
		tuple = sOp.getNextTuple();
		assertEquals(tuple, null);
		
		sOp.reset();
		assertEquals(sOp.getNextTuple().getTupleData(), "1 200 50 ");		
	}
	

}
