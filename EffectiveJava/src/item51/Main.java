package item51;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

enum TemperatureScale { FAHRENHEIT, CELSIUS, KELVIN }

class Thermometer {

    private final TemperatureScale curScale;

    private Thermometer(TemperatureScale curScale) {
        this.curScale = curScale;
    }

    public TemperatureScale getScale() {
        return curScale;
    }

    public static Thermometer newInstance(TemperatureScale curScale) {
        return new Thermometer(curScale);
    }
}

public class Main {

    public static void main(String[] args) {
        Thermometer celsius = Thermometer.newInstance(TemperatureScale.CELSIUS);
        Thermometer kelvin = Thermometer.newInstance(TemperatureScale.KELVIN);

        System.out.println(celsius.getScale());
        System.out.println(kelvin.getScale());
    }

    // springframework 의 ObjectUtils 에서 제공하는 메소드
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional) {
            return ((Optional<?>) obj).isEmpty();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }

        // else
        return false;
    }
}
