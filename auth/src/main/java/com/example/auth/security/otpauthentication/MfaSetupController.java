package com.example.auth.security.otpauthentication;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

/**
 * @author Arfat A. Chaus
 * since 2025-10-24
 */
@RestController
public class MfaSetupController {

    private static final Logger log = LoggerFactory.getLogger(MfaSetupController.class);

    private final SecretGenerator secretGenerator;
    private final QrDataFactory qrDataFactory;
    private final QrGenerator qrGenerator;
    private final CodeVerifier verifier;

    //TODO: remove strore in user object, use /otp/setup
    private final String secret = "6ESDDL72AIF7TNDRGHDJKESQPNOUPPC2";

    public MfaSetupController(SecretGenerator secretGenerator, QrDataFactory qrDataFactory, QrGenerator qrGenerator, CodeVerifier verifier) {
        this.secretGenerator = secretGenerator;
        this.qrDataFactory = qrDataFactory;
        this.qrGenerator = qrGenerator;
        this.verifier = verifier;
    }

    @GetMapping("/otp/setup")
    public ResponseEntity<String> setupDevice() throws QrGenerationException {

        //TODO: save secret in user DB
        //var secret = secretGenerator.generate();

        QrData data = qrDataFactory.newBuilder()
                .label("example@example.com")
                .secret(secret)
                .issuer("Auth App")
                .build();

        String qrCodeImage = getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType()
        );
        return ResponseEntity.ok(qrCodeImage);
    }

    //TODO: testing only
    @GetMapping("/otp/verify")
    public String verify(@RequestParam String code) {
        // secret is fetched from some storage

        if (verifier.isValidCode(secret, code)) {
            return "CORRECT CODE";
        }

        return "INCORRECT CODE";
    }
}