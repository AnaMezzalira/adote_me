package com.example.adoteme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/** Adapter do GridView na tela de perfil da ONG (cell = item_animal.xml). */
public class GridAnimalAdapter extends BaseAdapter {

    public interface OnAnimalDeleteListener { void onDelete(Animal a); }

    private final Context                ctx;
    private final List<Animal>           animais;
    private final OnAnimalDeleteListener listener;
    private final LayoutInflater         inflater;

    public GridAnimalAdapter(Context c, List<Animal> list, OnAnimalDeleteListener l) {
        this.ctx      = c;
        this.animais  = list;
        this.listener = l;
        this.inflater = LayoutInflater.from(c);
    }

    @Override public int    getCount()         { return animais.size(); }
    @Override public Object getItem(int pos)   { return animais.get(pos); }
    @Override public long   getItemId(int pos) { return animais.get(pos).id; }

    /* ---------- ViewHolder ---------- */
    private static class VH {
        ImageView    foto;
        ImageButton  left, right;
        TextView     nome, idade, raca, vac, sexo, cast, cond;
        Button       btnDel;
        int          idxFoto = 0;
        byte[][]     fotos;          // até 4 fotos
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int pos, View convert, ViewGroup parent) {
        VH h;
        if (convert == null) {
            convert = inflater.inflate(R.layout.item_animal, parent, false);
            h = new VH();

            h.foto  = convert.findViewById(R.id.selecionar_fotos);
            h.left  = convert.findViewById(R.id.seta_esquerda);
            h.right = convert.findViewById(R.id.seta_direita);

            h.nome  = convert.findViewById(R.id.nome_animal);
            h.idade = convert.findViewById(R.id.idade_animal);
            h.raca  = convert.findViewById(R.id.raca);
            h.vac   = convert.findViewById(R.id.vacina);
            h.sexo  = convert.findViewById(R.id.sexo);
            h.cast  = convert.findViewById(R.id.castrado);
            h.cond  = convert.findViewById(R.id.condicoes_medicas);

            h.btnDel = convert.findViewById(R.id.btnDelete);

            convert.setTag(h);
        } else {
            h = (VH) convert.getTag();
        }

        /* ---------- Dados do animal ---------- */
        Animal a = animais.get(pos);

        h.nome .setText(a.nome);
        h.idade.setText("Idade: "    + a.idade);
        h.raca .setText("Raça: "     + a.raca);
        h.vac  .setText("Vacinas: "  + a.vacina);
        h.sexo .setText("Sexo: "     + a.sexo);
        h.cast .setText("Castrado: " + a.castrado);
        h.cond .setText("Condições médicas: " + a.condicoes_medicas());

        /* ---------- Fotos ---------- */
        h.fotos   = new byte[][]{ a.foto1(), a.foto2(), a.foto3(), a.foto4() };
        h.idxFoto = 0;
        mostrarFoto(h);

        h.left.setOnClickListener(v -> {
            h.idxFoto = (h.idxFoto - 1 + 4) % 4;
            mostrarFoto(h);
        });
        h.right.setOnClickListener(v -> {
            h.idxFoto = (h.idxFoto + 1) % 4;
            mostrarFoto(h);
        });

        /* ---------- Clique na célula → ItemAnimalActivity ---------- */
        convert.setOnClickListener(v -> {
            Intent i = new Intent(ctx, ItemAnimalActivity.class);
            i.putExtra("ID_ANIMAL", a.id);     // mesma chave usada na Activity
            ctx.startActivity(i);
        });

        /* ---------- Remover ---------- */
        h.btnDel.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(a);
        });

        return convert;
    }

    /* Helper: exibe a foto atual ou placeholder */
    private void mostrarFoto(VH h) {
        byte[] foto = h.fotos[h.idxFoto];
        if (foto != null) {
            h.foto.setImageBitmap(BitmapFactory.decodeByteArray(foto, 0, foto.length));
        } else {
            h.foto.setImageResource(R.drawable.logo);  // placeholder
        }
    }
}
