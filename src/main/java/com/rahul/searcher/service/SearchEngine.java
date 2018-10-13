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
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
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
			MultiFieldQueryParser qp = new MultiFieldQueryParser(SConstants.feilds, analyzer);
			Query query2 = qp.parse(query.toLowerCase());
			log.info("The query created is {}", query2);
			totalResults = totalResults > 0 ? totalResults : 1000;
			TopDocs topDocs = searcher.search(query2, totalResults);
			for (ScoreDoc scDoc : topDocs.scoreDocs) {
				log.info("The doc matched is {}", scDoc.toString());
				result.add(convertDocTDocumentDTO(searcher.doc(scDoc.doc),scDoc.score));
			}

		} catch (Exception e) {
			log.error("There was an error Searching the docments indexed at {}", indexDirectoryPath, e);
		}
		log.info("Completed search for {}",query);
		return result;
	}

	@PostConstruct
	public void init() {
		indexDirectoryPath = env.getProperty("search.index.path");
	}

	private DocumentDTO convertDocTDocumentDTO(Document document, float score) {
		Map<String, String> data = new HashMap<>();
		StringBuffer fieldResult = new StringBuffer();
		for (IndexableField field : document.getFields()) {
			fieldResult.append(field.name()).append(field.stringValue());
			data.put(field.name(), field.stringValue());
			log.info("The value is {} : {}",field.name(),field.stringValue());
		}
		return new DocumentDTO(score, data, fieldResult.toString());
	}

}
