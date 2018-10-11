package com.rahul.searcher.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

@Service
public class SeachIndexer {
	private final Logger log = LoggerFactory.getLogger(SeachIndexer.class);
	
	private String srcFilePath;
	
	private String indexDirectoryPath;
	
	@Autowired
	private Environment env;
	

	public void index(File indexFile) {
		log.info("Indexing file {}", indexFile);
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = null;
		try {
			index = FSDirectory.open(new File(indexDirectoryPath).toPath());
		} catch (Exception e) {
			log.error("There was an error indexing files", e);
		}

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE);
		config.setRAMBufferSizeMB(2048.0);
		config.setRAMPerThreadHardLimitMB(512);
		IndexWriter w = null;
		try {
			try (ICsvMapReader listReader = new CsvMapReader(new FileReader(indexFile),
					CsvPreference.STANDARD_PREFERENCE)) {
				// First Column is header names
				final String[] headers = listReader.getHeader(true);
				final CellProcessor[] processors = getProcessors();
				w = new IndexWriter(index, config);
				Map<String, Object> fieldsInCurrentRow;
				log.info("Indexing stage 2 {}", indexFile);
				int count = 0;
				while ((fieldsInCurrentRow = listReader.read(headers, processors)) != null) {
					w.addDocument(convertToDocument(fieldsInCurrentRow));
					
					count++;
					if(count%500==0) {
						log.info("Indexed {} documents",count );
						w.commit();
					}
					
				}
				w.flush();
				log.info("The number of documents indexed is {}",count);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(w!=null)
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("Compled indexing");
	}
	@PostConstruct
	public void init(){
		srcFilePath = env.getProperty("search.index.inputFile");
		log.info("The srcFile path is {}",srcFilePath);
		indexDirectoryPath = env.getProperty("search.index.path");
		File indexFile = createIfNotExists(srcFilePath);
		createIfNotExists(indexDirectoryPath);
		index(indexFile);
		
	}
	public File createIfNotExists(String pathToFile) {
		File iFile = new File(pathToFile);
		if (!iFile.exists()) {
			iFile.mkdirs();
			log.info("The file created is  {}", iFile.getAbsolutePath());
		}
		return iFile;
	}

	private CellProcessor[] getProcessors() {
		CellProcessor[] res = new CellProcessor[SConstants.feilds.length];
		for(int i = 0;i<SConstants.feilds.length;i++){
			res[i] = new Optional();
		}
		return res;
	}

	private Iterable<? extends IndexableField> convertToDocument(Map<String, Object> fieldsInCurrentRow) {
		Document document =  new Document();
		for(String key: SConstants.feilds){
			log.info("Added data to doc {} : {}",key,fieldsInCurrentRow.get(key).toString());
		document.add(new StringField(key, fieldsInCurrentRow.get(key).toString().toLowerCase(), Field.Store.YES));
		}
		log.info("Indexed a doc");
		return document;
	}
}
