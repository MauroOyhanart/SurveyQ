package com.maurooyhanart.surveyq.logging.log;

import com.maurooyhanart.surveyq.shared.log.LogRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<String> receiveLog(@Valid @RequestBody LogRequest logRequest) {
        logService.processLog(logRequest);
        return ResponseEntity.ok("Noted");
    }
}
