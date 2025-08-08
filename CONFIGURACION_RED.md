# Configuración de Red para Aplicación Android

## Problema Resuelto

Tu aplicación Android no se conectaba desde tu teléfono porque tenía configuraciones incorrectas de red.

## Cambios Realizados

### 1. ✅ Corregido error tipográfico en `ApiClient.java`
- **Antes**: `http://192.162.100.171/car_wash_api/` (error: 162 en lugar de 168)
- **Después**: `http://192.168.100.171/car_wash_api/`

### 2. ✅ Creado sistema de configuración flexible
- Nuevo archivo: `NetworkConfig.java`
- Permite cambiar fácilmente entre emulador y dispositivo físico
- Para usar emulador: cambiar `IS_EMULATOR = true`
- Para usar dispositivo físico: mantener `IS_EMULATOR = false`

### 3. ✅ Actualizada configuración de seguridad de red
- Archivo: `network_security_config.xml`
- Ahora permite conexiones a:
  - `10.0.2.2` (emulador)
  - `192.168.100.171` (tu IP actual)
  - `localhost`

## Pasos para Usar la Aplicación en tu Teléfono

### Paso 1: Verificar que tu servidor web esté funcionando

1. **Si usas XAMPP:**
   - Abre XAMPP Control Panel
   - Inicia Apache
   - Verifica que esté en el puerto 80

2. **Si usas WAMP:**
   - Abre WAMP
   - Verifica que el ícono esté verde
   - Apache debe estar ejecutándose

3. **Si usas otro servidor:**
   - Asegúrate de que esté ejecutándose en el puerto 80

### Paso 2: Verificar conectividad

Ejecuta el script de prueba:
```bash
python test_server.py
```

### Paso 3: Configurar tu teléfono

1. **Conecta tu teléfono a la misma red WiFi** que tu computadora
2. **Habilita la depuración USB** en tu teléfono
3. **Conecta tu teléfono por USB** a tu computadora

### Paso 4: Compilar y ejecutar

1. En Android Studio, selecciona tu dispositivo físico
2. Compila y ejecuta la aplicación
3. La aplicación debería conectarse automáticamente a tu servidor

## Solución de Problemas

### Si la aplicación no se conecta:

1. **Verifica la IP:**
   ```bash
   ipconfig
   ```
   Asegúrate de que la IP en `NetworkConfig.java` coincida con tu IP actual.

2. **Verifica el firewall:**
   - Asegúrate de que el puerto 80 esté abierto
   - Verifica que Windows Firewall no esté bloqueando Apache

3. **Verifica la red:**
   - Tu teléfono y computadora deben estar en la misma red WiFi
   - Prueba hacer ping desde tu teléfono a tu computadora

4. **Verifica el servidor:**
   - Abre tu navegador y ve a: `http://192.168.100.171/car_wash_api/test_conexion.php`
   - Deberías ver una respuesta del servidor

### Para cambiar entre emulador y dispositivo:

En `NetworkConfig.java`:
```java
// Para emulador:
public static final boolean IS_EMULATOR = true;

// Para dispositivo físico:
public static final boolean IS_EMULATOR = false;
```

## Archivos Modificados

- `app/src/main/java/com/example/proyectoc/api/ApiClient.java`
- `app/src/main/java/com/example/proyectoc/api/RestApiMethods.java`
- `app/src/main/java/com/example/proyectoc/api/NetworkConfig.java` (nuevo)
- `app/src/main/res/xml/network_security_config.xml`

## Notas Importantes

- La aplicación ahora usa HTTP (no HTTPS) para desarrollo local
- Asegúrate de que tu servidor web esté ejecutándose antes de probar la aplicación
- Si cambias de red WiFi, es posible que necesites actualizar la IP en `NetworkConfig.java` 