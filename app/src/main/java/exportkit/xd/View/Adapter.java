package exportkit.xd.View;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exportkit.xd.R;
import exportkit.xd.View.Profile.IProfile;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    List<String> titles, images;
    List<Integer> ids;
    LayoutInflater inflater;
    IProfile view;

    public Adapter(Context ctx, List<Integer> ids, List<String> titles, List<String> images){
        this.titles = titles;
        this.images = images;
        this.ids= ids;
        this.inflater = LayoutInflater.from(ctx);
        this.view= (IProfile) ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_display_profile,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        if(images.get(position).equals("null"))
            holder.gridIcon.setImageResource(R.drawable.recipeimage);
        else
            holder.gridIcon.setImageURI(Uri.parse(images.get(position)));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView gridIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView2);
            gridIcon = itemView.findViewById(R.id.imageView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    view.viewRecipeDetails(ids.get(getAdapterPosition()));
                }
            });
        }
    }
}