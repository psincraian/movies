package com.psincraian.movies.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.psincraian.movies.R;

public class SelectMoviesDialog extends DialogFragment {

    public interface SelectMoviesListener {
        void onMovieSelected(int option);
    }

    public static final int TOP_MOVIES = 0;
    public static final int POPULAR_MOVIES = 1;

    private SelectMoviesListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_movies)
                .setItems(R.array.movies_filter_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onMovieSelected(which);
                    }
                });
        return builder.create();
    }

    public static SelectMoviesDialog newInstance(SelectMoviesListener listener) {
        SelectMoviesDialog dialog = new SelectMoviesDialog();
        dialog.listener = listener;
        return dialog;
    }
}
