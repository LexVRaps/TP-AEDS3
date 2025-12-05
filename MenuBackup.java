import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

import aeds3.Huffman;
import aeds3.LZW;
import aeds3.VetorDeBits;

public class MenuBackup {
    Path directoryPath;
    private static Scanner console = new Scanner(System.in);

    public MenuBackup() throws Exception{
        directoryPath = Files.createDirectories(Paths.get("backup"));
        Files.createDirectories(Paths.get("backup/Huffman"));
        Files.createDirectories(Paths.get("backup/LZW"));
    }
    public void menu(){
        int opcao;
        do {
            System.out.println("\n\n\nMENU BACKUP");
            System.out.println("------------");
            System.out.println("1 - Fazer Backup");
            System.out.println("2 - Escolher Backup");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            // Seleciona a operação
            switch (opcao) {
                case 1:
                    fazBackup();
                    break;
                case 2:
                    voltaBackup();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
                }
        } while (opcao != 0);
    }

    public void fazBackup(){
        int opcao;
        String algoritmo;
        do {
            System.out.println("\nAlgoritmo de compactação: ");
            System.out.println("1 - Huffman");
            System.out.println("2 - LZW");
            System.out.println("0 - Voltar");
            System.out.print("\nOpção: ");
            try {
                opcao = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }
            if (opcao == 0) return;
            if (opcao == 1)
            {
                algoritmo = "Huffman";
                break;
            }
            if (opcao == 2)
            {
                algoritmo = "LZW";
                break;
            }
            System.out.println("Opção inválida!");
        } while (true);
        
        RandomAccessFile arq;
        RandomAccessFile arq2;
        Path directoryPath2;
        byte[] ba;
        HashMap<Byte, String> hufftree;
        byte[] msgCod;
        int versao = 1;
        int ori = 0;
        int cod = 0;

        try{
            RandomAccessFile tmp = new RandomAccessFile("backup/" + algoritmo + "/versao.txt", "rw");
            if(tmp.length()>0){              // confere a versão a ser utilizada para o próximo backup;
                versao = tmp.readInt();
            } else {
                tmp.writeInt(versao);
            }                                          
            File folder = new File("dados");   
            File[] listOfFiles = folder.listFiles();            // array contendo todos os arquivos do diretório dados
            if(listOfFiles != null) {
                directoryPath2 = directoryPath.resolve(algoritmo + "/v" + Integer.toString(versao) );   //cria diretório para essa versão do backup
                Files.createDirectories(directoryPath2);
                for (int i = 0; i < listOfFiles.length; i++) {      // compacta cada arquivo e escreve no diretório
                    arq = new RandomAccessFile("dados/" + listOfFiles[i].getName(), "r");
                    //ori = ori + (int)arq.length();
                    ba = new byte[(int)arq.length()];
                    arq.read(ba);
                    arq2 = new RandomAccessFile("backup/" + algoritmo + "/v" + Integer.toString(versao) + "/" + listOfFiles[i].getName() , "rw" );
                    if (algoritmo.equals("Huffman"))
                    {
                        hufftree = Huffman.codifica(ba);
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backup/Huffman/v" + Integer.toString(versao) + "/" + listOfFiles[i].getName() + ".hufftree"));
                        oos.writeObject(hufftree);
                        oos.flush();
                        oos.close();
                        // System.out.println("Hufftree: " + hufftree);
                        VetorDeBits sequenciaCodificada = new VetorDeBits();
                        int j = 0;
                        for (byte b : ba) {
                            String codigo = hufftree.get(b);
                            for(char c : codigo.toCharArray()) {
                                if(c=='0')
                                    sequenciaCodificada.clear(j++);
                                else
                                    sequenciaCodificada.set(j++);
                            }
                        }
                        msgCod = sequenciaCodificada.toByteArray();
                        /* if(i == 0)
                        {
                            System.out.println(listOfFiles[i].getName());
                            System.out.println("Antes: ");
                            for (byte b1 : ba){
                                String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
                                s1 += " " + Integer.toHexString(b1);
                                s1 += " " + b1;
                                System.out.println(s1);
		                    }
                            System.out.println("Depois: ");
                            for (byte b1 : msgCod){
                                String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
                                s1 += " " + Integer.toHexString(b1);
                                s1 += " " + b1;
                                System.out.println(s1);
		                    }
                        } */
                    }
                    else msgCod = LZW.codifica(ba);
                    arq2.write(msgCod);
                    //cod = cod + (int)arq2.length();
                    arq.close();
                    arq2.close();
                }           
                //System.out.println((float)cod/ori);    
                versao++;
                tmp.seek(0);
                tmp.writeInt(versao); // atualiza versão
                tmp.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void voltaBackup(){
        int opcao;
        String algoritmo;
        do {
            System.out.println("\nAlgoritmo de descompactação: ");
            System.out.println("1 - Huffman");
            System.out.println("2 - LZW");
            System.out.println("0 - Voltar");
            System.out.print("\nOpção: ");
            try {
                opcao = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }
            if (opcao == 0) return;
            if (opcao == 1)
            {
                algoritmo = "Huffman";
                break;
            }
            if (opcao == 2)
            {
                algoritmo = "LZW";
                break;
            }
            System.out.println("Opção inválida!");

        } while (true);

        byte[] ba;
        HashMap<Byte, String> hufftree;
        byte[] msgDecod;
        RandomAccessFile arq;
        RandomAccessFile arq2;
        int i = 0;
        try{
            File folder = new File("backup/" + algoritmo);
            File[] listOfFiles = folder.listFiles();            // array contendo todos os backups
            
            if (listOfFiles.length > 0){                        // Menu para selecionar versão a ser restaurada
                System.out.println("Selecione a versão do backup: ");
                for  (i= 1; i < listOfFiles.length+1; i++) {
                    if(!listOfFiles[i-1].getName().equals("versao.txt")){
                        System.out.println(i + " - " + listOfFiles[i-1].getName());
                    }
                }
            } else {
                System.out.println("Nenhum backup disponível");
            }
            System.out.println("\n0 - Voltar");
            System.out.print("\nOpção: ");
            opcao = Integer.valueOf(console.nextLine());
            if (!(opcao> 0 && opcao<=i)){
                if(opcao==0){
                    return;
                } 
                System.out.println("Backup inválido");
                return;
            }                                                   // Menu para selecionar versão a ser restaurada
            folder = new File("backup/" + algoritmo + "/v" + opcao);
            listOfFiles = folder.listFiles();
            if (listOfFiles != null){
                for (i = 0; i < listOfFiles.length; i++) {                      //decodifica cada arquivo e sobrescreve no diretório de dados
                    //System.out.println(listOfFiles[i].getName());
                    if (listOfFiles[i].getName().endsWith("hufftree")) continue;
                    arq = new RandomAccessFile("backup/" + algoritmo + "/v" + opcao + "/" + listOfFiles[i].getName(), "r");
                    
                    ba = new byte[(int)arq.length()];
                    arq.read(ba);
                    //System.out.println(ba.length);
                    if (algoritmo.equals("Huffman"))
                    {
                        ObjectInputStream oos = new ObjectInputStream(new FileInputStream("backup/Huffman/v" + opcao + "/" + listOfFiles[i].getName() + ".hufftree"));
                        hufftree = (HashMap<Byte, String>) oos.readObject();
                        //System.out.println("Hufftree: " + hufftree);
                        oos.close();
                        VetorDeBits vb = new VetorDeBits(ba);
                        msgDecod = Huffman.decodifica(vb.toString(), hufftree);
                        /* if(i == 0)
                        {
                            System.out.println(listOfFiles[i].getName());
                            System.out.println("Antes: ");
                            for (byte b1 : ba){
                                String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
                                s1 += " " + Integer.toHexString(b1);
                                s1 += " " + b1;
                                System.out.println(s1);
		                    }
                            System.out.println("Depois: ");
                            for (byte b1 : msgDecod){
                                String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
                                s1 += " " + Integer.toHexString(b1);
                                s1 += " " + b1;
                                System.out.println(s1);
		                    }
                        } */
                    }
                    else msgDecod = LZW.decodifica(ba);
                    new File("dados/"+listOfFiles[i].getName()).delete();
                    arq2 = new RandomAccessFile("dados/"+listOfFiles[i].getName(), "rwd");
                    arq2.write(msgDecod);
                    arq2.close();
                }
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }
}

