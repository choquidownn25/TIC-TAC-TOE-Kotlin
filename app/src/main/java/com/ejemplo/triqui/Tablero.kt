package com.ejemplo.triqui

class Tablero {
    //Para PERSONA y COMPUTADORA
    companion object {
        const val PERSONA = "O"
        const val COMPUTADORA = "X"
    }


    // Esta es un tablero que vamos a usar
    // y para esto usamos una matriz de 3 por 3 de cadenas
    val tablero = Array(3) { arrayOfNulls<String>(3) }

    // Esta propiedad nos está dando
    // una lista de todas las celdas vacías
    val availableCells: List<Cell>
        get() {
            val cells = mutableListOf<Cell>()
            for (i in tablero.indices) {
                for (j in tablero.indices) {
                    if (tablero[i][j].isNullOrEmpty()) {
                        cells.add(Cell(i, j))
                    }
                }
            }
            return cells
        }


    // Esta propiedad dirá
    // si el juego ha terminado o no
    val isGameOver: Boolean
        get() = hasCOMPUTADORAGano() || hasPERSONAGano() || availableCells.isEmpty()

    // Estas funciones están comprobando
    // El tiempo que la COMPUTADORA o PERSONA ha ganado o no
    fun hasCOMPUTADORAGano(): Boolean {
        if (tablero[0][0] == tablero[1][1] &&
            tablero[0][0] == tablero[2][2] &&
            tablero[0][0] == COMPUTADORA ||
            tablero[0][2] == tablero[1][1] &&
            tablero[0][2] == tablero[2][0] &&
            tablero[0][2] == COMPUTADORA
        ) {
            return true
        }

        for (i in tablero.indices) {
            if (
                tablero[i][0] == tablero[i][1] &&
                tablero[i][0] == tablero[i][2] &&
                tablero[i][0] == COMPUTADORA ||
                tablero[0][i] == tablero[1][i] &&
                tablero[0][i] == tablero[2][i] &&
                tablero[0][i] == COMPUTADORA
            ) {
                return true
            }
        }

        return false
    }

    fun hasPERSONAGano(): Boolean {

        if (tablero[0][0] == tablero[1][1] &&
            tablero[0][0] == tablero[2][2] &&
            tablero[0][0] == PERSONA ||
            tablero[0][2] == tablero[1][1] &&
            tablero[0][2] == tablero[2][0] &&
            tablero[0][2] == PERSONA
        ) {
            return true
        }

        for (i in tablero.indices) {
            if (
                tablero[i][0] == tablero[i][1] &&
                tablero[i][0] == tablero[i][2] &&
                tablero[i][0] == PERSONA ||
                tablero[0][i] == tablero[1][i] &&
                tablero[0][i] == tablero[2][i] &&
                tablero[0][i] == PERSONA
            ) {
                return true
            }
        }

        return false
    }


    // En OBJETO var almacenaremos el movimiento COMPUTADORA
    var COMPUTADORAsMove: Cell? = null


    // Esta es nuestra función minimax para calcular
    // el mejor movimiento para la COMPUTADORA
    fun minimax(depth: Int, PERSONA: String): Int {
        if (hasCOMPUTADORAGano()) return +1
        if (hasPERSONAGano()) return -1

        if (availableCells.isEmpty()) return 0

        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE

        for (i in availableCells.indices) {
            val cell = availableCells[i]
            if (PERSONA == COMPUTADORA) {
                placeMove(cell, COMPUTADORA)
                val currentScore = minimax(depth + 1, PERSONA)
                max = Math.max(currentScore, max)

                if (currentScore >= 0) {
                    if (depth == 0) COMPUTADORAsMove = cell
                }

                if (currentScore == 1) {
                    tablero[cell.i][cell.j] = ""
                    break
                }

                if (i == availableCells.size - 1 && max < 0) {
                    if (depth == 0) COMPUTADORAsMove = cell
                }

            } else if (PERSONA == PERSONA) {
                placeMove(cell, PERSONA)
                val currentScore = minimax(depth + 1, COMPUTADORA)
                min = Math.min(currentScore, min)

                if (min == -1) {
                    tablero[cell.i][cell.j] = ""
                    break
                }
            }
            tablero[cell.i][cell.j] = ""
        }

        return if (PERSONA == COMPUTADORA) max else min
    }

    // Esta función está colocando un movimiento en la celda dada
    fun placeMove(cell: Cell, PERSONA: String) {
        tablero[cell.i][cell.j] = PERSONA
    }
}