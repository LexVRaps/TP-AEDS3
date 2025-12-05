package arquivos;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSA {
    // Limite para o tamanho do texto a ser cifrado é 2*TAM_PRIMO bits
    private static final int TAM_PRIMO = 2048;
    private static final Random RND = new SecureRandom(); 

    // Gera as chaves RSA: módulo (n), expoente público (e) e expoente privado (d).
    public static BigInteger[] gera_chaves() {
        BigInteger p = BigInteger.probablePrime(TAM_PRIMO, RND);
        BigInteger q = BigInteger.probablePrime(TAM_PRIMO, RND);

        BigInteger n = p.multiply(q);

        BigInteger p_minus_1 = p.subtract(BigInteger.ONE);
        BigInteger q_minus_1 = q.subtract(BigInteger.ONE);
        BigInteger phi_n = p_minus_1.multiply(q_minus_1);

        BigInteger e = BigInteger.valueOf(65537);

        // d = e^(-1) mod phi_n
        BigInteger d;
        while (true) {
            try {
                d = e.modInverse(phi_n);
                break;
            } catch (ArithmeticException ex) {
                // mdc(e, phi_n) != 1
                e = e.add(BigInteger.ONE);
            }
        }

        return new BigInteger[]{n, e, d};
    }

    // Codifica uma string em um BigInteger.
    public static BigInteger codifica(String str) {
        return new BigInteger(str.getBytes());
    }

    // Decodifica um BigInteger de volta para uma string.
    public static String decodifica(BigInteger n) {
        return new String(n.toByteArray());
    }

    // Criptografa a mensagem M: C = M^e mod n.
    public static BigInteger criptografa(BigInteger M, BigInteger n, BigInteger e) {
        return M.modPow(e, n);
    }
    
    // Descriptografa o texto cifrado C: M = C^d mod n.
    public static BigInteger descriptografa(BigInteger C, BigInteger n, BigInteger d) {
        return C.modPow(d, n);
    }

    // Teste
    public static void main(String[] args) {
        System.out.println("Iniciando Geração de Chaves RSA...");
        
        BigInteger[] chaves = gera_chaves();
        BigInteger n = chaves[0]; // Módulo
        BigInteger e = chaves[1]; // Expoente Público
        BigInteger d = chaves[2]; // Expoente Privado
        
        System.out.println("\n--- Chaves Geradas ---");
        System.out.println("Módulo (n): " + n.toString());
        System.out.println("\nExp. Público (e): " + e);
        System.out.println("\nExp. Privado (d): " + d.toString());
        
        String mensagemOriginal = "Esta é uma mensagem de teste para RSA.";
        System.out.println("\n--- Mensagem ---");
        System.out.println("Original: " + mensagemOriginal);
        
        BigInteger M = codifica(mensagemOriginal);
        System.out.println("\nCodificada (M): " + M.toString());
        
        BigInteger C = criptografa(M, n, e);
        System.out.println("\nCriptografada (C): " + C.toString());
        
        BigInteger M_decifrado = descriptografa(C, n, d);
        System.out.println("\nDescriptografada (M'): " + M_decifrado.toString());
        
        String mensagemDecodificada = decodifica(M_decifrado);
        System.out.println("\nDecodificada: " + mensagemDecodificada);
        
        System.out.println("\nVerificação: " + mensagemOriginal.equals(mensagemDecodificada));
    }
}