package com.n26.service.implementation;

import java.time.Instant;
import java.util.List;

import com.n26.dao.TransactionData;
import com.n26.service.dto.StatisticsResponseDTO;
import com.n26.service.dto.TransactionRequestDTO;

public class TransationServiceImpl {

	// Constant to mark the time frame this App works on
	private static final Long TIMEFRAME_IN_SECONDS = 60L;

	private TransactionData transanctionData = TransactionData.getInstance();

	public StatisticsResponseDTO getStatistics() {

		Instant thisInstance = Instant.now();
		Instant pastInstance = thisInstance.minusSeconds(TransationServiceImpl.TIMEFRAME_IN_SECONDS);

		StatisticsResponseDTO statistics = new StatisticsResponseDTO();

		
		for (List<Double> amounts : transanctionData.getTransactionAmountsForTimeperiod(pastInstance.toEpochMilli(), thisInstance.toEpochMilli())) {
			
			amounts.forEach(statistics::updateStatisticsForAmount);
		}

		System.out.println(statistics);

		return statistics;
	}

	public Boolean addTransaction(TransactionRequestDTO transactionRequest) {

		Boolean transactionAccepted = false;

		// Get difference in seconds. In case of future timestamps, result will be negative
		Long differenceInSeconds = (Instant.now().toEpochMilli() - transactionRequest.getTimestamp()) / 1000;

		
		// process only if timeperiod is within defined time frame
		if (differenceInSeconds >= 0 && differenceInSeconds <= TransationServiceImpl.TIMEFRAME_IN_SECONDS) {


			transanctionData.putTransactionData(transactionRequest);

			transactionAccepted = true;

		}

		return transactionAccepted;
	}
}
