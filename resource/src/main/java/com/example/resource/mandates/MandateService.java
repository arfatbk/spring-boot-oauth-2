package com.example.resource.mandates;

import com.example.resource.mandates.dto.CreateMandateCommand;
import com.example.resource.mandates.dto.Mandate;

public interface MandateService {

    Mandate createMandate(CreateMandateCommand createMandateCommand);
}
