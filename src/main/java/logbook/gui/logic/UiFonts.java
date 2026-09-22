package logbook.gui.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;

/** アプリ内のフォントを、元の書体・太さを保ったまま3pt拡大する。 */
public final class UiFonts {
    public static final int INCREMENT = 3;
    private static final Map<String, Font> fonts = new HashMap<>();
    private static final Set<Font> enlarged = new HashSet<>();

    private UiFonts() { }

    public static void install(Display display) {
        display.addListener(SWT.Skin, event -> {
            if (event.widget instanceof Control control) {
                control.setFont(enlarge(control.getFont()));
                control.requestLayout();
            } else if (event.widget instanceof CTabItem item) {
                item.setFont(enlarge(item.getFont()));
            }
        });
        display.disposeExec(() -> {
            fonts.values().forEach(Font::dispose);
            fonts.clear();
            enlarged.clear();
        });
    }

    public static void markEnlarged(Font font) {
        enlarged.add(font);
    }

    public static FontData[] originalData(Font font) {
        FontData[] data = font.getFontData();
        if (enlarged.contains(font)) {
            for (FontData item : data) item.setHeight(item.getHeight() - INCREMENT);
        }
        return data;
    }

    public static Font enlarge(Font font) {
        if (enlarged.contains(font)) return font;
        FontData[] data = font.getFontData();
        for (FontData item : data) item.setHeight(item.getHeight() + INCREMENT);
        String key = Arrays.toString(data);
        Font result = fonts.computeIfAbsent(key, unused -> new Font(Display.getCurrent(), data));
        enlarged.add(result);
        return result;
    }
}
