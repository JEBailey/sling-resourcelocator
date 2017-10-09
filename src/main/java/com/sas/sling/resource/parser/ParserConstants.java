/* Generated By:JavaCC: Do not edit this line. ParserConstants.java */
package com.sas.sling.resource.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int ALPHA = 3;
  /** RegularExpression Id. */
  int ESCAPED_CHAR = 4;
  /** RegularExpression Id. */
  int DOT = 5;
  /** RegularExpression Id. */
  int PLUS = 6;
  /** RegularExpression Id. */
  int MINUS = 7;
  /** RegularExpression Id. */
  int DIGIT = 8;
  /** RegularExpression Id. */
  int EXP = 9;
  /** RegularExpression Id. */
  int UNRESERVED_STR = 10;
  /** RegularExpression Id. */
  int SINGLE_QUOTED_STR = 11;
  /** RegularExpression Id. */
  int DOUBLE_QUOTED_STR = 12;
  /** RegularExpression Id. */
  int AND = 13;
  /** RegularExpression Id. */
  int OR = 14;
  /** RegularExpression Id. */
  int NULL = 15;
  /** RegularExpression Id. */
  int TRUE = 16;
  /** RegularExpression Id. */
  int FALSE = 17;
  /** RegularExpression Id. */
  int LPAREN = 18;
  /** RegularExpression Id. */
  int RPAREN = 19;
  /** RegularExpression Id. */
  int LBRACKET = 20;
  /** RegularExpression Id. */
  int RBRACKET = 21;
  /** RegularExpression Id. */
  int COMMA = 22;
  /** RegularExpression Id. */
  int COMP = 23;
  /** RegularExpression Id. */
  int NUMBER = 24;
  /** RegularExpression Id. */
  int INTEGER = 25;
  /** RegularExpression Id. */
  int FRACTIONAL_DIGITS = 26;
  /** RegularExpression Id. */
  int EXPONENT = 27;
  /** RegularExpression Id. */
  int DIGITS = 28;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "<ALPHA>",
    "<ESCAPED_CHAR>",
    "\".\"",
    "\"+\"",
    "\"-\"",
    "<DIGIT>",
    "<EXP>",
    "<UNRESERVED_STR>",
    "<SINGLE_QUOTED_STR>",
    "<DOUBLE_QUOTED_STR>",
    "<AND>",
    "<OR>",
    "\" null\"",
    "\" true\"",
    "\" false\"",
    "\"(\"",
    "\")\"",
    "\"[\"",
    "\"]\"",
    "\",\"",
    "<COMP>",
    "<NUMBER>",
    "<INTEGER>",
    "<FRACTIONAL_DIGITS>",
    "<EXPONENT>",
    "<DIGITS>",
  };

}
