package ink.qtum.org.managers;


import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;

public class DialogManager {

    public static void showCopyMnemonicsDialog(final DialogListener listener) {
        MaterialDialog.Builder builder = getBaseDialog(listener);
        builder.content(R.string.copy_mnemonics_attention_content)
                .positiveText(R.string.btn_still_copy)
                .negativeText(R.string.btn_give_up)
                .positiveColor(QtumApp.getAppContext().getResources().getColor(R.color.btnBlueTextColor))
                .negativeColor(QtumApp.getAppContext().getResources().getColor(R.color.lightGrayTextColor))
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        listener.onPositiveButtonClick();

                        super.onPositive(dialog);
                    }
                });
        MaterialDialog dialog = builder.build();
        dialog.getContentView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dialog.show();
    }

    public static void showCopySucceedDialog(){
        MaterialDialog.Builder builder = getBaseDialog(null);
        builder.customView(R.layout.dialog_copy_succeed, false);

        final MaterialDialog dialog = builder.build();
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private static MaterialDialog.Builder getBaseDialog(@Nullable final DialogListener listener) {
        return new MaterialDialog.Builder(QtumApp.getAppContext())
                .positiveColor(QtumApp.getAppContext().getResources().getColor(R.color.btnBlueTextColor))
                .negativeColor(QtumApp.getAppContext().getResources().getColor(R.color.lightGrayTextColor))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onAny(MaterialDialog dialog) {
                        super.onAny(dialog);
                        if (listener != null) {
                            listener.onAnyButtonClick();
                        }
                    }

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        if (listener != null) {
                            listener.onPositiveButtonClick();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        if (listener != null) {
                            listener.onNegativeButtonClick();
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                        if (listener != null) {
                            listener.onNeutralButtonClick();
                        }
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (listener != null) {
                            listener.onDismiss();
                        }
                    }
                });
    }

    public abstract static class DialogListener {
        public void onPositiveButtonClick() {}

        public void onNegativeButtonClick() {}

        public void onAnyButtonClick() {}

        public void onNeutralButtonClick() {}

        public void onDismiss() {}

    }

}
