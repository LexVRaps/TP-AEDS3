import java.util.Scanner;

public class MenuAdministrador {
    private Scanner console;

    public MenuAdministrador() {
        console = new Scanner(System.in);
    }

    public void menu() throws Exception {
        int opcao;
        do {
            System.out.println("\n\nMENU ADMINISTRADOR");
            System.out.println("--------------------");
            System.out.println("1 - Gerenciar Usuários");
            System.out.println("2 - Gerenciar Rotas");
            System.out.println("3 - Gerenciar Horários");
            System.out.println("4 - Gerenciar Assentos");
            System.out.println("5 - Gerenciar Passagens");
            System.out.println("6 - Fazer Backup");
            System.out.println("0 - Voltar");

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
                case 6:
                    MenuBackup menuBackup = new MenuBackup();
                    menuBackup.menu();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }
}
