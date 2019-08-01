package ru.ektitarev.cocktails.ui.fragments;

import android.app.Activity;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ektitarev.cocktails.R;
import ru.ektitarev.cocktails.mvp.models.CategoriesListModel;
import ru.ektitarev.cocktails.mvp.models.CategoryModel;
import ru.ektitarev.cocktails.mvp.models.DrinksListModel;
import ru.ektitarev.cocktails.mvp.presenters.DrinksPresenter;
import ru.ektitarev.cocktails.mvp.views.DrinksView;
import ru.ektitarev.cocktails.ui.adapters.DrinksAdapter;
import ru.ektitarev.cocktails.ui.view_models.CategoriesViewModel;

public class DrinksFragment extends MvpAppCompatFragment implements DrinksView {

    private final int ITEMS_DELTA_FOR_LOADING = 3;

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

        CategoriesViewModel categoriesViewModel = ViewModelProviders.of(getActivity()).get(CategoriesViewModel.class);
        categoriesViewModel.getCategoriesLiveData().observe(getActivity(), (Observer<CategoriesListModel>) categoriesListModel -> {
            categoriesViewModel.setCategoryIndex(0);

            presenter.loadDrinks(categoriesListModel.getCategories().get(0));
        });
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drinks, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();

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

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (manager != null) {
                    int totalItemsCount = manager.getItemCount();
                    int lastVisibleItemPos = manager.findLastVisibleItemPosition();

                    if (lastVisibleItemPos + ITEMS_DELTA_FOR_LOADING >= totalItemsCount) {
                        loadNextCategory();
                    }
                }
            }
        });
    }

    private void loadNextCategory() {
        CategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(getActivity()).get(CategoriesViewModel.class);

        int currentCategoryIndex = categoriesViewModel.getCategoryIndex();
        CategoriesListModel categoriesListModel =
                categoriesViewModel.getCategoriesLiveData().getValue();

        if (categoriesListModel != null) {
            int categoriesCount = categoriesListModel.getCategories().size();

            if (currentCategoryIndex < categoriesCount - 1) {
                presenter.loadDrinks(
                        categoriesListModel.getCategories().get(currentCategoryIndex + 1));
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
        CategoriesViewModel categoriesViewModel = ViewModelProviders.of(getActivity()).get(CategoriesViewModel.class);

        categoriesViewModel.setCategoriesLiveData(categoriesListModel);
    }

    @Override
    public void addLoadedDrinks(CategoryModel category, DrinksListModel drinksListModel) {
        CategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(getActivity()).get(CategoriesViewModel.class);

        categoriesViewModel.setCategoryIndex(categoriesViewModel.getCategoryIndex() + 1);

        RecyclerView.Adapter adapter = drinksList.getAdapter();

        if (adapter instanceof DrinksAdapter) {
            DrinksAdapter drinksAdapter = (DrinksAdapter) adapter;

            drinksAdapter.addItems(category.getStrCategory(), drinksListModel.getDrinks());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.drinks_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
