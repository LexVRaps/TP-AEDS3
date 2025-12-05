package arquivos;

import java.util.ArrayList;

public class BM {
    static int NO_DE_CHARS = 2 << Character.SIZE;

    public int[] calculaBadChar(String s) {
        int[] badchar = new int[NO_DE_CHARS];

        for (int i = 0; i < NO_DE_CHARS; i++)
            badchar[i] = -1;

        for (int i = 0; i < s.length(); i++)
            badchar[(int)s.charAt(i)] = i;
        
        return badchar;
    }

    public boolean busca(String padrao, String texto, int[] badchar) {
        int m = padrao.length();
        int n = texto.length();
        int i = 0;

        while (i <= (n - m)) {
            int j = m - 1;

            while (j >= 0 && texto.charAt(i + j) == padrao.charAt(j))
                j--;
            
            if (j < 0) {
                //i += (i + m < n) ? m - badchar[(int)texto.charAt(i + m)] : 1;
                return true;
            }

            else
                i += Math.max(1, j - badchar[(int)texto.charAt(i + j)]);
        }

        return false;
    }

    // Teste
    public static void main() {
        BM bm = new BM();
        String[] textos = {"ABABDABACDABABCABAB", "ABABCABA", "AAAAAAAA", "ABABCABABABABCABAB"};
        String padrao = "ABABCABAB";

        int[] badchar = bm.calculaBadChar(padrao);
        ArrayList<String> resultado = new ArrayList<>();
        for(String s: textos)
            if (bm.busca(padrao, s, badchar))
                resultado.add(s);
        
        for(String s: resultado)
            System.out.println(s);
    }
}