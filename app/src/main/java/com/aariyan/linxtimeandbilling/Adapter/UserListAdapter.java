package com.aariyan.linxtimeandbilling.Adapter;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aariyan.linxtimeandbilling.Interface.Authentication;
import com.aariyan.linxtimeandbilling.MainActivity;
import com.aariyan.linxtimeandbilling.Model.UserListModel;
import com.aariyan.linxtimeandbilling.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;
    private List<UserListModel> list;
    Authentication authentication;

    public UserListAdapter(Context context, List<UserListModel> list, Authentication authentication) {
        this.context = context;
        this.list = list;
        this.authentication = authentication;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_user_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserListModel model = list.get(position);
        holder.userName.setText(model.getStrName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication.onClick(model.getStrName(), model.getStrPinCode(), model.getuID());
                MainActivity.behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.useName);
        }
    }
}
