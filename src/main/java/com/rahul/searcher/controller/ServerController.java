package com.rahul.searcher.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.searcher.dto.DocumenDTO;
import com.rahul.searcher.dto.IndexDTO;
import com.rahul.searcher.service.SearchEngine;


@RestController
@RequestMapping("/")
public class ServerController {
	
	@Autowired
	private SearchEngine sEngine;
	
	@RequestMapping(value = "resturl",method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public IndexDTO getHomePage(){
		IndexDTO indexDTO = new IndexDTO();
		indexDTO.setId(1);
		indexDTO.setIndex("THis is index");
		return indexDTO;
	}
	
	@RequestMapping(value = "/search/{query}",method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<DocumenDTO> getSearchResult(@PathVariable String query){
		return sEngine.search(query);
	}
	

	
}
