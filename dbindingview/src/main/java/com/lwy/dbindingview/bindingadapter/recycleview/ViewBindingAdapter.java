package com.lwy.dbindingview.bindingadapter.recycleview;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lwy.dbindingview.ItemBinding;
import com.lwy.dbindingview.LayoutManagers;
import com.lwy.dbindingview.adapter.BindingRecyclerViewAdapter;
import com.lwy.dbindingview.command.ReplyCommand;
import com.lwy.dbindingview.command.ReplyCommand2;

import java.util.List;

public class ViewBindingAdapter {
    // RecyclerView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "onItemClick", "onItemLongClick"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView, ItemBinding<T> itemBinding,
                                      List<T> items, BindingRecyclerViewAdapter<T> adapter,
                                      BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
                                      BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,
                                      final ReplyCommand2 onItemClickCommand,
                                      final ReplyCommand2 onItemLongClickCommand) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = new BindingRecyclerViewAdapter<>();
            } else {
                adapter = oldAdapter;
            }
        }
        if (onItemClickCommand != null || onItemLongClickCommand != null)
            adapter.setOnItemClickListener(new BindingRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (onItemClickCommand != null) {
                        onItemClickCommand.execute(holder, position);
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (onItemLongClickCommand != null) {
                        onItemLongClickCommand.execute(holder, position);
                        return true;
                    } else
                        return false;
                }
            });
        adapter.setItemBinding(itemBinding);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }


    @BindingAdapter(value = {"onScrollChangeCommand", "onScrollStateChangedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final RecyclerView recyclerView,
                                             final ReplyCommand<ScrollDataWrapper> onScrollChangeCommand,
                                             final ReplyCommand<Integer> onScrollStateChangedCommand) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int state;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollChangeCommand != null) {
                    onScrollChangeCommand.execute(new ScrollDataWrapper(dx, dy, state));
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state = newState;
                if (onScrollStateChangedCommand != null) {
                    onScrollChangeCommand.equals(newState);
                }
            }
        });

    }

//    @BindingAdapter("footerVM")
//    public static void onLoadMoreCommand(final RecyclerView recyclerView, final RcVFooterVM rcVFooterVM) {
//        RecyclerView.OnScrollListener listener = new OnScrollListener(rcVFooterVM);
//        recyclerView.addOnScrollListener(listener);
//    }

//    public static class OnScrollListener extends RecyclerView.OnScrollListener {
//
//        private final RcVFooterVM RcVFooterVM;
//
//        public OnScrollListener(RcVFooterVM rcVFooterVM) {
//            this.RcVFooterVM = rcVFooterVM;
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            if (dy > 0) {
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                    if (this.RcVFooterVM.getOnLoadMoreCommand() != null && !this.RcVFooterVM.getIsFooterLoading().get()) {
//                        this.RcVFooterVM.switchLoading(true);
//                        this.RcVFooterVM.getOnLoadMoreCommand().execute(recyclerView.getAdapter().getItemCount());
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//        }
//
//
//    }

    public static class ScrollDataWrapper {
        public float scrollX;
        public float scrollY;
        public int state;

        public ScrollDataWrapper(float scrollX, float scrollY, int state) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
            this.state = state;
        }
    }


}
