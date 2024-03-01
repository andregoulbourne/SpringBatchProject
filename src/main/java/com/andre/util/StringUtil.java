package com.andre.util;

public class StringUtil {
	
	private StringUtil() {
		super();
	}
	
	public static String takeOutWhiteSpaceInFrontAndBack(String in) {
		return removeWhiteSpaceBack(removeWhiteSpaceFront(in));
	}
	
	public static String removeWhiteSpaceFront(String inputString) {
		StringBuilder stringResult=new StringBuilder();
		boolean isPassedWhiteSpaceInfront= false;
		for(int i=0; i<inputString.length(); i++) {
			if(!isPassedWhiteSpaceInfront && isAValidCharacter(inputString.charAt(i))) isPassedWhiteSpaceInfront=true;
			if(isPassedWhiteSpaceInfront) stringResult.append(inputString.charAt(i));
		}
		return stringResult.toString();
	}
	
	public static String removeWhiteSpaceBack(String in) {
		StringBuilder stringResult=new StringBuilder();
		boolean isPassedWhiteSpaceInBack= false;
		for(int i=in.length()-1; i>=0; i--) {
			if(!isPassedWhiteSpaceInBack && isAValidCharacter(in.charAt(i))) isPassedWhiteSpaceInBack=true;
			if(isPassedWhiteSpaceInBack) stringResult.append(in.charAt(i));
		}
		return reverseString(stringResult);
	}
	
	public static String reverseString(StringBuilder stringForProcessing) {
		return stringForProcessing.reverse().toString();
	}
	
	public static boolean isAValidCharacter(char c) {
		String key ="abcdefghijklmnopqrstuvwxyz0123456789.-+=!@?()'";
		return key.contains((c+"").toLowerCase());
	}

}
