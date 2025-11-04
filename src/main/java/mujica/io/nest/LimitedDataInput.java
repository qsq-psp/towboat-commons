package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;

import java.io.DataInput;

@CodeHistory(date = "2025/9/15")
public interface LimitedDataInput extends LimitedInput, DataInput {}
