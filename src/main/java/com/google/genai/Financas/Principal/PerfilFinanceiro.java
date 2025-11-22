package com.google.genai.Financas.Principal;

import com.google.genai.Financas.Modelos.Investment;
import com.google.genai.Financas.TestandoAPIInvestimentos.BrapiClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PerfilFinanceiro {

    public void pefilFinanceiro() {




        LeitorExtrato leitorExtrato = new LeitorExtrato();
//        String respostaExtrato = leitorExtrato.extrato();
        System.out.println("===============" +
                "Lendo o extrato" +
                "==============");
        System.out.println(leitorExtrato.extrato());



        Scanner scan = new Scanner(System.in);
        int pontos = 0;

        System.out.println("Agora responda algumas perguntas para definir seu perfil:");

        System.out.println("\n1️⃣ Qual é o seu conhecimento sobre investimentos?");
        System.out.println("(a) Nenhum conhecimento");
        System.out.println("(b) Conhecimento básico");
        System.out.println("(c) Experiência avançada");
        String resposta = scan.nextLine();
        if (resposta.equalsIgnoreCase("a")) pontos += 1;
        else if (resposta.equalsIgnoreCase("b")) pontos += 2;
        else if (resposta.equalsIgnoreCase("c")) pontos += 3;

        System.out.println("\n2️⃣ Como você reagiria a uma queda de 20% nos seus investimentos?");
        System.out.println("(a) Venderia tudo");
        System.out.println("(b) Esperaria recuperar");
        System.out.println("(c) Aumentaria meus aportes");
        resposta = scan.nextLine();
        if (resposta.equalsIgnoreCase("a")) pontos += 1;
        else if (resposta.equalsIgnoreCase("b")) pontos += 2;
        else if (resposta.equalsIgnoreCase("c")) pontos += 3;

        System.out.println("\n3️⃣ Qual é o seu objetivo de investimento?");
        System.out.println("(a) Preservação de capital");
        System.out.println("(b) Crescimento moderado");
        System.out.println("(c) Alto crescimento");
        resposta = scan.nextLine();
        if (resposta.equalsIgnoreCase("a")) pontos += 1;
        else if (resposta.equalsIgnoreCase("b")) pontos += 2;
        else if (resposta.equalsIgnoreCase("c")) pontos += 3;

        System.out.println("\n4️⃣ Qual horizonte de tempo planeja manter seus investimentos?");
        System.out.println("(a) Curto prazo");
        System.out.println("(b) Médio prazo");
        System.out.println("(c) Longo prazo");
        resposta = scan.nextLine();
        if (resposta.equalsIgnoreCase("a")) pontos += 1;
        else if (resposta.equalsIgnoreCase("b")) pontos += 2;
        else if (resposta.equalsIgnoreCase("c")) pontos += 3;

        String perfil;
        if (pontos <= 7) perfil = "Conservador";
        else if (pontos <= 11) perfil = "Intermediário";
        else perfil = "Experiente";

        System.out.println("\nSeu perfil de investidor é: " + perfil.toUpperCase());


        mostrarInvestimentos(perfil);

        scan.close();
    }

    private void mostrarInvestimentos(String perfil) {
        BrapiClient brapi = new BrapiClient();

        System.out.println("\n🇧🇷 AÇÕES NACIONAIS (Perfil: " + perfil.toUpperCase() + ")");
        List<Investment> nationalStocks = brapi.getNationalStocks();
        List<Investment> filteredBrapi = filtrarInvestimentos(nationalStocks, perfil);

        if (filteredBrapi.isEmpty()) {
            System.out.println("Nenhuma ação nacional disponível para esse perfil.");
        } else {
            filteredBrapi.forEach(System.out::println);
        }

        System.out.println("\n🌍 AÇÕES INTERNACIONAIS (Perfil: " + perfil.toUpperCase() + ")");
        List<Investment> internationalStocks = brapi.getInternationalStocks();
        List<Investment> filteredInternational = filtrarInvestimentos(internationalStocks, perfil);

        if (filteredInternational.isEmpty()) {
            System.out.println("Nenhuma ação internacional disponível para esse perfil.");
        } else {
            filteredInternational.forEach(System.out::println);
        }
    }

    private List<Investment> filtrarInvestimentos(List<Investment> investimentos, String perfil) {
        List<Investment> listaFiltrada = new ArrayList<>();

        double maxRisk = Double.MAX_VALUE;
        double minRisk = 0.0;
        final double PRECO_MINIMO = 1.00;

        if (perfil.equalsIgnoreCase("Conservador")) {
            maxRisk = 0.49;
        } else if (perfil.equalsIgnoreCase("Intermediário")) {
            minRisk = 0.50;
            maxRisk = 0.99;
        } else if (perfil.equalsIgnoreCase("Experiente")) {
            minRisk = 1.00;
        }

        for (Investment inv : investimentos) {
            if (inv.risk() >= minRisk &&
                    inv.risk() <= maxRisk &&
                    inv.price() >= PRECO_MINIMO) {
                listaFiltrada.add(inv);
            }
        }

        listaFiltrada.sort(Comparator.comparingDouble(Investment::risk));

        int limite = 3;
        return listaFiltrada.subList(0, Math.min(limite, listaFiltrada.size()));
    }
}