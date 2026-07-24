package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormatSymbols;

@CodeHistory(date = "2026/5/1")
public class DecimalFormatSymbolsTransformer implements JsonContextTransformer<DecimalFormatSymbols> {

    public static final DecimalFormatSymbolsTransformer INSTANCE = new DecimalFormatSymbolsTransformer();

    @Override
    public void transform(@NotNull DecimalFormatSymbols in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("zeroDigit");
            out.stringValue(String.valueOf(in.getZeroDigit()));
            out.key("groupingSeparator");
            out.stringValue(String.valueOf(in.getGroupingSeparator()));
            out.key("perMill");
            out.stringValue(String.valueOf(in.getPerMill()));
            out.key("percent");
            out.stringValue(String.valueOf(in.getPercent()));
            out.key("digit");
            out.stringValue(String.valueOf(in.getDigit()));
            out.key("patternSeparator");
            out.stringValue(String.valueOf(in.getPatternSeparator()));
            out.key("infinity");
            out.stringValue(in.getInfinity());
            out.key("NaN");
            out.stringValue(in.getNaN());
            out.key("minusSign");
            out.stringValue(String.valueOf(in.getMinusSign()));
            out.key("currencySymbol");
            out.stringValue(in.getCurrencySymbol());
            out.key("internationalCurrencySymbol");
            out.stringValue(in.getInternationalCurrencySymbol());
            out.key("monetarySeparator");
            out.stringValue(String.valueOf(in.getMonetaryDecimalSeparator()));
            out.key("exponentialSeparator");
            out.stringValue(in.getExponentSeparator());
        }
        out.closeObject();
    }
}
