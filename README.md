# SistemaMultiAgenteComercio ![version](https://img.shields.io/badge/version-1.0-blue)

Trabajo de la asignatura Programación Avanzada en el 3º Curso del Grado de Ingeniería Informática USAL

## Finalidad del proyecto 🚀

La finalidad de este trabajo es la comunicación entre agentes para realizar una tarea, este caso hay 3 agentes que se comunican entre sí para 

### Pre-requisitos 📋

_Para instalar este programa necesitas tener instalado al menos:_

Java 15 o superior
 
### Instalación 🔧

 Se descargan los archivos y el archivo stockTienda.csv se debe colocar en el *Escritorio*.
  
### Ejecución 🔧

  Se añade el proyecto a alguna IDE de Java (Está preparado para Eclipse) y se ejecuta, primero se lanza la tienda y luego el cliente o clientes y finalmente el proveedor. Y se ejecuta así:
  * Main class --> jade.Boot
  * *Tienda* (Program arguments)-->  **-gui Tienda:agentes.AgentTienda**
  * *Cliente/es* (Program arguments)-->  **-main false -host 127.0.0.1 Cliente:agentes.AgentCliente**
  * *Proveedor* (Program arguments)-->  **-main false -host 127.0.0.1 Proveedor:agentes.AgentProveedor**
  
## Construido con 🛠️

_Estas son las herramientas usadas para el desarrollo_

* biblioteca.jar - [Biblioteca](http://maxus.fis.usal.es/HOTHOUSE/p3/biblioteca.jar) propiedad de D. José R. García-Bermejo Giner 
* Eclipse - IDE de Java
* JADE - Biblioteca para gestión de agentes en Java

## Autores ✒️

Desarrollado por:

* **Ángel Picado Cuadrado** - [angelcubo01](https://github.com/angelcubo01)
* **David Salvador Aguado** - 
* **Jorge López Carnero** -

## Licencia 📄

Este proyecto está bajo la Licencia GNU General Public License v3.0 - mira el archivo [GNU General Public License v3.0](https://github.com/angelcubo01/SistamaMultiAgenteComercio/blob/main/LICENSE) para detalles

## Ayuda

En caso de necesitar más información contactar con [angelcubo01](https://github.com/angelcubo01)
