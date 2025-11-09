import java.util.Scanner;
import java.util.ArrayList;

import entidades.Horario;
import entidades.Passagem;
import entidades.Rota;
import entidades.Usuario;
import arquivos.ArquivoHorarios;
import arquivos.ArquivoPassagens;
import arquivos.ArquivoRotas;
import arquivos.ArquivoUsuarios;

public class MenuUsuarios {
    private Scanner console;
    private ArquivoUsuarios usuarioDAO;

    public MenuUsuarios() throws Exception {
        console = new Scanner(System.in);
        usuarioDAO = new ArquivoUsuarios();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nMENU USUÁRIOS");
            System.out.println("---------------");
            System.out.println("1 - Incluir usuário");
            System.out.println("2 - Buscar usuário");
            System.out.println("3 - Alterar usuário");
            System.out.println("4 - Excluir usuário");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    incluirUsuario();
                    break;
                case 2:
                    buscarUsuario();
                    break;
                case 3:
                    alterarUsuario();
                    break;
                case 4:
                    excluirUsuario();
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

    private void incluirUsuario() {
        System.out.println("\nINCLUIR USUÁRIO");
        System.out.print("Nome: ");
        String nome = console.nextLine();
        System.out.print("Email: ");
        String email = console.nextLine();
        System.out.print("Senha: ");
        String senha = console.nextLine();

        Usuario novoUsuario = new Usuario(nome, email, senha);
        try {
            int id = usuarioDAO.create(novoUsuario);
            System.out.println("Usuário incluído com ID: " + id);
        } catch (Exception e) {
            System.err.println("Erro ao incluir usuário: " + e.getMessage());
        }
    }

    private void buscarUsuario() {
        System.out.println("\nBUSCAR USUÁRIO");
        System.out.print("Nome do usuário: ");
        String nome = console.nextLine();

        try {
            Usuario usuario = usuarioDAO.readByNome(nome);
            if (usuario != null) {
                System.out.println(usuario);
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    private void alterarUsuario() {
        System.out.println("\nALTERAR USUÁRIO");
        System.out.print("Nome do usuário a ser alterado: ");
        String nomeBusca = console.nextLine();

        try {
            Usuario usuarioExistente = usuarioDAO.readByNome(nomeBusca);
            if (usuarioExistente != null) {
                System.out.println("Usuário atual: " + usuarioExistente);
                System.out.print("Novo Nome (deixe em branco para manter: " + usuarioExistente.getNome() + "): ");
                String nome = console.nextLine();
                if (!nome.isEmpty()) {
                    usuarioExistente.setNome(nome);
                }
                System.out.print("Novo Email (deixe em branco para manter: " + usuarioExistente.getEmail() + "): ");
                String email = console.nextLine();
                if (!email.isEmpty()) {
                    usuarioExistente.setEmail(email);
                }
                System.out.print("Nova Senha (deixe em branco para manter: " + usuarioExistente.getSenha() + "): ");
                String senha = console.nextLine();
                if (!senha.isEmpty()) {
                    usuarioExistente.setSenha(senha);
                }
                if (usuarioDAO.update(usuarioExistente)) {
                    System.out.println("Usuário alterado com sucesso!");
                } else {
                    System.out.println("Falha ao alterar usuário.");
                }
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao alterar usuário: " + e.getMessage());
        }
    }
    private void listarPassagensUsuario() {
    System.out.println("\nLISTAR PASSAGENS DO USUÁRIO");
    System.out.print("Nome do usuário: ");
    String nome = console.nextLine();

    try {
        Usuario usuario = usuarioDAO.readByNome(nome);
        if (usuario != null) {
            System.out.println(usuario);
            
            if (!usuario.getPassagensIds().isEmpty()) {
                System.out.println("\nDetalhes das Passagens:");
                ArquivoPassagens arqPassagens = new ArquivoPassagens();
                ArquivoRotas arqRotas = new ArquivoRotas();
                ArquivoHorarios arqHorarios = new ArquivoHorarios();
                
                for (Integer passagemId : usuario.getPassagensIds()) {
                    Passagem p = arqPassagens.read(passagemId);
                    if (p != null) {
                        Rota r = arqRotas.read(p.getRotaId());
                        Horario h = arqHorarios.read(p.getHorarioId());
                        
                        System.out.println("Passagem ID: " + p.getID());
                        System.out.println("  Rota: " + r.getOrigem() + " -> " + r.getDestino());
                        System.out.println("  Data: " + p.getDataViagem());
                        if (h != null) {
                            System.out.println("  Horário: " + h.getHoraPartida() + " - " + h.getHoraChegada());
                        }
                        System.out.println("  Preço: R$ " + String.format("%.2f", p.getPreco()));
                        System.out.println("---");
                    }
                }
            }
        } else {
            System.out.println("Usuário não encontrado.");
        }
    } catch (Exception e) {
        System.err.println("Erro ao listar passagens: " + e.getMessage());
    }
}



    private void excluirUsuario() {
        System.out.println("\nEXCLUIR USUÁRIO");
        System.out.print("Nome do usuário a ser excluído: ");
        String nomeBusca = console.nextLine();

        try {
            Usuario usuario = usuarioDAO.readByNome(nomeBusca);
            if (usuario != null) {
                if (usuarioDAO.delete(usuario.getID())) {
                    System.out.println("Usuário excluído com sucesso!");
                } else {
                    System.out.println("Falha ao excluir usuário.");
                }
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
        }
    }
}
