package com.fetchrewards.codingexercise.payertransaction.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.fetchrewards.codingexercise.exception.InvalidPointException;
import com.fetchrewards.codingexercise.payertransaction.model.PayerTransaction;

@Service
public class PayerTransactionService {
	private Map<String, Long> totalPayerPoints;
	private TreeMap<LocalDateTime, List<PayerTransaction>> sortedPayerTransactions;
	private Long totalPoints;

	public PayerTransactionService() {
		totalPayerPoints = new HashMap<String, Long>();
		sortedPayerTransactions = new TreeMap<LocalDateTime, List<PayerTransaction>>();
		totalPoints = 0l;
	}

	/**
	 * Adds transactions for a specific payer and date in our system We will store
	 * current total points for a payer in totalPayerPoints We will store a
	 * key-value sorted pair for timestamp for future convenience
	 * 
	 * @param pt
	 * @throws Exception
	 */
	public void addPayerList(PayerTransaction pt) throws Exception {
		try {
			boolean validInput = validatePayerTransaction(pt);
			if (!validInput) {
				throw new IllegalArgumentException("Invalid input was provided");
			}
			totalPayerPoints.put(pt.getPayer(), totalPayerPoints.getOrDefault(pt.getPayer(), 0l) + pt.getPoints());
			LocalDateTime timestamp = pt.getTimestamp();
			totalPoints += pt.getPoints();

			if (sortedPayerTransactions.containsKey(timestamp)) {
				sortedPayerTransactions.get(timestamp).add(pt);
			} else {
				List<PayerTransaction> payerTransactionList = new ArrayList<>();
				payerTransactionList.add(pt);
				sortedPayerTransactions.put(timestamp, payerTransactionList);
			}
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("An error occured when spending points");
		}
	}

	/**
	 * A validation check for input for addPayerList method Checks if the payer name
	 * is null or empty Checks if the timestamp is null or a date in future
	 * 
	 * @param pt
	 * @return boolean of whether or not the input is valid
	 */
	private boolean validatePayerTransaction(PayerTransaction pt) {
		if (pt.getPayer() == null || pt.getTimestamp() == null || pt.getPayer().equals(""))
			return false;
		if (pt.getTimestamp().isAfter(LocalDateTime.now()))
			return false;
		return true;
	}

	/**
	 * spendPoints() method deducts the points supposed to be spent, in an order of
	 * occurrence in timestamp history and logs for individual payers and their
	 * deducted points. The methods throws an exception if the points to be deducted
	 * is negative or more than the sum of points of all the payers.
	 * 
	 * @param points
	 * @return Map<String, Long> containing PayerName -> deducted points key-value
	 *         pair
	 */
	public Map<String, Long> spendPoints(Long points) throws Exception {
		try {
			if (points < 0 || points > totalPoints) {
				throw new InvalidPointException("Points cannot be less than 0 or more than total points");
			}

			Map<String, Long> individualPayerPoints = new HashMap<String, Long>();
			Long pointsToBeDeducted = Long.valueOf(points);

			for (LocalDateTime time : sortedPayerTransactions.keySet()) {
				for (PayerTransaction pt : sortedPayerTransactions.get(time)) {
					if (pt.getPoints() <= points) {
						points -= pt.getPoints();
						individualPayerPoints.put(pt.getPayer(),
								individualPayerPoints.getOrDefault(pt.getPayer(), 0l) + (-1 * pt.getPoints()));
						totalPayerPoints.put(pt.getPayer(), totalPayerPoints.get(pt.getPayer()) - pt.getPoints());
						pt.setPoints(0l);
					} else {
						individualPayerPoints.put(pt.getPayer(),
								individualPayerPoints.getOrDefault(pt.getPayer(), 0l) + (-points));
						pt.setPoints(pt.getPoints() - points);
						totalPayerPoints.put(pt.getPayer(), totalPayerPoints.get(pt.getPayer()) - points);
						points = 0l;
						this.totalPoints -= pointsToBeDeducted;
						return individualPayerPoints;
					}
				}
			}
			return null;
		} catch (InvalidPointException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("An error occured when spending points");
		}
	}

	/**
	 * getTotalPoints() method returns a map of payers and their corresponding
	 * current points balance
	 * 
	 * @return Map<String, Long> containing PayerName -> Points key-pair
	 */
	public Map<String, Long> getTotalPayerPoints() {
		return totalPayerPoints;
	}
}
