package arquivos;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ParIntInt;
import entidades.Usuario;
import java.util.ArrayList;

public class ArquivoUsuarios extends Arquivo<Usuario> {

    public ArquivoUsuarios() throws Exception {
        super("usuarios", Usuario.class.getConstructor());
        

    }

    @Override
    public int create(Usuario obj) throws Exception {
        return super.create(obj);
    }

    @Override
    public Usuario read(int id) throws Exception {
        return super.read(id);
    }

    public Usuario readByNome(String nome) throws Exception {
        for (int id = 1; id <= 100; id++) {
            Usuario usuario = super.read(id);
            if (usuario != null && usuario.getNome().equalsIgnoreCase(nome)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario obj = super.read(id);
        if (obj != null) {

            return super.delete(id);
        }
        return false;
    }

    @Override
    public boolean update(Usuario novoUsuario) throws Exception {
        return super.update(novoUsuario);
    }


}
