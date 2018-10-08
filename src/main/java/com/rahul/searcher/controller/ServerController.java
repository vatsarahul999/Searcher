package com.rahul.searcher.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.searcher.dto.IndexDTO;


@RestController
@RequestMapping("/")
public class ServerController {
	
	@RequestMapping(value = "resturl",method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public IndexDTO getHomePage(){
		IndexDTO indexDTO = new IndexDTO();
		indexDTO.setId(1);
		indexDTO.setIndex("THis is index");
		return indexDTO;
	}

	
}
