/*
 * Copyright (c) 2011 by the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.mapping.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * @author Jon Brisbin <jbrisbin@vmware.com>
 */
public interface MappingConfigurationBuilder {

  <T> boolean isPersistentEntity(Class<T> clazz);

  <T> PersistentEntity<T> createPersistentEntity(Class<T> clazz, MappingContext mappingContext) throws MappingConfigurationException;

  boolean isPersistentProperty(Field field, PropertyDescriptor descriptor) throws MappingConfigurationException;

  PersistentProperty<?> createPersistentProperty(Field field, PropertyDescriptor descriptor) throws MappingConfigurationException;

  <T> PreferredConstructor<?> getPreferredConstructor(Class<T> clazz) throws MappingConfigurationException;

  boolean isAssociation(Field field, PropertyDescriptor descriptor) throws MappingConfigurationException;

  Association createAssociation(PersistentProperty<?> property);

}