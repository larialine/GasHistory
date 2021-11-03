package aula.pdm.gashistory.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aula.pdm.gashistory.MainActivity
import aula.pdm.gashistory.R
import aula.pdm.gashistory.databinding.LayoutGasBinding
import aula.pdm.gashistory.model.Gas

class GasRvAdapter(
    private val onGasClickListener: MainActivity,
    private val gasList: MutableList<Gas>
): RecyclerView.Adapter<GasRvAdapter.GasLayoutHolder>(){

    // Posicao que será recuperada pelo menu de contexto
    var posicao: Int = -1

    //ViewHolder
    inner class GasLayoutHolder(layoutGasBinding: LayoutGasBinding): RecyclerView.ViewHolder(layoutGasBinding.root), View.OnCreateContextMenuListener{
        val dataTv: TextView = layoutGasBinding.dataTv
        val precoTv: TextView = layoutGasBinding.precoTv
        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_main, menu)
        }
    }

    // Quando uma nova célula precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GasLayoutHolder {
        //Criar uma nova célula
        val layoutGasBinding = LayoutGasBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um viewHolder associado a nova célula
        val viewHolder = GasLayoutHolder(layoutGasBinding)
        return viewHolder
    }

    // Quando necessário atualizar valores de uma célula, seja uma célula nova ou antiga
    override fun onBindViewHolder(holder: GasLayoutHolder, position: Int) {
        //Buscar histórico gas
        val gas = gasList[position]

        //Atualizar os valores do viewHolder
        with(holder){
            dataTv.text = gas.data
            precoTv.text = gas.preco.toString()
            itemView.setOnClickListener {
                onGasClickListener.onGasClick(position)
            }
            itemView.setOnLongClickListener {
                posicao = position
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return gasList.size
    }

}
