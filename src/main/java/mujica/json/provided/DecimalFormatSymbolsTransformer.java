package mujica.json.provided;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormatSymbols;

/**
 * Created on 2026/5/1.
 */
public class DecimalFormatSymbolsTransformer implements JsonContextTransformer<DecimalFormatSymbols> {

    public static final DecimalFormatSymbolsTransformer INSTANCE = new DecimalFormatSymbolsTransformer();

    @Override
    public void transform(DecimalFormatSymbols in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("zeroDigit");
            out.stringValue(String.valueOf(in.getZeroDigit()));
            out.stringKey("groupingSeparator");
            out.stringValue(String.valueOf(in.getGroupingSeparator()));
            out.stringKey("perMill");
            out.stringValue(String.valueOf(in.getPerMill()));
            out.stringKey("percent");
            out.stringValue(String.valueOf(in.getPercent()));
            out.stringKey("digit");
            out.stringValue(String.valueOf(in.getDigit()));
            out.stringKey("patternSeparator");
            out.stringValue(String.valueOf(in.getPatternSeparator()));
            out.stringKey("infinity");
            out.stringValue(in.getInfinity());
            out.stringKey("NaN");
            out.stringValue(in.getNaN());
            out.stringKey("minusSign");
            out.stringValue(String.valueOf(in.getMinusSign()));
            out.stringKey("currencySymbol");
            out.stringValue(in.getCurrencySymbol());
            out.stringKey("internationalCurrencySymbol");
            out.stringValue(in.getInternationalCurrencySymbol());
            out.stringKey("monetarySeparator");
            out.stringValue(String.valueOf(in.getMonetaryDecimalSeparator()));
            out.stringKey("exponentialSeparator");
            out.stringValue(in.getExponentSeparator());
        }
        out.closeObject();
    }
}
