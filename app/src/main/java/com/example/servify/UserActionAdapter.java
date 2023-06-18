package com.example.servify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserActionAdapter extends RecyclerView.Adapter<UserActionAdapter.UserActionViewHolder> {

    private List<UserAction> userActionList;

    public UserActionAdapter(List<UserAction> userActionList) {
        this.userActionList = userActionList;
    }

    @NonNull
    @Override
    public UserActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_action, parent, false);
        return new UserActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserActionViewHolder holder, int position) {
        UserAction userAction = userActionList.get(position);
        holder.username.setText(userAction.getUser());
        holder.action.setText(userAction.getAction());

        String username = userAction.getUser();
        if (username.equals("User")) {
            holder.imageView.setImageResource(R.drawable.user_pic);
        } else if (username.equals("Admin")) {
            holder.imageView.setImageResource(R.drawable.admin_pic);
        } else {
            holder.imageView.setImageResource(R.drawable.default_pic);
        }
    }

    @Override
    public int getItemCount() {
        return userActionList.size();
    }

    public static class UserActionViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView action;
        ImageView imageView;
        public UserActionViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            action = itemView.findViewById(R.id.action);
            imageView = itemView.findViewById(R.id.image_profile);

        }
    }
}
