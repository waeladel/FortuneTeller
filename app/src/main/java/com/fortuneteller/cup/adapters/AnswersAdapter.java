package com.fortuneteller.cup.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fortuneteller.cup.Interface.ItemClickListener;
import com.fortuneteller.cup.R;
import com.fortuneteller.cup.databinding.AnswerItemBinding;
import com.fortuneteller.cup.models.Answer;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import static android.graphics.Typeface.BOLD;

public class AnswersAdapter extends PagingDataAdapter<Answer, AnswersAdapter.ViewHolder> {

    private final static String TAG = AnswersAdapter.class.getSimpleName();

    private AnswerItemBinding mBinding;
    private ItemClickListener itemClickListener;

    public Context context;

    private String[] mSpinnerArray;
    private ArrayAdapter<String> mSpinnerAdapter;

    public AnswersAdapter(Context context, @NotNull DiffUtil.ItemCallback<Answer> diffCallback, ItemClickListener itemClickListener) {
        super(diffCallback);
        this.context = context;
        this.itemClickListener = itemClickListener;

        // Create an ArrayAdapter that will contain all list items, to get the text by position in the array
        mSpinnerArray = context.getResources().getStringArray(R.array.answers_array);

        /* Assign the name array to that adapter and
        also choose a simple layout for the list items */
        mSpinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mSpinnerArray);

    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mBinding = AnswerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(mBinding);
    }



    @Override
    public void onBindViewHolder(@NonNull final AnswersAdapter.ViewHolder holder, final int position) {

        final Answer answer = getItem(position);
        if (answer != null) {
            // Answer message value
            if (answer.getPosition() != 0) {
                holder.mBinding.answerMessage.setText(mSpinnerAdapter.getItem(answer.getPosition()));
                //holder.mBinding.answerMessage.setText(context.getText(answer.getPosition()));
            }else if(!TextUtils.isEmpty(answer.getMessage())){
                holder.mBinding.answerMessage.setText(answer.getMessage());
            } else{
                holder.mBinding.answerMessage.setText(null);
            }

            // Created value
            if (answer.getCreated() > 0) {
                long now = System.currentTimeMillis();
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(answer.getCreated(), now, DateUtils.MINUTE_IN_MILLIS);
                holder.mBinding.createDate.setText(ago);
            }else{
                holder.mBinding.createDate.setText(null);
            }

         }

    }


    /*private void updateAvatarUri(String key, Uri uri) {
        if(mNotificationsRef != null){
            //DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
            //mNotificationsRef = mDatabaseRef.child("notifications").child("alerts").child(currentUserId);
            mNotificationsRef.child(key).child("senderAvatar").setValue(String.valueOf(uri)); // update senderAvatar to the new uri
        }

    }*/

    // CALLBACK to calculate the difference between the old item and the new item
    public static class AnswerComparator extends DiffUtil.ItemCallback<Answer> {
                // User details may have changed if reloaded from the database,
                // but ID is fixed.
                // if the two items are the same
                @Override
                public boolean areItemsTheSame(Answer oldItem, Answer newItem) {
                    Log.d(TAG, " DIFF_CALLBACK areItemsTheSame keys= old: " + oldItem.getMessage() +" new: "+ newItem.getMessage()+ " value= "+oldItem.getId().equals(newItem.getId()));
                    return oldItem.getId().equals(newItem.getId());
                }

                // if the content of two items is the same
                @Override
                public boolean areContentsTheSame(Answer oldItem, Answer newItem) {
                    Log.d(TAG, "notifications query DIFF_CALLBACK areContentsTheSame old name: " + oldItem.getMessage() + " new name: "+newItem.getMessage()+ " value= "+(oldItem.equals(newItem)));
                    return oldItem.equals(newItem);

                }
            };


   /* @Override
    public void submitList(PagedList<Message> pagedList) {
        super.submitList(pagedList);
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<Message> currentList) {
        super.onCurrentListChanged(currentList);
    }*/


        /// ViewHolder for ReceivedMessages list /////
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private AnswerItemBinding mBinding;


        public ViewHolder(AnswerItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if(itemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
                itemClickListener.onClick(view, getAdapterPosition(), false);
            }

            /*if(getAdapterPosition() != RecyclerView.NO_POSITION){
                Log.i(TAG, "user row clicked= "+view.getId()+ " Position= "+ getAdapterPosition());
                // get clicked notification
                DatabaseNotification notification = getItem(getAdapterPosition());
                if(notification != null){
                    notification.setClicked(true); // set clicked notification to true
                    mNotificationsRef.child(notification.getKey()).child("clicked").setValue(true);// update clicked field on database

                    // to open customers when notification type is not a message
                    NavDirections customersDirection = NotificationsFragmentDirections.actionNotificationsFragToProfileFrag(notification.getSenderId());
                    // to open chat room when notification type is a message
                    NavDirections MessageDirection = NotificationsFragmentDirections.actionNotificationsFragToMessagesFrag(notification.getChatId(), notification.getSenderId(), false);

                    if (null != notification.getType()) {
                        switch (notification.getType()){
                            case NOTIFICATION_TYPE_QUEUE_FRONT:
                                Navigation.findNavController(view).navigate(customersDirection);
                                break;
                            case NOTIFICATION_TYPE_QUEUE_NEXT:
                                Navigation.findNavController(view).navigate(customersDirection);
                                break;
                            case NOTIFICATION_TYPE_MESSAGE:
                                Navigation.findNavController(view).navigate(MessageDirection);
                                break;
                            default:
                                Navigation.findNavController(view).navigate(customersDirection);
                                break;
                        }

                    }
                }

            }*/

        }

    }

}

