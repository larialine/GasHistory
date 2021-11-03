package aula.pdm.gashistory.model

interface GasDAO {
    fun criarHistorico(gas: Gas): Long
    fun recuperarHistorico(data: String): Gas
    fun recuperarHistoricos(): MutableList<Gas>
    fun atualizarHistorico(gas: Gas): Int //retornar qtde de linhas alteradas
    fun removerHistorico(data: String): Int //retornar qtde de linhas removidas
}