import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import entidades.Usuario;
import entidades.Passagem;
import entidades.Rota;
import entidades.Horario;
import entidades.Assento;
import arquivos.ArquivoPassagens;
import arquivos.ArquivoUsuarios;
import arquivos.ArquivoRotas;
import arquivos.ArquivoHorarios;
import arquivos.ArquivoAssentos;

public class MenuUsuario {
    private Scanner console;
    private Usuario usuarioLogado;
    private ArquivoPassagens arqPassagens;
    private ArquivoUsuarios arqUsuarios;
    private ArquivoRotas arqRotas;
    private ArquivoHorarios arqHorarios;
    private ArquivoAssentos arqAssentos;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MenuUsuario(Usuario usuarioLogado) throws Exception {
        console = new Scanner(System.in);
        this.arqPassagens = new ArquivoPassagens();
        this.arqUsuarios = new ArquivoUsuarios();
        this.arqRotas = new ArquivoRotas();
        this.arqHorarios = new ArquivoHorarios();
        this.arqAssentos = new ArquivoAssentos();
        this.usuarioLogado = usuarioLogado;
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nMENU USUÁRIO");
            System.out.println("--------------------");
            System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
            System.out.println("1 - Comprar Passagem");
            System.out.println("2 - Buscar Minhas Passagens");
            System.out.println("3 - Cancelar Passagem");
            System.out.println("0 - Logout");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            try {
                switch (opcao) {
                    case 1:
                        comprarPassagem();
                        break;
                    case 2:
                        buscarMinhasPassagens();
                        break;
                    case 3:
                        cancelarPassagem();
                        break;
                    case 0:
                        System.out.println("Fazendo logout...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Erro na operação: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void comprarPassagem() throws Exception {
        System.out.println("\nCOMPRAR PASSAGEM");
        System.out.print("Origem: ");
        String origem = console.nextLine();
        System.out.print("Destino: ");
        String destino = console.nextLine();
        System.out.print("Data da Viagem (dd/MM/yyyy): ");
        String dataStr = console.nextLine();
        LocalDate dataViagem = LocalDate.parse(dataStr, dateFormatter);

        Rota rota = arqRotas.readByOrigemDestino(origem, destino);
        if (rota == null) {
            System.out.println("Erro: Rota '" + origem + " -> " + destino + "' não encontrada!");
            return;
        }
        int rotaId = rota.getID();

        ArrayList<Horario> horariosDisponiveis = new ArrayList<>();
        ArrayList<Integer> horarioIds = arqRotas.buscarHorariosDaRota(rotaId);
        for (Integer id : horarioIds) {
            Horario h = arqHorarios.read(id);
            if (h != null) {
                horariosDisponiveis.add(h);
            }
        } 
        if (horariosDisponiveis.isEmpty()) {
            System.out.println("Nenhum horário disponível para esta rota.");
            return;
        }

        System.out.println("\nHorários disponíveis:");
        for (int i = 0; i < horariosDisponiveis.size(); i++) {
            Horario h = horariosDisponiveis.get(i);
            System.out.println((i + 1) + " - ID: " + h.getID() + " | " + h.getHoraPartida() + " -> " + h.getHoraChegada());
        }

        System.out.print("Selecione o número do horário: ");
        int idxHorario = Integer.parseInt(console.nextLine()) - 1;
        if (idxHorario < 0 || idxHorario >= horariosDisponiveis.size()) {
            System.out.println("Seleção de horário inválida.");
            return;
        }
        Horario horarioSelecionado = horariosDisponiveis.get(idxHorario);
        int horarioId = horarioSelecionado.getID();

        ArrayList<Assento> assentosDisponiveis = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Assento a = arqAssentos.read(i);
            if (a != null) {
                assentosDisponiveis.add(a);
            }
        }
        ArrayList<Passagem> passagensExistentes = new ArrayList<>();
        ArrayList<Passagem> passagensPorHorario = arqPassagens.readByHorario(horarioId);
        for (Passagem p : passagensPorHorario) {
            if (p.getRotaId() == rotaId && p.getDataViagem().isEqual(dataViagem)) {
                passagensExistentes.add(p);
            }
        } 
        
        ArrayList<Integer> assentosOcupadosIds = new ArrayList<>();
        for (Passagem p : passagensExistentes) {
            assentosOcupadosIds.add(p.getAssentoId());
        }

        System.out.println("\nAssentos disponíveis:");
        ArrayList<Assento> assentosLivres = new ArrayList<>();
        for (Assento a : assentosDisponiveis) {
            if (assentosOcupadosIds.contains(a.getID())) {
                assentosLivres.add(a);
                System.out.print(a.getNumeroAssento() + " ");
            }
        }
        System.out.println();

        if (assentosLivres.isEmpty()) {
            System.out.println("Não há assentos disponíveis para este horário e data.");
            return;
        }

        System.out.print("Selecione o número do assento: ");
        String numeroAssento = console.nextLine();
        Assento assentoSelecionado = null;
        for (Assento a : assentosLivres) {
            if (a.getNumeroAssento().equalsIgnoreCase(numeroAssento)) {
                assentoSelecionado = a;
                break;
            }
        }

        if (assentoSelecionado == null) {
            System.out.println("Assento inválido ou ocupado.");
            return;
        }
        int assentoId = assentoSelecionado.getID();

        System.out.print("Preço da Passagem: ");
        float preco = Float.parseFloat(console.nextLine());

        Passagem novaPassagem = new Passagem(dataViagem, preco, rotaId, horarioId, assentoId, usuarioLogado.getID());
        int idPassagem = arqPassagens.create(novaPassagem, rotaId, horarioId, assentoId, usuarioLogado.getID());
        
        usuarioLogado.adicionarPassagem(idPassagem);
        arqUsuarios.update(usuarioLogado);

        System.out.println("\nPassagem comprada com sucesso! ID: " + idPassagem);;
    }

    private void buscarMinhasPassagens() throws Exception {
        System.out.println("\nMINHAS PASSAGENS");
        
        Usuario usuarioAtualizado = arqUsuarios.read(usuarioLogado.getID());
        if (usuarioAtualizado == null) {
            System.out.println("Erro: Usuário não encontrado no sistema.");
            return;
        }
        this.usuarioLogado = usuarioAtualizado; 
        if (usuarioLogado.getPassagensIds().isEmpty()) {
            System.out.println("Você não possui passagens compradas.");
            return;
        }

        System.out.println("Total de passagens: " + usuarioLogado.getPassagensIds().size());
        System.out.println("\nDetalhes das Passagens:");
        
        for (Integer passagemId : usuarioLogado.getPassagensIds()) {
            Passagem p = arqPassagens.read(passagemId);
            if (p != null) {
                Rota r = arqRotas.read(p.getRotaId());
                Horario h = arqHorarios.read(p.getHorarioId());
                Assento a = arqAssentos.read(p.getAssentoId());
                
                System.out.println("--- Passagem ID: " + p.getID() + " ---");
                System.out.println("  Rota: " + r.getOrigem() + " -> " + r.getDestino());
                System.out.println("  Data: " + p.getDataViagem().format(dateFormatter));
                if (h != null) {
                    System.out.println("  Horário: " + h.getHoraPartida() + " - " + h.getHoraChegada());
                }
                if (a != null) {
                    System.out.println("  Assento: " + a.getNumeroAssento());
                }
                System.out.println("  Preço: R$ " + String.format("%.2f", p.getPreco()));
                System.out.println("---");
            }
        }
    }

    private void cancelarPassagem() throws Exception {
        System.out.println("\nCANCELAR PASSAGEM");
        buscarMinhasPassagens();

        if (usuarioLogado.getPassagensIds().isEmpty()) {
            return;
        }

        System.out.print("Digite o ID da passagem que deseja cancelar: ");
        int idPassagem = Integer.parseInt(console.nextLine());

        Passagem passagem = arqPassagens.read(idPassagem);
        if (passagem == null) {
            System.out.println("Erro: Passagem não encontrada.");
            return;
        }

        if (passagem.getUsuarioId() != usuarioLogado.getID()) {
            System.out.println("Erro: Esta passagem não pertence a você.");
            return;
        }

        usuarioLogado.removerPassagem(idPassagem);
        arqUsuarios.update(usuarioLogado);

        if (arqPassagens.delete(idPassagem)) {
            System.out.println("Passagem ID " + idPassagem + " cancelada com sucesso!");
        } else {
            System.out.println("Erro ao excluir a passagem do arquivo.");
            usuarioLogado.adicionarPassagem(idPassagem);
            arqUsuarios.update(usuarioLogado);
        }
    }
}
