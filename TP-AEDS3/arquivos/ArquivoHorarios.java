package arquivos;

import aeds3.Arquivo;

import entidades.Horario;
import java.util.ArrayList;

public class ArquivoHorarios extends Arquivo<Horario> {



    public ArquivoHorarios() throws Exception {
        super("horarios", Horario.class.getConstructor());

    }



    @Override
    public int create(Horario obj) throws Exception {
        return super.create(obj);
    }

    @Override
    public Horario read(int id) throws Exception {
        return super.read(id);
    }



    @Override
    public boolean delete(int id) throws Exception {
        Horario obj = super.read(id);
        if (obj != null) {
            return super.delete(id);
        }
        return false;
    }



    @Override
    public boolean update(Horario novoHorario) throws Exception {
        return super.update(novoHorario);
    }

    public ArrayList<Horario> readAll() throws Exception {
        ArrayList<Horario> horarios = new ArrayList<>();
        for (int id = 1; id <= 100; id++) {
            Horario horario = super.read(id);
            if (horario != null) {
                horarios.add(horario);
            }
        }
        return horarios;
    }

    public Horario readByHorarios(java.time.LocalTime horaPartida, java.time.LocalTime horaChegada) throws Exception {
        for (int id = 1; id <= 100; id++) {
            Horario horario = super.read(id);
            if (horario != null && horario.getHoraPartida().equals(horaPartida) && horario.getHoraChegada().equals(horaChegada)) {
                return horario;
            }
        }
        return null;
    }
}
