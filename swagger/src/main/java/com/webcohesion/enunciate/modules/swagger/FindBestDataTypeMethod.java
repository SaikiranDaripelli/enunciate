/*
 * Copyright 2006-2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webcohesion.enunciate.modules.swagger;

import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.resources.*;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.*;

/**
 * @author Ryan Heaton
 */
public class FindBestDataTypeMethod implements TemplateMethodModelEx {

  public Object exec(List list) throws TemplateModelException {
    if (list.size() < 1) {
      throw new TemplateModelException("The responsesOf method must have a parameter.");
    }

    TemplateModel from = (TemplateModel) list.get(0);
    Object unwrapped = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build().unwrap(from);
    if (unwrapped instanceof Entity) {
      return findBestDataType((Entity) unwrapped);
    }

    throw new TemplateModelException("No responses for: " + unwrapped);
  }

  protected static DataTypeReference findBestDataType(Entity entity) {
    for (MediaTypeDescriptor mediaTypeDescriptor : entity.getMediaTypes()) {
      if (mediaTypeDescriptor.getSyntax() != null && mediaTypeDescriptor.getSyntax().toLowerCase().contains("json")) {
        return mediaTypeDescriptor.getDataType();
      }
    }

    //didn't find json; try again.
    for (MediaTypeDescriptor mediaTypeDescriptor : entity.getMediaTypes()) {
      return mediaTypeDescriptor.getDataType();
    }

    return null;
  }
}