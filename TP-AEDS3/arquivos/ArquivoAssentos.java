package arquivos;

import aeds3.Arquivo;
import entidades.Assento;

public class ArquivoAssentos extends Arquivo<Assento> {

    public ArquivoAssentos() throws Exception {
        super("assentos", Assento.class.getConstructor());
    }

    @Override
    public int create(Assento obj) throws Exception {
        return super.create(obj);
    }

    @Override
    public Assento read(int id) throws Exception {
        return super.read(id);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return super.delete(id);
    }

    @Override
    public boolean update(Assento novoAssento) throws Exception {
        return super.update(novoAssento);
    }

    public Assento readByNumeroAssento(String numeroAssento) throws Exception {
        for (int id = 1; id <= 100; id++) {
            Assento assento = super.read(id);
            if (assento != null && assento.getNumeroAssento().equalsIgnoreCase(numeroAssento)) {
                return assento;
            }
        }
        return null;
    }
}
