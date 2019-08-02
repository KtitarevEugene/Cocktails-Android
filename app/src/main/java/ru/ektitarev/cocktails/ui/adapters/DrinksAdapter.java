package ru.ektitarev.cocktails.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ektitarev.cocktails.R;
import ru.ektitarev.cocktails.mvp.models.DrinkModel;

public class DrinksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.drink_title)
        AppCompatTextView drinkTitle;

        @BindView(R.id.drink_image)
        AppCompatImageView drinkImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_name)
        AppCompatTextView categoryName;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private interface Item {
        int getItemType();
    }

    private static class ListItem implements Item {
        ListItem(String name, String imageUrl) {
            drinkName = name;
            this.imageUrl = imageUrl;
        }

        String drinkName;
        String imageUrl;

        @Override
        public int getItemType() {
            return TYPE_ITEM;
        }
    }

    private static class ListHeader implements Item {

        String categoryName;

        @Override
        public int getItemType() {
            return TYPE_HEADER;
        }
    }

    private List<Item> items;

    public DrinksAdapter() {
        items = new ArrayList<>();

        notifyDataSetChanged();
    }

    public DrinksAdapter(String categoryName, List<DrinkModel> items) {
        setItems(categoryName, items);
    }

    public void setItems(String categoryName, List<DrinkModel> items) {
        this.items = collectToList(categoryName, items);

        notifyDataSetChanged();
    }

    public void addItems(String categoryName, List<DrinkModel> items) {
        List<Item> itemsToAdd = collectToList(categoryName, items);

        int startPosition = this.items.size();
        int endPosition = startPosition + items.size() - 1;

        this.items.addAll(itemsToAdd);

        notifyItemRangeInserted(startPosition, endPosition);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drink, parent, false);
            return new ItemViewHolder(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_drink_category, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ListItem item = (ListItem) items.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            itemViewHolder.drinkTitle.setText(item.drinkName);

            Glide.with(itemViewHolder.itemView)
                    .load(item.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .placeholder(R.drawable.ic_drink)
                    .into(itemViewHolder.drinkImage);
        } else {
            ListHeader item = (ListHeader) items.get(position);
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.categoryName.setText(item.categoryName);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    @NotNull
    private List<Item> collectToList(@NotNull String categoryName, List<DrinkModel> items) {
        List<Item> list = new ArrayList<>();

        ListHeader item = new ListHeader();
        item.categoryName = categoryName;
        list.add(item);

        for (DrinkModel drink : items) {
            ListItem drinkItem = new ListItem(drink.getStrDrink(), drink.getStrDrinkThumb());
            list.add(drinkItem);
        }

        return list;
    }

    public void clear() {
        items.clear();

        notifyDataSetChanged();
    }
}
