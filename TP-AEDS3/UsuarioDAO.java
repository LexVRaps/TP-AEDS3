import arquivos.ArquivoUsuarios;
import entidades.Usuario;
import java.util.ArrayList;

public class UsuarioDAO {
    private ArquivoUsuarios arqUsuarios;

    public UsuarioDAO() throws Exception {
        arqUsuarios = new ArquivoUsuarios();
    }

    public int create(Usuario usuario) throws Exception {
        return arqUsuarios.create(usuario);
    }

    public Usuario read(int id) throws Exception {
        return arqUsuarios.read(id);
    }

    public boolean update(Usuario usuario) throws Exception {
        return arqUsuarios.update(usuario);
    }

    public boolean delete(int id) throws Exception {
        return arqUsuarios.delete(id);
    }
    

}
