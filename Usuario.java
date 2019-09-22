package modelos;

public class Usuario {
    //declaracion de las variables que se utilizaran en la base de datos
    private int id;
    private  String nicknames;
    private  String nombre;
    private String password;
    private  String correo;
    private  String direccion;

// se usa un contructor solo para la falta de informacion y podemos acceder a sus datos utilizando un get y un set
    public Usuario() {
    }

    public Usuario(int id, String nicknames, String nombre, String password, String correo, String direccion) {
        this.id = id;
        this.nicknames = nicknames;
        this.nombre = nombre;
        this.password = password;
        this.correo = correo;
        this.direccion = direccion;
    }

    public Usuario(String nicknames, String nombre, String password, String correo, String direccion) {
        this.nicknames = nicknames;
        this.nombre = nombre;
        this.password = password;
        this.correo = correo;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNicknames() {
        return nicknames;
    }

    public void setNicknames(String nicknames) {
        this.nicknames = nicknames;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
