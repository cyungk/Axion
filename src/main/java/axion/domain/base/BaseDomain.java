package axion.domain.base;

import axion.AxionApplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class BaseDomain implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private void readObject(ObjectInputStream in)
            throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        AxionApplication.getInjector().inject(this);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object obj) {
        if(this == obj) { return true; }
        if(obj == null) { return false; }
        if(getId() == null) { return false; }
        if(getId() <= 0) { return false; }
        if(!(obj instanceof BaseDomain)) { return false; }
        if(getBaseClass(this).equals(getBaseClass(obj))) {
            BaseDomain that = (BaseDomain)obj;
            if(that.getId() == null) { return false; }
            return getId().equals(that.getId());
        }
        return false;
    }

    private Class getBaseClass(Object obj) {
        Class clazz = obj.getClass();
        while(!clazz.equals(BaseDomain.class) && clazz.getSimpleName().contains("$$")) {
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    @Override
    public final int hashCode() {
        if(getId() == null) { return 0; }
        return getId();
    }

    // Adds in and exclamation point when the class has been proxied
    @Override
    public String toString() {
        Class baseClass = getBaseClass(this);
        if(baseClass.equals(getClass())) {
            return baseClass.getSimpleName() + "[id=" + getId() + "]";
        } else {
            return baseClass.getSimpleName() + "![id=" + getId() + "]";
        }
    }
}
