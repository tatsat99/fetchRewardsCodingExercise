package com.fetchrewards.codingexercise.payertransaction.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fetchrewards.codingexercise.exception.InvalidPointException;
import com.fetchrewards.codingexercise.payertransaction.model.PayerTransaction;
import com.fetchrewards.codingexercise.payertransaction.model.SpendPointsRequest;
import com.fetchrewards.codingexercise.payertransaction.service.PayerTransactionService;

@Controller
public class PayerTransactionController {
	private PayerTransactionService payerTransactionService;

	@Autowired
	public PayerTransactionController(PayerTransactionService payerTransactionService) {
		this.payerTransactionService = payerTransactionService;
	}

	/**
	 * Endpoint to add payer transactions
	 * @param payerTransaction
	 * @return
	 */
	@PostMapping("/addPayer")
	@ResponseBody
	public ResponseEntity<?> addPayer(@RequestBody PayerTransaction payerTransaction) {
		try {
			payerTransactionService.addPayerList(payerTransaction);
			return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Endpoint to get current points balance of each payer
	 * 
	 * @return Map<String, Long> containing PayerName -> Points key-pair
	 */
	@GetMapping("/payerPoints")
	@ResponseBody
	public Map<String, Long> getPayerPoints() {
		return payerTransactionService.getTotalPayerPoints();
	}

	/**
	 * Endpoint to spend points according to the above rules
	 * @param points
	 * @return
	 */
	@PostMapping("/spendPoints")
	@ResponseBody
	public ResponseEntity<?> getIndividualSpendingPoints(@RequestBody SpendPointsRequest points) {
		try {
			var resp = payerTransactionService.spendPoints(points.getPoints());
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (InvalidPointException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
