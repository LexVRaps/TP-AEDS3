package arquivos;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ParIntInt;
import entidades.Passagem;
import java.time.LocalDate;
import java.util.ArrayList;

public class ArquivoPassagens extends Arquivo<Passagem> {

    ArvoreBMais<ParIntInt> arvoreRotaPassagem;
    ArvoreBMais<ParIntInt> arvoreHorarioPassagem;
    ArvoreBMais<ParIntInt> arvoreAssentoPassagem;
    ArvoreBMais<ParIntInt> arvoreUsuarioPassagem;

    public ArquivoPassagens() throws Exception {
        super("passagens", Passagem.class.getConstructor());
        
        arvoreRotaPassagem = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            4,
            "dados/passagens_rota.btree.db"
        );
        
        arvoreHorarioPassagem = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            4,
            "dados/passagens_horario.btree.db"
        );
        
        arvoreAssentoPassagem = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            4,
            "dados/passagens_assento.btree.db"
        );

        arvoreUsuarioPassagem = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            4,
            "dados/passagens_usuario.btree.db"
        );
    }

    public int create(Passagem obj, int rotaId, int horarioId, int assentoId, int usuarioId) throws Exception {
        int id = super.create(obj);
        obj.setID(id);
        
        arvoreRotaPassagem.create(new ParIntInt(rotaId, id));
        arvoreHorarioPassagem.create(new ParIntInt(horarioId, id));
        arvoreAssentoPassagem.create(new ParIntInt(assentoId, id));
        arvoreUsuarioPassagem.create(new ParIntInt(usuarioId, id));
        
        return id;
    }

    @Override
    public int create(Passagem obj) throws Exception {
        return super.create(obj);
    }

    @Override
    public Passagem read(int id) throws Exception {
        return super.read(id);
    }

    public ArrayList<Passagem> readByRota(int rotaId) throws Exception {
        ArrayList<Passagem> passagens = new ArrayList<>();
        ArrayList<ParIntInt> pares = arvoreRotaPassagem.read(new ParIntInt(rotaId, -1));
        
        for (ParIntInt par : pares) {
            Passagem p = super.read(par.get2());
            if (p != null) {
                passagens.add(p);
            }
        }
        
        return passagens;
    }

    public ArrayList<Passagem> readByHorario(int horarioId) throws Exception {
        ArrayList<Passagem> passagens = new ArrayList<>();
        ArrayList<ParIntInt> pares = arvoreHorarioPassagem.read(new ParIntInt(horarioId, -1));
        
        for (ParIntInt par : pares) {
            Passagem p = super.read(par.get2());
            if (p != null) {
                passagens.add(p);
            }
        }
        
        return passagens;
    }

    public ArrayList<Passagem> readByAssento(int assentoId) throws Exception {
        ArrayList<Passagem> passagens = new ArrayList<>();
        ArrayList<ParIntInt> pares = arvoreAssentoPassagem.read(new ParIntInt(assentoId, -1));
        
        for (ParIntInt par : pares) {
            Passagem p = super.read(par.get2());
            if (p != null) {
                passagens.add(p);
            }
        }
        
        return passagens;
    }

    public ArrayList<Passagem> readByUsuario(int usuarioId) throws Exception {
        ArrayList<Passagem> passagens = new ArrayList<>();
        ArrayList<ParIntInt> pares = arvoreUsuarioPassagem.read(new ParIntInt(usuarioId, -1));
        
        for (ParIntInt par : pares) {
            Passagem p = super.read(par.get2());
            if (p != null) {
                passagens.add(p);
            }
        }
        
        return passagens;
    }

    public ArrayList<Passagem> readByData(LocalDate data) throws Exception {
        ArrayList<Passagem> passagens = new ArrayList<>();
        for (int id = 1; id <= 100; id++) {
            Passagem passagem = super.read(id);
            if (passagem != null && passagem.getDataViagem().isEqual(data)) {
                passagens.add(passagem);
            }
        }
        return passagens;
    }

    public ArrayList<Passagem> readByPreco(float preco) throws Exception {
        ArrayList<Passagem> passagens = new ArrayList<>();
        for (int id = 1; id <= 100; id++) {
            Passagem passagem = super.read(id);
            if (passagem != null && Math.abs(passagem.getPreco() - preco) < 0.001) {
                passagens.add(passagem);
            }
        }
        return passagens;
    }

    public boolean delete(int id, int rotaId, int horarioId, int assentoId, int usuarioId) throws Exception {
        Passagem obj = super.read(id);
        if (obj != null) {
            if (arvoreRotaPassagem.delete(new ParIntInt(rotaId, id)) &&
                arvoreHorarioPassagem.delete(new ParIntInt(horarioId, id)) &&
                arvoreAssentoPassagem.delete(new ParIntInt(assentoId, id)) &&
                arvoreUsuarioPassagem.delete(new ParIntInt(usuarioId, id))) {
                return super.delete(id);
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return super.delete(id);
    }

    public boolean update(Passagem novaPassagem, int rotaIdAntigo, int horarioIdAntigo, 
                         int assentoIdAntigo, int usuarioIdAntigo,
                         int rotaIdNovo, int horarioIdNovo, 
                         int assentoIdNovo, int usuarioIdNovo) throws Exception {
        
        Passagem passagemAntiga = super.read(novaPassagem.getID());
        
        if (passagemAntiga != null) {
            if (rotaIdAntigo != rotaIdNovo) {
                arvoreRotaPassagem.delete(new ParIntInt(rotaIdAntigo, novaPassagem.getID()));
                arvoreRotaPassagem.create(new ParIntInt(rotaIdNovo, novaPassagem.getID()));
            }
            
            if (horarioIdAntigo != horarioIdNovo) {
                arvoreHorarioPassagem.delete(new ParIntInt(horarioIdAntigo, novaPassagem.getID()));
                arvoreHorarioPassagem.create(new ParIntInt(horarioIdNovo, novaPassagem.getID()));
            }
            
            if (assentoIdAntigo != assentoIdNovo) {
                arvoreAssentoPassagem.delete(new ParIntInt(assentoIdAntigo, novaPassagem.getID()));
                arvoreAssentoPassagem.create(new ParIntInt(assentoIdNovo, novaPassagem.getID()));
            }
            
            if (usuarioIdAntigo != usuarioIdNovo) {
                arvoreUsuarioPassagem.delete(new ParIntInt(usuarioIdAntigo, novaPassagem.getID()));
                arvoreUsuarioPassagem.create(new ParIntInt(usuarioIdNovo, novaPassagem.getID()));
            }
            
            return super.update(novaPassagem);
        }
        
        return false;
    }

    @Override
    public boolean update(Passagem novaPassagem) throws Exception {
        return super.update(novaPassagem);
    }
}