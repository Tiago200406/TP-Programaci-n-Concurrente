package Java_Quicksort_secuencial_vs_concurrente;

//Implementación del algoritmo QuickSort en su versión concurrente para ordenar un arreglo de enteros

public class QuicksortConcurrenteJava {
	  // Establecemos el límite de profundidad para crear nuevos hilos (ajustable)
    private static final int MAX_NIVEL = 4;

    public static void main(String[] args) {
        int[] arreglo = {34, 7, 23, 32, 5, 62, 78, 1, 18, 13, 45, 53, 67};

        System.out.println("Arreglo original:");
        imprimirArreglo(arreglo);

        // Creamos el hilo principal de QuickSort y lo iniciamos
        QuickSortThread hiloPrincipal = new QuickSortThread(arreglo, 0, arreglo.length - 1, 0);
        hiloPrincipal.start();

        try {
            // Esperamos a que termine
            hiloPrincipal.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nArreglo ordenado:");
        imprimirArreglo(arreglo);
    }

    // Clase interna que extiende Thread para ejecutar QuickSort de forma concurrente

    static class QuickSortThread extends Thread {
        int[] arreglo;
        int inicio, fin;
        int nivel;

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

                // Si estamos por debajo del límite de hilos, creamos nuevos threads
                if (nivel < MAX_NIVEL) {
                    QuickSortThread izquierda = new QuickSortThread(arreglo, inicio, pivote - 1, nivel + 1);
                    QuickSortThread derecha = new QuickSortThread(arreglo, pivote + 1, fin, nivel + 1);

                    izquierda.start();
                    derecha.start();

                    try {
                        izquierda.join();
                        derecha.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Si ya llegamos al límite, seguimos de forma secuencial
                    quickSortSecuencial(arreglo, inicio, pivote - 1);
                    quickSortSecuencial(arreglo, pivote + 1, fin);
                }
            }
        }

        // Creamos el algoritmo de partición
        private int particionar(int[] arreglo, int inicio, int fin) {
            int pivote = arreglo[fin];
            int i = inicio - 1;
            for (int j = inicio; j < fin; j++) {
                if (arreglo[j] < pivote) {
                    i++;
                    intercambiar(arreglo, i, j);
                }
            }
            intercambiar(arreglo, i + 1, fin);
            return i + 1;
        }

        // Método secuencial usado como fallback
        private void quickSortSecuencial(int[] arreglo, int inicio, int fin) {
            if (inicio < fin) {
                int pivote = particionar(arreglo, inicio, fin);
                quickSortSecuencial(arreglo, inicio, pivote - 1);
                quickSortSecuencial(arreglo, pivote + 1, fin);
            }
        }

        private void intercambiar(int[] arreglo, int i, int j) {
            int temp = arreglo[i];
            arreglo[i] = arreglo[j];
            arreglo[j] = temp;
        }
    }

    public static void imprimirArreglo(int[] arreglo) {
        for (int valor : arreglo) {
            System.out.print(valor + " ");
        }
        System.out.println();
    }
}