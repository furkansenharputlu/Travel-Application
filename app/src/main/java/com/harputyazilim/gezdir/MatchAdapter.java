package com.harputyazilim.gezdir;

/**
 * Created by furkan on 26.03.2017.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.List;

        import android.support.v7.widget.RecyclerView;


        import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private List<Match> matchList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            image = (ImageView) view.findViewById(R.id.profileImage);
        }
    }


    public MatchAdapter(List<Match> matchList, Context context) {
        this.matchList = matchList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matches_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.name.setText(match.getName());
        holder.image.setBackground(ContextCompat.getDrawable(context,R.mipmap.location));
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }
}
