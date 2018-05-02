package com.n26.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import com.n26.service.dto.TransactionRequestDTO;

public class TransactionData {

	private static TransactionData transactionDataInstance = null;
	
	private TreeMap<Long, List<Double>> allTransactionsData = null;
	
	private TransactionData() {
		
		super();
		allTransactionsData = new TreeMap<Long, List<Double>>();
	}
	
	public static TransactionData getInstance() {
		
		if (transactionDataInstance == null) {
			
			transactionDataInstance = new TransactionData();
		}
		
		return transactionDataInstance;
	}
	
	public synchronized Collection<List<Double>> getTransactionAmountsForTimeperiod(Long startTime, Long endTime) {
		
		return allTransactionsData.subMap(startTime, true, endTime, true).values();
		
	}
	
	public synchronized void putTransactionData(TransactionRequestDTO transactionData) {
		
		List<Double> transactionAmounts = allTransactionsData.get(transactionData.getTimestamp());
		
		if (transactionAmounts == null) {
			
			List<Double> transactionAmount = new ArrayList<>();
			transactionAmount.add(transactionData.getAmount());
			
			allTransactionsData.put(transactionData.getTimestamp(), transactionAmount);
		} else {
			
			transactionAmounts.add(transactionData.getAmount());
		}
		
	}
}
