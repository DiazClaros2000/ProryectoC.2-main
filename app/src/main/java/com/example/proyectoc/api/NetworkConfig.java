package com.example.proyectoc.api;

public class NetworkConfig {
    
    // Configuración para diferentes entornos
    public static final boolean IS_EMULATOR = false; // Cambiar a true si usas emulador
    
    // URLs base para diferentes entornos
    private static final String EMULATOR_URL = "http://10.0.2.2/car_wash_api/";
    private static final String DEVICE_URL = "http://16.16.0.14/car_wash_api/";
    
    // URL que se usará según el entorno
    public static final String BASE_URL = IS_EMULATOR ? EMULATOR_URL : DEVICE_URL;
    
    // Método para obtener la URL base actual
    public static String getBaseUrl() {
        return BASE_URL;
    }
    
    // Método para cambiar dinámicamente la URL (útil para debugging)
    public static String getBaseUrl(boolean useEmulator) {
        return useEmulator ? EMULATOR_URL : DEVICE_URL;
    }
} 