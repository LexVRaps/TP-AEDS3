import arquivos.ArquivoPassagens;
import arquivos.ArquivoUsuarios;
import entidades.Passagem;
import arquivos.ArquivoRotas;
import arquivos.ArquivoHorarios;
import entidades.Rota;
import entidades.Horario;
import entidades.Usuario;
import java.time.LocalTime;
import arquivos.ArquivoAssentos;
import entidades.Assento;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import arquivos.ArquivoAssentos; 
public class MenuPassagens {

    private ArquivoPassagens arqPassagens;
    private ArquivoUsuarios arqUsuarios;
    private ArquivoRotas arqRotas;
    private ArquivoHorarios arqHorarios;
    private ArquivoAssentos arqAssentos; 
    private static Scanner console = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MenuPassagens() throws Exception {
        arqPassagens = new ArquivoPassagens();
        arqUsuarios = new ArquivoUsuarios();
        arqRotas = new ArquivoRotas();
        arqHorarios = new ArquivoHorarios();
        arqAssentos = new ArquivoAssentos(); 
    }

   public void menu() {
    int opcao;
    do {
        System.out.println("\n\n-------------------------------");
        System.out.println("          PASSAGENS");
        System.out.println("-------------------------------");
        System.out.println("1 - Incluir");
        System.out.println("2 - Buscar por ID");
        System.out.println("3 - Buscar por Rota (Origem-Destino)");
        System.out.println("4 - Buscar por Horário");
        System.out.println("5 - Buscar por Assento");
        System.out.println("6 - Buscar por Usuário");
        System.out.println("7 - Buscar por Data");
        System.out.println("8 - Buscar por Preço");
        System.out.println("9 - Alterar");
        System.out.println("10 - Excluir");
        System.out.println("11 - Associar Passagem a Usuário"); 
        System.out.println("12 - Desassociar Passagem de Usuário"); 
        System.out.println("13 - Listar Passagens do Usuário"); 
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
                buscarPorRota();
                break;
            case 4:
                buscarPorHorario();
                break;
            case 5:
                buscarPorAssento();
                break;
            case 6:
                buscarPorUsuario();
                break;
            case 7:
                buscarPorData();
                break;
            case 8:
                buscarPorPreco();
                break;
            case 9:
                alterar();
                break;
            case 10:
                excluir();
                break;
            case 11:
                associarPassagemAUsuario(); 
                break;
            case 12:
                desassociarPassagemDeUsuario(); 
                break;
            case 13:
                listarPassagensDoUsuario(); 
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
    System.out.println("\nInclusão de Passagem");
    
    System.out.print("ID do Usuário: ");
    int usuarioId = Integer.parseInt(console.nextLine());
    
    System.out.print("Data da Viagem (dd/MM/yyyy): ");
    String dataStr = console.nextLine();
    LocalDate dataViagem = LocalDate.parse(dataStr, dateFormatter);
    System.out.print("Preço: ");
    float preco = Float.parseFloat(console.nextLine());
    
    System.out.print("Origem da Rota: ");
    String origem = console.nextLine();
    System.out.print("Destino da Rota: ");
    String destino = console.nextLine();
    
    System.out.print("Hora de Partida (HH:mm): ");
    String horaPartidaStr = console.nextLine();
    System.out.print("Hora de Chegada (HH:mm): ");
    String horaChegadaStr = console.nextLine();
    
    System.out.print("Número do Assento: ");
    String numeroAssento = console.nextLine();

    try {
        Rota rota = arqRotas.readByOrigemDestino(origem, destino);
        if (rota == null) {
            System.out.println("Erro: Rota '" + origem + " -> " + destino + "' não encontrada!");
            System.out.println("Por favor, crie a rota antes de criar a passagem.");
            return;
        }
        int rotaId = rota.getID();
        
        LocalTime horaPartida = LocalTime.parse(horaPartidaStr, java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime horaChegada = LocalTime.parse(horaChegadaStr, java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        Horario horario = arqHorarios.readByHorarios(horaPartida, horaChegada);
        if (horario == null) {
            System.out.println("Erro: Horário '" + horaPartidaStr + " -> " + horaChegadaStr + "' não encontrado!");
            System.out.println("Por favor, crie o horário antes de criar a passagem.");
            return;
        }
        int horarioId = horario.getID();
        
        Assento assento = arqAssentos.readByNumeroAssento(numeroAssento);
        if (assento == null) {
            System.out.println("Erro: Assento '" + numeroAssento + "' não encontrado!");
            System.out.println("Por favor, crie o assento antes de criar a passagem.");
            return;
        }
        int assentoId = assento.getID();
        
        Passagem passagem = new Passagem(dataViagem, preco, rotaId, horarioId, assentoId, usuarioId);
        int id = arqPassagens.create(passagem, rotaId, horarioId, assentoId, usuarioId);
        System.out.println("Passagem criada com ID: " + id);
    } catch (Exception e) {
        System.out.println("Erro ao criar passagem: " + e.getMessage());
    }
}
    public void buscar() {
        System.out.println("\nBusca de Passagem");
        System.out.print("ID da passagem: ");
        int id = Integer.parseInt(console.nextLine());

        try {
            Passagem passagem = arqPassagens.read(id);
            if (passagem != null) {
                System.out.println(passagem);
            } else {
                System.out.println("Passagem não encontrada!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagem: " + e.getMessage());
        }
    }

    public void buscarPorRota() {
        System.out.println("\nBusca de Passagens por Rota");
        System.out.print("ID da Rota: ");
        int rotaId = Integer.parseInt(console.nextLine());

        try {
            ArrayList<Passagem> passagens = arqPassagens.readByRota(rotaId);
            if (!passagens.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Passagem p : passagens) {
                    System.out.println(p);
                    System.out.println("---");
                }
            } else {
                System.out.println("Nenhuma passagem encontrada para esta rota!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }

    public void buscarPorHorario() {
        System.out.println("\nBusca de Passagens por Horário");
        System.out.print("ID do Horário: ");
        int horarioId = Integer.parseInt(console.nextLine());

        try {
            ArrayList<Passagem> passagens = arqPassagens.readByHorario(horarioId);
            if (!passagens.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Passagem p : passagens) {
                    System.out.println(p);
                    System.out.println("---");
                }
            } else {
                System.out.println("Nenhuma passagem encontrada para este horário!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }

    public void buscarPorAssento() {
        System.out.println("\nBusca de Passagens por Assento");
        System.out.print("ID do Assento: ");
        int assentoId = Integer.parseInt(console.nextLine());

        try {
            ArrayList<Passagem> passagens = arqPassagens.readByAssento(assentoId);
            if (!passagens.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Passagem p : passagens) {
                    System.out.println(p);
                    System.out.println("---");
                }
            } else {
                System.out.println("Nenhuma passagem encontrada para este assento!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }
    public void buscarPorUsuario() {
    System.out.println("\nBusca de Passagens por Usuário");
    System.out.print("ID do Usuário: ");
    int usuarioId = Integer.parseInt(console.nextLine());

    try {
        ArrayList<Passagem> passagens = arqPassagens.readByUsuario(usuarioId);
        if (!passagens.isEmpty()) {
            System.out.println("\nPassagens encontradas:");
            for (Passagem p : passagens) {
                System.out.println(p);
                System.out.println("---");
            }
        } else {
            System.out.println("Nenhuma passagem encontrada para este usuário!");
        }
    } catch (Exception e) {
        System.out.println("Erro ao buscar passagens: " + e.getMessage());
    }
}

   public void alterar() {
    System.out.println("\nAlteração de Passagem");
    System.out.print("ID da passagem: ");
    int id = Integer.parseInt(console.nextLine());

    try {
        Passagem passagem = arqPassagens.read(id);
        if (passagem != null) {
            System.out.println(passagem);
            
            int rotaIdAntigo = passagem.getRotaId();
            int horarioIdAntigo = passagem.getHorarioId();
            int assentoIdAntigo = passagem.getAssentoId();
            int usuarioIdAntigo = passagem.getUsuarioId();
            
            System.out.print("Nova data (dd/MM/yyyy) (Enter para manter): ");
            String dataStr = console.nextLine();
            if (!dataStr.isEmpty()) {
                passagem.setDataViagem(LocalDate.parse(dataStr, dateFormatter));
            }
            
            System.out.print("Novo preço (Enter para manter): ");
            String precoStr = console.nextLine();
            if (!precoStr.isEmpty()) {
                passagem.setPreco(Float.parseFloat(precoStr));
            }
            
            System.out.print("Novo ID da Rota (Enter para manter): ");
            String rotaStr = console.nextLine();
            int rotaIdNovo = rotaStr.isEmpty() ? rotaIdAntigo : Integer.parseInt(rotaStr);
            if (!rotaStr.isEmpty()) {
                passagem.setRotaId(rotaIdNovo);
            }
            
            System.out.print("Novo ID do Horário (Enter para manter): ");
            String horarioStr = console.nextLine();
            int horarioIdNovo = horarioStr.isEmpty() ? horarioIdAntigo : Integer.parseInt(horarioStr);
            if (!horarioStr.isEmpty()) {
                passagem.setHorarioId(horarioIdNovo);
            }
            
            System.out.print("Novo ID do Assento (Enter para manter): ");
            String assentoStr = console.nextLine();
            int assentoIdNovo = assentoStr.isEmpty() ? assentoIdAntigo : Integer.parseInt(assentoStr);
            if (!assentoStr.isEmpty()) {
                passagem.setAssentoId(assentoIdNovo);
            }

            System.out.print("Novo ID do Usuário (Enter para manter): "); 
            String usuarioStr = console.nextLine();
            int usuarioIdNovo = usuarioStr.isEmpty() ? usuarioIdAntigo : Integer.parseInt(usuarioStr);
            if (!usuarioStr.isEmpty()) {
                passagem.setUsuarioId(usuarioIdNovo);
            }

            if (arqPassagens.update(passagem, rotaIdAntigo, horarioIdAntigo, assentoIdAntigo, usuarioIdAntigo,
                                   rotaIdNovo, horarioIdNovo, assentoIdNovo, usuarioIdNovo)) {
                System.out.println("Passagem atualizada com sucesso!");
            } else {
                System.out.println("Erro ao atualizar passagem!");
            }
        } else {
            System.out.println("Passagem não encontrada!");
        }
    } catch (Exception e) {
        System.out.println("Erro ao alterar passagem: " + e.getMessage());
    }
}
   public void excluir() {
    System.out.println("\nExclusão de Passagem");
    System.out.print("ID da passagem: ");
    int id = Integer.parseInt(console.nextLine());

    try {
        Passagem passagem = arqPassagens.read(id);
        if (passagem != null) {
            System.out.println(passagem);
            System.out.print("Confirma exclusão? (S/N): ");
            String confirmacao = console.nextLine();
            if (confirmacao.equalsIgnoreCase("S")) {
                if (arqPassagens.delete(id, passagem.getRotaId(), 
                                       passagem.getHorarioId(), passagem.getAssentoId(), passagem.getUsuarioId())) {
                    System.out.println("Passagem excluída com sucesso!");
                } else {
                    System.out.println("Erro ao excluir passagem!");
                }
            }
        } else {
            System.out.println("Passagem não encontrada!");
        }
    } catch (Exception e) {
        System.out.println("Erro ao excluir passagem: " + e.getMessage());
    }
}

    public void associarPassagemAUsuario() {
    System.out.println("\nASSOCIAR PASSAGEM A USUÁRIO");
    
    System.out.print("Nome do Usuário: ");
    String nomeUsuario = console.nextLine();
    
    System.out.print("Origem da Passagem: ");
    String origem = console.nextLine();
    System.out.print("Destino da Passagem: ");
    String destino = console.nextLine();
    System.out.print("Data da Viagem (dd/MM/yyyy): ");
    String dataStr = console.nextLine();

    try {
        Usuario usuario = arqUsuarios.readByNome(nomeUsuario);
        if (usuario == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }

        Rota rota = arqRotas.readByOrigemDestino(origem, destino);
        if (rota == null) {
            System.out.println("Rota não encontrada!");
            return;
        }

        LocalDate dataViagem = LocalDate.parse(dataStr, dateFormatter);
        ArrayList<Passagem> passagens = arqPassagens.readByRota(rota.getID());
        Passagem passagemEncontrada = null;
        
        for (Passagem p : passagens) {
            if (p.getDataViagem().equals(dataViagem)) {
                passagemEncontrada = p;
                break;
            }
        }

        if (passagemEncontrada == null) {
            System.out.println("Passagem não encontrada para esta data!");
            return;
        }

        if (passagemEncontrada.getUsuarioId() != -1 && passagemEncontrada.getUsuarioId() != usuario.getID()) {
            System.out.println("Esta passagem já está associada a outro usuário!");
            return;
        }

        passagemEncontrada.setUsuarioId(usuario.getID());
        if (arqPassagens.update(passagemEncontrada)) {
            usuario.adicionarPassagem(passagemEncontrada.getID());
            if (arqUsuarios.update(usuario)) {
                System.out.println("Passagem associada ao usuário com sucesso!");
            } else {
                System.out.println("Erro ao atualizar usuário!");
            }
        } else {
            System.out.println("Erro ao atualizar passagem!");
        }

    } catch (Exception e) {
        System.out.println("Erro ao associar passagem: " + e.getMessage());
    }
}

public void desassociarPassagemDeUsuario() {
    System.out.println("\nDESASSOCIAR PASSAGEM DE USUÁRIO");
    
    System.out.print("Nome do Usuário: ");
    String nomeUsuario = console.nextLine();
    
    System.out.print("Origem da Passagem: ");
    String origem = console.nextLine();
    System.out.print("Destino da Passagem: ");
    String destino = console.nextLine();
    System.out.print("Data da Viagem (dd/MM/yyyy): ");
    String dataStr = console.nextLine();

    try {
        Usuario usuario = arqUsuarios.readByNome(nomeUsuario);
        if (usuario == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }

        Rota rota = arqRotas.readByOrigemDestino(origem, destino);
        if (rota == null) {
            System.out.println("Rota não encontrada!");
            return;
        }

        LocalDate dataViagem = LocalDate.parse(dataStr, dateFormatter);
        ArrayList<Passagem> passagens = arqPassagens.readByRota(rota.getID());
        Passagem passagemEncontrada = null;
        
        for (Passagem p : passagens) {
            if (p.getDataViagem().equals(dataViagem) && p.getUsuarioId() == usuario.getID()) {
                passagemEncontrada = p;
                break;
            }
        }

        if (passagemEncontrada == null) {
            System.out.println("Passagem não encontrada ou não associada a este usuário!");
            return;
        }

        passagemEncontrada.setUsuarioId(-1);
        if (arqPassagens.update(passagemEncontrada)) {
            usuario.removerPassagem(passagemEncontrada.getID());
            if (arqUsuarios.update(usuario)) {
                System.out.println("Passagem desassociada do usuário com sucesso!");
            } else {
                System.out.println("Erro ao atualizar usuário!");
            }
        } else {
            System.out.println("Erro ao atualizar passagem!");
        }

    } catch (Exception e) {
        System.out.println("Erro ao desassociar passagem: " + e.getMessage());
    }
}

 public void listarPassagensDoUsuario() {
        System.out.println("\nLISTAR PASSAGENS DO USUÁRIO");
        System.out.print("Nome do Usuário: ");
        String nomeUsuario = console.nextLine();

        try {
            Usuario usuario = arqUsuarios.readByNome(nomeUsuario);
            if (usuario == null) {
                System.out.println("Usuário não encontrado!");
                return;
            }

            System.out.println("\nUsuário: " + usuario.getNome());
            System.out.println("Total de passagens: " + usuario.getPassagensIds().size());
            
            if (usuario.getPassagensIds().isEmpty()) {
                System.out.println("Nenhuma passagem associada a este usuário.");
                return;
            }

            System.out.println("\nPassagens do usuário:");
            for (Integer passagemId : usuario.getPassagensIds()) {
                Passagem p = arqPassagens.read(passagemId);
                if (p != null) {
                    Rota rota = arqRotas.read(p.getRotaId());
                    Horario horario = arqHorarios.read(p.getHorarioId());
                    Assento assento = arqAssentos.read(p.getAssentoId()); 
                    
                    System.out.println("--- Passagem ID: " + p.getID() + " ---");
                    System.out.println("Origem: " + rota.getOrigem() + " -> Destino: " + rota.getDestino());
                    System.out.println("Data: " + p.getDataViagem());
                    if (horario != null) {
                        System.out.println("Horário: " + horario.getHoraPartida() + " - " + horario.getHoraChegada());
                    }
                    System.out.println("Preço: R$ " + String.format("%.2f", p.getPreco()));
                    if (assento != null) {
                        System.out.println("Assento: " + assento.getNumeroAssento());
                    }
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar passagens: " + e.getMessage());
        }
    }


    public void buscarPorData() {
        System.out.println("\nBusca de Passagens por Data");
        System.out.print("Data da Viagem (dd/MM/yyyy): ");
        String dataStr = console.nextLine();

        try {
            LocalDate data = LocalDate.parse(dataStr, dateFormatter);
            ArrayList<Passagem> passagens = arqPassagens.readByData(data);
            if (!passagens.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Passagem p : passagens) {
                    System.out.println(p);
                    System.out.println("---");
                }
            } else {
                System.out.println("Nenhuma passagem encontrada para esta data!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }

    public void buscarPorPreco() {
        System.out.println("\nBusca de Passagens por Preço");
        System.out.print("Preço: ");
        float preco = Float.parseFloat(console.nextLine());

        try {
            ArrayList<Passagem> passagens = arqPassagens.readByPreco(preco);
            if (!passagens.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Passagem p : passagens) {
                    System.out.println(p);
                    System.out.println("---");
                }
            } else {
                System.out.println("Nenhuma passagem encontrada com este preço!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }

    public void buscarPorOrigem() {
        System.out.println("\nBusca de Passagens por Origem");
        System.out.print("Origem: ");
        String origem = console.nextLine();

        try {
            ArrayList<Rota> rotas = arqRotas.readByOrigem(origem);
            if (!rotas.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Rota r : rotas) {
                    ArrayList<Passagem> passagens = arqPassagens.readByRota(r.getID());
                    for (Passagem p : passagens) {
                        System.out.println(p);
                        System.out.println("---");
                    }
                }
            } else {
                System.out.println("Nenhuma rota encontrada com esta origem!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }

    public void buscarPorDestino() {
        System.out.println("\nBusca de Passagens por Destino");
        System.out.print("Destino: ");
        String destino = console.nextLine();

        try {
            ArrayList<Rota> rotas = arqRotas.readByDestino(destino);
            if (!rotas.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Rota r : rotas) {
                    ArrayList<Passagem> passagens = arqPassagens.readByRota(r.getID());
                    for (Passagem p : passagens) {
                        System.out.println(p);
                        System.out.println("---");
                    }
                }
            } else {
                System.out.println("Nenhuma rota encontrada com este destino!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }

    public void buscarPorDistancia() {
        System.out.println("\nBusca de Passagens por Distância");
        System.out.print("Distância (km): ");
        float distancia = Float.parseFloat(console.nextLine());

        try {
            ArrayList<Rota> rotas = arqRotas.readByDistancia(distancia);
            if (!rotas.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Rota r : rotas) {
                    ArrayList<Passagem> passagens = arqPassagens.readByRota(r.getID());
                    for (Passagem p : passagens) {
                        System.out.println(p);
                        System.out.println("---");
                    }
                }
            } else {
                System.out.println("Nenhuma rota encontrada com esta distância!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }

    public void buscarPorHoraPartida() {
        System.out.println("\nBusca de Passagens por Horário de Partida");
        System.out.print("Hora de Partida (HH:mm): ");
        String horaStr = console.nextLine();

        try {
            LocalTime horaPartida = LocalTime.parse(horaStr);
            
            ArrayList<Horario> horariosEncontrados = new ArrayList<>();
            for (int id = 1; id <= 100; id++) {
                Horario h = arqHorarios.read(id);
                if (h != null && h.getHoraPartida().equals(horaPartida)) {
                    horariosEncontrados.add(h);
                }
            }

            if (!horariosEncontrados.isEmpty()) {
                System.out.println("\nPassagens encontradas:");
                for (Horario h : horariosEncontrados) {
                    ArrayList<Passagem> passagens = arqPassagens.readByHorario(h.getID());
                    for (Passagem p : passagens) {
                        System.out.println(p);
                        System.out.println("---");
                    }
                }
            } else {
                System.out.println("Nenhum horário encontrado com esta hora de partida!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar passagens: " + e.getMessage());
        }
    }

    public void desassociarUsuario() {
        System.out.println("\nDesassociar Usuário da Passagem");
        System.out.print("Nome do Usuário: ");
        String nomeUsuario = console.nextLine();
        System.out.print("ID da Passagem: ");
        int passagemId = Integer.parseInt(console.nextLine());

        try {
            Usuario usuario = arqUsuarios.readByNome(nomeUsuario);
            if (usuario == null) {
                System.out.println("Erro: Usuário '" + nomeUsuario + "' não encontrado!");
                return;
            }
            System.out.println("Lógica de desassociação N:N removida. A passagem agora é 1:1 com o usuário.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
