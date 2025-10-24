package com.example.resource.mandates;

import com.example.resource.mandates.dto.CreateMandateCommand;
import com.example.resource.mandates.dto.CreateMandateResponse;
import com.example.resource.mandates.dto.Mandate;
import com.example.resource.qr.QRCodeGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/mandates")
public class MandateController {

    private static final String DEFAULT_LOGO_PATH = "classpath:static/logo.png";
    private static final String JSON = """
            {
            	"id": "68ee552ba98230c643e9a7eb",
            	"amount": 1000,
            	"amount_without_charges": 1000,
            	"bearer": "MERCHANT",
            	"discount_id": null,
            	"charge_id": "a0ed2d67-099c-47ec-9995-8c0ed6ed1c72",
            	"installment_amount": 100,
            	"frequency": "daily",
            	"installments": 10,
            	"product": "subscription",
            	"mandate_url": "https://checkout-staging.rocketpay.co.in/md/68ee552ba98230c643e9a7eb",
            	"payment_details": {
            		"method": "upi"
            	},
            	"customer": {
            		"account_id": "00000199-e2bc-2649-83d4-e648d88fcead",
            		"mobile_number": "+918149730316",
            		"name": "Arfat"
            	},
            	"merchant": "00000199-c504-0507-a9c2-7975e40929a1",
            	"merchant_info": {
            		"id": "00000199-c504-0507-a9c2-7975e40929a1",
            		"name": null,
            		"mobile_number": "+918149730316",
            		"created_at": 1759946999047
            	},
            	"description": "Swarajy Urban",
            	"amount_remaining": 1000,
            	"installments_paid": 0,
            	"next_charge_at": 1761998400000,
            	"gateway_mandate_id": "00000199-e2fc-b552-873c-7fc57c5d358e",
            	"start_at": 1761998400000,
            	"end_at": 1770552000000,
            	"created_at": 1760449835852,
            	"updated_at": 1760461423334,
            	"is_deleted": false,
            	"state": "active",
            	"status": "active",
            	"original_amount": 1000,
            	"meta": {
            		"cbAccountNumber": "100043000000001",
            		"txns": [
            			{
            				"id": "00000199-e2fc-b5e5-ae8e-fa028c951cdc",
            				"utr": null,
            				"meta": {
            					"QR": "https://payments-test.cashfree.com/subs-checkout/checkout/simulator/UPI/5F9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.K9QfiwGb15mI6ISZwlHV05WZtlXYwJCL3MDOxQDMzYzNxojIwhXZiwiIsxWduJiOiQWSuFGbwJCL5kDN0cTM6ICZJRnbhh2YyVWbiwSM2ADM0QTM6ICZJV2YuVmclZWZSJWdzJye.vJ7LfKEJIcfBlq_W6BsKZc5vk_JQnGkEzniTvb3fNviUyv8Zxkudw7NSERUvUigt5-?auth_id=888790",
            					"umrn": "tI24vPZfwcRzVVHKLVpln81JDPhu9o@upi",
            					"token": "sub_session_8Y7NLv8Pyza8bZ32d5zRo3aZzYgcGAzpBCiK5nWKgCLufQD9kIMWaCE1MDWe9SuxPi4mt24A66x1hM-urmJ_povx98MWfUv0ToYpayment",
            					"sub_state": null,
            					"gateway_name": "CASHFREE",
            					"gateway_reference_id": "1440061"
            				},
            				"state": "SUCCESS",
            				"medium": "QR",
            				"created_at": 1760449836549,
            				"generic_error": {
            					"code": "",
            					"meta": null,
            					"origin": null,
            					"status": null,
            					"message": "",
            					"generic_error": null,
            					"custom_message": ""
            				}
            			},
            			{
            				"id": "00000199-e3ac-9f8f-bbdf-b3b8c812f99c",
            				"utr": null,
            				"meta": {
            					"QR": "https://payments-test.cashfree.com/subs-checkout/checkout/simulator/UPI/yx9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.uRQfiwGb15mI6ISZwlHV05WZtlXYwJCL1YzMzUDMzYzNxojIwhXZiwiIsxWduJiOiQWSuFGbwJCL5kDN0cTM6ICZJRnbhh2YyVWbiwSM1ETM0QTM6ICZJV2YuVmclZWZSJWdzJye.9GSdWJBd3apPqZ3jb5wIYBAXpN32cMlzkWKNt78QZjjM1gCyYN2JzZBarbxtaH0Ln5?auth_id=889454",
            					"umrn": null,
            					"token": "sub_session_P2D_6agunNXxDHUB5C6qxzl3T66oYZCLhr7FJjRgHULKB4Bj3k2wVsMwlAp9J-VI8BgRunEFuMwngsE2nSGh70ZW3J98TZPRVGcpayment",
            					"sub_state": null,
            					"gateway_name": "CASHFREE",
            					"gateway_reference_id": "1441151"
            				},
            				"state": "IN_PROGRESS",
            				"medium": "LINK",
            				"created_at": 1760461365235,
            				"generic_error": null
            			}
            		],
            		"umrn": "tI24vPZfwcRzVVHKLVpln81JDPhu9o@upi",
            		"medium": "QR",
            		"charges": {
            			"amount": 1000,
            			"bearer": "MERCHANT",
            			"charges": [
            				{
            					"type": "MERCHANT",
            					"charges": 141.6,
            					"discount": 141.6
            				}
            			],
            			"charge_id": "a0ed2d67-099c-47ec-9995-8c0ed6ed1c72",
            			"tokens_consumed": 10,
            			"show_at_mandate_level": false,
            			"amount_without_charges": 1000,
            			"per_installment_amount": 100,
            			"per_installment_charges": [
            				{
            					"type": "MERCHANT",
            					"charges": 14.16,
            					"discount": 14.16
            				}
            			],
            			"charges_at_mandate_level": false,
            			"is_bearer_control_available": false,
            			"customer_charges_at_mandate_level": false,
            			"merchant_charges_at_mandate_level": false,
            			"per_installment_amount_without_charges": 100
            		},
            		"auth_meta": {
            			"QR": "https://payments-test.cashfree.com/subs-checkout/checkout/simulator/UPI/5F9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.K9QfiwGb15mI6ISZwlHV05WZtlXYwJCL3MDOxQDMzYzNxojIwhXZiwiIsxWduJiOiQWSuFGbwJCL5kDN0cTM6ICZJRnbhh2YyVWbiwSM2ADM0QTM6ICZJV2YuVmclZWZSJWdzJye.vJ7LfKEJIcfBlq_W6BsKZc5vk_JQnGkEzniTvb3fNviUyv8Zxkudw7NSERUvUigt5-?auth_id=888790"
            		},
            		"gateway_name": "CASHFREE",
            		"customer_name": "Arfat",
            		"generic_error": {
            			"code": "",
            			"meta": null,
            			"origin": null,
            			"status": null,
            			"message": "",
            			"generic_error": null,
            			"custom_message": ""
            		},
            		"self_checkout": {
            			"QR": {
            				"auth_data": {
            					"qr_data": "https://payments-test.cashfree.com/subs-checkout/checkout/simulator/UPI/5F9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.K9QfiwGb15mI6ISZwlHV05WZtlXYwJCL3MDOxQDMzYzNxojIwhXZiwiIsxWduJiOiQWSuFGbwJCL5kDN0cTM6ICZJRnbhh2YyVWbiwSM2ADM0QTM6ICZJV2YuVmclZWZSJWdzJye.vJ7LfKEJIcfBlq_W6BsKZc5vk_JQnGkEzniTvb3fNviUyv8Zxkudw7NSERUvUigt5-?auth_id=888790"
            				}
            			}
            		},
            		"enterprise_info": {
            			"id": "00000199-be38-d8f7-8101-712d3e948df8",
            			"preferences": {
            				"tnc_url": "https://www.rocketpay.co.in/termsofuse",
            				"support_number": null,
            				"skip_checkout_consent": false,
            				"enterprise_handler_entity": null
            			},
            			"enterprise_handler_entity": null
            		},
            		"product_order_id": "00000199-e2fc-b930-b0d9-747c577de910",
            		"gateway_reference_id": "1440061"
            	},
            	"actions": [
            		{
            			"id": "cancel",
            			"label": "Cancel",
            			"isEnabled": true
            		},
            		{
            			"id": "delete",
            			"label": "Delete",
            			"isEnabled": false
            		}
            	],
            	"reference_id": null,
            	"reference_type": "MAIN"
            }
            """;
    private final ObjectMapper objectMapper;
    private final MandateService mandateService;

