//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           descriptive title of the program making use of this file
// Files:           a list of all source files used by that program
// Course:          course number, term, and year
//
// Author:          Lenea Guse
// Email:           laguse@wisc.edu email address
// Lecturer's Name: Jim
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:    name of your pair programming partner
// Partner Email:   email address of your programming partner
// Lecturer's Name: name of your partner's lecturer
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   ___ Write-up states that pair programming is allowed for this assignment.
//   ___ We have both read and understand the course Pair Programming Policy.
//   ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates 
// strangers, etc do.  If you received no outside help from either type of 
// source, then please explicitly indicate NONE.
//
// Persons:         (identify each person and describe their help in detail)
// Online Sources:  (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The Eliza class holds the user input and response formation for a system that
 * collects user input and responds appropriately. Eliza is based off of a
 * computer program written at MIT in the 1960's by Joseph Weizenbaum. Eliza
 * uses keyword matching to respond to users in a way that displays interest in
 * the users and continues the conversation until instructed otherwise.
 */
public class Eliza {

	/*
	 * This method does input and output with the user. It calls supporting methods
	 * to read and write files and process each user input.
	 * 
	 * @param args (unused)
	 */
	public static void main(String[] args) {
		String[] inputArray = new String[] {};
		ArrayList<String> transcript = new ArrayList<String>();
		ArrayList<String> inputArrayList = new ArrayList<String>();
		boolean keepLooping = true;
		String response = "";
		// Milestone 2
		// create a scanner for reading user input and a random number
		Scanner scanner = new Scanner(System.in);
		// generator with Config.SEED as the seed
		Random rand = new Random(Config.SEED);
		String input;

		// Milestone 3
		// How the program starts depends on the command-line arguments.
		// Command-line arguments can be names of therapists for example:
		// Eliza Joe Laura
		// If no command-line arguments then the therapists name is Eliza
		// and this reads a file with that name and the Config.RESPONSE_FILE_EXTENSION.
		// Example filename: Eliza.rsp
		// If only one command-line argument, then read the responses from
		// the corresponding file with Config.RESPONSE_FILE_EXTENSION.
		// If there is more than one command-line argument then offer them
		// as a list of people to talk with. For the three therapists above the prompt
		// is
		// "Would you like to speak with Eliza, Joe, or Laura?"
		// When a user types a name then read the responses from the file which
		// is the selected name and Config.RESPONSE_FILE_EXTENSION extension.
		// Whatever name the user types has the extension appended and
		// is read using loadResponseTable. If loadResponseTable can't load
		// the file then it will report an error.
		File file;
		String therapistName = "";
		String fileName = "";
		ArrayList<ArrayList<String>> responseTable = null;
		//Choosing which therapist the user wants
		if (args.length == 0) {
			therapistName = "Eliza";
			fileName = therapistName + Config.RESPONSE_FILE_EXTENSION;
			file = new File(fileName);
			responseTable = loadResponseTable(fileName);
		}
		if (args.length == 1) {
			therapistName = args[0];
			fileName = therapistName + Config.RESPONSE_FILE_EXTENSION;
			file = new File(fileName);
			responseTable = loadResponseTable(fileName);
		}
		if (args.length > 1) {
			System.out.println("Would you like to speak with ");
			for (int i = 0; i < args.length - 1; i++) {
				System.out.println(args[i] + ", ");
			}
			System.out.println(" or " + args[args.length - 1] + "?");
			therapistName = scanner.next();
			fileName = therapistName + Config.RESPONSE_FILE_EXTENSION;
			file = new File(fileName);
			responseTable = loadResponseTable(fileName);
			
		}
		// Milestone 2
		// name prompt

		// Milestone 2
		// welcome prompt
		System.out.println("Hi I'm " + therapistName + ", what is your name?");
		String userName = scanner.next();
		System.out.println("Nice to meet you " + userName + ". What is on your mind?");
		// Milestone 2
		// begin conversation loop
		scanner.nextLine();
		while (keepLooping) {
			// Milestone 2
			// obtain user input
			// scanner.nextLine();
			input = scanner.nextLine();
			transcript.add(input);

			// Milestone 2
			// prepareInput
			if (prepareInput(input) == null) {
				break;
			} else {
				inputArray = prepareInput(input);
			}
			for (int i = 0; i < inputArray.length; i++) {
				inputArrayList.add(inputArray[i]);
			}
			// Milestone 3
			// if no quit words then prepareResponse
			if (!(foundQuitWord(inputArrayList))) {
				response = prepareResponse(inputArray, rand, responseTable);
				System.out.println(response);
				transcript.add(response);
			}
			
			// Milestone 2
			// end loop if quit word
			else {
				keepLooping = false;
			}
		}
		
		// Milestone 2
		// ending prompt

		// Milestone 3
		// Save all conversation (user and system responses) starting
		// with this program saying "Hi I'm..." and concludes with
		// "Goodbye <name>.".
		// Always prompt the user to see if they would like to save a
		// record of the conversation. If the user enters a y or Y as the
		// first non-whitespace character then prompt for filename and save,
		// otherwise don't save dialog. After successfully saving a dialog
		// print the message "Thanks again for talking! Our conversation is saved in:
		// <filename>".
		// If saveDialog throws an IOException then catch it, print out the error:
		// "Unable to save conversation to: " <name of file>
		// Repeat the code prompting the user if they want to save the dialog.

		String dialogFileName;
		System.out.println("Do you want to save a record of our conversation?");
		if (scanner.next().charAt(0) == 'y' || scanner.next().charAt(0) == 'Y') {
			System.out.println("Please enter a file name");
			dialogFileName = scanner.next() + Config.RESPONSE_FILE_EXTENSION;

			try {
				saveDialog(transcript, dialogFileName);
			} catch (IOException e) {
				System.out.println("Unable to save conversation to " + dialogFileName);
			}
		}
		
		
		System.out.println("Goodbye " + userName);
	}
	
	

