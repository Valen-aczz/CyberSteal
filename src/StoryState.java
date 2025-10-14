public class StoryState {
    private boolean cap1Completado;
    private boolean cap1Ganado;
    private boolean cap2Completado;
    private boolean cap2Ganado;
    private boolean cap3Completado;
    private boolean cap3Ganado;

    private String nombreJugador;
    private String avatarSeleccionado;

    // Getters y setters
    public boolean isCap1Completado() { return cap1Completado; }
    public void setCap1Completado(boolean c) { cap1Completado = c; }

    public boolean isCap1Ganado() { return cap1Ganado; }
    public void setCap1Ganado(boolean g) { cap1Ganado = g; }

    public boolean isCap2Completado() { return cap2Completado; }
    public void setCap2Completado(boolean c) { cap2Completado = c; }

    public boolean isCap2Ganado() { return cap2Ganado; }
    public void setCap2Ganado(boolean g) { cap2Ganado = g; }

    public boolean isCap3Completado() { return cap3Completado; }
    public void setCap3Completado(boolean c) { cap3Completado = c; }

    public boolean isCap3Ganado() { return cap3Ganado; }
    public void setCap3Ganado(boolean g) { cap3Ganado = g; }

    public String getNombreJugador() { return nombreJugador; }
    public void setNombreJugador(String n) { nombreJugador = n; }

    public String getAvatarSeleccionado() { return avatarSeleccionado; }
    public void setAvatarSeleccionado(String a) { avatarSeleccionado = a; }
    
    
    public int getCapitulosCompletados() {
    int completados = 0;
    if (cap1Completado) completados++;
    if (cap2Completado) completados++;
    if (cap3Completado) completados++;
    return completados;
}

    
    
    private String nombre;
private String pronombre;
private String avatar;

// getters y setters
public String getNombre() { return nombre; }
public void setNombre(String nombre) { this.nombre = nombre; }

public String getPronombre() { return pronombre; }
public void setPronombre(String pronombre) { this.pronombre = pronombre; }

public String getAvatar() { return avatar; }
public void setAvatar(String avatar) { this.avatar = avatar; }


    
  
    public void marcarCapitulo(int capitulo, boolean victoria) {
    switch (capitulo) {
        case 1:
            setCap1Completado(true);
            setCap1Ganado(victoria);
            break;
        case 2:
            setCap2Completado(true);
            setCap2Ganado(victoria);
            break;
        case 3:
            setCap3Completado(true);
            setCap3Ganado(victoria);
            break;
        default:
            System.err.println("⚠️ Capítulo no válido: " + capitulo);
    }
    }
    
}
