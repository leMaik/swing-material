package de.craften.ui.swingmaterial.util;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A timing target that doesn't rely on public getters and setters.
 * <p>
 * This code is based on the {@link org.jdesktop.core.animation.timing.PropertySetter} implementation by Tim Halloran.
 *
 * @see <a href="https://java.net/projects/timingframework/">Timing Framework</a>
 * @see org.jdesktop.core.animation.timing.PropertySetter
 */
public class SafePropertySetter<T> extends TimingTargetAdapter {
    private final AtomicReference<KeyFrames<T>> keyFrames;
    private final boolean isToAnimation;
    private final Getter<T> getter;
    private final Setter<T> setter;

    protected SafePropertySetter(KeyFrames<T> keyFrames, boolean isToAnimation, Getter<T> getter, Setter<T> setter) {
        this.keyFrames = new AtomicReference<>(keyFrames);
        this.isToAnimation = isToAnimation;
        this.getter = getter;
        this.setter = setter;
    }

    public static <T> TimingTarget getTarget(Setter<T> setter, T... values) {
        return new SafePropertySetter<>(new KeyFrames.Builder<T>().addFrames(values).build(), false, null, setter);
    }

    public static <T> TimingTarget getTarget(Setter<T> setter, KeyFrames<T> keyFrames) {
        return new SafePropertySetter<>(keyFrames, false, null, setter);
    }

    public static <T> TimingTarget getTargetTo(Getter<T> getter, Setter<T> setter, T... values) {
        return getTargetTo(getter, setter, new KeyFrames.Builder<T>(values[0]).addFrames(values).build());
    }

    public static <T> TimingTarget getTargetTo(GetterAndSetter<T> getterAndSetter, T... values) {
        return getTargetTo(getterAndSetter, getterAndSetter, values);
    }

    public static <T> TimingTarget getTargetTo(Getter<T> getter, Setter<T> setter, KeyFrames<T> keyFrames) {
        return new SafePropertySetter<>(keyFrames, true, getter, setter);
    }

    public static <T> TimingTarget getTargetTo(GetterAndSetter<T> getterAndSetter, KeyFrames<T> keyFrames) {
        return getTargetTo(getterAndSetter, getterAndSetter, keyFrames);
    }

    public static <U> Property<U> animatableProperty(Component component, U value) {
        return new Property<>(component, value);
    }

    public void timingEvent(Animator source, double fraction) {
        setter.setValue(this.keyFrames.get().getInterpolatedValueAt(fraction));
    }

    public void begin(Animator source) {
        if (isToAnimation) {
            KeyFrames.Builder<T> builder = new KeyFrames.Builder<>(getter.getValue());
            boolean first = true;
            for (KeyFrames.Frame<T> frame : keyFrames.get()) {
                if (first) {
                    first = false;
                } else {
                    builder.addFrame(frame);
                }
            }
            keyFrames.set(builder.build());
        }

        double fraction = source.getCurrentDirection() == Animator.Direction.FORWARD ? 0.0D : 1.0D;
        this.timingEvent(source, fraction);
    }

    public interface Getter<T> {
        T getValue();
    }

    public interface Setter<T> {
        void setValue(T value);
    }

    public interface GetterAndSetter<T> extends Getter<T>, Setter<T> {
    }

    public static class Property<T> implements GetterAndSetter<T> {
        private final Component component;
        private T value;

        public Property(Component component, T value) {
            this.component = component;
            this.value = value;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public void setValue(T newValue) {
            value = newValue;
            if (component != null) {
                component.repaint();
            }
        }
    }
}
