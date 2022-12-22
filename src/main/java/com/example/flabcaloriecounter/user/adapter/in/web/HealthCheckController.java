package com.example.flabcaloriecounter.user.adapter.in.web;

import static com.example.flabcaloriecounter.user.adapter.in.web.Constants.*;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	private static final Logger logger = Logger.getLogger(HealthCheckController.class.getName());

	@GetMapping(LATEST_API_VERSION + "/health")
	public ResponseEntity<String> healthCheck(@RequestHeader("x-session-id") final String sessionId) {
		logger.info("Health check request received. Session ID: " + sessionId);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
}
