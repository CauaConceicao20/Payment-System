package com.servico.sistemadepagamento.service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.servico.sistemadepagamento.dto.PixConfig;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {

    private final JSONObject options;

    public WebhookService(PixConfig pixConfig) {
        this.options = new JSONObject();
        this.options.put("client_id", pixConfig.getClientId());
        this.options.put("client_secret", pixConfig.getClientSecret());
        this.options.put("certificate", pixConfig.getCertificate());
        this.options.put("sandbox", pixConfig.isSandbox());

        options.put("x-skip-mtls-checking", "true");
    }

    public JSONObject paymentConfirm(String pixKey, String webhook) {
        Map<String, String> params = new HashMap<>();
        params.put("chave", pixKey);
        return performOperationConfigWebhook("pixConfigWebhook", params, webhook);
    }

    public JSONObject registeredWebhooks(final String start, final String end) {
        Map<String, String> params = new HashMap<>();
        params.put("inicio", start);
        params.put("fim", end);
        return performOperation("pixListWebhook", params);
    }

    public JSONObject pixDeleteWebhook(final String pixKey) {
        Map<String, String> params = new HashMap<>();
        params.put("chave", pixKey);
        return performOperation("pixDeleteWebhook", params);
    }
    public JSONObject performOperation(String operation, Map<String, String> params) {
        final var retorno = new JSONObject();
        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call(operation, params, new JSONObject());
            System.out.println(response);
            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            return retorno;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return retorno;
        }
    }

    private JSONObject performOperationConfigWebhook(String operation, Map<String, String> params, String webHook) {
        JSONObject body = new JSONObject();
        body.put("webhookUrl", webHook);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call(operation, params, body);
            System.out.println(response);
            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
