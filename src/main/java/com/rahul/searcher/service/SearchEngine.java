package com.rahul.searcher.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.rahul.searcher.dto.DocumentDTO;

@Service
@Component
public class SearchEngine {

	private final Logger log = LoggerFactory.getLogger(SearchEngine.class);

	private String indexDirectoryPath;
	
	@Autowired
	private Environment env;

	private String[] fields;

	private int totalResults;

	public Collection<DocumentDTO> search(String query) {
		log.info("Starting the search for {}", query);
		List<DocumentDTO> result = new ArrayList<>();
		Directory indexDirectory;
		
		try {
			indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
			log.debug("The indexDirectory is {}");
			IndexReader reader = DirectoryReader.open(indexDirectory);
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();
			MultiFieldQueryParser qp = new MultiFieldQueryParser(fields, analyzer);
			Query query2 = qp.parse(query);
			log.debug("The query created is {}", query2);
			TopDocs topDocs = searcher.search(query2, totalResults);
			for (ScoreDoc doc : topDocs.scoreDocs) {
				log.info("The doc matched is {}",doc.toString());
				result.add(convertDocTDocumentDTO(doc));
			}

		} catch (Exception e) {
			log.error("There was an error Searching the docments indexed at {}", indexDirectoryPath, e);
		}
		return result;
	}
	
	@PostConstruct
	public void init(){
		indexDirectoryPath = env.getProperty("search.index.path");
		File indexFile = new File(indexDirectoryPath);
		if (!indexFile.exists()) {
			indexFile.mkdirs();
			log.info("The index location is {}", indexFile.getAbsolutePath());
		}
		
	}

	private DocumentDTO convertDocTDocumentDTO(ScoreDoc doc) {
		Map<String, String> data = new HashMap<>();
		
		return new DocumentDTO(doc.score, data, doc.toString());
	}

}