    public MandateController(ObjectMapper objectMapper, MandateService mandateService) {
        this.objectMapper = objectMapper;
        this.mandateService = mandateService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mandate> getMandateById(@PathVariable("id") String mandateId) throws JsonProcessingException {
        var response = objectMapper.readValue(JSON, Mandate.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMandates(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                                              @RequestParam(value = "limit", required = false) Optional<Integer> limit) throws JsonProcessingException {
        var pageIndex = page.orElse(0);
        var perPage = limit.orElse(10);

        List<Mandate> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            var madate = objectMapper.readValue(JSON, Mandate.class);
            madate.setId(String.valueOf(i));
            data.add(madate);
        }
        var map = Map.of("data", data, "page", pageIndex, "limit", perPage, "total", 50);
        return ResponseEntity.ok(map);
    }

    @PostMapping
    public ResponseEntity<CreateMandateResponse> createMandate(@RequestBody CreateMandateCommand createMandateCommand) {
//        var response = mandateService.createMandate(createMandateCommand);
        //TODO: Remove mock response

        CreateMandateResponse res = null;
        try {
            var response = objectMapper.readValue(JSON, Mandate.class);

            String mandateDeepLink = "upi://mandate?ver=01&pa=rocketpayco.cfp@cashfreensdlpb&pn=Swarajy%20Urban%20-%20Finsolish&tr=enroll36900267&am=1250.00&cu=INR&amrule=MAX&mode=04&recur=ASPRESENTED&validitystart=11102025&validityend=05022026&mc=5817&orgid=000000&txnType=CREATE&tn=Cashfree%20Payments&fam=5.00&purpose=14&block=N&rev=Y";

            res = CreateMandateResponse.builder()
                    .mandateId(response.getId())
                    .mandateDeepLink(mandateDeepLink)
                    .qrCode(QRCodeGenerator.generateQRCodeAsDataURL(mandateDeepLink, DEFAULT_LOGO_PATH)
                    )
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(res);
    }


}
