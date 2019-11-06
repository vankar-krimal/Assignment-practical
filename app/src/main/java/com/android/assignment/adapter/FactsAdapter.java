package com.android.assignment.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.assignment.R;
import com.android.assignment.model.Fact;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.FactsViewHolder> {

    private Context context;
    private ArrayList<Fact> facts;

    // default constructor with context
    public FactsAdapter(Context context) {
        this.context = context;
        facts = new ArrayList<>();
    }

    // method for fetch facts after api call, set to adapter using notifyDataSetChanged()
    public void setFacts(ArrayList<Fact> facts) {
        this.facts = new ArrayList<>();
        this.facts = facts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fact, parent, false);
        return new FactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FactsViewHolder holder, int position) {
        // bind data to row by its position
        holder.bind(facts.get(position));
    }

    @Override
    public int getItemCount() {
        // no of rows
        return facts.size();
    }

    class FactsViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtDescription;
        private SimpleDraweeView draweeView;

        FactsViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            draweeView = itemView.findViewById(R.id.draweeView);
        }

        // set data to each row
        void bind(Fact fact) {

            // title
            if (TextUtils.isEmpty(fact.getTitle())) {
                txtTitle.setText(context.getString(R.string.no_title));
            } else {
                txtTitle.setText(fact.getTitle());
            }

            // description
            if (!TextUtils.isEmpty(fact.getDescription())) {
                //txtDescription.setVisibility(View.VISIBLE);
                txtDescription.setText(fact.getDescription());
            } else {
                txtDescription.setText(context.getString(R.string.no_description));

                // txtDescription.setVisibility(View.GONE);
            }

            // image
            if (!TextUtils.isEmpty(fact.getImageHref())) {
                draweeView.setVisibility(View.VISIBLE);
                draweeView.setImageURI(Uri.parse(fact.getImageHref()));
            }
        }
    }
}

