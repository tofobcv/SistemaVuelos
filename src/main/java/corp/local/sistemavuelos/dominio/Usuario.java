package corp.local.sistemavuelos.dominio;

import java.util.Date;

public class Usuario {
    private int id;
    private String email;
    private String passwordHash;
    private String nombre;
    private String apellido;
    private boolean emailVerificado;
    private Date fechaRegistro;
    private Date fechaVerificacion;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String email, String passwordHash, String nombre, String apellido) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public boolean isEmailVerificado() { return emailVerificado; }
    public void setEmailVerificado(boolean emailVerificado) { this.emailVerificado = emailVerificado; }
    
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public Date getFechaVerificacion() { return fechaVerificacion; }
    public void setFechaVerificacion(Date fechaVerificacion) { this.fechaVerificacion = fechaVerificacion; }
}