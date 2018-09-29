/*
 * Copyright 2018-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package blog.svenbayer.cache.refresh.ahead.model;

import blog.svenbayer.cache.refresh.ahead.exception.ReloadAheadException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * Key used for storing and finding cache values.
 *
 * @author Sven Bayer
 */
public class ReloadAheadKey implements Serializable {

	private String instanceName;

	private String methodName;

	private Object[] parameters;

	private String[] parameterClazzNames;

	public ReloadAheadKey() {
	}

	public ReloadAheadKey(String instanceName, String methodName, Object[] parameters,
			String[] parameterClazzNames) {
		this.instanceName = instanceName;
		this.methodName = methodName;
		this.parameters = parameters;
		this.parameterClazzNames = parameterClazzNames;
	}

	public String getInstanceName() {
		return this.instanceName;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public Object[] getParameters() {
		return this.parameters;
	}

	public String[] getParameterClazzNames() {
		return this.parameterClazzNames;
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
		if (this == o) {
			return true;
		}
		if (!(o instanceof ReloadAheadKey)) {
			return false;
		}
		ReloadAheadKey that = (ReloadAheadKey) o;
		return Objects.equals(this.instanceName, that.instanceName)
				&& Objects.equals(this.methodName, that.methodName)
				&& Arrays.equals(this.parameters, that.parameters)
				&& Arrays.equals(this.parameterClazzNames, that.parameterClazzNames);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(this.instanceName, this.methodName);
		result = 31 * result + Arrays.hashCode(this.parameters);
		result = 31 * result + Arrays.hashCode(this.parameterClazzNames);
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
		}
		catch (IOException ex) {
			throw new ReloadAheadException("Could not convert ReloadAheadKey to string!",
					ex);
		}
	}

}
