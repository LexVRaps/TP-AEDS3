package arquivos;

import aeds3.ArvoreBMais;
import aeds3.ParIntInt;
import java.util.ArrayList;

import aeds3.Arquivo;
import entidades.Rota;

public class ArquivoRotas extends Arquivo<Rota> {

    ArvoreBMais<ParIntInt> relRotaHorario;
    ArvoreBMais<ParIntInt> relHorarioRota;

    public ArquivoRotas() throws Exception {
        super("rotas", Rota.class.getConstructor());

        relRotaHorario = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            4,
            "dados/rota_horario.btree.db"
        );

        relHorarioRota = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            4,
            "dados/horario_rota.btree.db"
        );
    }

    @Override
    public int create(Rota obj) throws Exception {
        return super.create(obj);
    }

    @Override
    public Rota read(int id) throws Exception {
        return super.read(id);
    }

    @Override
    public boolean delete(int id) throws Exception {
        Rota obj = super.read(id);
        if (obj != null) {
            ArrayList<ParIntInt> pares = relRotaHorario.read(new ParIntInt(id, -1));
            for (ParIntInt par : pares) {
                relRotaHorario.delete(par);
                relHorarioRota.delete(new ParIntInt(par.get2(), par.get1()));
            }
            return super.delete(id);
        }
        return false;
    }

    @Override
    public boolean update(Rota novaRota) throws Exception {
        return super.update(novaRota);
    }

    public ArrayList<Rota> readByOrigem(String origem) throws Exception {
        ArrayList<Rota> rotas = new ArrayList<>();
        for (int id = 1; id <= 100; id++) {
            Rota rota = super.read(id);
            if (rota != null && rota.getOrigem().equalsIgnoreCase(origem)) {
                rotas.add(rota);
            }
        }
        return rotas;
    }

    public ArrayList<Rota> readByDestino(String destino) throws Exception {
        ArrayList<Rota> rotas = new ArrayList<>();
        for (int id = 1; id <= 100; id++) {
            Rota rota = super.read(id);
            if (rota != null && rota.getDestino().equalsIgnoreCase(destino)) {
                rotas.add(rota);
            }
        }
        return rotas;
    }

    public ArrayList<Rota> readByDistancia(float distancia) throws Exception {
        ArrayList<Rota> rotas = new ArrayList<>();
        for (int id = 1; id <= 100; id++) {
            Rota rota = super.read(id);
            if (rota != null && Math.abs(rota.getDistancia() - distancia) < 0.001) {
                rotas.add(rota);
            }
        }
        return rotas;
    }

    public boolean associarHorario(int rotaId, int horarioId) throws Exception {
        try {
            boolean sucesso1 = relRotaHorario.create(new ParIntInt(rotaId, horarioId));
            boolean sucesso2 = relHorarioRota.create(new ParIntInt(horarioId, rotaId));

            if (sucesso1 && sucesso2) {
                return true;
            } else {
                if (sucesso1) {
                    relRotaHorario.delete(new ParIntInt(rotaId, horarioId));
                }
                if (sucesso2) {
                    relHorarioRota.delete(new ParIntInt(horarioId, rotaId));
                }
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean desassociarHorario(int rotaId, int horarioId) throws Exception {
        boolean sucesso1 = relRotaHorario.delete(new ParIntInt(rotaId, horarioId));
        boolean sucesso2 = relHorarioRota.delete(new ParIntInt(horarioId, rotaId));
        return sucesso1 && sucesso2;
    }

    public ArrayList<Integer> buscarHorariosDaRota(int rotaId) throws Exception {
        ArrayList<Integer> horarioIds = new ArrayList<>();
        ArrayList<ParIntInt> pares = relRotaHorario.read(new ParIntInt(rotaId, -1));

        for (ParIntInt par : pares) {
            horarioIds.add(par.get2());
        }

        return horarioIds;
    }

    public ArrayList<Integer> buscarRotasDoHorario(int horarioId) throws Exception {
        ArrayList<Integer> rotaIds = new ArrayList<>();
        ArrayList<ParIntInt> pares = relHorarioRota.read(new ParIntInt(horarioId, -1));

        for (ParIntInt par : pares) {
            rotaIds.add(par.get2());
        }

        return rotaIds;
    }

    public Rota readByOrigemDestino(String origem, String destino) throws Exception {
        for (int id = 1; id <= 100; id++) {
            Rota rota = super.read(id);
            if (rota != null && rota.getOrigem().equalsIgnoreCase(origem) && rota.getDestino().equalsIgnoreCase(destino)) {
                return rota;
            }
        }
        return null;
    }
}
