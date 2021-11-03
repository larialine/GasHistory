package aula.pdm.gashistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import aula.pdm.gashistory.databinding.ActivityGasBinding;
import aula.pdm.gashistory.model.Gas;

public class GasActivity extends AppCompatActivity {

    private ActivityGasBinding activityGasBinding;
    private int posicao = -1;
    private Gas gas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGasBinding = ActivityGasBinding.inflate(getLayoutInflater());
        setContentView(activityGasBinding.getRoot());

        activityGasBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    gas = new Gas(
                            activityGasBinding.dataEt.getText().toString(),
                            Float.parseFloat(activityGasBinding.precoEt.getText().toString())
                    );

                    //retornar histórico gas(dados preenchido na tela) para a MainActivity
                    Intent resultadoIntent = new Intent();
                    if(posicao != -1){
                        resultadoIntent.putExtra(MainActivity.EXTRA_GAS, posicao);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        //Verificando se é uma edição ou consulta e preenchendo os campos
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        gas = getIntent().getParcelableExtra(MainActivity.EXTRA_GAS);
        if(gas != null){
            activityGasBinding.dataEt.setEnabled(false);
            activityGasBinding.precoEt.setText(String.valueOf(gas.getPreco()));
            if(posicao == -1){
                for(int i=0; i<activityGasBinding.getRoot().getChildCount(); i++){
                    activityGasBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityGasBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }


}
