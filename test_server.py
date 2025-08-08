#!/usr/bin/env python3
"""
Script para probar la conectividad del servidor web
"""
import requests
import socket
import sys

def get_local_ip():
    """Obtener la IP local de la máquina"""
    try:
        # Conectar a un servidor externo para obtener la IP local
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
        s.close()
        return ip
    except Exception:
        return "192.168.100.171"  # IP por defecto

def test_server_connection(ip, port=80):
    """Probar la conexión al servidor"""
    base_url = f"http://{ip}:{port}"
    test_url = f"{base_url}/car_wash_api/test_conexion.php"
    
    print(f"Probando conexión a: {test_url}")
    
    try:
        response = requests.get(test_url, timeout=5)
        print(f"✅ Conexión exitosa! Status: {response.status_code}")
        print(f"Respuesta: {response.text}")
        return True
    except requests.exceptions.ConnectionError:
        print(f"❌ Error de conexión: No se puede conectar a {test_url}")
        print("Asegúrate de que:")
        print("1. Tu servidor web (XAMPP, WAMP, etc.) esté ejecutándose")
        print("2. El puerto 80 esté disponible")
        print("3. El firewall no esté bloqueando la conexión")
        return False
    except requests.exceptions.Timeout:
        print(f"❌ Timeout: La conexión tardó demasiado")
        return False
    except Exception as e:
        print(f"❌ Error inesperado: {e}")
        return False

def main():
    print("=== Test de Conectividad del Servidor ===")
    
    # Obtener IP local
    local_ip = get_local_ip()
    print(f"IP local detectada: {local_ip}")
    
    # Probar conexión
    success = test_server_connection(local_ip)
    
    if success:
        print("\n🎉 ¡Servidor funcionando correctamente!")
        print(f"Tu aplicación Android debería poder conectarse a: http://{local_ip}/car_wash_api/")
    else:
        print("\n🔧 Pasos para solucionar:")
        print("1. Inicia tu servidor web (XAMPP, WAMP, etc.)")
        print("2. Verifica que el puerto 80 esté libre")
        print("3. Asegúrate de que tu teléfono y computadora estén en la misma red WiFi")
        print("4. Verifica que el firewall no esté bloqueando la conexión")

if __name__ == "__main__":
    main() 