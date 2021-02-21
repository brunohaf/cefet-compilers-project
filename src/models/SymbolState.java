package models;

public enum SymbolState {
	REGISTERED("Registered"),
	NOTFOUND("NotFound"),
	INITIALIZED("Initialized");

	private final String stateName;
	private SymbolState(String valor) {
		stateName = valor;
	}
}