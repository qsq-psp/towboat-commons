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
    public void transform(@NotNull DecimalFormatSymbols decimalFormatSymbols, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("zeroDigit");
            out.stringValue(String.valueOf(decimalFormatSymbols.getZeroDigit()));
            out.key("groupingSeparator");
            out.stringValue(String.valueOf(decimalFormatSymbols.getGroupingSeparator()));
            out.key("perMill");
            out.stringValue(String.valueOf(decimalFormatSymbols.getPerMill()));
            out.key("percent");
            out.stringValue(String.valueOf(decimalFormatSymbols.getPercent()));
            out.key("digit");
            out.stringValue(String.valueOf(decimalFormatSymbols.getDigit()));
            out.key("patternSeparator");
            out.stringValue(String.valueOf(decimalFormatSymbols.getPatternSeparator()));
            out.key("infinity");
            out.stringValue(decimalFormatSymbols.getInfinity());
            out.key("NaN");
            out.stringValue(decimalFormatSymbols.getNaN());
            out.key("minusSign");
            out.stringValue(String.valueOf(decimalFormatSymbols.getMinusSign()));
            out.key("currencySymbol");
            out.stringValue(decimalFormatSymbols.getCurrencySymbol());
            out.key("internationalCurrencySymbol");
            out.stringValue(decimalFormatSymbols.getInternationalCurrencySymbol());
            out.key("monetarySeparator");
            out.stringValue(String.valueOf(decimalFormatSymbols.getMonetaryDecimalSeparator()));
            out.key("exponentialSeparator");
            out.stringValue(decimalFormatSymbols.getExponentSeparator());
        }
        out.closeObject();
    }
}
