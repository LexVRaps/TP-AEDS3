package arquivos;
import aeds3.Arquivo;
import entidades.Usuario;
import aeds3.*;

public class ArquivoUsuarios extends aeds3.Arquivo<Usuario> {

    Arquivo<Usuario> arquivoUsuarios;
    HashExtensivel<ParEmailID> indiceIndiretoEmail;

    public ArquivoUsuarios() throws Exception {
        super("usuarios", Usuario.class.getConstructor());
        indiceIndiretoEmail = new HashExtensivel<>(
            ParEmailID.class.getConstructor(), 
            4, 
            "dados/indiceEmail.d.db",   // diretório
            "dados/indiceEmail.c.db"    // cestos 
        );
    }

    @Override
    public int create(Usuario c) throws Exception {
        int id = super.create(c);
        indiceIndiretoEmail.create(new ParEmailID(c.getEmail(), id));
        return id;
    }

    public Usuario read(String email) throws Exception {
        ParEmailID pci = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if(pci == null)
            return null;
        return read(pci.getId());
    }
    
    public boolean delete(String email) throws Exception {
        ParEmailID pci = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if(pci != null) 
            if(delete(pci.getId())) 
                return indiceIndiretoEmail.delete(ParEmailID.hash(email));
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario c = super.read(id);
        if(c != null) {
            if(super.delete(id))
                return indiceIndiretoEmail.delete(ParEmailID.hash(c.getEmail()));
        }
        return false;
    }

    @Override
    public boolean update(Usuario novoUsuario) throws Exception {
        Usuario usuarioVelho = read(novoUsuario.getEmail());
        if(super.update(novoUsuario)) {
            if(novoUsuario.getEmail().compareTo(usuarioVelho.getEmail())!=0) {
                indiceIndiretoEmail.delete(ParEmailID.hash(usuarioVelho.getEmail()));
                indiceIndiretoEmail.create(new ParEmailID(novoUsuario.getEmail(), novoUsuario.getID()));
            }
            return true;
        }
        return false;
    }
}