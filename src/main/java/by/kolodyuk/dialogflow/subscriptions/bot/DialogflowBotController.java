package by.kolodyuk.dialogflow.subscriptions.bot;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
public class DialogflowBotController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DialogflowBotController.class);

    private static final String PROJECT_ID = "smsmarketpreferences";
    private static final String LANGUAGE_CODE = "en";

    @PostMapping(path = "/message", consumes = "text/plain", produces = "text/plain")
    public ResponseEntity<String> sendMessage(@RequestBody String message, @RequestHeader(value = "sessionId", required = false) String sessionId) throws IOException {
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
            sessionId = sessionId == null ? UUID.randomUUID().toString() : sessionId;
            SessionName session = SessionName.of(PROJECT_ID, sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(message).setLanguageCode(LANGUAGE_CODE);
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            QueryResult queryResult = response.getQueryResult();

            LOGGER.info("Detected intent: {} with confidence: {}", queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());

            return ResponseEntity.ok().header("sessionId", sessionId).body(queryResult.getFulfillmentText());
        }
    }

}