	/**
	 * This method processes the user input, returning an ArrayList containing
	 * Strings, where each String is a phrase from the user's input. This is done by
	 * removing leading and trailing whitespace, making the user's input all lower
	 * case, then going through each character of the user's input. When going
	 * through each character this keeps all digits, alphabetic characters and '
	 * (single quote). The characters ? ! , . signal the end of a phrase, and
	 * possibly the beginning of the next phrase, but are not included in the
	 * result. All other characters such as ( ) - " ] etc. should be replaced with a
	 * space. This method makes sure that every phrase has some visible characters
	 * but no leading or trailing whitespace and only a single space between words
	 * of a phrase. If userInput is null then return null, if no characters then
	 * return a 0 length list, otherwise return a list of phrases. Empty phrases and
	 * phrases with just invalid/whitespace characters should NOT be added to the
	 * list.
	 * 
	 * Example userInput: "Hi, I am! a big-fun robot!!!" Example returned: "hi", "i
	 * am", "a big fun robot"
	 * 
	 * @param userInput text the user typed
	 * @return the phrases from the user's input
	 */
	public static ArrayList<String> separatePhrases(String userInput) {
		if (userInput == null) {
			return null;
		}
		if (userInput.isEmpty()) {
			return new ArrayList<String>();
		}
		ArrayList<String> editedInput = new ArrayList<String>();
		// char currentChar = 'a';

		String str = "";
		int i = 0;
		// trim the userinput and put to lowercase
		userInput = userInput.trim().toLowerCase();
		char[] currChar = {};
		currChar = userInput.toCharArray();

		// search for a punctuation character to signal end of phrase - for loop and
		// checking each character for punctuation
		if (userInput.length() > 0) {
			for (i = 0; i < currChar.length; i++) {
				char nextLetter = userInput.charAt(i);
				switch (nextLetter) {
				case '?':
					if (!str.equals("")) {
						editedInput.add(str);
					}
					str = "";
					break;
				case '!':
					if (!str.equals("")) {
						editedInput.add(str);
					}
					str = "";
					break;
				case ',':
					if (!str.equals("")) {
						editedInput.add(str);
					}
					str = "";
					break;
				case '.':
					if (!str.equals("")) {
						editedInput.add(str);
					}
					str = "";
					break;
				case '(':
					str = str.concat(" ");
					break;
				case ')':
					str = str.concat(" ");
					break;
				case '-':
					str = str.concat(" ");
					break;
				case '"':
					str = str.concat(" ");
					break;
				case ']':
					str = str.concat(" ");
					break;
				default:
					//checking if next char is a letter
					if (Character.isLetter(nextLetter)) {
						str = str + userInput.charAt(i);
						
						break;
					}
					//checking if next char is a number
					if (Character.isDigit(nextLetter)) {
						str = str + userInput.charAt(i);
						
						break;
					}
					//checking if next char is a '
					if (nextLetter == '\'') {
						str = str + userInput.charAt(i);
						
						break;
					}
					//checking if next char is a blank space
					//checking if next char after is blank space
					if (Character.isWhitespace(nextLetter)) {
						if (Character.isWhitespace(currChar[i + 1])) {
							str = str.concat("");
						} else if (!str.equals("")) {
							str = str.concat(" ");
						}
					}

				}

			}
			if (!str.equals("")) {
				editedInput.add(str);
			}
			return editedInput;
		}
		return null;
	}

