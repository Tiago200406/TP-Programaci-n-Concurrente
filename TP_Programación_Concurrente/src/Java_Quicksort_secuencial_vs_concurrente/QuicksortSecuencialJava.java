package Java_Quicksort_secuencial_vs_concurrente;

//Implementación del algoritmo QuickSort en su versión secuencial para ordenar un arreglo de enteros

public class QuicksortSecuencialJava {
	// Establecemos el método principal para probar el algoritmo
    public static void main(String[] args) {
        int[] arreglo = {34, 7, 23, 32, 5, 62, 78, 1, 18, 13, 45, 53, 67};

        System.out.println("Arreglo original:");
        imprimirArreglo(arreglo);

        // Llamamos al método de ordenamiento QuickSort
        quickSort(arreglo, 0, arreglo.length - 1);

        System.out.println("\nArreglo ordenado:");
        imprimirArreglo(arreglo);
    }

    // Método QuickSort recursivo
    public static void quickSort(int[] arreglo, int inicio, int fin) {
        // Caso base: si el subarreglo tiene un solo elemento o está vacío
        if (inicio < fin) {
            // Obtenemos el índice del pivote después de la partición
            int indicePivote = particionar(arreglo, inicio, fin);

            // Llamamos recursivamente para las sublistas izquierda y derecha
            quickSort(arreglo, inicio, indicePivote - 1);
            quickSort(arreglo, indicePivote + 1, fin);
        }
    }

    // Método para dividir el arreglo y retornar el índice del pivote
    public static int particionar(int[] arreglo, int inicio, int fin) {
        int pivote = arreglo[fin]; // Elegimos el último elemento como pivote
        int i = inicio - 1; // El valor i indica el lugar donde se deben colocar los menores al pivote

        // Recorremos el subarreglo
        for (int j = inicio; j < fin; j++) {
            if (arreglo[j] < pivote) {
                i++;
                // Intercambiamos los elementos
                intercambiar(arreglo, i, j);
            }
        }

        // Colocamos el pivote en su lugar correcto
        intercambiar(arreglo, i + 1, fin);

        return i + 1;
    }

    // Método encargado de intercambiar dos elementos en el arreglo
    public static void intercambiar(int[] arreglo, int i, int j) {
        int temp = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = temp;
    }

    // Establecemos el método para imprimir el contenido de un arreglo
    public static void imprimirArreglo(int[] arreglo) {
        for (int valor : arreglo) {
            System.out.print(valor + " ");
        }
        System.out.println();
    }
}
