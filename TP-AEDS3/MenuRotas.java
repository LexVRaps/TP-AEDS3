import arquivos.ArquivoHorarios;
import arquivos.ArquivoRotas;
import entidades.Horario;
import entidades.Rota;
import java.util.Scanner;
import java.util.ArrayList;

public class MenuRotas {

    private ArquivoRotas arqRotas;
    private static Scanner console = new Scanner(System.in);

    public MenuRotas() throws Exception {
        arqRotas = new ArquivoRotas();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\n-------------------------------");
            System.out.println("           ROTAS");
            System.out.println("-------------------------------");
            System.out.println("1 - Incluir");
            System.out.println("2 - Buscar");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Associar Horário");
            System.out.println("6 - Desassociar Horário");
            System.out.println("7 - Buscar Horários da Rota");
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
                    buscar();
                    break;
                case 3:
                    alterar();
                    break;
                case 4:
                    excluir();
                    break;
                case 5:
                    associarHorario();
                    break;
                case 6:
                    desassociarHorario();
                    break;
                case 7:
                    buscarHorariosDaRota();
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
    System.out.println("\nInclusão de Rota");
    System.out.print("Origem: ");
    String origem = console.nextLine();
    System.out.print("Destino: ");
    String destino = console.nextLine();
    System.out.print("Distância (km): ");
    float distancia = Float.parseFloat(console.nextLine());

    Rota rota = new Rota(origem, destino, distancia);
    try {
        int id = arqRotas.create(rota);
        System.out.println("Rota criada com ID: " + id);
        
        System.out.print("Deseja associar horários a esta rota agora? (S/N): ");
        String resposta = console.nextLine();
        if (resposta.equalsIgnoreCase("S")) {
            associarHorarioExistente(id);
        }
    } catch (Exception e) {
        System.out.println("Erro ao criar rota: " + e.getMessage());
    }
}

private void associarHorarioExistente(int rotaId) {
    try {
        ArquivoHorarios arqHorarios = new ArquivoHorarios();
        ArrayList<Horario> horarios = arqHorarios.readAll();
        
        if (horarios.isEmpty()) {
            System.out.println("Nenhum horário cadastrado. Crie horários primeiro.");
            return;
        }
        
        System.out.println("\nHorários disponíveis:");
        for (Horario h : horarios) {
            System.out.println("ID: " + h.getID() + " - " + h.getHoraPartida() + " às " + h.getHoraChegada());
        }
        
        System.out.print("ID do Horário para associar: ");
        int horarioId = Integer.parseInt(console.nextLine());
        
        if (arqRotas.associarHorario(rotaId, horarioId)) {
            System.out.println("Horário associado com sucesso!");
        } else {
            System.out.println("Falha ao associar horário!");
        }
    } catch (Exception e) {
        System.out.println("Erro ao associar horário: " + e.getMessage());
    }
}

