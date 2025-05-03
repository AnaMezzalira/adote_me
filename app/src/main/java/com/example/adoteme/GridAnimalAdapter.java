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

    Context context;
    List<Animal> animais;

    public GridAnimalAdapter(Context context, List<Animal> animais) {
        this.context = context;
        this.animais = animais;
    }

    @Override
    public int getCount() {
        return animais.size();
    }

    @Override
    public Object getItem(int position) {
        return animais.get(position);
    }

    @Override
    public long getItemId(int position) {
        return animais.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Animal animal = animais.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_animal, parent, false);
        }

        ImageView foto = convertView.findViewById(R.id.foto_animal);
        TextView nome = convertView.findViewById(R.id.nome_animal);
        ImageButton deletar = convertView.findViewById(R.id.botao_excluir);

        foto.setImageBitmap(BitmapFactory.decodeByteArray(animal.foto, 0, animal.foto.length));
        nome.setText(animal.nome);

        deletar.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Excluir animal")
                    .setMessage("Tem certeza que deseja excluir " + animal.nome + "?")
                    .setPositiveButton("Sim", (d, i) -> {
                        DatabaseHelper db = new DatabaseHelper(context);
                        db.excluirAnimal(animal.id);
                        Toast.makeText(context, "Removido!", Toast.LENGTH_SHORT).show();
                        animais.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        return convertView;
    }
}
