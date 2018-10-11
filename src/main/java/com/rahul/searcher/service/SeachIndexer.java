package com.rahul.searcher.service;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SeachIndexer {
	private final Logger log = LoggerFactory.getLogger(SeachIndexer.class);
	
	public void index(String pathToFile){
		log.info("Indexing file {}",pathToFile);
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		IndexWriter w =null;
		Document doc = new Document();
//		  doc.add(new TextField("title", title, Field.Store.YES));
//		  doc.add(new StringField("isbn", isbn, Field.Store.YES));
		  try {
			 w= new IndexWriter(index, config);
			w.addDocument(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
