package aula.pdm.gashistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import aula.pdm.gashistory.adapter.GasRvAdapter
import aula.pdm.gashistory.controller.GasController
import aula.pdm.gashistory.databinding.ActivityMainBinding
import aula.pdm.gashistory.model.Gas
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), onGasClickListener {

    //Conseguir passar paramêtros de uma tela para outra
    companion object Extras{
        const val EXTRA_GAS = "EXTRA GAS"
        const val EXTRA_POSICAO = "EXTRA POSICAO"
    }

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var gasActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarGasActivityResultLauncher: ActivityResultLauncher<Intent>

    //Data Source
    private val gasList: MutableList<Gas> by lazy{
        gasController.buscarHistoricos()
    }

    //Controler
    private val gasController: GasController by lazy {
        GasController(this)
    }

    // Adapter
    private val gasAdapter: GasRvAdapter by lazy {
        GasRvAdapter(this, gasList)
    }

    //LayoutManager
    private val gasLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        // Associando Adapter e LayoutManager ao RecycleView
        activityMainBinding.HistoricoRv.adapter = gasAdapter
        activityMainBinding.HistoricoRv.layoutManager = gasLayoutManager

        gasActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
            if(resultado.resultCode == RESULT_OK){
                //recebendo histórico gas
                resultado.data?.getParcelableExtra<Gas>(EXTRA_GAS)?.apply {
                    gasController.inserirHistorico(this)
                    //adicionando histórico no gasList e no Adapter
                    gasList.add(this)
                    gasAdapter.notifyDataSetChanged()
                }
            }
        }

        editarGasActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
            if(resultado.resultCode == RESULT_OK){
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Gas>(EXTRA_GAS)?.apply {
                    if(posicao != null && posicao != -1){
                        gasController.alterarHistorico(this)
                        gasList[posicao] = this
                        gasAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarHistoricoFab.setOnClickListener {
            gasActivityResultLauncher.launch(Intent(this, GasActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = gasAdapter.posicao
        val gas = gasList[posicao]

        return when(item.itemId){
            R.id.editarGasMi -> {
                //Editar histórico gas
                val gas = gasList[posicao]
                val editarGasIntent = Intent(this, GasActivity::class.java)
                editarGasIntent.putExtra(EXTRA_GAS, gas)
                editarGasIntent.putExtra(EXTRA_POSICAO, posicao)
                editarGasActivityResultLauncher.launch(editarGasIntent)
                true
            }
            R.id.removerGasMi -> {
                //Remover histórico gas
                with(AlertDialog.Builder(this)){
                    setMessage("Deseja realmente remover?")
                    setPositiveButton("Sim"){_, _ ->
                        gasController.apagarHistorico(gas.data)
                        gasList.removeAt(posicao)
                        gasAdapter.notifyDataSetChanged()
                        Snackbar.make(activityMainBinding.root, "Histórico removido", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não"){_, _ ->
                        Snackbar.make(activityMainBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            }
            else -> {
                false
            }
        }
    }

    override fun onGasClick(posicao: Int) {
        val gas = gasList[posicao]
        val consultarHistoricoGasIntent = Intent(this, GasActivity::class.java)
        consultarHistoricoGasIntent.putExtra(EXTRA_GAS, gas)
        startActivity(consultarHistoricoGasIntent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId){
        R.id.atualizarMi -> {
            gasAdapter.notifyDataSetChanged()
            true
        }
        else -> false
    }
}