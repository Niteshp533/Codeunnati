package exportkit.xd.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import exportkit.xd.R;
import exportkit.xd.View.Scanner.TrackIngredients_activity;

public class Adapter_ingr extends RecyclerView.Adapter<Adapter_ingr.ViewHolder>{

    String[] ingredient;
    LayoutInflater inflater;
    TrackIngredients_activity view;

    public Adapter_ingr(Context ctx, String[] ingredient){
        this.ingredient = ingredient;
        this.inflater = LayoutInflater.from(ctx);
        this.view= (TrackIngredients_activity) ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ingredient_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ingredient_name.setText(ingredient[position]);
    }

    @Override
    public int getItemCount() {
        return ingredient.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ingredient_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.igred_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    view.viewFacts(ingredient[getAdapterPosition()]);
                }
            });
        }
    }
}