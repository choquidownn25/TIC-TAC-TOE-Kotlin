package com.ejemplo.triqui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // Crear una matriz 2D de ImageViews
    private val boardCeldas = Array(3) { arrayOfNulls<ImageView>(3) }
    //creating the board instance
    var tablero = Tablero()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // llamando a la función para cargar nuestro tablero triqui
        cargaJuego()

        // funcionalidad de reinicio
        button_restart.setOnClickListener {

            // creando una nueva instancia de tablero
            // vaciará cada celda
            tablero = Tablero()
            // Mostrar el resultado como vacío
            text_view_result.text = ""
            // Llama función del tablero visual
            mapBoardToUi()
        }

    }
    /*
    * Esta función está generando el tablero
    * */
    private fun cargaJuego(){
        for (i in boardCeldas.indices) {
            for (j in boardCeldas.indices) {
                boardCeldas[i][j] = ImageView(this)
                //Para crear una variable que puede contener null, necesitas añadir un ? después del tipo de la variable.
                boardCeldas[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                // pintamos el tablero
                boardCeldas[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                // adjunto los clics al tablero
                boardCeldas[i][j]?.setOnClickListener(CellClickListener(i, j))
                layout_board.addView(boardCeldas[i][j])
            }
        }
    }

/*
    la función es mapeo de matriz ImageView
 */
private fun mapBoardToUi() {
    for (i in tablero.tablero.indices) {
        for (j in tablero.tablero.indices) {
            when (tablero.tablero[i][j]) {
                Tablero.PERSONA -> {
                    boardCeldas[i][j]?.setImageResource(R.drawable.circle)
                    boardCeldas[i][j]?.isEnabled = false
                }
                Tablero.COMPUTADORA -> {
                    boardCeldas[i][j]?.setImageResource(R.drawable.cross)
                    boardCeldas[i][j]?.isEnabled = false
                }
                else -> {
                    boardCeldas[i][j]?.setImageResource(0)
                    boardCeldas[i][j]?.isEnabled = true
                }
            }
        }
    }
}
/*
    Clase de invocaion click
 */
inner class CellClickListener(
val i: Int,
val j: Int
) : View.OnClickListener {
// aquí codificaremos el movimiento
override fun onClick(p0: View?) {
    // comprobar si el juego no ha terminado
    if (!tablero.isGameOver) {

        // Crear una nueva celda con el índice cliqueado
        val cell = Cell(i, j)

        // colocando el movimiento para el jugador
        tablero.placeMove(cell, Tablero.PERSONA)

        // llamando a minimax para calcular el movimiento de las computadoras
        tablero.minimax(0, Tablero.COMPUTADORA)

        // realizar el movimiento para computadora
        tablero.COMPUTADORAsMove?.let {
            tablero.placeMove(it, Tablero.COMPUTADORA)
        }

        mapBoardToUi()
    }

    // Mostrar los resultados
    // según el estado del juego
    when {//El Case para Java
        tablero.hasCOMPUTADORAGano() -> text_view_result.text = getString(R.string.app_juegocom)
        tablero.hasPERSONAGano() -> text_view_result.text = getString(R.string.app_juegoplay)
        tablero.isGameOver -> text_view_result.text = getString(R.string.app_juegoti)
    }
    }
}

}

