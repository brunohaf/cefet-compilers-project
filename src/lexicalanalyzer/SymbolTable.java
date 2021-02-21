package lexicalanalyzer;
import java.util.HashMap;

import org.javatuples.Pair;

import models.SymbolState;
import models.SymbolType;

// https://www.tutorialspoint.com/compiler_design/compiler_design_symbol_table.htm

// SymbolTable entry format
//<symbol name,  type,  attribute>
public class SymbolTable {
	
	public HashMap<String, Pair<SymbolType, String>> symbolTable = new HashMap<String, Pair<SymbolType, String>>();
	public TypeResolver typeResolver;
	
	// Inserts a new token to the symbol table.
	public void Insert(String symbolName, SymbolType type, String value) {
		symbolTable.put(symbolName, new Pair<SymbolType, String> (type, value));
	}
	
	//Checks if the SymbolTable contains the given symbolName and if it is INITIALIZED or REGISTERED.
	public SymbolState LookUp(String symbolName) {
		if(!symbolTable.containsKey(symbolName)) {
			return SymbolState.NOTFOUND;
		}
		else {
			return symbolTable.get(symbolName) != null ? SymbolState.INITIALIZED : SymbolState.REGISTERED;
		}
	}
}