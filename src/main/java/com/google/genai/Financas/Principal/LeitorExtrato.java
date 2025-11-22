package com.google.genai.Financas.Principal;

import com.google.genai.Client;
import com.google.genai.Financas.Service.LeitorPDF;
import com.google.genai.types.GenerateContentResponse;
import org.apache.http.HttpException;

import java.io.IOException;

public class LeitorExtrato {

    private static final String API_KEY = "AIzaSyDQwNQ3rC7lIylLeX4ir9ywErfnd_Q_UJk";

    private String caminhoExtrato = "C:/Users/toled/Downloads/extrato.pdf";

    public String extrato() {
        try {
            String textoExtrato = LeitorPDF.lerTextoDoPDF(caminhoExtrato);

            Client client = Client.builder().apiKey(API_KEY).build();

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    textoExtrato +
                            " Leia meu extrato e diz como posso economizar, " +
                            "e separa meus gastos em categorias, em um texto normal, " +
                            "sem firulas, sem negrito, sem markdown.",
                    null
            );

            if (response == null || response.text() == null) {
                return "Não houve resposta do Gemini.";
            }

            return response.text();

        } catch (IOException e) {
            return "Erro de leitura de arquivo ou comunicação: " + e.getMessage();
        } catch (HttpException e) {
            return "Erro HTTP ao acessar a API Gemini: " + e.getMessage();
        } catch (Exception e) {
            return "Erro inesperado: " + e.getMessage();
        }
    }
}