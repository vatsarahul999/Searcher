package com.rahul.searcher.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.searcher.dto.DocumentDTO;
import com.rahul.searcher.service.ReRankerEnginer;
import com.rahul.searcher.service.SearchEngine;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@RestController
@Api(value="search", description="Different Kinds of search")
@RequestMapping("/")
public class ServerController {
	
	@Autowired
	private SearchEngine sEngine;
	
	@Autowired
	private ReRankerEnginer reRankerEnginer;
	
	
	/**
	 * @param query
	 * This uses the standard search algorithm.
	 * @return
	 */
	@RequestMapping(value = "/search/{query}",method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Orginal search without Re-Ranking. ")
	public Collection<DocumentDTO> getSearchResult(@PathVariable String query){
		return sEngine.search(query);
	}
	
	/**
	 * @param query
	 * This uses the re-ranking algorithm to search.
	 * @return
	 */
	@RequestMapping(value = "/search/reranking/{query}",method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Orginal search with Re-Ranking. ")
	public Collection<DocumentDTO> getReRankingResult(@PathVariable String query){
		return reRankerEnginer.search(query);
	}
	

	
}
