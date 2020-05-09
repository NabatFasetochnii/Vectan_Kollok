import kotlin.math.*

const val EPS = 1.0E-9

fun main() {

    val tenz: Array<DoubleArray> = Array(size = 3) { DoubleArray(3) { 0.0 } } //объект тензора

    println( //сообщение для пользователя
        "Введите последовательно нужные элементы симметричного тензора второго ранга\n" +
                "T11, T12, T13, T22, T23, T33"
    )

    tRead(tenz) //функция чтения элементов тензора с клавиатуры
    writeTenz(tenz) //выводим тензор
    val sz = findSz(tenz) // ищем собственные числа тригонометрической формулой Виета
    writeDTenz(sz) //выводим тензор в главных осях

    val tenz1 = arrayOf(
        doubleArrayOf(tenz[0][0], tenz[0][1], tenz[0][2]),
        doubleArrayOf(tenz[1][0], tenz[1][1], tenz[1][2]),
        doubleArrayOf(tenz[2][0], tenz[2][1], tenz[2][2])
    )// в котлине нет нормальной функции clone() или copy()...
    val tenz2 = arrayOf(
        doubleArrayOf(tenz[0][0], tenz[0][1], tenz[0][2]),
        doubleArrayOf(tenz[1][0], tenz[1][1], tenz[1][2]),
        doubleArrayOf(tenz[2][0], tenz[2][1], tenz[2][2])
    )
    val tenz3 = arrayOf(
        doubleArrayOf(tenz[0][0], tenz[0][1], tenz[0][2]),
        doubleArrayOf(tenz[1][0], tenz[1][1], tenz[1][2]),
        doubleArrayOf(tenz[2][0], tenz[2][1], tenz[2][2])
    )


    val sv1 = sv(sz[0], tenz1) //ищем собственные вектора
    val sv2 = sv(sz[1], tenz2)
    val sv3 = sv(sz[2], tenz3)

    println("\n Собственные вектора:") //выводим их на экран
    writeSV(sv1)
    writeSV(sv2)
    writeSV(sv3)

    normSV(sv1) //проводим нормализацию
    normSV(sv2)
    normSV(sv3)

    println("\nнормализованные собственные вектора:") //выводим их на экран
    writeSV(sv1)
    writeSV(sv2)
    writeSV(sv3)

    val left = tr(sv1, sv2, sv3) //проверка на левую тройку

    println("левая тройка $left")

    if (left){ //если тройка левая - делаем ациклическую перестановку
        acik(sv1, sv2)
        println("\nнормализованные собственные вектора (правая тройка):") //выводим их на экран
        writeSV(sv1)
        writeSV(sv2)
        writeSV(sv3)
    }


}

fun acik(sv1: DoubleArray, sv2: DoubleArray) {

    var v: Double
    for (i in 0..2){
        v = sv1[i]
        sv1[i] = sv2[i]
        sv2[i] = v
    }

}

fun tr(sv1: DoubleArray, sv2: DoubleArray, sv3: DoubleArray): Boolean {

    val tenz = arrayOf(sv1, sv2, sv3)
    return det(tenz)<0
}

fun normSV(sv: DoubleArray) {

    var a = 0.0
    for (i in 0..2){
        a += sv[i].pow(2.0)
    }

    for (i in 0..2){
        sv[i] /= a
    }
}

fun writeSV(sv: DoubleArray) {
    for (i in 0..2) println(sv[i])
    println()

}

fun sv(d: Double, tenz: Array<DoubleArray>): DoubleArray {

    val v = DoubleArray(size = 3)
    val b = BooleanArray(size = 3) { false }

    for (i in 0..2) {
        tenz[i][i] -= d
    }

    for (i in 0..1) {///ууууффффф

        if (abs(tenz[i][i]) < EPS) { //проверка на нулевой элемент на диагонали
            for (j in i..2) {
                if (abs(tenz[j][i]) > EPS) {
                    for (t in i..2) {
                        val w = tenz[i][t]
                        tenz[i][t] = tenz[j][t]
                        tenz[j][t] = w
                    }
                    break
                }
            }
            continue
        }
    }

    for (i in 0..2) {//приводим к треугольному виду
        if (abs(tenz[i][i]) > EPS) {
            var e = 0
            for (r in i + 1..2) {
                if (abs(tenz[r][i]) > EPS) {
                    e++
                }
            }
            if (e > 0) {
                for (k in i..1) {
                    val q = tenz[i][k]
                    for (o in i + 1..2) {
                        val p = tenz[o][k]
                        for (l in k..2) {

                            tenz[i][l] /= q
                            tenz[i][l] *= p
                            tenz[o][l] -= tenz[i][l]
                        }
                    }
                }
            }
        }
    }



    for (i in 2 downTo 0) {
        var k = 0
        for (j in 0..2) {
            if (abs(tenz[i][j]) > EPS) {
                k++
            }
        }
        when (k) {
            1 -> {
                v[i] = 0.0
                b[i] = true
            }
            0 -> {
                v[i] = 1.0
                b[i] = true
            }
        }

    }


    if (!b[1]) {
        v[1] = (-tenz[1][0] - tenz[1][2]) / tenz[1][1]
    }
    if (!b[0]) {
        v[0] = (-tenz[0][1] - tenz[0][2]) / tenz[0][0]
    }



    return v
}


