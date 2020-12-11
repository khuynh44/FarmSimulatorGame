package main.java.ui;

public abstract class Controller<T extends Model> {
    protected T model;

    public void initialize(T model) {
        if (this.model != null) {
            throw new IllegalStateException(model.getClass().getSimpleName()
                + " model may only be initialized once.");
        }

        this.model = model;
    };

    public T getModel() {
        return model;
    }
}
