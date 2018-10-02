package data;

public class OrderedTable {
	String tableAlias;
    int index;
    
    public OrderedTable(String str, int ind) {
    	tableAlias = str;
    	index = ind;
    }
    public String getTableAliase(){
    	return this.tableAlias;
    }
    public int getIndex() {
    	return this.index;
    }
    
    @Override
	public int hashCode(){
    	return tableAlias.hashCode();
    }
    
    public boolean equals(OrderedTable ot) {
    	return this.tableAlias.equals(ot.getTableAliase());
    }

    

}
