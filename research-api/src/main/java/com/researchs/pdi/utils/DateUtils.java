package com.researchs.pdi.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class DateUtils {

    public static LocalDate getParse(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(data,formatter);
    }

    public static Date getDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Double calculamedia(int x1, int y1, int x2, int y2, int x3, int y3) {
        double first = (Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
        double second = (Math.pow((x3 - x2), 2) + Math.pow((y3 - y2), 2));

        double total = Math.sqrt(Math.pow((second -first), 2) + Math.pow((second -first), 2));

        return total;
    }

    public static boolean isAlmostPalindrome(String word) {
        String s = "";
        for (int i = word.length() - 1; i >= 0; i--)
            s = s + word.charAt(i);

        return word.equals(s) || difference(word, s) == 2;
    }

    private static int difference(String word, String s) {
        int numberDifferences = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != s.charAt(i))
                numberDifferences ++;
        }
        return numberDifferences;
    }

    public static int getMostPopularNumber(int[] entrada) {

        List<MostPopular> dados = new ArrayList<>();
        for (int valor : entrada) {
            boolean achou = false;
            for (MostPopular dado : dados) {
                if (valor == dado.getValue()) {
                    dado.setQuantity(dado.getQuantity() + 1);
                    achou = true;
                }
            }
            if (! achou)
                dados.add(new MostPopular(valor, 1));
        }

        int valorAtual = 0;
        int quantidade = 0;
        for (MostPopular dado : dados) {
            if (dado.getQuantity() > quantidade) {
                valorAtual = dado.getValue();
                quantidade = dado.getQuantity();
            }

        }

        return valorAtual;
    }
}
