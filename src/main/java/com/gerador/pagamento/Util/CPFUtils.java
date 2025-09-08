package com.gerador.pagamento.Util;

public class CPFUtils {
    public static String formatarCPF(String cpf) {
        if (cpf == null) return null;
        String numeros = cpf.replaceAll("\\D", "");
        if (numeros.length() != 11) return cpf;
        return numeros.substring(0, 3) + "." +
                numeros.substring(3, 6) + "." +
                numeros.substring(6, 9) + "-" +
                numeros.substring(9);
    }
}
