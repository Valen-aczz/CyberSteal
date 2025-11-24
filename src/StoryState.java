public class StoryState {
    // Estados de completado
    private boolean cap1Completado;
    private boolean cap2Completado;
    private boolean cap3Completado;
    
    // Estados de victoria (ganado)
    private boolean cap1Ganado;
    private boolean cap2Ganado;
    private boolean cap3Ganado;
    
    // Datos del jugador
    private String nombre;
    private String pronombre;
    private String avatar;
    private String genero; // "hombre" o "mujer"

    // ===== MÉTODOS PARA CAPÍTULO 1 =====
    public boolean isCap1Completado() { return cap1Completado; }
    public void setCap1Completado(boolean cap1Completado) { this.cap1Completado = cap1Completado; }
    
    public boolean isCap1Ganado() { return cap1Ganado; }
    public void setCap1Ganado(boolean cap1Ganado) { this.cap1Ganado = cap1Ganado; }

    // ===== MÉTODOS PARA CAPÍTULO 2 =====
    public boolean isCap2Completado() { return cap2Completado; }
    public void setCap2Completado(boolean cap2Completado) { this.cap2Completado = cap2Completado; }
    
    public boolean isCap2Ganado() { return cap2Ganado; }
    public void setCap2Ganado(boolean cap2Ganado) { this.cap2Ganado = cap2Ganado; }

    // ===== MÉTODOS PARA CAPÍTULO 3 =====
    public boolean isCap3Completado() { return cap3Completado; }
    public void setCap3Completado(boolean cap3Completado) { this.cap3Completado = cap3Completado; }
    
    public boolean isCap3Ganado() { return cap3Ganado; }
    public void setCap3Ganado(boolean cap3Ganado) { this.cap3Ganado = cap3Ganado; }

    // ===== MÉTODOS PARA DATOS DEL JUGADOR =====
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPronombre() { return pronombre; }
    public void setPronombre(String pronombre) { this.pronombre = pronombre; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    // ===== MÉTODOS UTILITARIOS =====
    
    /**
     * Obtiene el número total de capítulos completados (ganados o perdidos)
     */
    public int getCapitulosCompletados() {
        int completados = 0;
        if (cap1Completado) completados++;
        if (cap2Completado) completados++;
        if (cap3Completado) completados++;
        return completados;
    }
    
    /**
     * Obtiene el número de capítulos ganados (victorias)
     */
    public int getCapitulosGanados() {
        int ganados = 0;
        if (cap1Ganado) ganados++;
        if (cap2Ganado) ganados++;
        if (cap3Ganado) ganados++;
        return ganados;
    }
    
    /**
     * Verifica si todos los capítulos han sido completados
     */
    public boolean isHistoriaCompleta() {
        return cap1Completado && cap2Completado && cap3Completado;
    }
    
    /**
     * Verifica si todos los capítulos han sido ganados (final perfecto)
     */
    public boolean isHistoriaPerfecta() {
        return cap1Ganado && cap2Ganado && cap3Ganado;
    }
    
    /**
     * Marca un capítulo como completado y establece si fue ganado o perdido
     */
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
    
    /**
     * Reinicia el progreso de un capítulo específico
     */
    public void reiniciarCapitulo(int capitulo) {
        switch (capitulo) {
            case 1:
                setCap1Completado(false);
                setCap1Ganado(false);
                break;
            case 2:
                setCap2Completado(false);
                setCap2Ganado(false);
                break;
            case 3:
                setCap3Completado(false);
                setCap3Ganado(false);
                break;
            default:
                System.err.println("⚠️ Capítulo no válido: " + capitulo);
        }
    }
    
    /**
     * Reinicia todo el progreso de la historia
     */
    public void reiniciarHistoria() {
        cap1Completado = false;
        cap1Ganado = false;
        cap2Completado = false;
        cap2Ganado = false;
        cap3Completado = false;
        cap3Ganado = false;
    }
    
    /**
     * Obtiene el estado de progreso como texto para debug
     */
    public String getEstadoProgreso() {
        return String.format(
            "Cap1: %s/%s | Cap2: %s/%s | Cap3: %s/%s",
            cap1Completado ? "Completado" : "Pendiente",
            cap1Ganado ? "Ganado" : "Perdido",
            cap2Completado ? "Completado" : "Pendiente", 
            cap2Ganado ? "Ganado" : "Perdido",
            cap3Completado ? "Completado" : "Pendiente",
            cap3Ganado ? "Ganado" : "Perdido"
        );
    }
    
    /**
     * Verifica si un capítulo específico está disponible para jugar
     * (todos los anteriores completados o permitir jugar en cualquier orden)
     */
    public boolean isCapituloDisponible(int capitulo) {
        switch (capitulo) {
            case 1:
                return true; // Siempre disponible
            case 2:
                return cap1Completado; // Requiere cap1 completado
            case 3:
                return cap2Completado; // Requiere cap2 completado
            default:
                return false;
        }
    }
}