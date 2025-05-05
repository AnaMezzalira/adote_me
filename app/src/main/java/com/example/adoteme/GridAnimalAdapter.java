package com.example.adoteme;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class GridAnimalAdapter extends BaseAdapter {

    private final Context context;
    private final List<Animal> animais;

    public GridAnimalAdapter(Context context, List<Animal> animais) {
        this.context = context;
        this.animais = animais;
    }

    @Override public int getCount()               { return animais.size();         }
    @Override public Object getItem(int p)        { return animais.get(p);         }
    @Override public long getItemId(int p)        { return animais.get(p).id;      }

    @Override
    public View getView(int pos, View v, ViewGroup parent) {
        if (v == null)
            v = LayoutInflater.from(context).inflate(R.layout.activity_item_animal, parent, false);

        Animal  animal = animais.get(pos);
        ImageView  foto    = v.findViewById(R.id.selecionar_fotos);
        TextView   nome    = v.findViewById(R.id.nome_animal);
        ImageButton delBtn = v.findViewById(R.id.botao_remover);

        foto.setImageBitmap(BitmapFactory.decodeByteArray(animal.foto, 0, animal.foto.length));
        nome.setText(animal.nome);

        delBtn.setOnClickListener(btn -> {
            new AlertDialog.Builder(context)
                    .setTitle("Excluir animal")
                    .setMessage("Excluir " + animal.nome + "?")
                    .setPositiveButton("Sim", (d,i) -> {
                        DatabaseHelper db = new DatabaseHelper(context);
                        if (db.excluirAnimal(animal.id)) {
                            animais.remove(pos);
                            notifyDataSetChanged();
                            Toast.makeText(context,"Removido!",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
        return v;
    }
}
