package aula.pdm.gashistory.controller

import aula.pdm.gashistory.MainActivity
import aula.pdm.gashistory.model.Gas
import aula.pdm.gashistory.model.GasDAO
import aula.pdm.gashistory.model.GasFirebase

class GasController(mainActivity: MainActivity) {

    private val gasDao: GasDAO = GasFirebase()

    fun inserirHistorico(gas: Gas) = gasDao.criarHistorico(gas)
    fun buscarHistorico(data: String) = gasDao.recuperarHistorico(data)
    fun buscarHistoricos() = gasDao.recuperarHistoricos()
    fun alterarHistorico(gas: Gas) = gasDao.atualizarHistorico(gas)
    fun apagarHistorico(data: String) = gasDao.removerHistorico(data)
}