    public void buscar() {
        System.out.println("\nBusca de Rota");
        System.out.print("Origem da rota: ");
        String origem = console.nextLine();
        System.out.print("Destino da rota: ");
        String destino = console.nextLine();

        try {
            Rota rota = arqRotas.readByOrigemDestino(origem, destino);
            if (rota != null) {
                System.out.println(rota);
            } else {
                System.out.println("Rota não encontrada!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar rota: " + e.getMessage());
        }
    }

    public void alterar() {
        System.out.println("\nAlteração de Rota");
        System.out.print("Origem da rota a ser alterada: ");
        String origemBusca = console.nextLine();
        System.out.print("Destino da rota a ser alterada: ");
        String destinoBusca = console.nextLine();

        try {
            Rota rota = arqRotas.readByOrigemDestino(origemBusca, destinoBusca);
            if (rota != null) {
                System.out.println(rota);
                System.out.print("Nova origem (Enter para manter): ");
                String origem = console.nextLine();
                if (!origem.isEmpty()) {
                    rota.setOrigem(origem);
                }
                System.out.print("Novo destino (Enter para manter): ");
                String destino = console.nextLine();
                if (!destino.isEmpty()) {
                    rota.setDestino(destino);
                }
                System.out.print("Nova distância (Enter para manter): ");
                String distanciaStr = console.nextLine();
                if (!distanciaStr.isEmpty()) {
                    rota.setDistancia(Float.parseFloat(distanciaStr));
                }

                if (arqRotas.update(rota)) {
                    System.out.println("Rota atualizada com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar rota!");
                }
            } else {
                System.out.println("Rota não encontrada!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar rota: " + e.getMessage());
        }
    }

    public void buscarHorariosDaRota() {
        System.out.println("\nBUSCAR HORÁRIOS DA ROTA");
        System.out.print("Origem da rota: ");
        String origemBusca = console.nextLine();
        System.out.print("Destino da rota: ");
        String destinoBusca = console.nextLine();

        try {
            Rota rota = arqRotas.readByOrigemDestino(origemBusca, destinoBusca);
            if (rota != null) {
                System.out.println("Rota: " + rota.getID() + " - " + rota.getOrigem() + " -> " + rota.getDestino());
                ArrayList<Integer> horariosIds = arqRotas.buscarHorariosDaRota(rota.getID());
                if (!horariosIds.isEmpty()) {
                    System.out.println("Horários associados (IDs): " + horariosIds);
                } else {
                    System.out.println("Nenhum horário associado a esta rota.");
                }
            } else {
                System.out.println("Rota não encontrada!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar horários da rota: " + e.getMessage());
        }
    }

    public void desassociarHorario() {
        System.out.println("\nDESASSOCIAR HORÁRIO DA ROTA");
        System.out.print("ID da Rota: ");
        int rotaId = Integer.parseInt(console.nextLine());
        System.out.print("ID do Horário a ser desassociado: ");
        int horarioId = Integer.parseInt(console.nextLine());

        try {
            if (arqRotas.desassociarHorario(rotaId, horarioId)) {
                System.out.println("Horário desassociado da rota com sucesso!");
            } else {
                System.out.println("Falha ao desassociar horário (verifique os IDs).");
            }
        } catch (Exception e) {
            System.out.println("Erro ao desassociar horário: " + e.getMessage());
        }
    }

    public void associarHorario() {
        System.out.println("\nASSOCIAR HORÁRIO À ROTA");
        System.out.print("ID da Rota: ");
        int rotaId = Integer.parseInt(console.nextLine());
        System.out.print("ID do Horário a ser associado: ");
        int horarioId = Integer.parseInt(console.nextLine());

        try {
            if (arqRotas.associarHorario(rotaId, horarioId)) {
                System.out.println("Horário associado à rota com sucesso!");
            } else {
                System.out.println("Falha ao associar horário (associação já existe ou IDs inválidos).");
            }
        } catch (Exception e) {
            System.out.println("Erro ao associar horário: " + e.getMessage());
        }
    }

    public void excluir() {
        System.out.println("\nExclusão de Rota");
        System.out.print("Origem da rota a ser excluída: ");
        String origemBusca = console.nextLine();
        System.out.print("Destino da rota a ser excluída: ");
        String destinoBusca = console.nextLine();

        try {
            Rota rota = arqRotas.readByOrigemDestino(origemBusca, destinoBusca);
            if (rota != null) {
                System.out.println(rota);
                System.out.print("Confirma exclusão? (S/N): ");
                String confirmacao = console.nextLine();
                if (confirmacao.equalsIgnoreCase("S")) {
                    if (arqRotas.delete(rota.getID())) {
                        System.out.println("Rota excluída com sucesso!");
                    } else {
                        System.out.println("Erro ao excluir rota!");
                    }
                }
            } else {
                System.out.println("Rota não encontrada!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir rota: " + e.getMessage());
        }
    }
    
}
