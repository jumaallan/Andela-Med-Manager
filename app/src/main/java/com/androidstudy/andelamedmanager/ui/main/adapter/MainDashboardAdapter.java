package com.androidstudy.andelamedmanager.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidstudy.andelamedmanager.R;
import com.androidstudy.andelamedmanager.data.model.MenuView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainDashboardAdapter extends RecyclerView.Adapter<MainDashboardAdapter.MenuOptionsViewHolder> {
    CustomItemClickListener listener;
    private Context context;
    private List<MenuView> menuList;

    public MainDashboardAdapter(Context context, List<MenuView> menuList, CustomItemClickListener listener) {
        this.context = context;
        this.menuList = menuList;
        this.listener = listener;
    }

    @Override
    public MenuOptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_main_dashboard, null);
        final MenuOptionsViewHolder mViewHolder = new MenuOptionsViewHolder(view);
        view.setOnClickListener(v -> listener.onItemClick(v, mViewHolder.getPosition()));
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MenuOptionsViewHolder holder, int position) {
        MenuView menuItem = menuList.get(position);
        holder.bindData(menuItem);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    class MenuOptionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.textView)
        TextView textView;

        public MenuOptionsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindData(MenuView menuItem) {
            textView.setText(menuItem.getName());
            imageView.setImageResource(menuItem.getImage());
        }
    }

}