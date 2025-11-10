package entidades;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import aeds3.Registro;

public class Usuario implements Registro {
    private int ID;
    private String nome;
    private String email;
    private String senha;
    private List<Integer> passagensIds; 

    public Usuario() {
        this(-1, "", "", "", new ArrayList<>());
    }

    public Usuario(String n, String e, String s) {
        this(-1, n, e, s, new ArrayList<>());
    }

    public Usuario(int i, String n, String e, String s) {
        this(i, n, e, s, new ArrayList<>());
    }

    public Usuario(int i, String n, String e, String s, List<Integer> passagensIds) {
        this.ID = i;
        this.nome = n;
        this.email = e;
        this.senha = s;
        this.passagensIds = passagensIds != null ? passagensIds : new ArrayList<>();
    }

    public int getID() { return ID; }
    public void setID(int id) { this.ID = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public List<Integer> getPassagensIds() { return passagensIds; } 
    public void setPassagensIds(List<Integer> passagensIds) { this.passagensIds = passagensIds; } 

    public void adicionarPassagem(int passagemId) {
        if (!this.passagensIds.contains(passagemId)) {
            this.passagensIds.add(passagemId);
        }
    }

    public void removerPassagem(int passagemId) {
        this.passagensIds.remove(Integer.valueOf(passagemId));
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(this.ID);
            dos.writeUTF(this.nome != null ? this.nome : "");
            dos.writeUTF(this.email != null ? this.email : "");
            dos.writeUTF(this.senha != null ? this.senha : "");
            
            dos.writeInt(this.passagensIds.size());
            for (Integer passagemId : this.passagensIds) {
                dos.writeInt(passagemId);
            }
            
            dos.flush();
            return baos.toByteArray();
        } finally {
            dos.close();
            baos.close();
        }
    }

    public void fromByteArray(byte[] b) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.ID = dis.readInt();
            this.nome = dis.readUTF();
            this.email = dis.readUTF();
            this.senha = dis.readUTF();
            
            int tamanhoLista = dis.readInt();
            this.passagensIds = new ArrayList<>();
            for (int i = 0; i < tamanhoLista; i++) {
                this.passagensIds.add(dis.readInt());
            }
        } finally {
            dis.close();
            bais.close();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nID........: ").append(this.ID)
          .append("\nNome......: ").append(this.nome)
          .append("\nEmail.....: ").append(this.email)
          .append("\nSenha.....: ").append(this.senha)
          .append("\nPassagens.: ").append(this.passagensIds.size()).append(" passagens");
        
        if (!this.passagensIds.isEmpty()) {
            sb.append(" [IDs: ").append(this.passagensIds).append("]");
        }
        
        return sb.toString();
    }

    public int compareTo(Object b) {
        return this.getID() - ((Usuario) b).getID();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}