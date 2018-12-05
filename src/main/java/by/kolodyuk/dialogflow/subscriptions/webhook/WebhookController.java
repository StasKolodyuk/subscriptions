package by.kolodyuk.dialogflow.subscriptions.webhook;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class WebhookController {

    private JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody String request) throws IOException {
        GoogleCloudDialogflowV2WebhookRequest webhookRequest = jacksonFactory.fromString(request, GoogleCloudDialogflowV2WebhookRequest.class);

        GoogleCloudDialogflowV2WebhookResponse webhookResponse = new GoogleCloudDialogflowV2WebhookResponse();
        webhookResponse.setFulfillmentText("You have " + ThreadLocalRandom.current().nextInt(100) + " points");

        return jacksonFactory.toString(webhookResponse);
    }
}
