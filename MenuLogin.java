import java.util.Scanner;

import entidades.Usuario;
import arquivos.ArquivoUsuarios;
public class MenuLogin {
    private static Scanner console = new Scanner(System.in);
    private ArquivoUsuarios usuarioDAO;

    public MenuLogin() throws Exception {
        console = new Scanner(System.in);
        usuarioDAO = new ArquivoUsuarios();
    }

    public void menu(){
        int opcao;

        try {
            do {
                System.out.println("\n\nUSUÁRIO");
                System.out.println("-------");
                System.out.println("> Início");
                System.out.println("1 - Fazer Login");
                System.out.println("2 - Cadastrar-se");
                System.out.println("0 - Voltar");

                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }
                switch (opcao) {
                    case 1:
                        fazerLogin();
                        break;
                    case 2:
                        MenuUsuarios menuUsuarios = new MenuUsuarios();
                        menuUsuarios.incluirUsuario();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } while (opcao != 0);

        } catch (Exception e) {
            System.err.println("Erro fatal no sistema:");
            e.printStackTrace();
        }
    }

    private void fazerLogin() {
        System.out.println("\nLOGIN");
        System.out.print("Email: ");
        String email = console.nextLine();
        System.out.print("Senha: ");
        String senha = console.nextLine();

        try {
            Usuario usuario = usuarioDAO.read(email);
            if (usuario != null) {
                if (!usuario.getSenha().equals(senha)) {
                    System.out.println("Email ou senha inválidos.");
                    return;
                }
                System.out.println("Login realizado com sucesso! Bem-vindo(a), " + usuario.getNome() + ".");
                MenuUsuario menuUsuario = new MenuUsuario(usuario);
                menuUsuario.menu();
            } else {
                System.out.println("Email ou senha inválidos.");
            }
        } catch (Exception e) {
            System.err.println("Erro durante o login: " + e.getMessage());
        }
    }
}
