package arquivos;

import java.util.ArrayList;

public class KMP {

    public int[] calculaPrefixo(String s) {
        int n = s.length();
        int[] pi = new int[n];
        pi[0] = 0;

        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j))
                j = pi[j - 1];
            if (s.charAt(i) == s.charAt(j))
                j++;
            pi[i] = j;
        }

        return pi;
    }

    public boolean busca(String padrao, String texto, int[] pi) {
        int m = padrao.length();
        int n = texto.length();
        int i = 0;
        int j = 0;

        while (i < n) {
            if (texto.charAt(i) == padrao.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                //j = pi[j - 1];
                return true;
            }
            
            else if (i < n && texto.charAt(i) != padrao.charAt(j)) {
                if (j != 0)
                    j = pi[j - 1];
                else i++;
            }
        }

        return false;
    }

    // Teste
    public static void main() {
        KMP kmp = new KMP();
        String[] textos = {"ABABDABACDABABCABAB", "ABABCABA", "AAAAAAAA", "ABABCABABABABCABAB"};
        String padrao = "ABABCABAB";

        int[] pi = kmp.calculaPrefixo(padrao);
        ArrayList<String> resultado = new ArrayList<>();
        for(String s: textos)
            if (kmp.busca(padrao, s, pi))
                resultado.add(s);
        
        for(String s: resultado)
            System.out.println(s);
    }
}