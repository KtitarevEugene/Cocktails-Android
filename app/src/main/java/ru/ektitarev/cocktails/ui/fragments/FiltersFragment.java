package ru.ektitarev.cocktails.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.ektitarev.cocktails.R;
import ru.ektitarev.cocktails.mvp.presenters.FiltersPresenter;
import ru.ektitarev.cocktails.mvp.views.FiltersView;
import ru.ektitarev.cocktails.ui.adapters.FiltersAdapter;
import ru.ektitarev.cocktails.ui.view_models.SelectedCategoriesViewModel;

public class FiltersFragment extends MvpAppCompatFragment implements
        FiltersView,
        FiltersAdapter.OnItemClickListener {

    @BindView(R.id.root_layout)
    View rootLayout;

    @BindView(R.id.filters_list)
    RecyclerView filtersList;

    @InjectPresenter
    FiltersPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        filtersList.setLayoutManager(manager);
        filtersList.setAdapter(createFiltersListAdapter());
    }

    @NotNull
    @Contract(" -> new")
    private FiltersAdapter createFiltersListAdapter() {
        SelectedCategoriesViewModel selectedCategoriesViewModel =
                ViewModelProviders.of(getActivity()).get(SelectedCategoriesViewModel.class);

        List<FiltersAdapter.FilterItem> selectedCategories =
                selectedCategoriesViewModel.getSelectedCategories().getValue();

        FiltersAdapter adapter = new FiltersAdapter();
        adapter.setOnItemClickListener(this);

        if (selectedCategories != null) {
            adapter.setItems(listCopy(selectedCategories));
        }

        return adapter;
    }

    @OnClick(R.id.submit_filters)
    public void onSubmitButtonClick(View v) {
        RecyclerView.Adapter adapter = filtersList.getAdapter();
        if (adapter != null) {
            FiltersAdapter filtersAdapter = (FiltersAdapter) adapter;

            ViewModelProviders.of(getActivity()).get(SelectedCategoriesViewModel.class)
                    .setSelectedCategories(filtersAdapter.getItems());
        }

        NavHostFragment.findNavController(this).popBackStack();
    }

    private List<FiltersAdapter.FilterItem> listCopy(List<FiltersAdapter.FilterItem> list) {
        List<FiltersAdapter.FilterItem> copyList = new ArrayList<>();

        for (FiltersAdapter.FilterItem item : list) {
            FiltersAdapter.FilterItem copyItem= new FiltersAdapter.FilterItem();

            copyItem.categoryModel = item.categoryModel;
            copyItem.isSelected = item.isSelected;

            copyList.add(copyItem);
        }

        return copyList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                NavHostFragment.findNavController(this).popBackStack();
                return true;
        }

        return false;
    }

    @Override
    public void showProgressBar(boolean visible) {}

    @Override
    public void showErrorMessage() {}

    @Override
    public void onItemClick(FiltersAdapter.FilterItem filterItem, int position) {
        filterItem.isSelected = !filterItem.isSelected;
    }
}
