/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private int wordLength;



    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordLength = DEFAULT_WORD_LENGTH;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            // Initializes the hashmap that contains the length of each word
            if (sizeToWords.containsKey(word.length())) {
                ArrayList<String> list = new ArrayList<String>();
                list = sizeToWords.get(word.length());
                list.add(word);
                sizeToWords.put(word.length(), list);
            }
            else {
                ArrayList<String> list2 = new ArrayList<String>();
                list2.add(word);
                sizeToWords.put(word.length(), list2);
            }

            wordList.add(word);
            wordSet.add(word);
            // As you process words call helper method to sort
            // Check whether the hashmap already contains an entry for that key
            String sortedStr = sortLetters(word);
            if (lettersToWord.containsKey(sortedStr)) {
                // get the arraylist (value) and add the word into that list
                ArrayList<String> list1 = lettersToWord.get(sortedStr);
                list1.add(word);    // Add to the new word to the list
                lettersToWord.put(sortedStr, list1);    // put the string, ArrayList pair
            }
            else {
                ArrayList<String> list2 = new ArrayList<String>();
                list2.add(word);
                lettersToWord.put(sortedStr, list2);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word)) {
            if (!wordSet.contains(base)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        // need to call helper function to sort the given word
        String sortString = sortLetters(targetWord);    // Calls helper function

        for (String temp : wordList) {
            // need to compare the targetWord to every word in Dic
            // If they are anagrams add it to the result ArrayList
            String tmpSort = sortLetters(temp); // Sort the current word in Dic
            // Now you need to compare if they are anagrams
            if (sortString.length() == tmpSort.length()) {
                // Enter if statement IFF they are the same length
                int count = 0;
                for (int i = 0; i < sortString.length(); i++) {
                    // This for loop goes and checks each letter if they are the same
                    if (sortString.substring(i, i+1).equals(tmpSort.substring(i, i+1))) {
                        // If they are the same characters at same index
                        count++;
                    }
                }
                if (count == temp.length()) {
                    result.add(temp);
                }
            }
        }
        return result;
    }

    private String sortLetters(String string) {
        // need to sort the word and return it
        char tmp[] = string.toCharArray();
        Arrays.sort(tmp);
        return new String(tmp);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        char c;
        for (c='a'; c <= 'z'; c++) {
            String str = sortLetters(word);
            if (lettersToWord.containsKey(sortLetters(word + c))) {
                list = lettersToWord.get(sortLetters(word+c));
                result.addAll(list);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        // Gets all words in the list of length "wordLength"
        ArrayList<String> list = sizeToWords.get(wordLength);
        int randomNum;
        int num = 0;

        int sizeOfList = list.size();

        while (num < MIN_NUM_ANAGRAMS) {
            randomNum = random.nextInt(sizeOfList);
            String randWord = list.get(randomNum);

            num = getAnagramsWithOneMoreLetter(randWord).size();
            if (num >= MIN_NUM_ANAGRAMS) {
                if (wordLength < MAX_WORD_LENGTH) {
                    wordLength++;
                }
                return randWord;
            }
        }
        return "stop";
    }
}