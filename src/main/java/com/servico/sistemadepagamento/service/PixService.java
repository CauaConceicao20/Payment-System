package com.servico.sistemadepagamento.service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.servico.sistemadepagamento.dto.PixChargeRequestDto;
import com.servico.sistemadepagamento.dto.PixConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.*;

@Service
public class PixService {

    private final JSONObject options;

    @Value("${pix.key}")
    private String keyPix;

    public PixService(PixConfig pixConfig) {

        this.options = new JSONObject();
        this.options.put("client_id", pixConfig.getClientId());
        this.options.put("client_secret", pixConfig.getClientSecret());
        this.options.put("certificate", pixConfig.getCertificate());
        this.options.put("sandbox", pixConfig.isSandbox());
        this.options.put("debug", pixConfig.isDebug());
    }

    public JSONObject pixCreate() {
        return performOperation("pixCreateEvp", new HashMap<>());
    }

    public JSONObject pixList() {
        return performOperation("pixListEvp", new HashMap<>());
    }

    public JSONObject pixDelete(final String pixKey) {
        Map<String, String> params = new HashMap<>();
        params.put("chave", pixKey);
        return performOperation("pixDeleteEvp", params);
    }

    public JSONObject pixReceived(final String start, final String end) {
        Map<String, String> params = new HashMap<>();
        params.put("inicio", start);
        params.put("fim", end);
        return performOperation("pixReceivedList", params);
    }

    public JSONObject pixDetailsCharge(final String txId) {
        Map<String, String> params = new HashMap<>();
        params.put("txid", txId);
        return performOperation("pixDetailCharge", params);
    }

    public JSONObject pixCreateCharge(PixChargeRequestDto pixChargeRequestDto) {
        System.out.println(pixChargeRequestDto.cpf());

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 3600));
        body.put("devedor", new JSONObject().put("cpf", pixChargeRequestDto.cpf()).put("nome", pixChargeRequestDto.debtorName()));
        body.put("valor", new JSONObject().put("original", pixChargeRequestDto.value()));
        body.put("chave", keyPix);

        JSONArray infoAdicionais = new JSONArray();
        infoAdicionais.put(new JSONObject().put("nome", pixChargeRequestDto.informationName1()).put("valor", pixChargeRequestDto.informationContent1()));
        infoAdicionais.put(new JSONObject().put("nome", pixChargeRequestDto.informationName2()).put("valor", pixChargeRequestDto.informationContent2()));
        body.put("infoAdicionais", infoAdicionais);


        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixCreateImmediateCharge", new HashMap<String, String>(), body);

            int idFromJson = response.getJSONObject("loc").getInt("id");
            pixGenerateQRCode(String.valueOf(idFromJson));

            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void pixGenerateQRCode(String id) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        try {
            EfiPay efi = new EfiPay(options);
            Map<String, Object> response = efi.call("pixGenerateQRCode", params, new HashMap<String, Object>());

            System.out.println(response);

            File outputfile = new File("qrCodeImage.png");
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(javax.xml.bind.DatatypeConverter.parseBase64Binary(((String) response.get("imagemQrcode")).split(",")[1]))), "png", outputfile);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(outputfile);

        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private JSONObject performOperation(String operation, Map<String, String> params) {
        final var retorno = new JSONObject();
        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call(operation, params, new JSONObject());
            System.out.println(response.toString());
            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return retorno;
    }
}