	/**
	 * Checks whether any of the phrases in the parameter match a quit word from
	 * Config.QUIT_WORDS. Note: complete phrases are matched, not individual words
	 * within a phrase.
	 * 
	 * @param phrases List of user phrases
	 * @return true if any phrase matches a quit word, otherwise false
	 */
	public static boolean foundQuitWord(ArrayList<String> phrases) {
		//iterates through the arraylist
		for (int j = 0; j < phrases.size(); j++) {
			//iterates through the quit words size
			for (int i = 0; i < Config.QUIT_WORDS.length; i++) {
				//checks if phrase is a quit word
				if (phrases.get(j).equals(Config.QUIT_WORDS[i])) {
					return true;
				}
			}
		}

		return false;

	}

	/**
	 * Iterates through the phrases of the user's input, finding the longest phrase
	 * to which to respond. If two phrases are the same length, returns whichever
	 * has the lower index in the list. If phrases parameter is null or size 0 then
	 * return "" (Update 11/15/18).
	 * 
	 * @param phrases List of user phrases
	 * @return the selected phrase
	 */
	public static String selectPhrase(ArrayList<String> phrases) {

		String largestString = "";
		//iterates through the arraylist
		for (int i = 0; i < phrases.size(); i++) {
			//checks if the current phrase is larger than the previous
			//largest phrase
			if (largestString.length() < phrases.get(i).length()) {
				largestString = phrases.get(i);
			}

		}

		return largestString;
	}

	/**
	 * Looks for a replacement word for the word parameter and if found, returns the
	 * replacement word. Otherwise if the word parameter is not found then the word
	 * parameter itself is returned. The wordMap parameter contains rows of match
	 * and replacement strings. On a row, the element at the 0 index is the word to
	 * match and if it matches return the string at index 1 in the same row. Some
	 * example word maps that will be passed in are Config.INPUT_WORD_MAP and
	 * Config.PRONOUN_MAP.
	 * 
	 * If word is null return null. If wordMap is null or wordMap length is 0 simply
	 * return word parameter. For this implementation it is reasonable to assume
	 * that if wordMap length is >= 1 then the number of elements in each row is at
	 * least 2.
	 * 
	 * @param word    The word to look for in the map
	 * @param wordMap The map of words to look in
	 * @return the replacement string if the word parameter is found in the wordMap
	 *         otherwise the word parameter itself.
	 */
	public static String replaceWord(String word, String[][] wordMap) {
		if (word == null) {
			return null;
		}
		if (wordMap == null || wordMap.length == 0) {
			return word;
		}
		if (Config.INPUT_WORD_MAP.length == 0 || Config.PRONOUN_MAP.length == 0) {
			return "0";
		}
		//iterates through the input word map
		for (int i = 0; i < Config.INPUT_WORD_MAP.length; i++) {
			//checks if the input word map is equal to the word
			//if yes changes word
			if (Config.INPUT_WORD_MAP[i][0] == word) {
				word = Config.INPUT_WORD_MAP[i][1];
				return word;
			} else {
			}
		}
		//iterates through pronoun map
		for (int i = 0; i < Config.PRONOUN_MAP.length; i++) {
			//checks if the pronoun map is equal to word
			//if yes changes word
			if (Config.PRONOUN_MAP[i][0] == word) {
				word = Config.PRONOUN_MAP[i][1];
				return word;
			} else {
			}
		}

		return null;
	}

	/**
	 * Concatenates the elements in words parameter into a string with a single
	 * space between each array element. Does not change any of the strings in the
	 * words array. There are no leading or trailing spaces in the returned string.
	 * 
	 * @param words a list of words
	 * @return a string containing all the words with a space between each.
	 */
	public static String assemblePhrase(String[] words) {
		String str = "";
		//puts the array of words together
		for (int i = 0; i < words.length; i++) {
			str = str + " " + words[i];
		}
		return str.trim();
	}

