# Despliegue local

## Gradle y JDK

Configurar el entorno con el gradle y compilar el proyecto

El proyecto usa Java 17, spring 3.4.3 y R2DBC para la conexión con base de datos


## Base de datos
Se necesita tener una base de datos postgresSQL con los siguentes datos

```
    url: r2dbc:postgresql://localhost:5432/franchises?currentSchema=public
    username: postgres
    password: 1234
```
O si no tiene los mismos datos para ingresar a db modifiquelos en el archivo [application.yaml](src/main/resources/application.yaml)

Antes de iniciar la aplicación, correr el script de las tablas en las base de datos, que se encuentra en [tables_database.sql](src/main/resources/tables_database.sql)

Tener libre el puerto 8080 o cambiarlo desde el yaml y para la base de datos el 5432

## Endpoints

Cuando el proyecto este corriendo se puede ingresar a la documentación con openapi en
http://localhost:8080/webjars/swagger-ui/index.html#/