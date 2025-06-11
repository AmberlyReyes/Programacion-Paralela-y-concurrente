# Topologias de conexión entre nodos

##  Objetivo de la práctica

Simular el envío de mensajes entre nodos que se encuentran interconectados mediante 
distintas topologías de red, y observar el comportamiento de cada una de ellas, como 
el enrutamiento y la recepción de mensajes.

##  Topologías implementadas

Este simulador incluye soporte para las siguientes topologías:

| Topología     | Descripción |
|---------------|-------------|
| Bus           | Todos los nodos comparten un canal común. |
| Ring          | Cada nodo está conectado al siguiente formando un ciclo. |
| Mesh          | Nodos en una malla 2D conectados a sus vecinos ortogonales. |
| Star          | Todos los nodos están conectados a un nodo central (hub). |
| Hypercube     | Nodos organizados como vértices de un hipercubo. |
| Tree         | Red jerárquica con nodos padres e hijos (árbol binario). |
| FullyConnected | Todos los nodos están conectados directamente entre sí. |
| Switched    | Todos los nodos se comunican a través de un switch central. |

---

## Requisitos

- Java 8 o superior
- Gradle (ya incluido vía wrapper)

---

## ¿Cómo ejecutar el proyecto?

1. Clonar o descomprime este repositorio.
2. Abre una terminal en la carpeta raíz.
3. Ejecuta:
./gradlew run

De todas formas, al correr el archivo principal "Main.java" debe funcionarle correctamente

## Importante
# ¿Cómo funciona?
Al ejecutar el programa, se mostrará un menú interactivo:
1. Selecciona la topología deseada (del 1 al 8) 🩷
2. Especifica el número de nodos.
3. Envía mensajes entre nodos
Se puede enviar varios mensajes y finalizar escribiendo -1 como ID de origen.