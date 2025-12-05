import java.util.ArrayList;
import java.util.Scanner;
import arquivos.*;
import entidades.*;

public class MenuPesquisa {
    private Scanner console;
    private ArquivoUsuarios arqUsuarios;
    private ArquivoRotas arqRotas;
    private ArquivoPassagens arqPassagens;
    private KMP kmp;
    private BM bm;

    public MenuPesquisa() throws Exception {
        console = new Scanner(System.in);
        arqUsuarios = new ArquivoUsuarios();
        arqRotas = new ArquivoRotas();
        arqPassagens = new ArquivoPassagens();
        kmp = new KMP();
        bm = new BM();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("PESQUISA POR PADRÃO (KMP/BM)");
            System.out.println("1 - Pesquisar Usuários");
            System.out.println("2 - Pesquisar Rotas");
            System.out.println("3 - Pesquisar Passagens");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    pesquisarUsuarios();
                    break;
                case 2:
                    pesquisarRotas();
                    break;
                case 3:
                    pesquisarPassagens();
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    private void pesquisarUsuarios() {
        System.out.println("PESQUISAR USUÁRIOS");
        
        int algoritmo = escolherAlgoritmo();
        if (algoritmo == 0) return;
        
        System.out.print("Digite o padrão de busca (Nome ou email): ");
        String padrao = console.nextLine();
        
        if (padrao.isEmpty()) {
            System.out.println("Padrão não pode ser vazio!");
            return;
        }

        try {
            ArrayList<Usuario> resultados = new ArrayList<>();
            ArrayList<Usuario> todosUsuarios = listarTodosUsuarios();
            
            long inicio = System.nanoTime();
            
            if (algoritmo == 1) {
                // KMP
                int[] pi = kmp.calculaPrefixo(padrao);
                for (Usuario u : todosUsuarios) {
                    if (kmp.busca(padrao, u.getNome(), pi) || 
                        kmp.busca(padrao, u.getEmail(), pi)) {
                        resultados.add(u);
                    }
                }
            } else {
                // Boyer-Moore
                int[] badchar = bm.calculaBadChar(padrao);
                for (Usuario u : todosUsuarios) {
                    if (bm.busca(padrao, u.getNome(), badchar) || 
                        bm.busca(padrao, u.getEmail(), badchar)) {
                        resultados.add(u);
                    }
                }
            }
            
            long fim = System.nanoTime();
            double tempoMs = (fim - inicio) / 1_000_000.0;
            
            exibirResultadosUsuarios(resultados, padrao, algoritmo == 1 ? "KMP" : "Boyer-Moore", tempoMs);
            
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar usuários: " + e.getMessage());
        }
    }

    private void pesquisarRotas() {
        System.out.println("PESQUISAR ROTAS");
        
        int algoritmo = escolherAlgoritmo();
        if (algoritmo == 0) return;
        
        System.out.print("Digite o padrão de busca (Origem ou Destino): ");
        String padrao = console.nextLine();
        
        if (padrao.isEmpty()) {
            System.out.println("Padrão não pode ser vazio!");
            return;
        }

        try {
            ArrayList<Rota> resultados = new ArrayList<>();
            ArrayList<Rota> todasRotas = listarTodasRotas();
            
            long inicio = System.nanoTime();
            
            if (algoritmo == 1) {
                // KMP
                int[] pi = kmp.calculaPrefixo(padrao);
                for (Rota r : todasRotas) {
                    if (kmp.busca(padrao, r.getOrigem(), pi) || 
                        kmp.busca(padrao, r.getDestino(), pi)) {
                        resultados.add(r);
                    }
                }
            } else {
                // Boyer-Moore
                int[] badchar = bm.calculaBadChar(padrao);
                for (Rota r : todasRotas) {
                    if (bm.busca(padrao, r.getOrigem(), badchar) || 
                        bm.busca(padrao, r.getDestino(), badchar)) {
                        resultados.add(r);
                    }
                }
            }
            
            long fim = System.nanoTime();
            double tempoMs = (fim - inicio) / 1_000_000.0;
            
            exibirResultadosRotas(resultados, padrao, algoritmo == 1 ? "KMP" : "Boyer-Moore", tempoMs);
            
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar rotas: " + e.getMessage());
        }
    }

    private void pesquisarPassagens() {
        System.out.println("PESQUISAR PASSAGENS");
        
        int algoritmo = escolherAlgoritmo();
        if (algoritmo == 0) return;
        
        System.out.print("Digite o padrão de busca (Data) (data no formato AAAA-MM-DD): ");
        String padrao = console.nextLine();
        
        if (padrao.isEmpty()) {
            System.out.println("Padrão não pode ser vazio!");
            return;
        }

        try {
            ArrayList<Passagem> resultados = new ArrayList<>();
            ArrayList<Passagem> todasPassagens = listarTodasPassagens();
            
            long inicio = System.nanoTime();
            
            if (algoritmo == 1) {
                // KMP
                int[] pi = kmp.calculaPrefixo(padrao);
                for (Passagem p : todasPassagens) {
                    String dataStr = p.getDataViagem().toString();
                    if (kmp.busca(padrao, dataStr, pi)) {
                        resultados.add(p);
                    }
                }
            } else {
                // Boyer-Moore
                int[] badchar = bm.calculaBadChar(padrao);
                for (Passagem p : todasPassagens) {
                    String dataStr = p.getDataViagem().toString();
                    if (bm.busca(padrao, dataStr, badchar)) {
                        resultados.add(p);
                    }
                }
            }
            
            long fim = System.nanoTime();
            double tempoMs = (fim - inicio) / 1_000_000.0;
            
            exibirResultadosPassagens(resultados, padrao, algoritmo == 1 ? "KMP" : "Boyer-Moore", tempoMs);
            
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar passagens: " + e.getMessage());
        }
    }

    private int escolherAlgoritmo() {
        System.out.println("\nEscolha o algoritmo de busca:");
        System.out.println("1 - KMP (Knuth-Morris-Pratt)");
        System.out.println("2 - Boyer-Moore");
        System.out.println("0 - Cancelar");
        System.out.print("Opção: ");
        
        try {
            int opcao = Integer.valueOf(console.nextLine());
            if (opcao < 0 || opcao > 2) {
                System.out.println("Opção inválida!");
                return 0;
            }
            return opcao;
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida!");
            return 0;
        }
    }

    private ArrayList<Usuario> listarTodosUsuarios() throws Exception {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            Usuario u = arqUsuarios.read(i);
            if (u != null) {
                usuarios.add(u);
            }
        }
        return usuarios;
    }