	/**
	 * Replaces words in phrase parameter if matching words are found in the mapWord
	 * parameter. A word at a time from phrase parameter is looked for in wordMap
	 * which may result in more than one word. For example: i'm => i am Uses the
	 * replaceWord and assemblePhrase methods. Example wordMaps are
	 * Config.PRONOUN_MAP and Config.INPUT_WORD_MAP. If wordMap is null then phrase
	 * parameter is returned. Note: there will Not be a case where a mapping will
	 * itself be a key to another entry. In other words, only one pass through
	 * swapWords will ever be necessary.
	 * 
	 * @param phrase  The given phrase which contains words to swap
	 * @param wordMap Pairs of corresponding match & replacement words
	 * @return The reassembled phrase
	 */
	public static String swapWords(String phrase, String[][] wordMap) {
		String str = "";
		boolean swap = false;
		if (wordMap == null) {
			return phrase;
		}
		//creates an array of phrases by splitting them at each " "
		String[] part = phrase.split(" ");
		//iterates through each part from phrase
		for (int k = 0; k < part.length; k++) {
			//iterates through the wordmap passed in
			for (int i = 0; i < wordMap.length; i++) {
				//if the strings are equal than change it
				if (part[k].equals(wordMap[i][0])) {
					phrase = wordMap[i][1];
					str = str + " " + phrase;
					swap = true;

				}
			}

			if (!swap) {
				str = str + " " + part[k];
			} else {
				swap = false;
			}
		}

		return str.trim();
	}

	/**
	 * This prepares the user input. First, it separates input into phrases (using
	 * separatePhrases). If a phrase is a quit word (foundQuitWord) then return
	 * null. Otherwise, select a phrase (selectPhrase), swap input words (swapWords
	 * with Config.INPUT_WORD_MAP) and return an array with each word its own
	 * element in the array.
	 * 
	 * @param input The input from the user
	 * @return words from the selected phrase
	 */
	public static String[] prepareInput(String input) {
		//calls several methods to tie them together to create a response
		ArrayList<String> returnString = separatePhrases(input);

		boolean progQuit = foundQuitWord(returnString);
		if (progQuit) {
			return null;
		} else {
			String phrase = selectPhrase(returnString);
			String words = swapWords(phrase, Config.INPUT_WORD_MAP);
			String[] str = words.split(" ");
			return str;
		}

	}

