package arquivos;

import java.io.*;
import java.math.BigInteger;

public class CriptoRSA {
    private static final String ARQUIVO_CHAVES = "dados/chaves_rsa.dat";
    private static BigInteger n;
    private static BigInteger e;
    private static BigInteger d;
    
    static {
        try {
            carregarOuGerarChaves();
        } catch (Exception ex) {
            System.err.println("Erro ao inicializar chaves RSA: " + ex.getMessage());
        }
    }
    
    private static void carregarOuGerarChaves() throws Exception {
        File arquivo = new File(ARQUIVO_CHAVES);
        
        if (arquivo.exists()) {
            // Carregar chaves existentes
            try (DataInputStream dis = new DataInputStream(new FileInputStream(arquivo))) {
                int nLen = dis.readInt();
                byte[] nBytes = new byte[nLen];
                dis.readFully(nBytes);
                n = new BigInteger(nBytes);
                
                int eLen = dis.readInt();
                byte[] eBytes = new byte[eLen];
                dis.readFully(eBytes);
                e = new BigInteger(eBytes);
                
                int dLen = dis.readInt();
                byte[] dBytes = new byte[dLen];
                dis.readFully(dBytes);
                d = new BigInteger(dBytes);
            }
        } else {
            // Gerar novas chaves
            BigInteger[] chaves = RSA.gera_chaves();
            n = chaves[0];
            e = chaves[1];
            d = chaves[2];
            
            // Salvar chaves
            new File("dados").mkdirs();
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(arquivo))) {
                byte[] nBytes = n.toByteArray();
                dos.writeInt(nBytes.length);
                dos.write(nBytes);
                
                byte[] eBytes = e.toByteArray();
                dos.writeInt(eBytes.length);
                dos.write(eBytes);
                
                byte[] dBytes = d.toByteArray();
                dos.writeInt(dBytes.length);
                dos.write(dBytes);
            }
        }
    }
    
    public static String criptografar(String senha) {
        if (senha == null || senha.isEmpty()) {
            return "";
        }
        BigInteger M = RSA.codifica(senha);
        BigInteger C = RSA.criptografa(M, n, e);
        return C.toString();
    }
    
    public static String descriptografar(String senhaCriptografada) {
        if (senhaCriptografada == null || senhaCriptografada.isEmpty()) {
            return "";
        }
        try {
            BigInteger C = new BigInteger(senhaCriptografada);
            BigInteger M = RSA.descriptografa(C, n, d);
            return RSA.decodifica(M);
        } catch (Exception ex) {
            System.err.println("Erro ao descriptografar senha: " + ex.getMessage());
            return "";
        }
    }
}