    private ArrayList<Rota> listarTodasRotas() throws Exception {
        ArrayList<Rota> rotas = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            Rota r = arqRotas.read(i);
            if (r != null) {
                rotas.add(r);
            }
        }
        return rotas;
    }

    private ArrayList<Passagem> listarTodasPassagens() throws Exception {
        ArrayList<Passagem> passagens = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            Passagem p = arqPassagens.read(i);
            if (p != null) {
                passagens.add(p);
            }
        }
        return passagens;
    }

    private void exibirResultadosUsuarios(ArrayList<Usuario> resultados, String padrao, String algoritmo, double tempoMs) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESULTADOS DA PESQUISA");
        System.out.println("═".repeat(50));
        System.out.println("Algoritmo: " + algoritmo);
        System.out.println("Padrão buscado: \"" + padrao + "\"");
        System.out.println("Tempo de execução: " + String.format("%.4f", tempoMs) + " ms");
        System.out.println("Registros encontrados: " + resultados.size());
        System.out.println("═".repeat(50));
        
        if (resultados.isEmpty()) {
            System.out.println("\nNenhum usuário encontrado com o padrão especificado.");
        } else {
            for (Usuario u : resultados) {
                System.out.println(u);
                System.out.println("-".repeat(50));
            }
        }
    }

    private void exibirResultadosRotas(ArrayList<Rota> resultados, String padrao, String algoritmo, double tempoMs) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESULTADOS DA PESQUISA");
        System.out.println("═".repeat(50));
        System.out.println("Algoritmo: " + algoritmo);
        System.out.println("Padrão buscado: \"" + padrao + "\"");
        System.out.println("Tempo de execução: " + String.format("%.4f", tempoMs) + " ms");
        System.out.println("Registros encontrados: " + resultados.size());
        System.out.println("═".repeat(50));
        
        if (resultados.isEmpty()) {
            System.out.println("\nNenhuma rota encontrada com o padrão especificado.");
        } else {
            for (Rota r : resultados) {
                System.out.println(r);
                System.out.println("-".repeat(50));
            }
        }
    }

    private void exibirResultadosPassagens(ArrayList<Passagem> resultados, String padrao, String algoritmo, double tempoMs) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESULTADOS DA PESQUISA");
        System.out.println("═".repeat(50));
        System.out.println("Algoritmo: " + algoritmo);
        System.out.println("Padrão buscado: \"" + padrao + "\"");
        System.out.println("Tempo de execução: " + String.format("%.4f", tempoMs) + " ms");
        System.out.println("Registros encontrados: " + resultados.size());
        System.out.println("═".repeat(50));
        
        if (resultados.isEmpty()) {
            System.out.println("\nNenhuma passagem encontrada com o padrão especificado.");
        } else {
            for (Passagem p : resultados) {
                System.out.println(p);
                System.out.println("-".repeat(50));
            }
        }
    }
}
