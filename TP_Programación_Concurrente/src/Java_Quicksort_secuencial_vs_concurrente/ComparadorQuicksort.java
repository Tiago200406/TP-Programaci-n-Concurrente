package Java_Quicksort_secuencial_vs_concurrente;

import java.util.Random;

public class ComparadorQuicksort {

    private static final int[] TAMANIOS = {10, 1_000, 100_000, 1_000_000, 10_000_000};
    private static final int MAX_NIVEL = 4;

    public static void main(String[] args) {
        System.out.println("Comparativa de tiempos (en milisegundos):");
        System.out.printf("%-12s %-20s %-20s%n", "Tamaño", "Secuencial (ms)", "Concurrente (ms)");

        for (int tamaño : TAMANIOS) {
            int[] arregloBase = generarArregloAleatorio(tamaño);

            // Secuencial
            int[] arregloSecuencial = arregloBase.clone();
            long inicioSec = System.nanoTime();
            quickSortSecuencial(arregloSecuencial, 0, arregloSecuencial.length - 1);
            long finSec = System.nanoTime();
            long tiempoSec = (finSec - inicioSec) / 1_000_000;

            // Concurrente
            int[] arregloConcurrente = arregloBase.clone();
            long inicioConc = System.nanoTime();
            QuickSortThread hilo = new QuickSortThread(arregloConcurrente, 0, arregloConcurrente.length - 1, 0);
            hilo.start();
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long finConc = System.nanoTime();
            long tiempoConc = (finConc - inicioConc) / 1_000_000;

            System.out.printf("%-12d %-20d %-20d%n", tamaño, tiempoSec, tiempoConc);
        }

        // Especificaciones de tu PC
        System.out.println("\nEspecificaciones del sistema:");
        System.out.println("Procesador: AMD A6-7480 Radeon R5, 8 Compute Cores 2C+6G 3.50 GHz");
        System.out.println("RAM: 4,00 GB (3,88 GB usable)");
        System.out.println("Almacenamiento: SSD PNY CS900 240GB");
        System.out.println("Tarjeta gráfica: AMD Radeon R5 Graphics (63 MB)");
        System.out.println("Sistema operativo: Windows 10 64 bits (x64)");
    }

    // Quicksort Secuencial
    public static void quickSortSecuencial(int[] arr, int inicio, int fin) {
        if (inicio < fin) {
            int pivote = particionar(arr, inicio, fin);
            quickSortSecuencial(arr, inicio, pivote - 1);
            quickSortSecuencial(arr, pivote + 1, fin);
        }
    }

    public static int particionar(int[] arr, int inicio, int fin) {
        int pivote = arr[fin];
        int i = inicio - 1;
        for (int j = inicio; j < fin; j++) {
            if (arr[j] < pivote) {
                i++;
                intercambiar(arr, i, j);
            }
        }
        intercambiar(arr, i + 1, fin);
        return i + 1;
    }

    public static void intercambiar(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Quicksort Concurrente
    static class QuickSortThread extends Thread {
        int[] arreglo;
        int inicio, fin, nivel;

        QuickSortThread(int[] arreglo, int inicio, int fin, int nivel) {
            this.arreglo = arreglo;
            this.inicio = inicio;
            this.fin = fin;
            this.nivel = nivel;
        }

        @Override
        public void run() {
            if (inicio < fin) {
                int pivote = particionar(arreglo, inicio, fin);
                if (nivel < MAX_NIVEL) {
                    QuickSortThread izq = new QuickSortThread(arreglo, inicio, pivote - 1, nivel + 1);
                    QuickSortThread der = new QuickSortThread(arreglo, pivote + 1, fin, nivel + 1);
                    izq.start();
                    der.start();
                    try {
                        izq.join();
                        der.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    quickSortSecuencial(arreglo, inicio, pivote - 1);
                    quickSortSecuencial(arreglo, pivote + 1, fin);
                }
            }
        }
    }

    public static int[] generarArregloAleatorio(int tamaño) {
        int[] arr = new int[tamaño];
        Random r = new Random();
        for (int i = 0; i < tamaño; i++) {
            arr[i] = r.nextInt(1_000_000);
        }
        return arr;
    }
}
