package blog.svenbayer.cache.refresh.ahead.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class ReloadAheadKey implements Serializable {

    private String instanceName;
    private String methodName;
    private Object[] parameters;
    private String[] parameterClazzNames;

    public ReloadAheadKey() {
    }

    public ReloadAheadKey(String instanceName, String methodName, Object[] parameters, String[] parameterClazzNames) {
        this.instanceName = instanceName;
        this.methodName = methodName;
        this.parameters = parameters;
        this.parameterClazzNames = parameterClazzNames;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public String[] getParameterClazzNames() {
        return parameterClazzNames;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public void setParameterClazzNames(String[] parameterClazzNames) {
        this.parameterClazzNames = parameterClazzNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReloadAheadKey)) return false;
        ReloadAheadKey that = (ReloadAheadKey) o;
        return Objects.equals(instanceName, that.instanceName) &&
                Objects.equals(methodName, that.methodName) &&
                Arrays.equals(parameters, that.parameters) &&
                Arrays.equals(parameterClazzNames, that.parameterClazzNames);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(instanceName, methodName);
        result = 31 * result + Arrays.hashCode(parameters);
        result = 31 * result + Arrays.hashCode(parameterClazzNames);
        return result;
    }

    @Override
    public String toString() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(this);
            oos.flush();
            byte[] objectAsBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(objectAsBytes);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
