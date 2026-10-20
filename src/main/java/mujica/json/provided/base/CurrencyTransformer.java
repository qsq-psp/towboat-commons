package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Currency;

@CodeHistory(date = "2026/9/22")
public class CurrencyTransformer implements JsonContextTransformer<Currency>, JsonStructure {

    public static final CurrencyTransformer INSTANCE = new CurrencyTransformer();

    @Override
    public void transform(Currency in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("currencyCode");
            out.stringValue(in.getCurrencyCode());
            out.key("symbol");
            out.stringValue(in.getSymbol());
            out.key("defaultFractionDigits");
            out.numberValue(in.getDefaultFractionDigits());
            out.key("numericCode");
            out.numberValue(in.getNumericCode());
            out.key("displayName");
            out.stringValue(in.getDisplayName());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        jh.openArray();
        for (Currency currency : Currency.getAvailableCurrencies()) {
            transform(currency, jh, null);
        }
        jh.closeArray();
    }
}
