import arquivos.ArquivoHorarios;
import arquivos.ArquivoRotas;
import entidades.Horario;
import entidades.Rota;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuHorarios {

    private ArquivoHorarios arqHorarios;
    private ArquivoRotas arqRotas;
    private static Scanner console = new Scanner(System.in);
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public MenuHorarios() throws Exception {
        arqHorarios = new ArquivoHorarios();
        arqRotas = new ArquivoRotas();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\n-------------------------------");
            System.out.println("          HORÁRIOS");
            System.out.println("-------------------------------");
            System.out.println("1 - Incluir");
            System.out.println("2 - Buscar");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Associar Rota");
            System.out.println("6 - Desassociar Rota");
            System.out.println("7 - Buscar Rotas do Horário");
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
                    associarRota();
                    break;
                case 6:
                    desassociarRota();
                    break;
                case 7:
                    buscarRotasDoHorario();
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
    System.out.println("\nInclusão de Horário");
    System.out.print("Hora de Partida (HH:mm): ");
    String horaPartidaStr = console.nextLine();
    System.out.print("Hora de Chegada (HH:mm): ");
    String horaChegadaStr = console.nextLine();

    try {
        LocalTime horaPartida = LocalTime.parse(horaPartidaStr, timeFormatter);
        LocalTime horaChegada = LocalTime.parse(horaChegadaStr, timeFormatter);
        
        Horario horario = new Horario(horaPartida, horaChegada);
        int id = arqHorarios.create(horario);
        System.out.println("Horário criado com ID: " + id);
    } catch (Exception e) {
        System.out.println("Erro ao criar horário: " + e.getMessage());
    }
}

    public void buscar() {
        System.out.println("\nBusca de Horário");
        System.out.print("Hora de Partida (HH:mm): ");
        String horaPartidaStr = console.nextLine();
        System.out.print("Hora de Chegada (HH:mm): ");
        String horaChegadaStr = console.nextLine();

        try {
            LocalTime horaPartida = LocalTime.parse(horaPartidaStr, timeFormatter);
            LocalTime horaChegada = LocalTime.parse(horaChegadaStr, timeFormatter);
            
            Horario horario = arqHorarios.readByHorarios(horaPartida, horaChegada);
            if (horario != null) {
                System.out.println(horario);
            } else {
                System.out.println("Horário não encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar horário: " + e.getMessage());
        }
    }



    public void alterar() {
        System.out.println("\nAlteração de Horário");
        System.out.print("Hora de Partida do horário a ser alterado (HH:mm): ");
        String horaPartidaBuscaStr = console.nextLine();
        System.out.print("Hora de Chegada do horário a ser alterado (HH:mm): ");
        String horaChegadaBuscaStr = console.nextLine();

        try {
            LocalTime horaPartidaBusca = LocalTime.parse(horaPartidaBuscaStr, timeFormatter);
            LocalTime horaChegadaBusca = LocalTime.parse(horaChegadaBuscaStr, timeFormatter);
            
            Horario horario = arqHorarios.readByHorarios(horaPartidaBusca, horaChegadaBusca);
            if (horario != null) {
                System.out.println(horario);
                System.out.print("Nova hora de partida (HH:mm) (Enter para manter): ");
                String horaPartidaStr = console.nextLine();
                if (!horaPartidaStr.isEmpty()) {
                    horario.setHoraPartida(LocalTime.parse(horaPartidaStr, timeFormatter));
                }
                System.out.print("Nova hora de chegada (HH:mm) (Enter para manter): ");
                String horaChegadaStr = console.nextLine();
                if (!horaChegadaStr.isEmpty()) {
                    horario.setHoraChegada(LocalTime.parse(horaChegadaStr, timeFormatter));
                }

                if (arqHorarios.update(horario)) {
                    System.out.println("Horário atualizado com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar horário!");
                }
            } else {
                System.out.println("Horário não encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar horário: " + e.getMessage());
        }
    }

    public void excluir() {
        System.out.println("\nExclusão de Horário");
        System.out.print("Hora de Partida do horário a ser excluído (HH:mm): ");
        String horaPartidaBuscaStr = console.nextLine();
        System.out.print("Hora de Chegada do horário a ser excluído (HH:mm): ");
        String horaChegadaBuscaStr = console.nextLine();

        try {
            LocalTime horaPartidaBusca = LocalTime.parse(horaPartidaBuscaStr, timeFormatter);
            LocalTime horaChegadaBusca = LocalTime.parse(horaChegadaBuscaStr, timeFormatter);
            
            Horario horario = arqHorarios.readByHorarios(horaPartidaBusca, horaChegadaBusca);
            if (horario != null) {
                System.out.println(horario);
                System.out.print("Confirma exclusão? (S/N): ");
                String confirmacao = console.nextLine();
                if (confirmacao.equalsIgnoreCase("S")) {
                    if (arqHorarios.delete(horario.getID())) {
                        System.out.println("Horário excluído com sucesso!");
                    } else {
                        System.out.println("Erro ao excluir horário!");
                    }
                }
            } else {
                System.out.println("Horário não encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir horário: " + e.getMessage());
        }
    }

    public void buscarRotasDoHorario() {
        System.out.println("\nBUSCAR ROTAS DO HORÁRIO");
        System.out.print("ID do Horário: ");
        int horarioId = Integer.parseInt(console.nextLine());

        try {
            ArrayList<Integer> rotasIds = arqRotas.buscarRotasDoHorario(horarioId);
            if (!rotasIds.isEmpty()) {
                System.out.println("Rotas associadas (IDs): " + rotasIds);
            } else {
                System.out.println("Nenhuma rota associada a este horário.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar rotas do horário: " + e.getMessage());
        }
    }

    public void desassociarRota() {
        System.out.println("\nDESASSOCIAR ROTA DO HORÁRIO");
        System.out.print("ID do Horário: ");
        int horarioId = Integer.parseInt(console.nextLine());
        System.out.print("ID da Rota a ser desassociada: ");
        int rotaId = Integer.parseInt(console.nextLine());

        try {
            if (arqRotas.desassociarHorario(rotaId, horarioId)) {
                System.out.println("Rota desassociada do horário com sucesso!");
            } else {
                System.out.println("Falha ao desassociar rota (verifique os IDs).");
            }
        } catch (Exception e) {
            System.out.println("Erro ao desassociar rota: " + e.getMessage());
        }
    }

    public void associarRota() {
        System.out.println("\nASSOCIAR ROTA AO HORÁRIO");
        System.out.print("ID do Horário: ");
        int horarioId = Integer.parseInt(console.nextLine());
        System.out.print("ID da Rota a ser associada: ");
        int rotaId = Integer.parseInt(console.nextLine());

        try {
            if (arqRotas.associarHorario(rotaId, horarioId)) {
                System.out.println("Rota associada ao horário com sucesso!");
            } else {
                System.out.println("Falha ao associar rota (associação já existe ou IDs inválidos).");
            }
        } catch (Exception e) {
            System.out.println("Erro ao associar rota: " + e.getMessage());
        }
    }
}
