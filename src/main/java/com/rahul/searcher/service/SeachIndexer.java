package com.rahul.searcher.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

@Service
public class SeachIndexer {
	private final Logger log = LoggerFactory.getLogger(SeachIndexer.class);

	public void index(File indexFile) {
		log.info("Indexing file {}", indexFile);
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = null;
		try {
			index = FSDirectory.open(indexFile.toPath());
		} catch (Exception e) {
			log.error("There was an error indexing files", e);
		}

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE);
		config.setRAMBufferSizeMB(2048.0);
		config.setRAMPerThreadHardLimitMB(512);
		IndexWriter w = null;
		try {
			try (ICsvMapReader listReader = new CsvMapReader(new FileReader(indexFile.getAbsolutePath()),
					CsvPreference.STANDARD_PREFERENCE)) {
				// First Column is header names
				final String[] headers = listReader.getHeader(true);
				final CellProcessor[] processors = getProcessors();
				w = new IndexWriter(index, config);
				Map<String, Object> fieldsInCurrentRow;
				while ((fieldsInCurrentRow = listReader.read(headers, processors)) != null) {
					w.addDocument(convertToDocument(fieldsInCurrentRow));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private CellProcessor[] getProcessors() {
		List<CellProcessor> list = new ArrayList<>();
		for (String fString : SConstants.feilds)
			list.add(new Optional());
		return (CellProcessor[]) list.toArray();
	}

	private Iterable<? extends IndexableField> convertToDocument(Map<String, Object> fieldsInCurrentRow) {
		Document document =  new Document();
		for(String key: SConstants.feilds)
		document.add(new StringField(key, fieldsInCurrentRow.get(key).toString(), Field.Store.YES));
		return document;
	}
}