	/**
	 * Reads a file that contains keywords and responses. A line contains either a
	 * list of keywords or response, any blank lines are ignored. All leading and
	 * trailing whitespace on a line is ignored. A keyword line begins with
	 * "keywords" with all the following tokens on the line, the keywords. Each line
	 * that follows a keyword line that is not blank is a possible response for the
	 * keywords. For example (the numbers are for our description purposes here and
	 * are not in the file):
	 * 
	 * 1 keywords computer 2 Do computers worry you? 3 Why do you mention computers?
	 * 4 5 keywords i dreamed 6 Really, <3>? 7 Have you ever fantasized <3> while
	 * you were awake? 8 9 Have you ever dreamed <3> before?
	 *
	 * In line 1 is a single keyword "computer" followed by two possible responses
	 * on lines 2 and 3. Line 4 and 8 are ignored since they are blank (contain only
	 * whitespace). Line 5 begins new keywords that are the words "i" and "dreamed".
	 * This keyword list is followed by three possible responses on lines 6, 7 and
	 * 9.
	 * 
	 * The keywords and associated responses are each stored in their own ArrayList.
	 * The response table is an ArrayList of the keyword and responses lists. For
	 * every keywords list there is an associated response list. They are added in
	 * pairs into the list that is returned. There will always be an even number of
	 * items in the returned list.
	 * 
	 * Note that in the event an IOException occurs when trying to read the file
	 * then an error message "Error reading <fileName>", where <fileName> is the
	 * parameter, is printed and a non-null reference is returned, which may or may
	 * not have any elements in it.
	 * 
	 * @param fileName The name of the file to read
	 * @return The response table
	 */
	public static ArrayList<ArrayList<String>> loadResponseTable(String fileName) {
		Scanner sc;
		ArrayList<String> currResponses = new ArrayList<>();
		ArrayList<String> currLine = new ArrayList<String>();
		ArrayList<ArrayList<String>> responseTable = new ArrayList<ArrayList<String>>();
		File f = new File(fileName);
		//checking to see if the file is available
		//if not catches the exception
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Error reading " + fileName);
			return responseTable;
		}
		//iterates through the file
		while (sc.hasNextLine()) {
			String str = sc.nextLine();
			//trims any whitespace
			if (str.trim().isEmpty()) {
				continue;
			}
			int index = str.indexOf("keywords");
			//checks if keywords is in the phrase
			//if not, index will be -1
			if (index == 0) {
				if (responseTable.isEmpty()) {
				} else {
					responseTable.add(currResponses);
					currResponses = new ArrayList<String>();
				}

				//trimming the "keyword " to get the actual keyword
				String line = str.substring(9);
				String[] keywordsArr = line.split(" ");
				//adding the keyword to an array
				for (int i = 0; i < keywordsArr.length; i++) {
					currLine.add(keywordsArr[i]);
				}
				//adding it to the responseTable
				responseTable.add(currLine);
				currLine = new ArrayList<String>();
				continue;

			} else {
				// adding reponses to a temp array
				currResponses.add(str);

			}

		}
		//adding responses to a spot in an array
		responseTable.add(currResponses);
		return responseTable;
	}

	/**
	 * Checks to see if the keywords match the sentence. In other words, checks to
	 * see that all the words in the keyword list are in the sentence and in the
	 * same order. If all the keywords match then this method returns an array with
	 * the unmatched words before, between and after the keywords. If the keywords
	 * do not match then null is returned.
	 * 
	 * When the phrase contains elements before, between, and after the keywords,
	 * each set of the three is returned in its own element String[] keywords =
	 * {"i", "dreamed"}; String[] phrase = {"do", "you", "know", that", "i", "have",
	 * "dreamed", "of", "being", "an", "astronaut"};
	 * 
	 * toReturn[0] = "do you know that" toReturn[1] = "have" toReturn[2] = "of being
	 * an astronaut"
	 * 
	 * In an example where there is a single keyword, the resulting List's first
	 * element will be the the pre-sequence element and the second element will be
	 * everything after the keyword, in the phrase String[] keywords = {"always"};
	 * String[] phrase = {"I", "always", "knew"};
	 * 
	 * toReturn[0] = "I" toReturn[1] = "knew"
	 * 
	 * In an example where a keyword is not in the phrase in the correct order, null
	 * is returned. String[] keywords = {"computer"}; String[] phrase = {"My","dog",
	 * "is", "lost"};
	 * 
	 * return null
	 * 
	 * @param keywords The words to match, in order, in the sentence.
	 * @param phrase   Each word in the sentence.
	 * @return The unmatched words before, between and after the keywords or null if
	 *         the keywords are not all matched in order in the phrase.
	 */
	public static String[] findKeyWordsInPhrase(ArrayList<String> keywords, String[] phrase) {
		// see the algorithm presentation linked in Eliza.pdf.
		String[] unmatched = new String[keywords.size() + 1];
		String str = "";
		int i = 0;
		int j = 0;
		int k = 0;
		//iterating through the index of phrase
		for (i = 0; i < phrase.length; i++) {
			//also contains the iteration to the length of keywords given
			if (j == keywords.size()) {
				//used for the first iteration so there is no " " before
				//the phrase in unmatched array
				if (str.isEmpty()) {
					str = phrase[i];
					unmatched[k] = str;
				} 
				// adds the phrase with a space to an array
				else {
					unmatched[k] += " " + phrase[i];
				}
			} 
			//checks if the phrase is a keyword
			//if yes ends the str and adds the str to the array
			else if (phrase[i].equals(keywords.get(j))) {
				unmatched[k] = str;
				str = "";
				j++;
				k++;

			} 
			else {
				if (str.isEmpty()) {
					str = phrase[i];
				} else {
					str = str + " " + phrase[i];
				}
			}

		}
		//prevents from any inputs that may cause errors
		if (j != keywords.size()) {
			return null;
		}
		/*if (unmatched.length == 1 && unmatched[1].isEmpty() || j != keywords.size()) {
			return null;
		}*/

		if (unmatched[unmatched.length - 1] == null) {
			unmatched[unmatched.length - 1] = "";
		}
		return unmatched;
	}

	/**
	 * Selects a randomly generated response within the list of possible responses
	 * using the provided random number generator where the number generated
	 * corresponds to the index of the selected response. Use Random nextInt(
	 * responseList.size()) to generate the random number. If responseList is null
	 * or 0 length then return null.
	 * 
	 * @param rand         A random number generator.
	 * @param responseList A list of responses to choose from.
	 * @return A randomly selected response
	 */
	public static String selectResponse(Random rand, ArrayList<String> responseList) {
		if (responseList == null || responseList.size() == 0) {
			return null;
		} 
		//chooses a random response
		else {
			int i = rand.nextInt(responseList.size());
			String rspChosen = responseList.get(i).toString();
			return rspChosen;
		}

	}

	/**
	 * This method takes processed user input and forms a response. This looks
	 * through the response table in order checking to see if each keyword pattern
	 * matches the userWords. The first matching keyword pattern found determines
	 * the list of responses to choose from. A keyword pattern matches the
	 * userWords, if all the keywords are found, in order, but not necessarily
	 * contiguous. This keyword matching is done by findKeyWordsInPhrase method. See
	 * the findKeyWordsInPhrase algorithm in the Eliza.pdf.
	 * 
	 * If no keyword pattern matches then Config.NO_MATCH_RESPONSE is returned.
	 * Otherwise one of possible responses for the matched keywords is selected with
	 * selectResponse method. The response selected is checked for the replacement
	 * symbol <n> where n is 1 to the length of unmatchedWords array returned by
	 * findKeyWordsInPhrase. For each replacement symbol the corresponding unmatched
	 * words element (index 0 for <1>, 1 for <2> etc.) has its pronouns swapped with
	 * swapWords using Config.PRONOUN_MAP and then replaces the replacement symbol
	 * in the response.
	 * 
	 * @param userWords     using input after preparing.
	 * @param rand          A random number generator.
	 * @param responseTable A table containing a list of keywords and response
	 *                      pairs.
	 * @return The generated response
	 */
	public static String prepareResponse(String[] userWords, Random rand, ArrayList<ArrayList<String>> responseTable) {
		String[] findKeyWord = null;
		ArrayList<String> responses = null;
		// Iterate through the response table for keywords
		for (int i = 0; i < responseTable.size(); i += 2) {
			//creates an array of just keywords
			ArrayList<String> words = responseTable.get(i);
			findKeyWord = findKeyWordsInPhrase(words, userWords);
			if (findKeyWord != null) {
				responses = responseTable.get(i + 1);
				break;
			}

		}
		if (findKeyWord == null) {
			return Config.NO_MATCH_RESPONSE;
		}
		
		//int index = rand.nextInt(responses.size());
		String outResponse = selectResponse(rand, responses);
		//replaces through any of the <n> 's in a phrase
		for (int i = 0; i < 5; i++) {
			String s = "<" + i + ">";
			//replaces <n> if found
			if (outResponse.contains(s)) {
				String unmatched = findKeyWord[i - 1];
				outResponse = outResponse.replaceAll(s, swapWords(unmatched, Config.PRONOUN_MAP));
			}
		}
		return outResponse;

		// The response table has paired rows. The first row is a list of key
		// words, the next a list of corresponding responses. The 3rd row another
		// list of keywords and 4th row the corresponding responses.

		// checks to see if the current keywords match the user's words
		// using findKeyWordsInPhrase.

		// if no keyword pattern was matched, return Config.NO_MATCH_RESPONSE
		// else, select a response using the appropriate list of responses for the
		// keywords

		// Look for <1>, <2> etc in the chosen response. The number starts with 1 and
		// there won't be more than the number of elements in unmatchedWords returned by
		// findKeyWordsInPhrase. Note the number of elements in unmatchedWords will be
		// 1 more than the number of keywords.
		// For each <n> found, find the corresponding unmatchedWords phrase (n-1) and
		// swap
		// its pronoun words (swapWords using Config.PRONOUN_MAP). Then use the
		// result to replace the <n> in the chosen response.

		// in the selected echo, swap pronouns

		// inserts the new phrase with pronouns swapped, into the response

	}

	/**
	 * Creates a file with the given name, and fills that file line-by-line with the
	 * tracked conversation. Every line ends with a newline. Throws an IOException
	 * if a writing error occurs.
	 * 
	 * @param dialog   the complete conversation
	 * @param fileName The file in which to write the conversation
	 * @throws IOException
	 */
	public static void saveDialog(ArrayList<String> dialog, String fileName) throws IOException {
		PrintWriter pw;
		//checks if file can be found
		try {
			pw = new PrintWriter(fileName);
		} 
		//catches an exception if file does not exist
		catch (FileNotFoundException e) {
			throw new IOException();
		}
		//puts the string to the file
		for (String dialogLine : dialog) {
			pw.println(dialogLine);
		}
		pw.close();
	}
}