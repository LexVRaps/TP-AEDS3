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
                System.out.println("1 - Usuários");
                System.out.println("2 - Rotas");
                System.out.println("3 - Horários");
                System.out.println("4 - Assentos");
                System.out.println("5 - Passagens");
                System.out.println("0 - Sair");

                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }

                switch (opcao) {
                    case 1:
                        MenuUsuarios menuUsuarios = new MenuUsuarios();
                        menuUsuarios.menu();
                        break;
                    case 2:
                        MenuRotas menuRotas = new MenuRotas();
                        menuRotas.menu();
                        break;
                    case 3:
                        MenuHorarios menuHorarios = new MenuHorarios();
                        menuHorarios.menu();
                        break;
                    case 4:
                        MenuAssentos menuAssentos = new MenuAssentos();
                        menuAssentos.menu();
                        break;
                    case 5:
                        MenuPassagens menuPassagens = new MenuPassagens();
                        menuPassagens.menu();
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