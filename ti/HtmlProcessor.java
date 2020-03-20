// Copyright (C) 2015  Julián Urbano <urbano.julian@gmail.com>
// Distributed under the terms of the MIT License.

package ti;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A processor to extract terms from HTML documents.
 */
public class HtmlProcessor implements DocumentProcessor
{

	// P2
	// Hash set for stop-words.txt
	HashSet<String> stopwords = new HashSet<String>();
	
	/**
	 * Creates a new HTML processor.
	 *
	 * @param pathToStopWords the path to the file with stopwords, or {@code null} if stopwords are not filtered.
	 * @throws IOException if an error occurs while reading stopwords.
	 */
	public HtmlProcessor(File pathToStopWords) throws IOException
	{
		// P2
		//if(pathToStopWords!=null){
		// Load stopwords
		Scanner sc = new Scanner(pathToStopWords); 
		  
		    while (sc.hasNextLine()) 
		      this.stopwords.add(sc.nextLine());
	}
	//}
	/**
	 * {@inheritDoc}
	 */
	public Tuple<String, String> parse(String html)
	{
		// P2
		// Parse document
		Document doc = Jsoup.parse(html);
        String title = doc.title();
        String body = doc.body().text();

		Tuple<String, String> t = new Tuple<String, String>(title,body);
		return t; // Return title and body separately
	}

	/**
	 * Process the given text (tokenize, normalize, filter stopwords and stemize) and return the list of terms to index.
	 *
	 * @param text the text to process.
	 * @return the list of index terms.
	 */
	public ArrayList<String> processText(String text)
	{
		ArrayList<String> terms = new ArrayList<>();

		// P2
		// Tokenizing, normalizing, stopwords, stemming, etc. 
		
		// Normalize
		text = normalize(text);
		// Tokenize
		terms = tokenize(text);
		// remove stopwords and stem
		for (int i = 0; i < terms.size(); i++){
			if(isStopWord(terms.get(i))){
				terms.remove(i);
			}
			else{
				// replace the text with stem
				terms.set(i, stem(terms.get(i)));
			}
		}
		

		return terms;
	}

	/**
	 * Tokenize the given text.
	 *
	 * @param text the text to tokenize.
	 * @return the list of tokens.
	 */
	protected ArrayList<String> tokenize(String text)
	{
		ArrayList<String> tokens = new ArrayList<>();

		// P2
		String str[] = text.split(" ");
		Collections.addAll(tokens, str);

		return tokens;
	}

	/**
	 * Normalize the given term.
	 *
	 * @param text the term to normalize.
	 * @return the normalized term.
	 */
	protected String normalize(String text)
	{
		String normalized = null;

		// P2
		normalized = text.toLowerCase();
		// replace all non letters
		normalized = normalized.replaceAll("[^a-zA-Z ]", "");
		normalized = normalized.replaceAll("'\'w*'\'d'\'w*", "");
		normalized = normalized.replaceAll("'\'n", "");
		return normalized;
	}

	/**
	 * Checks whether the given term is a stopword.
	 *
	 * @param term the term to check.
	 * @return {@code true} if the term is a stopword and {@code false} otherwise.
	 */
	protected boolean isStopWord(String term)
	{
		boolean isTopWord = false;

		// P2
        
		// is the term a stopword 
		isTopWord = this.stopwords.contains(term);
		return isTopWord;
	}

	/**
	 * Stem the given term.
	 *
	 * @param term the term to stem.
	 * @return the stem of the term.
	 */
	protected String stem(String term)
	{
		String stem = null;

		// P2
		Stemmer s = new Stemmer();
		for (int i=0;i<term.length();i++){
			s.add(term.charAt(i));
		}
		s.stem();
		
		stem = s.toString();
		return stem;
	}
}
