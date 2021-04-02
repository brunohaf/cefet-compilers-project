package Models;

public class Tag {
  public final static int
  // Palavras reservadas
  INIT = 256, STOP = 257, IS = 258, INT = 259, STRING = 260, REAL = 261, IF = 262, BEG = 263, END = 264, ELSE = 265, DO=266, WHILE=267, READ = 268, WRITE = 269,
  // Operadores e pontuação
  EQ = 288, GE = 289, LE = 290, NE = 291, AND = 292, OR = 293, NOT = 294, EOF = 300,
  // Outros tokens
  NUM = 278, ID = 279, TRUE = 280, FALSE = 281;
}