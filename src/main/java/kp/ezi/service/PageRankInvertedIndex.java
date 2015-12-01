package kp.ezi.service;

import ir.vsr.DocumentIterator;
import ir.vsr.InvertedIndex;

import java.io.File;
import java.util.Vector;

/**
 * Created by Krzysztof on 30.11.2015.
 */
public class PageRankInvertedIndex extends InvertedIndex {
	public PageRankInvertedIndex(File dirFile, short docType, boolean stem, boolean feedback) {
		super(dirFile, docType, stem, feedback);
	}

	public static void main(String[] args) {
		// Parse the arguments into a directory name and optional flag
		String dirName = args[args.length - 1];
		short docType = DocumentIterator.TYPE_TEXT;
		boolean stem = false, feedback = false;
		for(int i = 0; i < args.length - 1; i++) {
			String flag = args[i];
			if (flag.equals("-html"))
				// Create HTMLFileDocuments to filter HTML tags
				docType = DocumentIterator.TYPE_HTML;
			else if (flag.equals("-stem"))
				// Stem tokens with Porter stemmer
				stem = true;
			else if (flag.equals("-feedback"))
				// Use relevance feedback
				feedback = true;
			else {
				System.out.println("\nUnknown flag: " + flag);
				System.exit(1);
			}
		}
		// Create an inverted index for the files in the given directory.
		InvertedIndex index = new PageRankInvertedIndex(new File(dirName), docType, stem, feedback);
		 index.print();
		// Interactively process queries to this index.
		index.processQueries();
	}

}
