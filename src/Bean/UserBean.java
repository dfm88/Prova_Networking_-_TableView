package Bean;

public class UserBean {
    private int IDUser;
    private String nome;
    private String cognome;

    public UserBean(int IDUser, String nome, String cognome) {
        this.IDUser = IDUser;
        this.nome = nome;
        this.cognome = cognome;
    }

    public int getIDUser() {
        return IDUser;
    }

    public void setIDUser(int IDUser) {
        this.IDUser = IDUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
}
