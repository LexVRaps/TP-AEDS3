import arquivos.ArquivoAssentos;
import entidades.Assento;
import java.util.Scanner;

public class MenuAssentos {

    private ArquivoAssentos arqAssentos;
    private static Scanner console = new Scanner(System.in);

    public MenuAssentos() throws Exception {
        arqAssentos = new ArquivoAssentos();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\n-------------------------------");
            System.out.println("          ASSENTOS");
            System.out.println("-------------------------------");
            System.out.println("1 - Incluir");
            System.out.println("2 - Buscar por Número");
            System.out.println("3 - Alterar por Número");
            System.out.println("4 - Excluir por Número");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            try {
                opcao = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    incluir();
                    break;
                case 2:
                    buscarPorNumero();
                    break;
                case 3:
                    alterarPorNumero();
                    break;
                case 4:
                    excluirPorNumero();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    public void incluir() {
        System.out.println("\nInclusão de Assento");
        System.out.print("Número do Assento: ");
        String numero = console.nextLine();
        
        try {
            Assento assentoExistente = arqAssentos.readByNumeroAssento(numero);
            if (assentoExistente != null) {
                System.out.println("Erro: Já existe um assento com o número '" + numero + "'!");
                return;
            }
            
            System.out.print("Disponível? (S/N): ");
            String disponivelStr = console.nextLine();
            boolean disponivel = disponivelStr.equalsIgnoreCase("S");

            Assento assento = new Assento(numero, disponivel);
            int id = arqAssentos.create(assento);
            System.out.println("Assento criado com ID: " + id);
        } catch (Exception e) {
            System.out.println("Erro ao criar assento: " + e.getMessage());
        }
    }

    public void buscarPorNumero() {
        System.out.println("\nBusca de Assento por Número");
        System.out.print("Número do assento: ");
        String numero = console.nextLine();

        try {
            Assento assento = arqAssentos.readByNumeroAssento(numero);
            if (assento != null) {
                System.out.println(assento);
            } else {
                System.out.println("Assento não encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar assento: " + e.getMessage());
        }
    }

    public void alterarPorNumero() {
        System.out.println("\nAlteração de Assento por Número");
        System.out.print("Número do assento: ");
        String numero = console.nextLine();

        try {
            Assento assento = arqAssentos.readByNumeroAssento(numero);
            if (assento != null) {
                System.out.println(assento);
                System.out.print("Novo número (Enter para manter): ");
                String novoNumero = console.nextLine();
                if (!novoNumero.isEmpty()) {
                    Assento assentoExistente = arqAssentos.readByNumeroAssento(novoNumero);
                    if (assentoExistente != null && assentoExistente.getID() != assento.getID()) {
                        System.out.println("Erro: Já existe um assento com o número '" + novoNumero + "'!");
                        return;
                    }
                    assento.setNumeroAssento(novoNumero);
                }
                System.out.print("Disponível? (S/N/Enter para manter): ");
                String disponivelStr = console.nextLine();
                if (!disponivelStr.isEmpty()) {
                    assento.setDisponivel(disponivelStr.equalsIgnoreCase("S"));
                }

                if (arqAssentos.update(assento)) {
                    System.out.println("Assento atualizado com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar assento!");
                }
            } else {
                System.out.println("Assento não encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar assento: " + e.getMessage());
        }
    }

    public void excluirPorNumero() {
        System.out.println("\nExclusão de Assento por Número");
        System.out.print("Número do assento: ");
        String numero = console.nextLine();

        try {
            Assento assento = arqAssentos.readByNumeroAssento(numero);
            if (assento != null) {
                System.out.println(assento);
                System.out.print("Confirma exclusão? (S/N): ");
                String confirmacao = console.nextLine();
                if (confirmacao.equalsIgnoreCase("S")) {
                    if (arqAssentos.delete(assento.getID())) {
                        System.out.println("Assento excluído com sucesso!");
                    } else {
                        System.out.println("Erro ao excluir assento!");
                    }
                }
            } else {
                System.out.println("Assento não encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir assento: " + e.getMessage());
        }
    }
}
