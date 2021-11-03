package aula.pdm.gashistory.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GasFirebase: GasDAO {
    companion object{
        private val BD_GAS = "gas"
    }
    //Referência para o RtDb -> gas
    private val gasRtDb = Firebase.database.getReference(BD_GAS)

    // Lista de históricos que simula uma consulta
    private val gasList:MutableList<Gas> = mutableListOf()
    init {
        gasRtDb.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novoHistGas: Gas? = snapshot.value as? Gas

                novoHistGas?.apply {
                    if(gasList.find { it.data == this.data } == null){
                        gasList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val histGasEditado: Gas? = snapshot.value as? Gas

                histGasEditado?.apply {
                    gasList[gasList.indexOfFirst { it.data == this.data }] = this
                }
            }


            override fun onChildRemoved(snapshot: DataSnapshot) {
                val histGasRemovido: Gas? = snapshot.value as? Gas

                histGasRemovido?.apply {
                    gasList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }

        })
    }

    override fun criarHistorico(gas: Gas): Long {
        criarOuAtualizarHistGas(gas)
        return 0
    }

    override fun recuperarHistorico(data: String): Gas {
        return gasList.firstOrNull { it.data == data } ?: Gas()
    }

    override fun recuperarHistoricos(): MutableList<Gas> {
        return gasList
    }

    override fun atualizarHistorico(gas: Gas): Int {
        criarOuAtualizarHistGas(gas)
        return 1
    }

    override fun removerHistorico(data: String): Int {
        gasRtDb.child(data).removeValue()
        return 103
    }


    private fun criarOuAtualizarHistGas(gas: Gas){
        gasRtDb.child(gas.data).setValue(gas)
    }
}