fun writeDTenz(sz: DoubleArray) {

    println(
        "диагональный вид тензора: \n" +
                sz[0].toString() + "\t 0 \t" + "0 \n" +
                "0 \t" + sz[1].toString() + "\t 0 \n" +
                "0 \t0 \t" + sz[2].toString()
    )

}

fun writeTenz(tenz: Array<DoubleArray>) {
    println(
        tenz[0][0].toString() + "\t" + tenz[0][1].toString() + "\t" + tenz[0][2].toString() + "\n" +
                tenz[1][0].toString() + "\t" + tenz[1][1].toString() + "\t" + tenz[1][2].toString() + "\n" +
                tenz[2][0].toString() + "\t" + tenz[2][1].toString() + "\t" + tenz[2][2].toString() + "\n"
    )

}

fun findSz(tenz: Array<DoubleArray>): DoubleArray {

    val b = -(tenz[0][0] + tenz[1][1] + tenz[2][2])
    val c = tenz[1][1] * tenz[2][2] - tenz[1][2].pow(2.0) +
            tenz[0][0] * tenz[2][2] - tenz[0][2].pow(2.0) +
            tenz[1][1] * tenz[0][0] - tenz[0][1].pow(2.0)
    val d = -1 * det(tenz)

    //val p = c - b.pow(2.0)/3.0
    //val q = (2*b.pow(3.0)/27) - (b*c/3) + d

    //val qBig = (p/3.0).pow(3.0)+(q/2.0).pow(2.0)
    val qBig = (b.pow(2.0) - 3.0 * c) / 9.0
    val rBig = (2.0 * b.pow(3.0) - 9.0 * b * c + 27.0 * d) / 54.0

    when {
        qBig.pow(3.0) < rBig.pow(2.0) -> {
            println("ошибка в тригонометрической формуле Виета")
            return doubleArrayOf(0.0, 0.0, 0.0)
        }
        qBig.pow(3.0) > rBig.pow(2.0) -> {
            val x1 = -2 * qBig.pow(0.5) * cos(
                (1.0 / 3.0) *
                        acos(rBig / qBig.pow(3.0 / 2.0))
            ) - b / 3.0
            val x2 = -2 * qBig.pow(0.5) * cos(
                2.0 * PI / 3.0 + (1.0 / 3.0) *
                        acos(rBig / qBig.pow(3.0 / 2.0))
            ) - b / 3.0
            val x3 = -2 * qBig.pow(0.5) * cos(
                -2.0 * PI / 3.0 + (1.0 / 3.0) *
                        acos(rBig / qBig.pow(3.0 / 2.0))
            ) - b / 3.0
            return doubleArrayOf(x1, x2, x3)
        }
        else -> {
            val x1 = -2 * rBig.pow(1.0/3.0) - b / 3.0
            val x2 = rBig.pow(1.0/3.0)  - b / 3.0

            return doubleArrayOf(x1, x2, x2)
        }
    }


}

fun det(tenz: Array<DoubleArray>): Double {

    return (tenz[0][0] * (tenz[1][1] * tenz[2][2] - tenz[1][2] * tenz[2][1]) -
            tenz[0][1] * (tenz[1][0] * tenz[2][2] - tenz[2][0] * tenz[1][2]) +
            tenz[0][2] * (tenz[1][0] * tenz[2][1] - tenz[1][1] * tenz[2][0]))
}


fun tRead(tenz: Array<DoubleArray>) {

    for (i in 0..5) {

        val line = readLine()
        if (line != null) {
            when (i) {
                in 0..2 -> {
                    tenz[0][i] = line.toDouble()

                    if (i != 0) {
                        tenz[i][0] = line.toDouble()
                    }
                }
                3 -> {
                    tenz[1][1] = line.toDouble()
                }
                4 -> {
                    tenz[1][2] = line.toDouble()
                    tenz[2][1] = line.toDouble()
                }
                5 -> {
                    tenz[2][2] = line.toDouble()
                }
            }
        } else {
            println(
                "Введите числа"
            )
        }

    }
}
