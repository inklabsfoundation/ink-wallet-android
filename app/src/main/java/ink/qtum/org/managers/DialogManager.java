package ink.qtum.org.managers;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.utils.Coders;
import ink.qtum.org.views.custom.PinCodeLayout;

public class DialogManager {

    public static void showCopyMnemonicsDialog(Context context, final DialogListener listener) {
        MaterialDialog.Builder builder = getBaseDialog(context, listener);
        builder.content(R.string.copy_mnemonics_attention_content)
                .positiveText(R.string.btn_give_up)
                .negativeText(R.string.btn_still_copy)
                .positiveColor(QtumApp.getAppContext().getResources().getColor(R.color.btnBlueTextColor))
                .negativeColor(QtumApp.getAppContext().getResources().getColor(R.color.lightGrayTextColor))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        listener.onNegativeButtonClick();
                    }

                });
        MaterialDialog dialog = builder.build();
        dialog.getContentView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dialog.show();
    }

    public static void showCopySucceedDialog(Context context) {
        MaterialDialog.Builder builder = getBaseDialog(context, null);
        builder.customView(R.layout.dialog_copy_succeed, false);

        final MaterialDialog dialog = builder.build();
        ((TextView) dialog.findViewById(R.id.tv_succeed_text)).setText(R.string.copy_succeed);
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void showSucceedDialog(Context context, DialogInterface.OnDismissListener listener) {
        MaterialDialog.Builder builder = getBaseDialog(context, null);
        builder.customView(R.layout.dialog_copy_succeed, false);

        final MaterialDialog dialog = builder.build();
        ((TextView) dialog.findViewById(R.id.tv_succeed_text)).setText(R.string.succeeded);
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(listener);

        dialog.show();
    }

    public static void showTransferFailDialog(Context context, DialogInterface.OnDismissListener listener) {
        MaterialDialog.Builder builder = getBaseDialog(context, null);
        builder.customView(R.layout.dialog_fail, false);

        final MaterialDialog dialog = builder.build();
        ((TextView) dialog.findViewById(R.id.tv_fail_text)).setText(R.string.transfer_fail);
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(listener);
        dialog.show();
    }

    public static void showPinCodeDialog(final Context context, final String pinHash, String title,
                                         final boolean cancelable, final DialogListener listener) {
        MaterialDialog.Builder builder = getBaseDialog(context, null);
        builder.customView(R.layout.dialog_pin, false);

        final MaterialDialog dialog = builder.build();
        dialog.setCancelable(cancelable);
        ((TextView) dialog.findViewById(R.id.pin_dialog_title)).setText(title);
        final PinCodeLayout pinLayout = (PinCodeLayout) dialog.findViewById(R.id.dialog_pin_layout);
        pinLayout.setInputListener(new PinCodeLayout.OnPinCodeListener() {
            @Override
            public void pinCodeCreated(String pin) {
                if (pinHash.equals(Coders.getSha1Hex(pin))) {
                    listener.onPositiveButtonClick();
                    dialog.dismiss();
                } else {
                    pinLayout.callError();
                    Toast.makeText(context, "PIN code does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNegativeButtonClick();
                if (cancelable){
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public static void showLogOutDialog(Context context, final DialogListener listener) {
        MaterialDialog.Builder builder = getBaseDialog(context, listener);
        builder.content(R.string.logout_dialog_content)
                .title(R.string.title_warning)
                .positiveText(R.string.btn_go_to_backup)
                .negativeText("Understand, Log out")
                .positiveColor(QtumApp.getAppContext().getResources().getColor(R.color.btnBlueTextColor))
                .negativeColor(QtumApp.getAppContext().getResources().getColor(R.color.lightGrayTextColor))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        listener.onNegativeButtonClick();

                        super.onNegative(dialog);
                    }

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        listener.onPositiveButtonClick();

                        super.onPositive(dialog);
                    }
                });
        MaterialDialog dialog = builder.build();
        dialog.getTitleView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dialog.show();
    }

    private static MaterialDialog.Builder getBaseDialog(Context context, @Nullable final DialogListener listener) {
        return new MaterialDialog.Builder(context)
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
        public void onPositiveButtonClick() {
        }

        public void onNegativeButtonClick() {
        }

        public void onAnyButtonClick() {
        }

        public void onNeutralButtonClick() {
        }

        public void onDismiss() {
        }

    }

}
