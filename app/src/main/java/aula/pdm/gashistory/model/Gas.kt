package aula.pdm.gashistory.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gas(
    val data: String = "", //chave primária
    val preco: Float = 0.00f,
): Parcelable
