package com.frg96.careercompare.ui.joblist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frg96.careercompare.R;
import com.frg96.careercompare.domain.model.RankedJob;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JobListAdaptor extends RecyclerView.Adapter<JobListAdaptor.MyViewHolder> {
    private final List<RankedJob> jobList;
    private final Context context;
    private Long currentJobId;

    public interface OnJobClickListener { void onJobClicked(long jobId); }
    private final Set<Long> selectedIds = new HashSet<>();
    private OnJobClickListener clickListener;

    public JobListAdaptor(Context context, List<RankedJob> jobList ){
        setHasStableIds(true);
        this.context = context;
        this.jobList = jobList;

    }
    public void setOnJobClickListener(OnJobClickListener l) { this.clickListener = l; }
    public void replaceData(List<RankedJob> newData) {
        jobList.clear();
        jobList.addAll(newData);
        notifyDataSetChanged(); // For production, prefer ListAdapter + DiffUtil
    }

    public void setSelectedIds(Collection<Long> ids) {
        selectedIds.clear();
        if (ids != null) selectedIds.addAll(ids);
        notifyDataSetChanged();
    }

    public void setCurrentJobId(Long currentJobId) {
        this.currentJobId = currentJobId;
        notifyDataSetChanged(); // refresh colors when changed
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.job_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cardView.animate().cancel();
        holder.cardView.setScaleX(1f);
        holder.cardView.setScaleY(1f);

        holder.jobTitle.setText(jobList.get(position).job.title);
        holder.jobCompany.setText(jobList.get(position).job.company);


        boolean isCurrent = currentJobId != null && currentJobId.equals(jobList.get(position).job.jobId);
        boolean isSelected = selectedIds.contains(jobList.get(position).job.jobId);

        int primary            = MaterialColors.getColor(holder.cardView, com.google.android.material.R.attr.colorOnPrimary);
        int primaryContainer   = MaterialColors.getColor(holder.cardView, com.google.android.material.R.attr.colorPrimaryContainer);
        int onPrimaryContainer = MaterialColors.getColor(holder.cardView, com.google.android.material.R.attr.colorOnPrimaryContainer);
        int surface            = MaterialColors.getColor(holder.cardView, com.google.android.material.R.attr.colorSurface);
        int outline            = MaterialColors.getColor(holder.cardView, com.google.android.material.R.attr.colorOutline);
        int onSurface          = MaterialColors.getColor(holder.cardView, com.google.android.material.R.attr.colorOnSurface);
        int onSurfaceVariant   = MaterialColors.getColor(holder.cardView, com.google.android.material.R.attr.colorOnSurfaceVariant);
        holder.cardView.setCheckable(true);

        if(isCurrent){
            String currentString = jobList.get(position).job.title + " (Current)";
            holder.jobTitle.setText(currentString);
            holder.cardView.setCardBackgroundColor(primaryContainer);
            holder.cardView.setStrokeColor(primary);
            holder.cardView.setStrokeWidth(2);               // highlight border
            holder.cardView.setClickable(true);              // for ripple
            holder.cardView.setOutlineSpotShadowColor(outline);
            holder.jobTitle.setTextColor(onPrimaryContainer);
            holder.jobCompany.setTextColor(onPrimaryContainer);
            holder.cardView.setChecked(isSelected);
        }else {
            holder.cardView.setCardBackgroundColor(primaryContainer);
            holder.cardView.setStrokeColor(primary);
            holder.cardView.setStrokeWidth(2);
            holder.jobTitle.setTextColor(onSurface);
            holder.jobCompany.setTextColor(onSurfaceVariant);
            holder.cardView.setChecked(isSelected);
        }

        holder.cardView.setOnClickListener(v -> {
            int pos = holder.getAbsoluteAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            final long jobId = jobList.get(pos).job.jobId;

            // Haptic first
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);

            holder.cardView.animate()
                    .scaleX(0.97f)
                    .scaleY(0.97f)
                    .setDuration(80)
                    .withEndAction(() -> {
                        holder.cardView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction(() -> {
                                    if (clickListener != null) clickListener.onJobClicked(jobId);
                                })
                                .start();
                    })
                    .start();
        });
    }

    @Override
    public int getItemCount() {
        Log.d("JobListAdaptor", "counting size of joblist: "+ jobList.isEmpty());
        return jobList.size();
    }

    @Override
    public long getItemId(int position) {
        return jobList.get(position).job.jobId;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobCompany;
        MaterialCardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jobTitle = itemView.findViewById(R.id.card_title);
            jobCompany = itemView.findViewById(R.id.card_company);
            cardView = itemView.findViewById(R.id.job_card);
        }
    }
}
