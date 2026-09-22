/**
 * 
 */
package logbook.gui.logic;

import logbook.config.AppConfig;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * @author Nekopanda
 *
 */
public class ColorManager {

    /**
     * タイマーの状態色を、テキスト欄のシステム背景色に合わせます。
     * macOSのダーク表示では親の背景色に合わせ、それ以外はシステム背景色を使います。
     * 暗いテーマでは状態色を30%混ぜた背景色を使います。
     * SWTのUIスレッドから呼び出してください。
     */
    public static Color getTimerBackground(Text timer, Color statusColor) {
        boolean macDark = isMacDarkTheme();
        Color background = macDark ? timer.getParent().getBackground() : getColor(SWT.COLOR_LIST_BACKGROUND);
        if (statusColor == null) {
            return background;
        }
        RGB base = background.getRGB();
        if (!macDark && !isDark(base)) {
            return statusColor;
        }
        RGB status = statusColor.getRGB();
        return getColor(new RGB(
                (base.red * 7 + status.red * 3) / 10,
                (base.green * 7 + status.green * 3) / 10,
                (base.blue * 7 + status.blue * 3) / 10));
    }

    /** SWTのUIスレッドで、現在のウィジェット背景色からmacOSのダーク表示を判定します。 */
    public static boolean isMacDarkTheme() {
        return "cocoa".equals(SWT.getPlatform()) && isDark(getColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB());
    }

    private static boolean isDark(RGB color) {
        return (299 * color.red + 587 * color.green + 114 * color.blue) < 128000;
    }

    public static Color getColor(RGB[] rgbs) {
        int index = AppConfig.get().isColorSupport() ? 1 : 0;
        return SWTResourceManager.getColor(rgbs[index]);
    }

    public static Color getColor(RGB rgb) {
        return SWTResourceManager.getColor(rgb);
    }

    public static Color getColor(int[] ids) {
        int index = AppConfig.get().isColorSupport() ? 1 : 0;
        return SWTResourceManager.getColor(ids[index]);
    }

    public static Color getColor(int id) {
        return SWTResourceManager.getColor(id);
    }

}
