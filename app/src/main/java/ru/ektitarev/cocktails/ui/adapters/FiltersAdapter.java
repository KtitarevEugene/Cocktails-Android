package ru.ektitarev.cocktails.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ektitarev.cocktails.R;
import ru.ektitarev.cocktails.mvp.models.CategoryModel;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(FilterItem filterItem, int position);
    }

    public static class FilterItem {
        public CategoryModel categoryModel;
        public boolean isSelected;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_name)
        AppCompatTextView categoryName;

        @BindView(R.id.checkbox)
        AppCompatImageView checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<FilterItem> items;
    private OnItemClickListener onItemClickListener;

    public FiltersAdapter() {
        items = new ArrayList<>();
    }

    public FiltersAdapter(List<FilterItem> items) {
        this();
        this.items.addAll(items);
    }

    public void setItems(List<FilterItem> items) {
        this.items.clear();
        this.items.addAll(items);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilterItem filterItem = items.get(holder.getAdapterPosition());

        holder.categoryName.setText(filterItem.categoryModel.getStrCategory());
        holder.checkbox.setVisibility(filterItem.isSelected ? View.VISIBLE : View.INVISIBLE);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(
                        items.get(holder.getAdapterPosition()), holder.getAdapterPosition());

                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<FilterItem> getItems() {
        return items;
    }
}
