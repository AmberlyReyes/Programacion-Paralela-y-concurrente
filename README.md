# Práctica 1 - Programación Paralela y Concurrente

## 💻 Descripción del Proyecto

Práctica #01 del curso **Programación Paralela y Concurrente** ICC 303. 
El objetivo principal es analizar el rendimiento de algoritmos paralelos utilizando la **Ley de Amdahl**, 
al implementar la suma de un arreglo de enteros de 1,000,000 de manera **secuencial** y **paralela**, y comparar los tiempos de ejecución al variar 
el número de hilos utilizados.



## ✨ Tecnologías utilizadas

- Java 21
- IDE: IntelliJ IDEA
- Procesador: 16 CPUs (~8 núcleos físicos + hyperthreading) -importante tomar en cuenta


## 📂 Archivos incluidos

| Archivo                | Descripción                                  |
|------------------------|----------------------------------------------|
| `GeneraArchivo.java`   | Genera el archivo base para realizar la suma.    
| `SumaSecuencial.java`  | Suma los números de forma secuencial (1 solo hilo). |
| `SumaEnParalelo.java`  | Suma los números utilizando múltiples hilos. |
| `AmbersNumbers`        | Archivo generado con los números aleatorios. |

---

## :) ¿Cómo compilar y ejecutar?

### 1. Compilar
pd: compilar en el siguiente orden, y tratar de tomar en cuenta primero el valor obtenido 
de la suma secuencial antes de compararlo con el paralelo :)

```bash
javac GeneraArchivo.java
javac SumaSecuencial.java
javac SumaEnParalelo.java
```

El archivo generado debe contener una estructura guiada por cambios de línea entre números.

Ejemplo de salida 👇🏼

- SumaSecuencial:

Suma total: 5002002777.

Tiempo secuencial: 0.005 segundos


- SumaEnParalelo: 

Suma total: 5002002777


Tiempo paralelo con 8 hilos: 0.001885 segundos

Autor:
Amber ♥
2025