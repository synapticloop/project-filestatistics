package synapticloop.projectfilestatistics.util;

/*
 * Copyright (c) 2009-2011 synapticloop.
 * All rights reserved.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENCE shipped with 
 * this source code or binaries.
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * Licence for the specific language governing permissions and limitations 
 * under the Licence. 
 */


/**
 * A simple class for text based printing utilities
 * 
 */
public class PrintHelper {

	/**
	 * Return a string which repeats the specified character for a specified
	 * number of times. 
	 *  
	 * @param character the character to use
	 * @param numTimes the number of times 
	 * @return the newly created string
	 */
	
	public static String underline(char character, int numTimes) {
		StringBuilder stringBuilder = new StringBuilder(numTimes);
		for(int i = 0; i < numTimes; i++) {
			stringBuilder.append(character);
		}
		return(stringBuilder.toString());
	}
	
	/**
	 * Underline text with a specific character returning both the newline and
	 * the underlining.  For example, the following text:
	 * <pre>
	 *   This should be underlined
	 * </pre>
	 * when called with the character '=' and skipSpace = false, would return:
	 * <pre>
	 *   This should be underlined
	 *   =========================
	 * </pre>
	 * With skipSpace set to true, the return value would be:
	 * <pre>
	 *   This should be underlined
	 *   ==== ====== == ========== 
	 * </pre>
	 * @param text the text to underline
	 * @param character the character to 
	 * @param skipSpace whether to underline spaces or just letters
	 * @return the underlined string, with new line appended
	 */

	public static String underlineText(String text, char character, boolean skipSpace) {
		int textLength = text.length();

		// initialise the string buffer
		StringBuilder stringBuilder = new StringBuilder((textLength * 2) + 1);
		for(int i = 0; i < (textLength *2 + 1); i ++) {
			stringBuilder.append(' ');
		}

		for(int i = 0; i < textLength; i++) {
			char currentChar = text.charAt(i);
			stringBuilder.setCharAt(i, currentChar);
			if(skipSpace && currentChar == ' ') {
				stringBuilder.setCharAt(textLength + 1 + i, ' ');
			} else {
				stringBuilder.setCharAt(textLength + 1 + i, character);
			}
		}

		// now for the new line character
		stringBuilder.setCharAt(textLength, '\n');
		return(stringBuilder.toString());
	}
}
