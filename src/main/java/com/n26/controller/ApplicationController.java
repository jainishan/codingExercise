package com.n26.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.n26.service.dto.StatisticsResponseDTO;
import com.n26.service.dto.TransactionRequestDTO;
import com.n26.service.implementation.TransationServiceImpl;

@Controller
@EnableAutoConfiguration
public class ApplicationController {
	
	TransationServiceImpl transactionService = new TransationServiceImpl();
	

	@RequestMapping(value = "/statistics", produces = "application/json")
    @ResponseBody
    public ResponseEntity<StatisticsResponseDTO> statistics() {
        
		return new ResponseEntity<StatisticsResponseDTO>(transactionService.getStatistics(), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/transactions", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> transactions(@RequestBody TransactionRequestDTO transactionRequest) {
		
		HttpStatus statusCode;
		
		if (transactionService.addTransaction(transactionRequest)) {
			
			statusCode = HttpStatus.CREATED;
			
		} else {
			
			statusCode = HttpStatus.NO_CONTENT;
			
		}
		
        return ResponseEntity.status(statusCode).build();
    }
}
