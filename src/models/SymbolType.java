package models;

public enum SymbolType {
	integer("int"),
	character("char"),
	string_m("string"),
	real("real");

	private final String typeName;
	private SymbolType(String valor) {
		typeName = valor;
	}
}