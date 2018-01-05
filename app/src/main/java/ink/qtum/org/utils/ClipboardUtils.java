package ink.qtum.org.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import ink.qtum.org.inkqtum.R;


public final class ClipboardUtils {

    private ClipboardUtils() {
    }

    public  static void copyToClipBoard(Context context, String text){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(context.getString(R.string.address), text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    public  static void silentCopyToClipBoard(Context context, String text){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(context.getString(R.string.address), text);
        clipboardManager.setPrimaryClip(clipData);
    }
}
