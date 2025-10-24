package com.example.resource.upi;

import com.example.resource.mandates.MandateService;
import com.example.resource.mandates.dto.CreateMandateCommand;
import com.example.resource.mandates.dto.Mandate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class MandateAdapterService implements MandateService {

    private final RestClient restClient;
    private final String createMandateUri;

    public MandateAdapterService(
            RestClient restClient,
            @Value("${upi.auto-pay.mandate.create}") String createMandateUri) {
        this.restClient = restClient;
        this.createMandateUri = createMandateUri;
    }

    @Override
    public Mandate createMandate(CreateMandateCommand createMandateCommand) {
        var createCommand = CreateMandateAdapterCommand.from(createMandateCommand);
        return restClient.post()
                .uri(createMandateUri)
                .contentType(APPLICATION_JSON)
                .body(createCommand)
                .retrieve()
                .body(Mandate.class);
    }
}
