import java.util.Scanner;

class Principal {

  public static void main(String args[]) {
    Scanner console = new Scanner(System.in);
        int opcao;

        try {
            do {
                System.out.println("\n\nAEDsIII");
                System.out.println("-------");
                System.out.println("> Início");
                System.out.println("1 - Administrador");
                System.out.println("2 - Usuário");
                System.out.println("3 - Pesquisar por Padrão (KMP/BM)");
                System.out.println("0 - Sair");

                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }

                switch (opcao) {
                    case 1:
                        MenuAdministrador menuAdministrador = new MenuAdministrador();
                        menuAdministrador.menu();
                        break;
                    case 2:
                        MenuLogin menuLogin = new MenuLogin();
                        menuLogin.menu();
                        break;
                    case 3:
                        MenuPesquisa menuPesquisa = new MenuPesquisa();
                        menuPesquisa.menu();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } while (opcao != 0);

        } catch (Exception e) {
            System.err.println("Erro fatal no sistema:");
            e.printStackTrace();
        } finally {
            console.close();
        }

  }

}