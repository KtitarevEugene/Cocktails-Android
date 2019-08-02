package ru.ektitarev.cocktails.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ektitarev.cocktails.R;
import ru.ektitarev.cocktails.mvp.models.CategoriesListModel;
import ru.ektitarev.cocktails.mvp.models.CategoryModel;
import ru.ektitarev.cocktails.mvp.models.DrinksListModel;
import ru.ektitarev.cocktails.mvp.presenters.DrinksPresenter;
import ru.ektitarev.cocktails.mvp.views.DrinksView;
import ru.ektitarev.cocktails.ui.adapters.DrinksAdapter;
import ru.ektitarev.cocktails.ui.adapters.FiltersAdapter;
import ru.ektitarev.cocktails.ui.view_models.SelectedCategoriesViewModel;

public class DrinksFragment extends MvpAppCompatFragment implements
        DrinksView,
        Observer<List<FiltersAdapter.FilterItem>> {

    private final int ITEMS_DELTA_BEFORE_LOADING = 3;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.root_layout)
    View rootLayout;

    @BindView(R.id.drinks_list)
    RecyclerView drinksList;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @InjectPresenter
    DrinksPresenter presenter;

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
        View view = inflater.inflate(R.layout.fragment_drinks, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        initSwipeRefreshLayout();

        return view;
    }

    private void initRecyclerView() {

        drinksList.setAdapter(new DrinksAdapter());

        drinksList.setLayoutManager(
                new LinearLayoutManager(getActivity()));
        drinksList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager manager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();

                if (manager != null) {
                    int totalItemsCount = manager.getItemCount();
                    int lastVisibleItemPos = manager.findLastVisibleItemPosition();

                    if (lastVisibleItemPos + ITEMS_DELTA_BEFORE_LOADING >= totalItemsCount) {
                        loadNextCategory();
                    }
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        SelectedCategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(getActivity()).get(SelectedCategoriesViewModel.class);

        List<FiltersAdapter.FilterItem> categories = categoriesViewModel
                .getSelectedCategories().getValue();

        if (categories != null) {

            int selectedCategoryIndex = categoriesViewModel.findNextSelectedCategoryPos(0);

            RecyclerView.Adapter adapter = drinksList.getAdapter();
            if (adapter != null) {
                if (selectedCategoryIndex != -1) {
                    ((DrinksAdapter) adapter).clear();
                    presenter.loadDrinks(categories
                            .get(selectedCategoryIndex).categoryModel, selectedCategoryIndex);
                } else {
                    ((DrinksAdapter) adapter).clear();
                }
            }
        }
    }

    private void loadNextCategory() {
        SelectedCategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(getActivity()).get(SelectedCategoriesViewModel.class);

        int currentCategoryIndex = categoriesViewModel.getCategoryIndex();
        List<FiltersAdapter.FilterItem> categoriesListModel =
                categoriesViewModel.getSelectedCategories().getValue();

        if (categoriesListModel != null) {
            int categoriesCount = categoriesListModel.size();

            if (currentCategoryIndex < categoriesCount - 1) {
                int nextIndex = categoriesViewModel.
                        findNextSelectedCategoryPos(currentCategoryIndex + 1);

                if (nextIndex != -1) {
                    presenter.loadDrinks(categoriesListModel.get(nextIndex).categoryModel, nextIndex);
                }
            }
        }
    }

    @Override
    public void showProgressBar(boolean visible) {

        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showErrorMessage() {

        Snackbar.make(rootLayout, R.string.loading_error_text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setAllCategories(CategoriesListModel categoriesListModel) {
        SelectedCategoriesViewModel selectedCategoriesViewModel =
                ViewModelProviders.of(getActivity()).get(SelectedCategoriesViewModel.class);

        selectedCategoriesViewModel
                .setSelectedCategories(
                        convertCategoriesModelList(categoriesListModel.getCategories()));
    }

    private List<FiltersAdapter.FilterItem> convertCategoriesModelList(
            @NotNull List<CategoryModel> categoryModels) {

        List<FiltersAdapter.FilterItem> filterItems = new ArrayList<>();
        for (CategoryModel categoryModel : categoryModels) {

            FiltersAdapter.FilterItem item = new FiltersAdapter.FilterItem();

            item.isSelected = true;
            item.categoryModel = categoryModel;

            filterItems.add(item);
        }

        return filterItems;
    }

    @Override
    public void addLoadedDrinks(CategoryModel category, DrinksListModel drinksListModel, int index) {
        refreshLayout.setRefreshing(false);

        SelectedCategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(getActivity()).get(SelectedCategoriesViewModel.class);

        categoriesViewModel.setCategoryIndex(index);

        RecyclerView.Adapter adapter = drinksList.getAdapter();

        if (adapter instanceof DrinksAdapter) {
            DrinksAdapter drinksAdapter = (DrinksAdapter) adapter;

            drinksAdapter.addItems(category.getStrCategory(), drinksListModel.getDrinks());
        }
    }

    @Override
    public void clearState() {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.drinks_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.filter:
                NavHostFragment.findNavController(this)
                        .navigate(R.id.navigate_to_filters);
                setObserver();
                return true;
        }

        return false;
    }

    private boolean avoid;

    private void setObserver() {
        avoid = true;
        SelectedCategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(getActivity()).get(SelectedCategoriesViewModel.class);
        categoriesViewModel.getSelectedCategories().observe(getActivity(), this);
    }

    @Override
    public void onChanged(List<FiltersAdapter.FilterItem> categoriesListModel) {
        if (avoid) {
            avoid = false;
            return;
        }

        SelectedCategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(getActivity()).get(SelectedCategoriesViewModel.class);

        int selectedCategoryIndex = categoriesViewModel.findNextSelectedCategoryPos(0);

        RecyclerView.Adapter adapter = drinksList.getAdapter();
        if (adapter != null) {
            if (selectedCategoryIndex != -1) {
                ((DrinksAdapter) adapter).clear();
                presenter.clearState();
                presenter.loadDrinks(categoriesListModel
                        .get(selectedCategoryIndex).categoryModel, selectedCategoryIndex);
            } else {
                ((DrinksAdapter) adapter).clear();
            }
        }

        categoriesViewModel.getSelectedCategories().removeObserver(this);
    }
}
