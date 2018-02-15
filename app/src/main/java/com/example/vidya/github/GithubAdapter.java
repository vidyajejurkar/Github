package com.example.vidya.github;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;

/**
 * Created by Vidya on 2/15/2018.
 */


public class GithubAdapter extends RecyclerView.Adapter<GithubAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<UserList> userList;
    private List<UserList> userListFiltered;
    private UserListAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            user = view.findViewById(R.id.txtUser);
            thumbnail = view.findViewById(R.id.imageUser);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected user in callback
                    listener.onUserSelected(userListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public GithubAdapter(Context context, List<UserList> userList, UserListAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.userList = userList;
        this.userListFiltered = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GithubAdapter.MyViewHolder holder, int position) {
        final UserList user = userListFiltered.get(position);
        holder.user.setText(user.getLogin());

        Glide.with(holder.thumbnail.getContext()).load(user.getAvatarUrl()).into(holder.thumbnail);

    }


    @Override
    public int getItemCount() {
        return userListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    userListFiltered = userList;
                } else {
                    List<UserList> filteredList = new ArrayList<>();
                    for (UserList row : userList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name
                        if (row.getLogin().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    userListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = userListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userListFiltered = (ArrayList<UserList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface UserListAdapterListener {
        void onUserSelected(UserList user);
    }
}
