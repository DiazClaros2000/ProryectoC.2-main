package com.example.proyectoc.api;

public class RestApiMethodsE {
    public static final String BASE_URL = "http://192.168.0.190/car_wash_api/empleados/";

    // Endpoints del CRUD de Empleados
    public static final String EndpointGetEmpleados = BASE_URL + "GetEmpleados.php";
    public static final String EndpointCreateEmpleado = BASE_URL + "CreateEmpleado.php";
    public static final String EndpointUpdateEmpleado = BASE_URL + "UpdateEmpleado.php";
    public static final String EndpointDeleteEmpleado = BASE_URL + "DeleteEmpleado.php";

    // Endpoints del CRUD de Servicios
    public static final String EndpointGetServicios = BASE_URL + "GetServicios.php";

}
