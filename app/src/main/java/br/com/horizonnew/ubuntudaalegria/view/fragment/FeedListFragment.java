package br.com.horizonnew.ubuntudaalegria.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.OnGetUserFeedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.UserProviderBus;
import br.com.horizonnew.ubuntudaalegria.model.User;
import br.com.horizonnew.ubuntudaalegria.view.adapter.FeedListAdapter;
import retrofit2.Call;

public class FeedListFragment extends Fragment {

    public static final String TAG = "br.com.horizonnew.ubuntudaalegria.view.fragment.FeedListFragment";

    public static final String LOG_TAG = "FeedList";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private FeedListAdapter mAdapter;

    private User mUser;

    private Call mCall;

    public FeedListFragment() {
        // Required empty public constructor
    }

    public static FeedListFragment newInstance(@NonNull User user) {
        FeedListFragment fragment = new FeedListFragment();
        Bundle args = new Bundle();
        args.putParcelable(User.ARG, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null)
            mUser = getArguments().getParcelable(User.ARG);
        else
            mUser = savedInstanceState.getParcelable(User.ARG);

        try {
            UserProviderBus.getInstance().register(this);
        } catch (Exception e) {
            Log.d(LOG_TAG, "onCreate: ", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_feed_list_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_feed_list_recycler_view);

        mLayoutManager = new LinearLayoutManager(getContext());

        mAdapter = new FeedListAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeedList();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshFeedList();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            UserProviderBus.getInstance().unregister(this);
        } catch (Exception e) {
            Log.d(LOG_TAG, "onDestroy: ", e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(User.ARG, mUser);
    }

    private void refreshFeedList() {
        if (mCall == null) {
            mSwipeRefreshLayout.setRefreshing(true);

            mCall = mUser.getFeed();
        }
    }

    @Subscribe
    public void onGetUserFeed(OnGetUserFeedEvent event) {
        if (mUser.getId() == event.user.getId()) {

        }
    }
}